package crypt;

import java.nio.ByteOrder;
import java.util.Arrays;

import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.protocol.Protocol;
import com.jds.nio.buffer.NioBuffer;
import crypt.helpers.Obfuscator;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 16.09.2009
 * Time: 10:20:30
 */
public class L2GameCrypter implements ProtocolCrypter
{
	private Protocol _protocol;
	private boolean _isEnables;

	private byte[] _inKey;
	private byte[] _outKey;

	private Obfuscator _obs = new Obfuscator();


	private final Object _clientLock = new Object();
	private final Object _serverLock = new Object();

	@Override
	public byte[] decrypt(byte[] raw, PacketType dir)
	{
		if (!_isEnables && dir == PacketType.CLIENT)
		{
			return raw;
		}

		if (!_isEnables && dir == PacketType.SERVER)
		{
			DecryptedPacket packet = new DecryptedPacket(Arrays.copyOf(raw, raw.length), dir, _protocol);

			if (packet.isKey())
			{
				searchKey(packet);
			}

			return raw;
		}

		switch (dir)
		{
			case CLIENT:
				synchronized (_clientLock)
				{
					decode(raw, _outKey);

					NioBuffer buff = NioBuffer.wrap(raw);
					buff.order(ByteOrder.LITTLE_ENDIAN);
					int val = buff.getUnsigned();

					if(val != 0xD0)
					{
						val = _obs.decodeSingleOpcode(val);
						buff.put(0, (byte)val);
					}
					else
					{
						buff.put(0, (byte)val);

						int second = _obs.decodeDoubleOpcode(buff.getUnsignedShort(1));
						buff.putShort(1, (short)second);
					}
					
					return buff.array();
				}
			case SERVER:
				synchronized (_serverLock)
				{
					decode(raw, _inKey);

					if(raw[0] == 0x0B)
					{
						searchSeed(raw);
					}
				}
				break;
		}

		return raw;
	}

	private synchronized void searchKey(DecryptedPacket packet)
	{
		byte[] key = new byte[16];

		for (int i = 0; i < 8; i++)
		{
			VisualValuePart part = (VisualValuePart) packet.getRootNode().getPartByName("key" + i); // key
			key[i] = part.getValueAsByte();
		}
		//xor key
		key[8] = (byte) 0xc8;
		key[9] = (byte) 0x27;
		key[10] = (byte) 0x93;
		key[11] = (byte) 0x01;
		key[12] = (byte) 0xa1;
		key[13] = (byte) 0x6c;
		key[14] = (byte) 0x31;
		key[15] = (byte) 0x97;

		int seed = packet.getInt("seed");
		if(seed != 0)
			_obs.init_tables(seed);

		_inKey = Arrays.copyOf(key, key.length);
		_outKey = Arrays.copyOf(key, key.length);

		_isEnables = true;
	}

	public void searchSeed(byte[] raw)
	{
		DecryptedPacket packet = new DecryptedPacket(Arrays.copyOf(raw, raw.length), PacketType.SERVER, _protocol);

		if(packet.getPacketInfo() != null && !packet.hasError())
		{
			if(packet.getRootNode().getPartByName("seed") != null)
			{
				_obs.disable();
				int seed = packet.getInt("seed");
				if(seed != 0)
				{
					_obs.init_tables(seed);
				}
			}	
		}
	}

	public void decode(byte[] raw, byte[] key)
	{
		int size = raw.length;
		int temp = 0;

		for (int i = 0; i < size; i++)
		{
			int temp2 = raw[i] & 0xFF;
			raw[i] = (byte) (temp2 ^ key[i & 15] ^ temp);
			temp = temp2;
		}

		int old = key[8] & 0xff;
		old |= key[9] << 8 & 0xff00;
		old |= key[10] << 0x10 & 0xff0000;
		old |= key[11] << 0x18 & 0xff000000;

		old += size;

		key[8] = (byte) (old & 0xff);
		key[9] = (byte) (old >> 0x08 & 0xff);
		key[10] = (byte) (old >> 0x10 & 0xff);
		key[11] = (byte) (old >> 0x18 & 0xff);
	}

	@Override
	public byte[] encrypt(byte[] raw, PacketType dir)
	{
		return null;
	}

	@Override
	public void setProtocol(Protocol protocol)
	{
		_protocol = protocol;
	}
}
