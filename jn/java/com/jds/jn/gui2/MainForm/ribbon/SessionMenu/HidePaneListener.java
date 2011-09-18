package com.jds.jn.gui2.MainForm.ribbon.SessionMenu;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.panels.viewpane.HiddenPanel;
import com.jds.jn.gui2.ASessionActionListener;
import com.jds.jn.session.Session;

/**
 * @author VISTALL
 * @date 15:08/18.09.2011
 */
public class HidePaneListener extends ASessionActionListener
{
	private HiddenPanel _panel;

	public HidePaneListener(Session session, HiddenPanel panel)
	{
		super(session);
		_panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		ViewPane pane = _session.getViewPane();
		if(pane == null)
			return;

		if(!_panel.isHidden())
			pane.removeTab(_panel);
		else
			pane.addTab(((JCheckBox)e.getSource()).getText(), _panel, false);
	}
}
