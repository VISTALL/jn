package com.jds.nio;

import com.jds.nio.core.NioHandler;
import com.jds.nio.core.NioService;
import com.jds.nio.core.Protocol;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 18:03:45
 */
public class NioConnector extends NioService
{
	private NioSession _session;

	public NioConnector(NioHandler handler, Protocol protocol)
	{
		super(handler, protocol);
	}

	public void connect(InetSocketAddress address) throws Exception
	{
		//try
		//{
		if (_session != null)
		{
			_session.clear();
		}

		_selector = Selector.open();
		SocketChannel channel = SocketChannel.open(address);
		channel.configureBlocking(false);
		channel.register(_selector, SelectionKey.OP_CONNECT);

		_session = new NioSession(channel, channel.socket(), this);

		fireServiceActivated();
		fireSessionCreate(_session);
		/**}
		 catch (Throwable e)
		 {
		 fireServiceDeactivated();
		 fireCatchException(e);
		 }  */
	}

	public NioSession getSession()
	{
		return _session;
	}
}
