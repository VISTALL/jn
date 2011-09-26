package com.jds.jn.network.packets;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:41:35/20.06.2010
 */
public interface IPacket
{
	PacketType getPacketType() ;

	long getTime();

	byte[] getAllData();
}
