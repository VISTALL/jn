package com.jds.jn.logs.writers;

import com.jds.jn.session.Session;
import com.jds.nio.buffer.NioBuffer;

import java.io.IOException;
import java.io.RandomAccessFile;

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

	public void write() throws IOException
	{
		writeHeader();
		writePackets();
		close();
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
