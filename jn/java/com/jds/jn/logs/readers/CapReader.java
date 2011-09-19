package com.jds.jn.logs.readers;

import java.io.IOException;

import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.session.Session;
import com.jds.jn.util.version_control.Program;
import com.jds.jn.util.version_control.Version;

/**
 * @author VISTALL
 * @date 18:40/03.03.2011
 */
public class CapReader extends AbstractReader
{

	@Override
	public boolean parseHeader() throws IOException
	{
		/*
		 * d - header
		 */
		readB(57);


		return true;
	}

	@Override
	public void parsePackets() throws IOException
	{
		while(true)
		{
			try
			{
				readB(23);
				int size = readD();
				int size2 = readD();
				if(size != size2)
					break;
				int pos = _buffer.position();

				readB(6); // desc address
				readB(6); // source address
				readH(); // ethernet type
				readC(); // version
				readC(); // service field
				readH();   // total lenght
				readH();	// indefication
				readH();	// fragment flags
				readC(); // time to live
				readC(); // next protocol
				readH(); // checksum
				readD(); // source address
				readD(); // desc address
				int sourcePort = makeShort(readC(), readC());
				int descPort = makeShort(readC(), readC());
				readD(); // sequence number
				readD(); // ask number
				readC(); // flags
				readH(); // window
				readH(); // checksum
				readH(); // urgent pointer
				readC();

				NetworkProfile profile = NetworkProfiles.getInstance().active();

				PacketType p = null;
				if(_session == null)
				{
					ListenerType t = ListenerType.Game_Server;
					if(profile.getPart(ListenerType.Auth_Server).getDevicePort() == sourcePort || profile.getPart(ListenerType.Auth_Server).getDevicePort() == descPort)
						t = ListenerType.Auth_Server;

					_session = new Session(t, sourcePort * descPort);
					_session.setVersion(new Version(Program.UNKNOWN, 1, 0, Version.STABLE, 1));
				}

				ListenerType listenerType = _session.getListenerType();
				NetworkProfilePart part = profile.getPart(listenerType);
				p = part.getDevicePort() == sourcePort ? PacketType.SERVER : PacketType.CLIENT;

				int diff = _buffer.position() - pos;
				if(diff >= size)
					continue;

				byte a1 = readC();
				byte a2 = readC();

				byte[] data = readB(makeShort(a2, a1) - 2);
				CryptedPacket packet = new CryptedPacket(p, data, System.currentTimeMillis());

				_session.receiveQuitPacket(packet);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				break;
			}
		}
	}

	static private short makeShort(byte b1, byte b0)
	{
		return (short) ((b1 << 8) | (b0 & 0xff));
	}

	@Override
	public String getFileExtension()
	{
		return "cap";
	}

	@Override
	public String getReaderInfo()
	{
		return "MS Network Monitor log file";
	}
}
