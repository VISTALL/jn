package com.jds.swing;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * @author balajihe
 */
public class XCheckedButton extends JButton
{
	/**
	 * These colors are required in order to simulate the JMenuItem's L&F
	 */
	public static final Color MENU_HIGHLIGHT_BG_COLOR = UIManager.getColor("MenuItem.selectionBackground");
	public static final Color MENU_HIGHLIGHT_FG_COLOR = UIManager.getColor("MenuItem.selectionForeground");
	public static final Color MENUITEM_BG_COLOR = UIManager.getColor("MenuItem.background");
	public static final Color MENUITEM_FG_COLOR = UIManager.getColor("MenuItem.foreground");

	public XCheckedButton()
	{
		super();

		init();
	}

	public XCheckedButton(Action a)
	{
		super(a);
		init();
	}

	public XCheckedButton(Icon icon)
	{
		super(icon);
		init();
	}

	public XCheckedButton(String text, Icon icon)
	{
		super(text, icon);
		init();
	}

	public XCheckedButton(String text)
	{
		super(text);
		init();
	}

	/**
	 * Initialize component LAF and add Listeners
	 */
	private void init()
	{
		MouseAdapter mouseAdapter = getMouseAdapter();

		//	Basically JGoodies LAF UI for JButton does not allow Background color to be set.
		// So we need to set the default UI,
		ComponentUI ui = BasicButtonUI.createUI(this);
		setUI(ui);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		setMenuItemDefaultColors();
		setHorizontalTextPosition(SwingConstants.RIGHT);
		setHorizontalAlignment(SwingConstants.LEFT);
		setModel(new XCheckedButtonModel());
		setSelected(false);
		this.addMouseListener(mouseAdapter);

	}

	private void setMenuItemDefaultColors()
	{
		XCheckedButton.this.setBackground(MENUITEM_BG_COLOR);
		XCheckedButton.this.setForeground(MENUITEM_FG_COLOR);
	}

	/**
	 * @return
	 */
	private MouseAdapter getMouseAdapter()
	{
		return new MouseAdapter()
		{
			// For static menuitems, the background color remains the highlighted color, if this is not overridden
			public void mousePressed(MouseEvent e)
			{
				setMenuItemDefaultColors();
			}

			public void mouseEntered(MouseEvent e)
			{
				XCheckedButton.this.setBackground(MENU_HIGHLIGHT_BG_COLOR);
				XCheckedButton.this.setForeground(MENU_HIGHLIGHT_FG_COLOR);
			}

			public void mouseExited(MouseEvent e)
			{
				setMenuItemDefaultColors();
			}

		};
	}

	private class XCheckedButtonModel extends JToggleButton.ToggleButtonModel
	{
		/*
		 * Need to Override keeping the super code, else the check mark won't come
		 */
		public void setSelected(boolean b)
		{
			ButtonGroup group = getGroup();
			if(group != null)
			{
				// use the group model instead
				group.setSelected(this, b);
				b = group.isSelected(this);
			}

			if(isSelected() == b)
				return;

			if(b)
			{
				stateMask |= SELECTED;
			}
			else
			{
				stateMask &= ~SELECTED;
			}

			//			 Send ChangeEvent
			fireStateChanged();

			// Send ItemEvent
			fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this, this.isSelected() ? ItemEvent.SELECTED : ItemEvent.DESELECTED));
		}
	}
}