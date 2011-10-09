package com.jds.jn.ui;

import javax.swing.JTabbedPane;

/**
 * @author VISTALL
 * @date 22:43/06.10.2011
 */
public class JTabs extends JTabbedPane
{
	public JTabs()
	{
		//
	}

	public void addTab(JTab t)
	{
		addTab(t.getTitle(), t.getComponent());
	}
}
