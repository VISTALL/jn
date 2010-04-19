package com.jds.jn.logs.readers;

import com.jds.jn.Jn;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.network.packets.JPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.ProtocolManager;
import com.jds.jn.session.Session;
import com.jds.nio.buffer.NioBuffer;

import java.io.File;
import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:47:47
 */
public class JNLReader extends AbstractReader
{
	private int _size;
	private Session _session;
	private boolean _isDecode = false;

	public JNLReader(File file) throws IOException
	{
		super(file);
	}

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
				version = readS(_buffer);
				sessionId = _buffer.getLong();
			}
			else
			{
				sessionId = test2;
			}

			_size = _buffer.getInt();
			_session = new Session(type, sessionId, ProtocolManager.getInstance().getProtocol(type));

			if (version != null)
			{
				_session.setVersion(version);
			}

			Jn.getInstance().showSession(_session);
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
						byte[] data = readBytes(_buffer, size);
						JPacket packet = new JPacket(type, NioBuffer.wrap(data));

						_session.receivePacket(packet);
						int p = (int) ((100D * (i + 1)) / _size);
						// 100 - 222
						//  x  - 11
						Jn.getInstance().getProgressBar().setValue(p);
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
						byte[] data = readBytes(_buffer, size);
						JPacket packet = new JPacket(type, NioBuffer.wrap(data));
						DataPacket dp = new DataPacket(packet, _session.getProtocol());

						_session.receivePacket(dp);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
