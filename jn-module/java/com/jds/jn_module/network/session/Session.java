package com.jds.jn_module.network.session;

import com.jds.jn_module.JnModule;
import com.jds.jn_module.network.packets.JPacket;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  2:55:48/04.04.2010
 */
public class Session implements Iterable<JPacket>
{
	private final long _sessionId;
	private final PacketBuffer _serverbuf = new PacketBuffer();
	private final PacketBuffer _clientbuf = new PacketBuffer();

	private final Sequenced _clientSequenced = new Sequenced();
	private final Sequenced _serverSequenced = new Sequenced();

	private volatile ArrayList<JPacket> _packets = new ArrayList<JPacket>();

	public Session(long s)
	{
		_sessionId = s;
	}

	public long getSessionId()
	{
		return _sessionId;
	}

	public void receivePacket(JPacket packet)
	{
		JnModule.getInstance().setPacketCount(_packets.size());

		_packets.add(packet);
	}

	public int size()
	{
		return _packets.size();
	}

	public PacketBuffer getServerbuf()
	{
		return _serverbuf;
	}

	public PacketBuffer getClientbuf()
	{
		return _clientbuf;
	}

	public Sequenced getClientSequenced()
	{
		return _clientSequenced;
	}

	public Sequenced getServerSequenced()
	{
		return _serverSequenced;
	}

	@Override
	public Iterator<JPacket> iterator()
	{
		return _packets.iterator();
	}
}
