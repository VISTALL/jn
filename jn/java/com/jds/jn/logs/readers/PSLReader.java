package com.jds.jn.logs.readers;

import java.io.IOException;

import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.*;
import com.jds.jn.session.Session;
import com.jds.jn.version_control.Programs;
import com.jds.jn.version_control.Version;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  12:39:26/25.06.2010
 */
public class PSLReader extends AbstractReader
{
	private int _size;
	private boolean _isDecrypted;
	
	@Override
	public boolean parseHeader() throws IOException
	{
		readC(); //log version
	   	_size = readD();
		readC();
		readH();
		int port = readH();
		readD(); //client ip
		readD(); //server ip
		String pot = readS(); // protocol name
		readS(); //session comment
		readS(); //server type
		readQ();
		long sessionId = readQ();
		_isDecrypted = !readBoolC();

		_session = new Session(ListenerType.Game_Server, sessionId);
		_session.setVersion(new Version(Programs.PACKET_SAMURAI, 1, 0, Version.STABLE, 0));


		return true;
	}

	@Override
	public void parsePackets() throws IOException
	{
		for(int i = 0; i < _size; i ++)
		{
			PacketType type = readC() == 1 ? PacketType.SERVER : PacketType.CLIENT;
			int packetSize = readH();
			long time = readQ();
			byte[] data = readB(packetSize - 2);

			NotDecryptPacket packet = new NotDecryptPacket(type, data, time, _session.getProtocol().getOrder());

			if(_isDecrypted)
			{
				DecryptPacket p = new DecryptPacket(packet, _session.getProtocol());
				_session.receiveQuitPacket(p);
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
		return "psl";
	}

	@Override
	public String getReaderInfo()
	{
		return "Packet Samurai Log file";
	}
}
