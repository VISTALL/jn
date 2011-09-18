package com.jds.jn.logs.readers;

import java.io.IOException;

import org.apache.log4j.Logger;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
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
		MainForm.getInstance().getProgressBar().setMaximum(_size);
		try
		{
			if (!_isDecode)
			{
				for (int i = 0; i < _size; i++)
				{
					PacketType type = PacketType.values()[_buffer.getInt()];
					int size = _buffer.getInt();

					byte[] data = readB(size);
					CryptedPacket packet = new CryptedPacket(type, NioBuffer.wrap(data), System.currentTimeMillis());

					_session.receiveQuitPacket(packet);

					MainForm.getInstance().getProgressBar().setValue(i);
				}
			}
			else
			{
				for (int i = 0; i < _size; i++)
				{
					PacketType type = PacketType.values()[_buffer.getInt()];
					int size = _buffer.getInt();
					byte[] data = readB(size);
					CryptedPacket packet = new CryptedPacket(type, NioBuffer.wrap(data), System.currentTimeMillis());
					DecryptedPacket dp = new DecryptedPacket(packet, _session.getProtocol());

					_session.receiveQuitPacket(dp, true, true);

					MainForm.getInstance().getProgressBar().setValue(i);
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
