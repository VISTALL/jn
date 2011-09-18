package com.jds.jn.gui.panels.viewpane;

import javax.swing.JPanel;

/**
 * @author VISTALL
 * @date 16:04/18.09.2011
 */
public class HiddenPanel extends JPanel
{
	private boolean _hidden;

	public boolean isHidden()
	{
		return _hidden;
	}

	public void setHidden(boolean hidden)
	{
		_hidden = hidden;
	}
}
