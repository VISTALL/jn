package com.jds.jn.gui2.MainForm.ribbon.SessionMenu;

import java.awt.event.ActionEvent;

import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui2.ASessionActionListener;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:51:29/22.07.2010
 */
class InfoActionListener extends ASessionActionListener
{
	public InfoActionListener(Session session)
	{
		super(session);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		ViewPane pane = _session.getViewPane();
		if (pane == null)
		{
			return;
		}
		if (!pane.getInfoPane().IS_HIDE)
		{
			int index = pane.getIndex(pane.getInfoPane());
			pane.getPacketAndSearch().removeTabAt(index);
			pane.getInfoPane().IS_HIDE = true;
		}
		else
		{
			pane.getPacketAndSearch().addTab(Bundle.getString("Info"), pane.getInfoPane());
			pane.getInfoPane().IS_HIDE = false;
		}

	}
}
