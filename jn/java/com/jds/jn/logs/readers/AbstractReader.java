package com.jds.jn.logs.readers;

import javolution.text.TextBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import com.jds.jn.Jn;
import com.jds.jn.session.Session;
import com.jds.jn.util.ThreadPoolManager;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02/01/2010
 * Time: 0:19:45
 */
public abstract class AbstractReader
{
	protected RandomAccessFile _file;
	protected ByteBuffer _buffer;
	protected FileChannel _channel;
	protected Session _session;

	private boolean _isBusy;

	public void read(File file) throws IOException
	{
		if(!file.exists())
		{
			Jn.getForm().info("File not exists: " + file);
			return;
		}

		if(_isBusy)
		{
			Jn.getForm().info("Reader is busy");
			return;
		}

		_isBusy = true;

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

		_isBusy = false;
	}

	protected void read() throws IOException
	{
		ThreadPoolManager.getInstance().execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Jn.getForm().getProgressBar().setVisible(true);
					Jn.getForm().getProgressBar().setValue(0);

					if (parseHeader())
					{
						parsePackets();
					}

					close();

					if(_session != null)
					{
						Jn.getForm().showSession(_session);
						_session = null;
					}

					Jn.getForm().getProgressBar().setVisible(false);
					Jn.getForm().getProgressBar().setValue(0);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public abstract boolean parseHeader() throws IOException;

	public abstract boolean parsePackets() throws IOException;

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
			TextBuilder tb = TextBuilder.newInstance();
			char ch;

			while ((ch = _buffer.getChar()) != 0)
			{
				tb.append(ch);
			}
			String str = tb.toString();
			TextBuilder.recycle(tb);
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
