package com.jds.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/**
 * This class implements a scrollable Popup Menu
 *
 * @author balajihe
 */
public class XJPopupMenu extends JPopupMenu implements ActionListener
{
	private JPanel panelMenus = new JPanel();

	private List<XCheckedButton> _items = new ArrayList<XCheckedButton>();

	public XJPopupMenu()
	{
		super();

		setLayout(new BorderLayout());
		panelMenus.setLayout(new GridLayout(0, 1));
		panelMenus.setBackground(UIManager.getColor("MenuItem.background"));
		add(panelMenus, BorderLayout.CENTER);
	}

	public void show(Component invoker, int x, int y)
	{
		panelMenus.validate();

		this.pack();
		this.setInvoker(invoker);
		Point invokerOrigin = invoker.getLocationOnScreen();
		this.setLocation((int) invokerOrigin.getX() + x, (int) invokerOrigin.getY() + y);
		this.setVisible(true);
	}

	public void hideMenu()
	{
		if(isVisible())
			setVisible(false);
	}

	public void add(XCheckedButton menuItem)
	{
		if(menuItem == null)
			return;

		_items.add(menuItem);
		panelMenus.add(menuItem);
		menuItem.removeActionListener(this);
		menuItem.addActionListener(this);
	}

	@Override
	public void addSeparator()
	{
		panelMenus.add(new XSeperator());
	}

	public void actionPerformed(ActionEvent e)
	{
		this.hideMenu();
	}

	@Override
	public Component[] getComponents()
	{
		return panelMenus.getComponents();
	}

	public void removeItems()
	{
		for(XCheckedButton b : _items)
		{
			b.removeActionListener(this);
			panelMenus.remove(b);
		}
	}

	private static class XSeperator extends JSeparator
	{
		XSeperator()
		{
			ComponentUI ui = XBasicSeparatorUI.createUI(this);
			XSeperator.this.setUI(ui);
		}

		private static class XBasicSeparatorUI extends BasicSeparatorUI
		{

			public static ComponentUI createUI(JComponent c)
			{
				return new XBasicSeparatorUI();
			}

			public void paint(Graphics g, JComponent c)
			{
				Dimension s = c.getSize();

				if(((JSeparator) c).getOrientation() == JSeparator.VERTICAL)
				{
					g.setColor(c.getForeground());
					g.drawLine(0, 0, 0, s.height);

					g.setColor(c.getBackground());
					g.drawLine(1, 0, 1, s.height);
				}
				else // HORIZONTAL
				{
					g.setColor(c.getForeground());
					g.drawLine(0, 7, s.width, 7);

					g.setColor(c.getBackground());
					g.drawLine(0, 8, s.width, 8);
				}
			}
		}
	}

	public List<XCheckedButton> getItems()
	{
		return _items;
	}
}