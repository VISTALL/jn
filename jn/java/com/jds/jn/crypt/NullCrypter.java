package com.jds.jn.crypt;


import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.Protocol;

/**
 * @author Gilles Duboscq
 */
public class NullCrypter implements ProtocolCrypter
{

	public byte[] decrypt(byte[] raw, PacketType dir)
	{
   		return raw;
	}

	@Override
	public byte[] encrypt(byte[] raw, PacketType dir)
	{
		return null;
	}

	public void setProtocol(Protocol protocol)
	{

	}
}