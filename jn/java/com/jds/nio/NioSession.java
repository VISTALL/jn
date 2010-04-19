package com.jds.nio;

import javolution.util.FastMap;
import com.jds.nio.buffer.NioBuffer;
import com.jds.nio.core.CloseType;
import com.jds.nio.core.NioService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 18:03:20
 */
public class NioSession
{
	protected NioService _service;
	protected final SelectionKey _key;
	protected final SocketChannel _channel;
	protected final Socket _socket;
	protected final ReadableByteChannel _readByteChannel;
	protected final WritableByteChannel _writeByteChannel;
	protected boolean _pendingClose;

	protected CloseType _closeType;
	protected boolean _isClosing;

	public NioBuffer READ_BUFFER;
	public NioBuffer WRITE_BUFFER;

	protected final Deque<NioBuffer> _sendQueue = new ArrayDeque<NioBuffer>();

	protected final FastMap<String, Object> _attributes = new FastMap<String, Object>();

	public NioSession(SocketChannel channel, Socket socket, NioService service) throws Exception
	{
		_channel = channel;
		_socket = socket;
		_readByteChannel = socket.getChannel();
		_writeByteChannel = socket.getChannel();
		_service = service;

		_key = _channel.register(_service.getSelector(), SelectionKey.OP_READ);
		_key.attach(this);

		READ_BUFFER = NioBuffer.allocate(1024 * 16);
		READ_BUFFER.order(ByteOrder.LITTLE_ENDIAN);

		WRITE_BUFFER = NioBuffer.allocate(1024 * 16);
		WRITE_BUFFER.flip();
		WRITE_BUFFER.order(ByteOrder.LITTLE_ENDIAN);
	}

	public int read(final NioBuffer buf) throws Exception
	{
		return _readByteChannel.read(buf.buf());
	}

	public int write(final NioBuffer buf) throws Exception
	{
		return _writeByteChannel.write(buf.buf());
	}

	public CloseType getCloseType()
	{
		return _closeType;
	}

	public void setCloseType(CloseType s)
	{
		_closeType = s;
	}

	public boolean isClosing()
	{
		return _isClosing;
	}

	public void setClosing(boolean s)
	{
		_isClosing = s;
	}

	public final void put(NioBuffer buff)
	{
		NioBuffer buf = _service.getProtocol().encode(this, buff);

		if (buf.hasRemaining())
		{
			synchronized (_sendQueue)
			{
				if (!isWriteDisabled())
				{
					_sendQueue.addLast(buf);
				}
			}

			enableWriteInterest();
		}
	}

	protected void enableWriteInterest()
	{
		if (_key.isValid())
		{
			_key.interestOps(_key.interestOps() | SelectionKey.OP_WRITE);
			_key.selector().wakeup();
		}
	}

	public NioBuffer getNextMessage()
	{
		return _sendQueue.pollFirst();
	}

	protected final boolean isWriteDisabled()
	{
		return _pendingClose || _isClosing;
	}

	public void close()
	{
		//_sendQueue.clear();
		_service.getProcessor().close(this, CloseType.NORMAL);
	}

	public void clear() throws IOException
	{
		_socket.close();
		_channel.close();
		_readByteChannel.close();
		_writeByteChannel.close();
	}

	public SelectionKey getSelectionKey()
	{
		return _key;
	}

	public Socket getSocket()
	{
		return _socket;
	}

	public void setAttribute(String name, Object att)
	{
		_attributes.put(name, att);
	}

	public Object getAttribute(String name)
	{
		return _attributes.get(name);
	}

	public InetAddress getAddress()
	{
		return _socket.getInetAddress();
	}

	public long getId()
	{
		return this.hashCode();
	}
}
