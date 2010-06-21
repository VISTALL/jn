package com.jds.jn.logs.writers;

import java.io.IOException;
import java.util.List;

import com.jds.jn.config.RValues;
import com.jds.jn.network.packets.IPacketData;
import com.jds.jn.session.Session;
import com.jds.jn.util.Version;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:34:07/20.06.2010
 */
public class JNL2Writer  extends AbstractWriter
{
	protected JNL2Writer(String file, Session session) throws IOException
	{
		super(file, session);
	}

	@Override
	protected void writeHeader() throws IOException
	{
		writeD(Version.currentEnum().ordinal());
		writeC(_session.getListenerType().ordinal());
		writeQ(_session.getSessionId());
		writeBoolC(RValues.SAVE_AS_DECODE.asBoolean());
	}

	@Override
	protected void writePackets() throws IOException
	{
		List<? extends IPacketData> packets = RValues.SAVE_AS_DECODE.asBoolean() ? _session.getDecryptPackets() :  _session.getNotDecryptPackets();

		writeD(packets.size());

		for (IPacketData p : packets)
		{
			writeD(p.getPacketType().ordinal());
			writeQ(p.getTime());
			writeD(p.getAllData().length);
			writeB(p.getAllData());
		}
	}
}
