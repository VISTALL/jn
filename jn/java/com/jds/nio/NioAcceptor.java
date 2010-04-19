package com.jds.nio;

import com.jds.nio.core.NioHandler;
import com.jds.nio.core.NioService;
import com.jds.nio.core.Protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 18:03:34
 */
public class NioAcceptor extends NioService
{
	private ServerSocketChannel _serverSocket;

	public NioAcceptor(NioHandler handler, Protocol protocol)
	{
		super(handler, protocol);
	}

	/**
	 * Биндит хост по определенном адресе
	 */
	public void bind(InetSocketAddress address) throws IOException
	{
		//try
		//{
		_selector = Selector.open();

		_serverSocket = ServerSocketChannel.open();
		_serverSocket.configureBlocking(false);
		_serverSocket.socket().bind(address);
		_serverSocket.register(_selector, SelectionKey.OP_ACCEPT);
		_serverSocket.socket().setReceiveBufferSize(1024 * 16);

		fireServiceActivated();
		//}
		//catch (IOException e)
		/**{
		 fireServiceDeactivated();
		 fireCatchException(e);
		 }  */
	}

	public void unbind()
	{
		//try
		{
			try
			{
				_acceptor.shutdown();
				_serverSocket.close();
				_selector.close();
			}
			catch (IOException e)
			{
				//e.printStackTrace();
			}
		}
		/*catch (IOException e)
		{
			//
		}*/
	}
}