package com.jds.jn.logs.writers;

import java.io.IOException;

import com.jds.jn.config.RValues;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.NotDecryptPacket;
import com.jds.jn.session.Session;
import com.jds.jn.util.Version;

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
		if (RValues.SAVE_AS_DECODE.asBoolean())
		{
			_isDecode = true;
			_buf.putInt(-1); //используется для совместимости с прежними версиями
		}

		_buf.putInt(_session.getListenerType().getId());
		writeS(Version.current());
		_buf.putLong(_session.getSessionId());
		_buf.putInt(_session.getNotDecryptPackets().size());   //FIX ME глюк%)
	}

	@Override
	protected void writePackets() throws IOException
	{
		if (!_isDecode)
		{
			for (NotDecryptPacket packet : _session.getNotDecryptPackets())
			{
				_buf.putInt(packet.getPacketType().ordinal());
				_buf.putInt(packet.getBuffer().array().length);
				_buf.put(packet.getBuffer().array());
			}
		}
		else
		{
			for (DecryptPacket packet : _session.getDecryptPackets())
			{
				_buf.putInt(packet.getPacketType().ordinal());
				_buf.putInt(packet.getAllData().length);
				_buf.put(packet.getAllData());
			}
		}
	}

}
