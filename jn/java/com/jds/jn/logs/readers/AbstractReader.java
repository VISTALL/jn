package com.jds.jn.logs.readers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.logs.listeners.ReaderListener;
import com.jds.jn.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02/01/2010
 * Time: 0:19:45
 */
public abstract class AbstractReader
{
	protected final Logger _log = Logger.getLogger(getClass());

	protected File _currentFile;
	protected RandomAccessFile _file;
	protected ByteBuffer _buffer;
	protected FileChannel _channel;
	protected Session _session;

	protected ReaderListener _listener;

	public void read(File file, ReaderListener listener) throws IOException
	{
		if(!file.exists())
		{
			_log.info("File not exists: " + file);
			listener.onFinish(null, null);
			return;
		}

		if(_currentFile != null)
		{
			_log.info("Reader is busy: " + _currentFile.getName());
			listener.onFinish(null, null);
			return;
		}

		_listener = listener;
		_currentFile = file;

		_file = new RandomAccessFile(file, "r");
		_buffer = ByteBuffer.allocate((int) _file.length());
		_channel = _file.getChannel();

		_channel.read(_buffer);

		_buffer.flip();
		_buffer.order(ByteOrder.LITTLE_ENDIAN);

		read();
	}

	protected void close() throws IOException
	{
		_buffer.clear();
		_file.close();
		_channel.close();

		_file = null;
		_channel = null;
		_buffer = null;
		_session = null;

		_currentFile = null;
	}

	protected void read()
	{
		MainForm.getInstance().getProgressBar().setVisible(true);

		try
		{
			if(parseHeader())
				parsePackets();

			File f = _currentFile;
			Session s = _session;
			close();
			_listener.onFinish(s, f);
		}
		catch(IOException e)
		{
			_log.warn(getClass().getSimpleName() + ".read(): " + e, e);
		}
		finally
		{
			MainForm.getInstance().getProgressBar().setVisible(false);
		}
	}

	public abstract boolean parseHeader() throws IOException;

	public abstract void parsePackets() throws IOException;

	public abstract String getFileExtension();

	public abstract String getReaderInfo();

	protected boolean readBoolC()
	{
		return _buffer.get() == 1;
	}

	protected byte readC()
	{
		return _buffer.get();
	}

	protected short readH()
	{
		return _buffer.getShort();
	}

	protected int readD()
	{
		return _buffer.getInt();
	}

	protected long readQ()
	{
		return _buffer.getLong();
	}

	protected final String readS()
	{
		try
		{
			StringBuffer tb = new StringBuffer();
			char ch;

			while ((ch = _buffer.getChar()) != 0)
				tb.append(ch);

			String str = tb.toString();
			tb.reverse();
			return str;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	protected final byte[] readB(int length)
	{
		if (length < 0)
		{
			return null;
		}

		byte[] result = new byte[length];

		_buffer.get(result);

		return result;
	}
}
