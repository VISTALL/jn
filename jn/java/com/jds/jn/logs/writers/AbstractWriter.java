package com.jds.jn.logs.writers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

import com.jds.jn.session.Session;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 22:42:26
 */
public abstract class AbstractWriter
{
	protected Session _session;
	protected RandomAccessFile _file;
	protected NioBuffer _buf = null;

	protected AbstractWriter(String file, Session session) throws IOException
	{
		_session = session;
		_file = new RandomAccessFile(file, "rw");
	}

	protected void close() throws IOException
	{
		if (_buf != null)
		{
			_file.write(_buf.array());
		}

		_file.close();
	}

	protected abstract void writeHeader() throws IOException;

	protected abstract void writePackets() throws IOException;

	protected void createBuffer()
	{
		_buf = NioBuffer.allocate(1);
		_buf.setAutoExpand(true);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
	}

	protected void crypt() throws IOException
	{

	}

	public void write() throws IOException
	{
		createBuffer();
		writeHeader();
		writePackets();
		crypt();
		close();
	}

	protected void writeBoolC(boolean b)
	{
		_buf.put(b ? (byte)1 : 0);
	}

	protected void writeC(int i)
	{
		_buf.put((byte)i);
	}

	protected void writeD(int i)
	{
		_buf.putInt(i);
	}

	protected void writeQ(long i)
	{
		_buf.putLong(i);
	}

	protected void writeB(byte[] i)
	{
		_buf.put(i);
	}

	protected void writeS(CharSequence text)
	{
		if (text == null)
		{
			_buf.putChar('\000');
		}
		else
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
			{
				_buf.putChar(text.charAt(i));
			}
			_buf.putChar('\000');
		}
	}
}
