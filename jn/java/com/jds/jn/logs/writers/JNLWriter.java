package com.jds.jn.logs.writers;

import com.jds.jn.Jn;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.network.packets.JPacket;
import com.jds.jn.config.RValues;
import com.jds.jn.session.Session;
import com.jds.nio.buffer.NioBuffer;

import java.io.IOException;
import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:48:02
 */
public class JNLWriter extends AbstractWriter
{
	private boolean _isDecode;

	public JNLWriter(String file, Session session) throws IOException
	{
		super(file, session);
	}

	@Override
	protected void writeHeader() throws IOException
	{
		_buf = NioBuffer.allocate(1);
		_buf.setAutoExpand(true);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		if (RValues.SAVE_AS_DECODE.asBoolean())
		{
			_isDecode = true;
			_buf.putInt(-1); //используется для совместимости с прежними версиями
		}

		_buf.putInt(_session.getListenerType().getId());
		_buf.putLong(-2);  //используется для совместимости с прежними версиями
		writeS(Jn.VERSION);
		_buf.putLong(_session.getSessionId());
		_buf.putInt(_session.getNotDecryptPackets().size());
	}

	@Override
	protected void writePackets() throws IOException
	{
		if (!_isDecode)
		{
			for (JPacket packet : _session.getNotDecryptPackets())
			{
				_buf.putInt(packet.getType().ordinal());
				_buf.putInt(packet.getBuffer().array().length);
				_buf.put(packet.getBuffer().array());
			}
		}
		else
		{
			for (DataPacket packet : _session.getDecryptPackets())
			{
				_buf.putInt(packet.getPacketType().ordinal());
				_buf.putInt(packet.getFullBuffer().array().length);
				_buf.put(packet.getFullBuffer().array());
			}
		}
	}

}
