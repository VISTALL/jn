package crypt;

import com.jds.jn.Jn;
import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 30.10.2009
 * Time: 8:09:42
 */
public class AionGameCrypter implements ProtocolCrypter
{
	private static byte[] staticKey = "nKO/WctQ0AVLbpzfBkS6NevDYT8ourG5CRlmdjyJ72aswx4EPq1UgZhFMXH?3iI9".getBytes();

	private byte[] clientPacketkey;
	private byte[] serverPacketkey;

	private boolean _enabled;

	public byte[] decrypt(byte[] raw, PacketType dir, Session session)
	{
		if (dir == PacketType.CLIENT)
		{
			if (!_enabled)
				return raw;

			decode(raw, clientPacketkey);
		}
		else
		{
			if (!_enabled)
			{
				decodeServerOpcode(raw);
				searchKey(session, raw, dir);
				_enabled = true;
			}
			else
			{
				decode(raw, serverPacketkey);
				decodeServerOpcode(raw);
			}
		}

		return raw;
	}

	@Override
	public byte[] encrypt(byte[] raw, PacketType dir, Session session)
	{
		return null;
	}

	private boolean searchKey(Session session, byte[] raw, PacketType dir)
	{
		DecryptedPacket packet = new DecryptedPacket(null, dir, raw, System.currentTimeMillis(), session.getProtocol() , false);

		if (dir == PacketType.SERVER && packet.getPacketInfo() != null && packet.getPacketInfo().isKey())
		{
			int key;
			VisualValuePart part = (VisualValuePart) packet.getRootNode().getPartByName("key");
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
		raw[0] = (byte) ((raw[0] ^ 0xFFFFFFFF) + 68);
	}
}
