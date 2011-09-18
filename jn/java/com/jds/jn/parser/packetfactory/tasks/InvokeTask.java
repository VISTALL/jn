package com.jds.jn.parser.packetfactory.tasks;

import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.session.Session;
import com.jds.jn.util.RunnableImpl;

/**
 * @author VISTALL
 * @date 14:29/18.09.2011
 */
public class InvokeTask extends RunnableImpl
{
	private List<DecryptedPacket> _packetList;
	private Session _session;

	public InvokeTask(Session session, List<DecryptedPacket> packetList)
	{
		_session = session;
		_packetList = packetList;
	}

	@Override
	protected void runImpl() throws Throwable
	{
		for(DecryptedPacket packet : _packetList)
			_session.fireInvokePacket(packet);
	}
}
