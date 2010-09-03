package com.jds.jn.logs.readers;

import org.apache.log4j.Logger;

import java.io.IOException;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.NotDecryptPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.session.Session;
import com.jds.jn.util.version_control.Program;
import com.jds.jn.util.version_control.Version;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:47:47
 * FIX ME больше не используется
 */
public class JNLReader extends AbstractReader
{
	private static final Logger _log = Logger.getLogger(JNLReader.class);

	private int _size;
	private boolean _isDecode = false;

	@Override
	public boolean parseHeader() throws IOException
	{
		try
		{
			int test = _buffer.getInt();
			ListenerType type;

			if (test == -1)
			{
				_isDecode = true;
				int id = _buffer.getInt();
				type = ListenerType.valuesOf(id);
			}
			else
			{
				type = ListenerType.valuesOf(test);
			}

			long sessionId;
			long test2 = _buffer.getLong();
			String version = null;

			if (test2 == -2)
			{
				version = readS();
				sessionId = _buffer.getLong();
			}
			else
			{
				sessionId = test2;
			}

			_size = _buffer.getInt();
			_session = new Session(type, sessionId);
			_session.setVersion(new Version(Program.JN, 1, 0, Version.M, 1));
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void parsePackets() throws IOException
	{
		try
		{
			if (!_isDecode)
			{
				for (int i = 0; i < _size; i++)
				{
					try
					{
						PacketType type = PacketType.values()[_buffer.getInt()];
						int size = _buffer.getInt();

						byte[] data = readB(size);
						NotDecryptPacket packet = new NotDecryptPacket(type, NioBuffer.wrap(data), System.currentTimeMillis());

						_session.receiveQuitPacket(packet);

						int p = (int) ((100D * (i + 1)) / _size);
						MainForm.getInstance().getProgressBar().setValue(p);
					}
					catch (Exception e)
					{
						_log.info("Exception: " + e, e);
					}
				}
			}
			else
			{
				for (int i = 0; i < _size; i++)
				{
					try
					{
						PacketType type = PacketType.values()[_buffer.getInt()];
						int size = _buffer.getInt();
						byte[] data = readB(size);
						NotDecryptPacket packet = new NotDecryptPacket(type, NioBuffer.wrap(data), System.currentTimeMillis());
						DecryptPacket dp = new DecryptPacket(packet, _session.getProtocol());

						_session.receiveQuitPacket(dp);

						int p = (int) ((100D * (i + 1)) / _size);
						MainForm.getInstance().getProgressBar().setValue(p);
					}
					catch (Exception e)
					{
						_log.info("Exception: " + e, e);
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.info("Exception: " + e, e);
		}
	}

	@Override
	public String getFileExtension()
	{
		return "jnl";
	}

	@Override
	public String getReaderInfo()
	{
		return "Jn old log file";
	}
}
