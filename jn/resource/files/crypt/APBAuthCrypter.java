package crypt;

import java.util.Arrays;

import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.Protocol;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  23:36:41/18.07.2010
 */
public class APBAuthCrypter implements ProtocolCrypter
{
	private Protocol _protocol;
	//private Cipher _decrypt;
	private byte[] _key = null;

	@Override
	public byte[] decrypt(byte[] raw, PacketType dir)
	{
		if (_key == null)
		{
			DecryptPacket packet = new DecryptPacket(Arrays.copyOf(raw, raw.length), dir, _protocol);
			if (packet != null && packet.isKey())
			{
				try
				{
					//_decrypt = Cipher.getInstance("RC4");

					//byte[] data = new byte[16];
					//System.arraycopy(packet.getBytes("key"), 0, data, 0, 12);

					//SecretKeySpec spec = new SecretKeySpec(data, "ARCFOUR");

					//_decrypt.init(Cipher.DECRYPT_MODE, spec);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				_key = packet.getBytes("key");
			}
		}
		else
		{
			return encdec(raw, _key);
		}

		return raw;
	}

	public static byte[] encdec(byte[] bytes, byte[] key)
	{
		byte[] s = new byte[256];
		byte[] k = new byte[256];
		byte temp;
		int i, j;

		for (i = 0; i < 256; i++)
		{
			s[i] = (byte) i;
			k[i] = key[i % key.length];
		}

		j = 0;
		for (i = 0; i < 256; i++)
		{
			j = (j + s[i] + k[i]) % 256;
			temp = s[i];
			s[i] = s[j];
			s[j] = temp;
		}

		i = j = 0;
		for (int x = 0; x < bytes.length; x++)
		{
			i = (i + 1) % 256;
			j = (j + s[i]) % 256;
			temp = s[i];
			s[i] = s[j];
			s[j] = temp;
			int t = (s[i] + s[j]) % 256;
			bytes[x] ^= s[t];
		}
		return s;
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
