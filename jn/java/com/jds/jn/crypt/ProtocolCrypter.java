package com.jds.jn.crypt;


import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.Protocol;

/**
 * This interface is used to interface with all the different protocol encryptions
 *
 * @author Gilles Duboscq
 */
public interface ProtocolCrypter
{
	public byte[] decrypt(byte[] raw, PacketType dir);

	public byte[] encrypt(byte[] raw, PacketType dir);

	public void setProtocol(Protocol protocol);
}