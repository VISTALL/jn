package com.jds.jn.gui.listeners;

import com.jds.jn.Jn;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21.08.2009
 * Time: 3:20:20
 */
public class HideWatcher extends MouseAdapter
{
	private final JMenuItem _menuItem;

	public HideWatcher(JMenuItem item)
	{
		_menuItem = item;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			if (Jn.getForm().isVisible())
			{
				_menuItem.setText("Hide");
			}
			else
			{
				_menuItem.setText("Show");
			}
		}
	}
}
