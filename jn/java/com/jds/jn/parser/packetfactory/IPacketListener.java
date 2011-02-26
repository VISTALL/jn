package com.jds.jn.parser.packetfactory;

import java.util.List;

import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import com.jds.jn.network.packets.DecryptedPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  0:15:56/25.03.2010
 */
public interface IPacketListener
{
	void invoke(DecryptedPacket p);

	List<JRibbonBand> getRibbonBands();

	void close();
}
