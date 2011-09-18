package com.jds.jn_module.logs.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import com.jds.jn_module.network.packets.JPacket;
import com.jds.jn_module.network.session.Session;
import com.jds.jn_module.utils.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:27:43/04.04.2010
 */
public class Writer
{
	protected Session _session;
	protected NioBuffer _buf;

	public Writer(Session session) throws IOException
	{
		_session = session;
		_buf = NioBuffer.allocate(65535 * 5);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		_buf.setAutoExpand(true);
	}

	protected void close() throws IOException
	{
		File file = new File(String.format("./logs/session-%d.jnl2", _session.getSessionId()));

		FileOutputStream stream = new FileOutputStream(file);
		stream.write(_buf.array());
		stream.close();

		System.out.println("Success writer session " + file.getAbsolutePath() + " array " + _buf.position());
	}

	protected void writeHeader() throws IOException
	{
		_buf.putInt(2); // ver
		_buf.putInt(1); // major
		_buf.putInt(1); // minor
		_buf.put((byte)2); // type
		_buf.putInt(1); // number

		_buf.put((byte)1); // listener type
		_buf.putLong(_session.getSessionId());
		_buf.put((byte) 0);
	}

	protected void writePackets() throws IOException
	{
		_buf.putInt(_session.size());

		for (JPacket packet : _session)
		{
			_buf.put((byte)packet.getType().ordinal());
			_buf.putLong(packet.getTime());
			_buf.putInt(packet.getBuffer().array().length);
			_buf.put(packet.getBuffer().array());
		}
	}

	public void write() throws IOException
	{
		writeHeader();
		writePackets();
		close();
	}

	protected void writeS(CharSequence text) throws IOException
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
