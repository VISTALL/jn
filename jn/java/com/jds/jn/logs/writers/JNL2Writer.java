package com.jds.jn.logs.writers;

import java.io.IOException;
import java.util.List;

import com.jds.jn.config.RValues;
import com.jds.jn.network.packets.IPacket;
import com.jds.jn.util.version_control.Program;
import com.jds.jn.util.version_control.Version;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:34:07/20.06.2010
 *
 * version control
 * ----------------- 
 * d - program type
 * d - major
 * d - minor
 * c - type
 * d - number
 * ------------
 * session info
 * c - listener type
 * Q - session id
 * c - decode ?
 */
public class JNL2Writer  extends AbstractWriter
{
	@Override
	protected void writeHeader() throws IOException
	{
		//
		writeD(Program.JN.ordinal());
		writeD(Version.CURRENT.getMajor());
		writeD(Version.CURRENT.getMinor());
		writeC(Version.CURRENT.getType());
		writeD(Version.CURRENT.getNumber());

		writeC(_session.getListenerType().ordinal());
		writeQ(_session.getSessionId());
		writeBoolC(RValues.SAVE_AS_DECODE.asBoolean());
	}

	@Override
	protected void writePackets() throws IOException
	{
		List<? extends IPacket> packets = RValues.SAVE_AS_DECODE.asBoolean() ? _session.getDecryptPackets() :  _session.getCryptedPackets();

		writeD(packets.size());

		for (IPacket p : packets)
		{
			writeC(p.getPacketType().ordinal());
			writeQ(p.getTime());
			writeD(p.getAllData().length);
			writeB(p.getAllData());
		}
	}

	@Override
	public String getFileExtension()
	{
		return "jnl2";
	}

	@Override
	public String getWriterInfo()
	{
		return "Jn log format";
	}
}
