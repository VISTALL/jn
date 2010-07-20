package com.jds.jn.logs.readers;

import java.io.IOException;

import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.*;
import com.jds.jn.session.Session;
import com.jds.jn.version_control.Programs;
import com.jds.jn.version_control.Version;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:32:04/20.06.2010
 * version control
 * -----------------
 * d - program type
 * d - major
 * c - type
 * d - number
 * ------------
 * session info
 * c - listener type
 * Q - session id
 * c - decode ?
 */
public class JNL2Reader extends AbstractReader
{
	private boolean _isDecode = false;

	@Override
	public boolean parseHeader() throws IOException
	{
		Programs p = Programs.values()[readD()];
		int major = readD();
		int minor = readD();
		byte type = readC();
		int number = readD();
		Version v = new Version(p, major, minor, type, number);

		ListenerType list = ListenerType.values()[readC()];
		long sessionId = readQ();
		_isDecode = readBoolC();

		_session = new Session(list, sessionId);
		_session.setVersion(v);
		return true;
	}

	@Override
	public void parsePackets() throws IOException
	{
		int size = readD();
		PacketType[] values = PacketType.values();
		for(int i = 0; i < size; i ++)
		{
			PacketType t = values[readC()];
			long time = readQ();
			int sizeArray = readD();
			byte[] data = readB(sizeArray);

			NotDecryptPacket packet = new NotDecryptPacket(t, NioBuffer.wrap(data), time);

			if(_isDecode)
			{
				DecryptPacket dp = new DecryptPacket(packet, _session.getProtocol());

				_session.receiveQuitPacket(dp);
			}
			else
			{				
				_session.receiveQuitPacket(packet);
			}
		}
	}

	@Override
	public String getFileExtension()
	{
		return "jnl2";
	}

	@Override
	public String getReaderInfo()
	{
		return "Jn log file";
	}
}
