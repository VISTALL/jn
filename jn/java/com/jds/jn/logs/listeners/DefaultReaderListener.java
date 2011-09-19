package com.jds.jn.logs.listeners;

import java.io.File;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:11:47/03.09.2010
 */
public class DefaultReaderListener implements ReaderListener
{
	@Override
	public DecryptedPacket newPacket(Session session, CryptedPacket p)
	{
		return new DecryptedPacket(session, p.getPacketType(), p.getAllData(), p.getTime(),  session.getProtocol(), false);
	}

	@Override
	public void readPacket(Session session, DecryptedPacket p)
	{
		session.receiveQuitPacket(p, true, true);
	}

	@Override
	public void readPacket(Session session, CryptedPacket p)
	{
		session.receiveQuitPacket(p);
	}

	@Override
	public void onFinish(Session session, File file)
	{
		if(session != null)
		{
			MainForm.getInstance().showSession(session);
		}
	}
}
