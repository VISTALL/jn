package com.jds.jn.gui2.MainForm.ribbon.SessionMenu;

import java.awt.event.ActionEvent;

import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui2.ASessionActionListener;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:47:44/22.07.2010
 */
class FilterActionListener extends ASessionActionListener
{
	public FilterActionListener(Session session)
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
		if (!pane.getFilterPane().IS_HIDE)
		{
			int index = pane.getIndex(pane.getFilterPane());
			pane.getPacketAndSearch().removeTabAt(index);
			pane.getFilterPane().IS_HIDE = true;
		}
		else
		{
			pane.getPacketAndSearch().addTab(Bundle.getString("Filter"), pane.getFilterPane());
			pane.getFilterPane().IS_HIDE = false;
		}
	}
}
