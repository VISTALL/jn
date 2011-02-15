package com.jds.nio;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jds.nio.buffer.NioBuffer;
import com.jds.nio.core.CloseType;
import com.jds.nio.core.NioService;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 18:47:26
 */
public class NioProcessor
{
	private NioService _service;
	private final Queue<NioSession> _pendingClose;

	public NioProcessor(NioService service)
	{
		_service = service;
		_pendingClose = new ConcurrentLinkedQueue<NioSession>();
	}

	public void accept(SelectionKey key)
	{
		SocketChannel sc;
		try
		{
			while ((sc = ((ServerSocketChannel) key.channel()).accept()) != null)
			{
				sc.configureBlocking(false);
				NioSession nioSession = new NioSession(sc, sc.socket(), _service);

				_service.fireSessionCreate(nioSession);
			}
		}
		catch (Exception e)
		{
			//_service.fireCatchException(e);
		}
	}

	public void read(SelectionKey key)
	{
		NioSession nioSession = (NioSession) key.attachment();

		if (nioSession.getSocket().isClosed())
		{
			return;
		}
		try
		{
			NioBuffer buf = nioSession.READ_BUFFER;
			int readBytes = 0;

			try
			{
				readBytes = nioSession.read(buf);
			}
			finally
			{
				buf.flip();
			}

			if (readBytes == -1)
			{
				close(nioSession, CloseType.NORMAL);
			}
			if (readBytes == -2)
			{
				close(nioSession, CloseType.FORCE);
			}

			if (readBytes == 0)
			{
				return;
			}

			int pos = buf.position();
			byte[] at = new byte[buf.remaining()];
			buf.get(at);
			buf.position(pos);

			//System.out.println("In: " + Util.printData(at));

			NioBuffer buff0 = NioBuffer.wrap(at).order(ByteOrder.LITTLE_ENDIAN).position(0);

			_service.fireReceiveBuffer(nioSession, buff0);

			if (buf.hasRemaining())
			{
				nioSession.READ_BUFFER.compact();
			}
			else
			{
				buf.clear();
			}
		}
		catch (Exception e)
		{
			if (e instanceof IOException)
			{
				close(nioSession, CloseType.FORCE);
			}
			_service.fireCatchException(e);
		}
	}

	public void write(SelectionKey key)
	{
		NioSession nioSession = (NioSession) key.attachment();
		NioBuffer buf = nioSession.WRITE_BUFFER;
		int writeBytes = 0;

		try
		{
			if (buf.hasRemaining())
			{
				writeBytes = nioSession.write(buf);

				if (writeBytes == 0)
				{
					return;
				}

				if (buf.hasRemaining())
				{
					return;
				}
			}

			while (true)
			{
				buf.clear();

				NioBuffer buf2 = nioSession.getNextMessage();

				if (buf2 == null)
				{
					buf.limit(0);
					break;
				}

				buf.put(buf2);
				buf.flip();

				writeBytes = nioSession.write(buf);

				if (writeBytes == 0)
				{
					return;
				}

				if (buf.hasRemaining())
				{
					return;
				}
			}

			key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
		}
		catch (Exception e)
		{
			if (e instanceof IOException)
			{
				close(nioSession, CloseType.FORCE);
			}

			_service.fireCatchException(e);
		}
	}

	public Queue<NioSession> pendingClose()
	{
		return _pendingClose;
	}

	public void close(NioSession session, CloseType type)
	{
		if (session.isClosing())
		{
			return;
		}

		session.setCloseType(type);
		session.setClosing(true);

		synchronized (_pendingClose)
		{
			_pendingClose.add(session);
		}
	}
}