package com.jds.nio;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.*;
import java.util.*;
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

			while (buf.remaining() > 2 && buf.remaining() >= buf.getShort(buf.position()))
			{
				int size = buf.getShort() - 2;
				NioBuffer OUT = NioBuffer.allocate(size);
				OUT.order(ByteOrder.LITTLE_ENDIAN);
				OUT.put(buf.array(), buf.position(), size);
				OUT.flip();
				//System.out.println("size: " +  size + "; position: " + buf.position() + "; limit: " + buf.limit());
				buf.position(buf.position() + size);

				_service.fireReceiveBuffer(nioSession, OUT);
			}

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