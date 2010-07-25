package com.jds.jn.gui2.MainForm.ribbon.SessionMenu;

import java.awt.event.ActionEvent;

import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui2.ASessionActionListener;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:34:17/22.07.2010
 */
class PacketListActionListener extends ASessionActionListener
{
	public PacketListActionListener(Session session)
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

		if (!pane.getPacketList().isHide())
		{
			int index = pane.getIndex(pane.getPacketList());
			pane.getPacketAndSearch().removeTabAt(index);
			pane.getPacketList().setIsHide(true);
		}
		else
		{
			pane.getPacketAndSearch().addTab(Bundle.getString("PacketList"), pane.getPacketList());
			pane.getPacketList().setIsHide(false);
		}
	}
}
