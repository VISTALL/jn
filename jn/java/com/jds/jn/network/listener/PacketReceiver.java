package com.jds.jn.network.listener;

import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.packets.JPacket;
import com.jds.jn.session.SessionTable;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05.09.2009
 * Time: 6:59:08
 */
public class PacketReceiver
{
	public static synchronized void receive(IMethod method, JPacket packet)
	{
		switch (method.getType())
		{
			case PROXY:
				if (SessionTable.getInstance().getSession(method.getSessionId()) == null)
				{
					return;
				}

				SessionTable.getInstance().getSession(method.getSessionId()).receivePacket(packet);
				break;
			case JPCAP:

				if (SessionTable.getInstance().getSession(method.getSessionId()) == null)
				{
					SessionTable.getInstance().newGameSession(method);
				}

				SessionTable.getInstance().getSession(method.getSessionId()).receivePacket(packet);
				break;
		}

	}
}
