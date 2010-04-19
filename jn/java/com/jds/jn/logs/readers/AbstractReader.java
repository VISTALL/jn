package com.jds.jn.logs.readers;

import com.jds.jn.Jn;
import javolution.text.TextBuilder;
import com.jds.jn.util.ThreadPoolManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02/01/2010
 * Time: 0:19:45
 */
public abstract class AbstractReader
{
	protected final RandomAccessFile _file;
	protected final ByteBuffer _buffer;
	protected final FileChannel _channel;

	protected AbstractReader(File file) throws IOException
	{
		_file = new RandomAccessFile(file, "r");
		_buffer = ByteBuffer.allocate((int) _file.length());
		_channel = _file.getChannel();

		_channel.read(_buffer);

		_buffer.flip();
		_buffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	protected void close() throws IOException
	{
		_buffer.clear();
		_file.close();
		_channel.close();
	}


	public void read() throws IOException
	{

		ThreadPoolManager.getInstance().execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Jn.getInstance().getProgressBar().setVisible(true);
					Jn.getInstance().getProgressBar().setValue(0);

					if (parseHeader())
					{
						parsePackets();
					}

					close();
					Jn.getInstance().getProgressBar().setVisible(false);
					Jn.getInstance().getProgressBar().setValue(0);
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

	protected final String readS(ByteBuffer buf)
	{
		try
		{
			TextBuilder tb = TextBuilder.newInstance();
			char ch;

			while ((ch = buf.getChar()) != 0)
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

	protected final byte[] readBytes(ByteBuffer buf, int length)
	{
		if (length < 0)
		{
			return null;
		}

		byte[] result = new byte[length];

		buf.get(result);

		return result;
	}
}
