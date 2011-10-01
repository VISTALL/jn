package com.jds.jn.parser.packetfactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  0:15:56/25.03.2010
 */
public interface IPacketListener
{
	void invoke(Session session, DecryptedPacket p);

	List<String> getPackets();

	List<JRibbonBand> getRibbonBands();

	void close() throws IOException;

	public class Abstract implements IPacketListener
	{
		@Override
		public void invoke(Session session, DecryptedPacket p)
		{

		}

		@Override
		public List<String> getPackets()
		{
			return Collections.emptyList();
		}

		@Override
		public List<JRibbonBand> getRibbonBands()
		{
			return Collections.emptyList();
		}

		@Override
		public void close() throws IOException
		{

		}
	}
}
