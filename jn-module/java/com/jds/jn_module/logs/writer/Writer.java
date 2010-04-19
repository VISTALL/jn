package com.jds.jn_module.logs.writer;

import com.jds.jn_module.network.packets.JPacket;
import com.jds.jn_module.network.session.Session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:27:43/04.04.2010
 */
public class Writer
{
	protected Session _session;
	protected ByteBuffer _buf;

	public Writer(Session session) throws IOException
	{
		_session = session;
		_buf = ByteBuffer.allocate(65535 * 5);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
	}

	protected void close() throws IOException
	{
		File file = new File(String.format("./logs/session-%d.jnl", _session.getSessionId()));

		FileOutputStream stream = new FileOutputStream(file);
		stream.write(_buf.array(), 0, _buf.position() + 10);
		stream.close();

		System.out.println("Success writer session " + file.getAbsolutePath() + " array " + _buf.position());
	}

	protected void writeHeader() throws IOException
	{
		_buf.putInt(2);
		_buf.putLong(-2);
		writeS("Jn Module 0.1");
		_buf.putLong(_session.getSessionId());
		_buf.putInt(_session.size());
	}

	protected void writePackets() throws IOException
	{
		for (JPacket packet : _session)
		{
			_buf.putInt(packet.getType().ordinal());
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
