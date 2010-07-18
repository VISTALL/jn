package com.jds.jn.network.methods.jpcap.buffers;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  22:29:05/18.07.2010
 */
public interface IPacketBuffer
{
	public void putData(byte[] dat);

	public int nextAvaliablePacket();

	public void getNextPacket(byte[] header, byte[] data);
}
