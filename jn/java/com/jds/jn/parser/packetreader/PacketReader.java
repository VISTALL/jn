package com.jds.jn.parser.packetreader;

import com.jds.jn.network.packets.DecryptedPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 16.09.2009
 * Time: 13:48:08
 */
public interface PacketReader
{
	public void show();

	public boolean read(DecryptedPacket packet);
}
