package com.jds.jn.network.methods.proxy.handlers;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.jds.jn.Jn;
import com.jds.jn.network.methods.proxy.Proxy;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.session.SessionTable;
import com.jds.nio.NioSession;
import com.jds.nio.buffer.NioBuffer;
import com.jds.nio.core.CloseType;
import com.jds.nio.core.NioHandler;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05.09.2009
 * Time: 7:12:55
 */
public class ClientToProxyHandler implements NioHandler
{
	private Proxy _proxy;

	public ClientToProxyHandler(Proxy p)
	{
		_proxy = p;
	}

	/**
	 * Is called when is session is create
	 *
	 * @param nioSession
	 */
	@Override
	public void sessionCreate(NioSession nioSession)
	{
		_proxy.setClientSession(nioSession);

		try
		{
			SessionTable.getInstance().newSession(_proxy);

			_proxy.getConnector().connect(new InetSocketAddress(_proxy.getRemoteHost(), _proxy.getRemotePort()));

			_proxy.setServerSession(_proxy.getConnector().getSession());

		}
		catch (Exception e)
		{
			try
			{
				_proxy.getClientSession().clear();
			}
			catch (IOException e1)
			{
				//e1.printStackTrace();
			}
			Jn.getForm().warn("Exception " + e, e);
		}
	}

	/**
	 * Is called when is session is closing, closeType is type of closing force or normal
	 *
	 * @param nioSession
	 * @param closeType
	 */
	@Override
	public void sessionClose(NioSession nioSession, CloseType closeType)
	{
		try
		{
			_proxy.getServerSession().clear();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * is called when exception is throw
	 *
	 * @param nioSession
	 * @param throwable
	 */
	@Override
	public void catchException(NioSession nioSession, Throwable throwable)
	{
		Jn.getForm().warn("Throwable " + throwable, throwable);
	}

	/**
	 * is called if message is receive
	 *
	 * @param nioSession
	 * @param buffer
	 */
	@Override
	public void receive(NioSession nioSession, NioBuffer buffer)
	{
		NioBuffer buf = NioBuffer.wrap(buffer.array());

		CryptedPacket packet = new CryptedPacket(PacketType.CLIENT, buf, System.currentTimeMillis());

		_proxy.getServerSession().put(buffer);

		if (SessionTable.getInstance().getSession(_proxy.getSessionId()) == null)
		{
			return;
		}

		SessionTable.getInstance().getSession(_proxy.getSessionId()).receivePacket(packet);
	}
}
