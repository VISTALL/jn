package com.jds.jn.crypt;

import java.util.Arrays;

import com.jds.jn.Jn;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.parser.datatree.NumberValuePart;
import com.jds.jn.protocol.Protocol;

/**
 * @author -Nemesiss-
 */
public class AionGameCrypter implements ProtocolCrypter
{
	private Protocol _protocol;
	private static byte[] staticKey = "nKO/WctQ0AVLbpzfBkS6NevDYT8ourG5CRlmdjyJ72aswx4EPq1UgZhFMXH?3iI9".getBytes();

	byte[] clientPacketkey;
	byte[] serverPacketkey;

	public final static byte STATIC_CLIENT_KEY = 0x5D;
	public final static byte STATIC_SERVER_KEY = 0x54;

	public byte[] decrypt(byte[] raw, PacketType dir)
	{
		if (dir == PacketType.CLIENT)
		{
			if (clientPacketkey == null)
			{
				return raw;
			}
			decode(raw, clientPacketkey);
			decodeClientOpcode(raw);
		}
		else
		{
			if (clientPacketkey == null)
			{
				if (!validatePacket(raw, (byte) STATIC_CLIENT_KEY))
				{
					//MainForm.getInstance().error("1 Invalid JPacket!!!");
				}
				decodeServerOpcode(raw);
				searchKey(Arrays.copyOf(raw, raw.length), dir);
			}
			decode(raw, serverPacketkey);
			if (!validatePacket(raw, (byte) STATIC_CLIENT_KEY))
			{
				//MainForm.getInstance().error("2 Invalid JPacket!!!");
			}
			decodeServerOpcode(raw);
		}

		return raw;
	}

	@Override
	public byte[] encrypt(byte[] raw, PacketType dir)
	{
		return null;
	}

	private boolean searchKey(byte[] raw, PacketType dir)
	{
		DecryptPacket packet = new DecryptPacket(raw, dir, _protocol);

		if (dir == PacketType.SERVER && packet.getPacketFormat() != null && packet.getPacketFormat().isKey())
		{
			int key;
			NumberValuePart part = (NumberValuePart) packet.getRootNode().getPartByName("key");
			if (part == null)
			{
				Jn.getForm().warn("Check your protocol there is no part called 'key' which is required in key packet of the GS protocol.");
				return false;
			}
			key = part.getValueAsInt();
			key = (key - 0x3FF2CC87) ^ 0xCD92E451;

			clientPacketkey = new byte[8];
			clientPacketkey[0] = (byte) (key & 0xff);
			clientPacketkey[1] = (byte) ((key >> 8) & 0xff);
			clientPacketkey[2] = (byte) ((key >> 16) & 0xff);
			clientPacketkey[3] = (byte) ((key >> 24) & 0xff);
			clientPacketkey[4] = (byte) 0xa1;
			clientPacketkey[5] = (byte) 0x6c;
			clientPacketkey[6] = (byte) 0x54;
			clientPacketkey[7] = (byte) 0x87;
			serverPacketkey = new byte[8];
			System.arraycopy(clientPacketkey, 0, serverPacketkey, 0, 8);
			return true;
		}
		Jn.getForm().warn("No key found...");
		return false;
	}

	public void decode(byte[] raw, byte[] key)
	{
		int prev = raw[0];
		/** Decode first byte */
		raw[0] ^= (key[0] & 0xff);

		for (int i = 1; i < raw.length; i++)
		{
			int curr = raw[i] & 0xff;
			raw[i] = (byte) (curr ^ (staticKey[i & 63] & 0xff) ^ (key[i & 7] & 0xff) ^ prev);
			prev = curr;
		}

		long old = (((long) key[0] & 0xff) << 0) | (((long) key[1] & 0xff) << 8) | (((long) key[2] & 0xff) << 16) | (((long) key[3] & 0xff) << 24) | (((long) key[4] & 0xff) << 32) | (((long) key[5] & 0xff) << 40) | (((long) key[6] & 0xff) << 48) | (((long) key[7] & 0xff) << 56);

		old += raw.length;

		key[0] = (byte) (old >> 0 & 0xff);
		key[1] = (byte) (old >> 8 & 0xff);
		key[2] = (byte) (old >> 16 & 0xff);
		key[3] = (byte) (old >> 24 & 0xff);
		key[4] = (byte) (old >> 32 & 0xff);
		key[5] = (byte) (old >> 40 & 0xff);
		key[6] = (byte) (old >> 48 & 0xff);
		key[7] = (byte) (old >> 56 & 0xff);
	}

	private boolean validatePacket(byte[] raw, byte code)
	{
		return raw[0] == ~raw[2] && raw[1] == code;
	}

	public void decodeServerOpcode(byte[] raw)
	{
		raw[0] = (byte) ((raw[0] ^ 0xFF) + 0x4A); ///(byte) ((op - 0x4A) ^ 0xFF);
	}

	private void decodeClientOpcode(byte[] raw)
	{
		raw[0] = (byte) ((raw[0] + 0x83) ^ 0x3D); //byte) ((op + 0x83) ^ 0x3D); // можно и так ((op +0x03) ^0xBD;
	}

	public void setProtocol(Protocol protocol)
	{
		_protocol = protocol;
	}
}
