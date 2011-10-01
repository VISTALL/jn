package com.jds.jn.parser.packetfactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.session.Session;
import com.jds.jn.util.iterator.JoinedIterator;

/**
 * @author VISTALL
 * @date 23:37/30.09.2011
 */
public class ListenerInvoker extends IPacketListener.Abstract
{
	private static final Logger LOGGER = Logger.getLogger(ListenerInvoker.class);

	private IPacketListener _listener;

	private List<PacketInfo> _packetInfos = Collections.emptyList();

	public ListenerInvoker(IPacketListener listener, Protocol protocol)
	{
		_listener = listener;

		List<String> packets = listener.getPackets();
		_packetInfos = new ArrayList<PacketInfo>(packets.size());

		JoinedIterator<PacketInfo> packetInfos = new JoinedIterator<PacketInfo>(protocol.getFamillyPackets(PacketType.SERVER).iterator(), protocol.getFamillyPackets(PacketType.CLIENT).iterator());
		while(packetInfos.hasNext())
		{
			PacketInfo packetInfo = packetInfos.next();
			if(packets.contains(packetInfo.getName()))
				_packetInfos.add(packetInfo);
		}
	}

	@Override
	public List<JRibbonBand> getRibbonBands()
	{
		return _listener.getRibbonBands();
	}

	@Override
	public void invoke(Session session, DecryptedPacket packet)
	{
		if(packet.hasError() || packet.getPacketInfo() == null || !_packetInfos.contains(packet.getPacketInfo()))
			return;

		_listener.invoke(session, packet);
	}

	@Override
	public void close()
	{
		try
		{
			_listener.close();
		}
		catch(IOException e)
		{
			LOGGER.warn("IOException: " + e, e);
		}
	}
}
