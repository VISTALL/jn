package com.jds.jn.parser.packetfactory;

import com.jds.jn.network.packets.DecryptPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  0:15:56/25.03.2010
 */
public interface IPacketListener
{
	void invoke(DecryptPacket p);

	void close();
}
