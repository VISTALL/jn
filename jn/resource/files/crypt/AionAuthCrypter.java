package crypt;

import java.io.IOException;
import java.util.Arrays;

import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.parser.datatree.RawValuePart;
import com.jds.jn.protocol.Protocol;
import crypt.helpers.NewCrypt;

/**
 * Author: VISTALL
 * Date: 15.08.2009
 * Time: 21:20:33
 */
public class AionAuthCrypter implements ProtocolCrypter
{
	private static byte[] STATIC_BLOWFISH_KEY =
	{
			(byte) 0x6b,
			(byte) 0x60,
			(byte) 0xcb,
			(byte) 0x5b,
			(byte) 0x82,
			(byte) 0xce,
			(byte) 0x90,
			(byte) 0xb1,
			(byte) 0xcc,
			(byte) 0x2b,
			(byte) 0x6c,
			(byte) 0x55,
			(byte) 0x6c,
			(byte) 0x6c,
			(byte) 0x6c,
			(byte) 0x6c
	};
	private Protocol _protocol;
	private NewCrypt _crypt;
	private NewCrypt _initcrypt = new NewCrypt(STATIC_BLOWFISH_KEY);

	@Override
	public byte[] decrypt(byte[] raw, PacketType dir)
	{
		try
		{
			if (_crypt == null)
			{
				byte[] potentialInit = Arrays.copyOf(raw, raw.length);
				_initcrypt.decrypt(potentialInit);

				NewCrypt.decXORPass(potentialInit);

				DecryptedPacket packet = new DecryptedPacket(Arrays.copyOf(potentialInit, potentialInit.length), dir, _protocol);

				if (packet.getPacketFormat() != null && dir == PacketType.SERVER && packet.getPacketFormat().isKey())
				{
					RawValuePart part = (RawValuePart) packet.getRootNode().getPartByName("Blowfish key");
					if (part == null)
					{
						MainForm.getInstance().warn("Check your protocol there is no part called 'Blowfish key' which is required in key packet of the AS protocol.");
						return raw;
					}
					_crypt = new NewCrypt(part.getBytes());
					System.arraycopy(potentialInit, 0, raw, 0, raw.length);
					return raw; // no checksum here
				}
				MainForm.getInstance().warn("No key was ready to read JPacket, there should have been an Init packet before");
				return raw;
			}

			if (dir == PacketType.SERVER)
			{
				_crypt.decrypt(raw);

				if (!NewCrypt.verifyChecksum(raw))
				{
					//MainForm.getInstance().error("AionAuthCrypter : Wrong checksum (packet id: " + raw[0] + ", dir:" + dir + ")");
				}

				return raw;
			}

			_crypt.decrypt(raw);

			if (!NewCrypt.verifyChecksum(raw))
			{
				//MainForm.getInstance().error("AionAuthCrypter : Wrong checksum (packet id: " + raw[0] + ", dir:" + dir + ")");
			}
			return raw;

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return raw;
	}

	@Override
	public byte[] encrypt(byte[] raw, PacketType dir)
	{
		try
		{
			return _crypt.encrypt(raw);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setProtocol(Protocol protocol)
	{
		_protocol = protocol;
	}
}
