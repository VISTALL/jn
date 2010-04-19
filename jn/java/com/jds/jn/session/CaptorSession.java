package com.jds.jn.session;

import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.methods.jpcap.PacketBuffer;
import com.jds.jn.network.methods.jpcap.Sequenced;
import com.jds.jn.protocol.Protocol;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 20:50 07/12/2009
 */
public class CaptorSession extends Session
{
	private final PacketBuffer _serverbuf = new PacketBuffer();
	private final PacketBuffer _clientbuf = new PacketBuffer();

	private final Sequenced _clientSequenced = new Sequenced();
	private final Sequenced _serverSequenced = new Sequenced();

	public CaptorSession(IMethod iMethod, Protocol protocol)
	{
		super(iMethod, protocol);
	}

	public CaptorSession(ListenerType type, long sessionId, Protocol protocol)
	{
		super(type, sessionId, protocol);
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
}
