package com.jds.jn.ui;

import javax.swing.JComponent;

/**
 * @author VISTALL
 * @date 22:44/06.10.2011
 */
public class JTab
{
	private final JComponent _component;
	private String _title;

	public JTab(JComponent c)
	{
		_component = c;
	}

	public String getTitle()
	{
		return _title;
	}

	public JTab setTitle(String title)
	{
		_title = title;
		return this;
	}

	public JComponent getComponent()
	{
		return _component;
	}
}
