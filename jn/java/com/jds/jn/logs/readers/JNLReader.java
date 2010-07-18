package com.jds.jn.logs.readers;

import java.io.IOException;

import com.jds.jn.Jn;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.*;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.ProtocolManager;
import com.jds.jn.session.Session;
import com.jds.jn.version_control.Version;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:47:47
 * TODO убрать старую поддержку
 */
public class JNLReader extends AbstractReader
{
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

			Protocol protocol = ProtocolManager.getInstance().getProtocol(type);
			if(protocol == null)
			{
				Jn.getForm().warn("Not find protocol for type: " + type);
				throw new IllegalArgumentException("Not find protocol");
			}

			_session = new Session(type, sessionId, ProtocolManager.getInstance().getProtocol(type), true);
			_session.setVersion(Version.UNKNOWN);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean parsePackets() throws IOException
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
						Jn.getForm().getProgressBar().setValue(p);
					}
					catch (Exception e)
					{
						e.printStackTrace();
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
						Jn.getForm().getProgressBar().setValue(p);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}

			_session.updateUI();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
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
