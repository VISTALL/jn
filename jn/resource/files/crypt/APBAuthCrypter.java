package crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.Protocol;
import crypt.helpers.RC4;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  23:36:41/18.07.2010
 */
public class APBAuthCrypter implements ProtocolCrypter
{
	private Protocol _protocol;
	private RC4 _crypt;

	private boolean _first = true;

	@Override
	public byte[] decrypt(byte[] raw, PacketType dir)
	{
		if(_crypt == null && _first)
		{
			try
			{
				DecryptPacket packet = new DecryptPacket(Arrays.copyOf(raw, raw.length), dir, _protocol);
				// LS2GC_LOGIN_PIZZLE
				if(packet != null && packet.getPacketFormat() != null)
				{
					MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

					byte[] nounce = packet.getBytes("nounce");
					sha1.update(nounce);

					byte[] hash = sha1.digest();

					_crypt = new RC4();
					_crypt.setKey(hash);
					_first = false;
				}
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
		}
		else if(_crypt != null)
		{
			byte[] newdata = new byte[raw.length];

			_crypt.decrypt(raw, 0, newdata, 0, raw.length);

			//_crypt = null;

			return newdata;
		}


		return raw;
	}


	@Override
	public byte[] encrypt(byte[] raw, PacketType dir)
	{
		return new byte[0];
	}

	@Override
	public void setProtocol(Protocol protocol)
	{
		_protocol = protocol;
	}
}
