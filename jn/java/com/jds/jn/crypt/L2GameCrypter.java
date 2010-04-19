package com.jds.jn.crypt;

import com.jds.jn.crypt.helpers.Obfuscator;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.parser.datatree.NumberValuePart;
import com.jds.jn.protocol.Protocol;

import java.util.Arrays;

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
	public void decrypt(byte[] raw, PacketType dir)
	{
		if (!_isEnables && dir == PacketType.CLIENT)
		{
			return;
		}

		if (!_isEnables && dir == PacketType.SERVER)
		{
			DataPacket packet = new DataPacket(Arrays.copyOf(raw, raw.length), dir, _protocol);

			if (packet.isKey())
			{
				searchKey(Arrays.copyOf(raw, raw.length));
			}

			return;
		}

		switch (dir)
		{
			case CLIENT:
				synchronized (_clientLock)
				{
					decode(raw, _outKey);

					int d = raw[0] & 0xFF;
					int id = _obs.decodeSingleOpcode(d);
					raw[0] = (byte) id;
				}
				break;
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
	}

	private synchronized void searchKey(byte[] twice)
	{
		DataPacket packet = new DataPacket(twice, PacketType.SERVER, _protocol);


		byte[] key = new byte[16];

		for (int i = 0; i < 8; i++)
		{
			NumberValuePart part = (NumberValuePart) packet.getRootNode().getPartByName("key" + i); // key
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

		NumberValuePart part = (NumberValuePart) packet.getRootNode().getPartByName("seed");
		int seed = part.getValueAsInt();
		if(seed != 0)
			_obs.init_tables(seed);

		_inKey = Arrays.copyOf(key, key.length);
		_outKey = Arrays.copyOf(key, key.length);

		_isEnables = true;
	}

	public void searchSeed(byte[] raw)
	{
		DataPacket packet = new DataPacket(Arrays.copyOf(raw, raw.length), PacketType.SERVER, _protocol);

		if(packet.getPacketFormat() != null)
		{
			if(packet.getRootNode().getPartByName("seed") != null)
			{
				_obs.disable();
				int seed = ((NumberValuePart)packet.getRootNode().getPartByName("seed")).getValueAsInt();
				if(seed != 0)
					_obs.init_tables(seed);
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
