package com.jds.jn.logs.writers;

import java.io.*;
import java.nio.ByteOrder;

import com.jds.jn.Jn;
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

	private boolean _isBusy;

	public void write(File file, Session session) throws IOException
	{
		if(_isBusy)
		{
			Jn.getForm().info("Writer is busy");
			return;
		}

		_file = new RandomAccessFile(file, "rw");
		_session = session;

		_isBusy = true;

		_buf = NioBuffer.allocate(1);
		_buf.setAutoExpand(true);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		write();
	}

	protected void write() throws IOException
	{
		writeHeader();
		writePackets();
		crypt();
		close();
	}

	protected void close() throws IOException
	{
		if (_buf != null)
		{
			_file.write(_buf.array());
		}

		_file.close();

		_isBusy = false;
	}

	protected abstract void writeHeader() throws IOException;

	protected abstract void writePackets() throws IOException;

	public abstract String getFileExtension();

	public abstract String getWriterInfo();

	protected void crypt() throws IOException
	{

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
