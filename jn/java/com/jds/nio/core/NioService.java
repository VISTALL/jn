package com.jds.nio.core;

import javolution.util.FastList;
import com.jds.nio.NioProcessor;
import com.jds.nio.NioSession;
import com.jds.nio.buffer.NioBuffer;
import com.jds.nio.core.impl.DefaultHandler;

import java.nio.channels.Selector;
import java.util.NoSuchElementException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 18:09:17
 */
public abstract class NioService
{
	protected final FastList<NioServiceListener> _listeners;
	protected volatile Selector _selector;
	protected final NioHandler _handler;
	protected final NioProcessor _processor;
	protected Acceptor _acceptor;
	protected final Protocol _protocol;

	protected FastList<NioSession> _sessionList = new FastList<NioSession>();

	protected NioService(NioHandler handler, Protocol protocol)
	{
		_listeners = new FastList<NioServiceListener>();

		if (handler == null)
		{
			_handler = new DefaultHandler();
		}
		else
		{
			_handler = handler;
		}

		if (protocol == null)
		{
			_protocol = new Protocol();
		}
		else
		{
			_protocol = protocol;
		}

		_processor = new NioProcessor(this);
	}

	public void init()
	{
		_acceptor = new Acceptor(this);
		_acceptor.start();
	}

	public void fireCatchException(Throwable throwable)
	{
		if (_handler == null)
		{
			throw new NullPointerException("handler");
		}

		_handler.catchException(null, throwable);
	}

	public void fireSessionCreate(NioSession nioSession)
	{
		if (_handler == null)
		{
			throw new NullPointerException("handler");
		}

		_handler.sessionCreate(nioSession);
		_sessionList.add(nioSession);
	}

	public void fireSessionClose(NioSession nioSession, CloseType type)
	{
		if (_handler == null)
		{
			throw new NullPointerException("handler");
		}

		_handler.sessionClose(nioSession, type);
		_sessionList.remove(nioSession);
	}

	public void fireReceiveBuffer(NioSession nioSession, NioBuffer buffer)
	{
		if (_handler == null)
		{
			throw new NullPointerException("handler");
		}

		NioBuffer buf = _protocol.decode(nioSession, buffer);

		if (buf.hasRemaining())
		{
			_handler.receive(nioSession, buf);
		}
	}

	public void fireCatchException(NioSession nioSession, Throwable throwable)
	{
		if (_handler == null)
		{
			throw new NullPointerException("handler");
		}

		_handler.catchException(nioSession, throwable);
	}

	public void fireServiceActivated()
	{
		for (NioServiceListener listener : _listeners)
		{
			if (listener == null)
			{
				throw new NullPointerException();
			}
			listener.serviceActivated(this);
		}

		init();
	}

	public void fireServiceDeactivated()
	{
		for (NioServiceListener listener : _listeners)
		{
			if (listener == null)
			{
				throw new NullPointerException("listener");
			}
			listener.serviceDeactivated(this);
		}
	}

	public void addListener(NioServiceListener listener)
	{
		if (listener == null)
		{
			throw new NullPointerException("listener");
		}

		_listeners.add(listener);
	}

	public void removeListener(NioServiceListener listener)
	{
		if (!_listeners.contains(listener))
		{
			throw new NoSuchElementException("listener");
		}

		_listeners.remove(listener);
	}

	public Selector getSelector()
	{
		return _selector;
	}

	public NioProcessor getProcessor()
	{
		return _processor;
	}

	public Protocol getProtocol()
	{
		return _protocol;
	}

	public FastList<NioSession> getSessions()
	{
		return _sessionList;
	}
}
