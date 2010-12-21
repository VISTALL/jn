package com.jds.jn.logs.listeners;

import java.io.File;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:11:47/03.09.2010
 */
public class DefaultReaderListener implements ReaderListener
{
	@Override
	public void onFinish(Session session, File file)
	{
		if(session != null)
		{
			MainForm.getInstance().showSession(session);
		}
	}
}
