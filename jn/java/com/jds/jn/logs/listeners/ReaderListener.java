package com.jds.jn.logs.listeners;

import java.io.File;

import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:10:37/03.09.2010
 */
public interface ReaderListener
{
	DecryptedPacket newPacket(Session session, CryptedPacket p);

	void readPacket(Session session, DecryptedPacket p);

	void readPacket(Session session, CryptedPacket p);

	void onFinish(Session session, File file);
}
