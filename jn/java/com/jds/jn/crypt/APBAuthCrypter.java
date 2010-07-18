package com.jds.jn.crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.util.Arrays;

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
	private Cipher _decrypt;

	@Override
	public byte[] decrypt(byte[] raw, PacketType dir)
	{
		if (_decrypt == null)
		{
	   		DecryptPacket packet = new DecryptPacket(Arrays.copyOf(raw, raw.length), dir, _protocol);
			if(packet != null && packet.isKey())
			{
				try
				{
					_decrypt = Cipher.getInstance("RC4");

					byte[] data = new byte[16];
					System.arraycopy(packet.getBytes("key"), 0, data, 0, 12);
		
					SecretKeySpec spec = new SecretKeySpec(data, "ARCFOUR");

					_decrypt.init(Cipher.DECRYPT_MODE, spec);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{

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
