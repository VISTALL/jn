package com.jds.jn.logs.readers;

import java.io.IOException;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.session.Session;
import com.jds.jn.util.version_control.Program;
import com.jds.jn.util.version_control.Version;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:32:04/20.06.2010
 *
 * version info
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
 * d - size of packets
 * {
 *    c - packet type
 *    Q - time millis
 *    d - size of bytes
 *    b - bytes of size - up
 * }
 */
public class JNL2Reader extends AbstractReader
{
	private boolean _isDecrypted = false;

	@Override
	public boolean parseHeader() throws IOException
	{
		Program p = Program.values()[readD()];
		int major = readD();
		int minor = readD();
		byte type = readC();
		int number = readD();
		Version v = new Version(p, major, minor, type, number);

		ListenerType list = ListenerType.values()[readC()];
		long sessionId = readQ();
		_isDecrypted = readBoolC();

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

			CryptedPacket packet = new CryptedPacket(t, data, time, _session.getProtocol().getOrder());

			if(_isDecrypted)
			{
				DecryptedPacket dp = new DecryptedPacket(packet, _session.getProtocol());

				_session.receiveQuitPacket(dp, true, true);
			}
			else
			{				
				_session.receiveQuitPacket(packet);
			}

			int p = (int) ((100D * (i + 1)) / size);
			MainForm.getInstance().getProgressBar().setValue(p);
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
