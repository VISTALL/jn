package com.jds.jn.session;

import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.methods.jpcap.Sequenced;
import com.jds.jn.network.methods.jpcap.buffers.IPacketBuffer;
import com.jds.jn.network.methods.jpcap.buffers.LittleEndianShortPacketBuffer;
import com.jds.jn.protocol.Protocol;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 20:50 07/12/2009
 */
public class CaptorSession extends Session
{
	private final IPacketBuffer _serverbuf = new LittleEndianShortPacketBuffer();
	private final IPacketBuffer _clientbuf = new LittleEndianShortPacketBuffer();

	private final Sequenced _clientSequenced = new Sequenced();
	private final Sequenced _serverSequenced = new Sequenced();

	public CaptorSession(IMethod iMethod, Protocol protocol)
	{
		super(iMethod, protocol);
	}

	public IPacketBuffer getServerbuf()
	{
		return _serverbuf;
	}

	public IPacketBuffer getClientbuf()
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
