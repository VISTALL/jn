package com.jds.jn.logs.listeners;

import java.io.File;

import com.jds.jn.session.Session;

/**
 * @author VISTALL
 * @date 17:10:37/03.09.2010
 */
public interface ReaderListener
{
	void onFinish(Session session, File file);
}
