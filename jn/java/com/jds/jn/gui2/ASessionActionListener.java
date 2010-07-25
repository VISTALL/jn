package com.jds.jn.gui2;

import java.awt.event.ActionListener;

import com.jds.jn.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:49:22/22.07.2010
 */
public abstract class ASessionActionListener implements ActionListener
{
	protected final Session _session;

	public ASessionActionListener(Session session)
	{
		_session = session;
	}
}
