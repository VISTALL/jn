package com.jds.swing;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: VISTALL
 * I not a author. @author INTERNET
 * Company: J Develop Station
 * Date: 20.09.2009
 * Time: 22:53:16
 */
public class JTrayIcon extends TrayIcon
{
	private JPopupMenu menu;
	private static JDialog dialog;

	static
	{
		dialog = new JDialog((Frame) null);
		dialog.setUndecorated(true);
		dialog.setAlwaysOnTop(true);
	}

	private static PopupMenuListener popupListener = new PopupMenuListener()
	{
		public void popupMenuWillBecomeVisible(PopupMenuEvent e)
		{
		}

		public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
		{
			dialog.setVisible(false);
		}

		public void popupMenuCanceled(PopupMenuEvent e)
		{
			dialog.setVisible(false);
		}
	};

	public JTrayIcon(Image image)
	{
		super(image);

		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				showJPopupMenu(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				showJPopupMenu(e);
			}
		});
	}

	protected void showJPopupMenu(MouseEvent e)
	{
		if (e.isPopupTrigger() && menu != null)
		{
			Dimension size = menu.getPreferredSize();
			showJPopupMenu(e.getX(), e.getY() - size.height);
		}
	}

	public void setJPopupMenu(JPopupMenu menu)
	{
		if (this.menu != null)
		{
			this.menu.removePopupMenuListener(popupListener);
		}
		this.menu = menu;
		menu.addPopupMenuListener(popupListener);
	}

	protected void showJPopupMenu(int x, int y)
	{
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		menu.show(dialog.getContentPane(), 0, 0);
		// popup works only for focused windows
		dialog.toFront();
	}
}
