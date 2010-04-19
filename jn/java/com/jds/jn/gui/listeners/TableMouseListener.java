package com.jds.jn.gui.listeners;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 22:33:55
 */
public class TableMouseListener implements MouseListener
{
	private JTable _table;

	private void forwardEvent(MouseEvent e)
	{
		TableColumnModel columnModel = _table.getColumnModel();
		int column = columnModel.getColumnIndexAtX(e.getX());
		int row = e.getY() / _table.getRowHeight();
		Object value;
		JComponent c;

		if (row >= _table.getRowCount() || row < 0 || column >= _table.getColumnCount() || column < 0)
		{
			return;
		}

		value = _table.getValueAt(row, column);

		if (!(value instanceof JComponent))
		{
			return;
		}

		c = (JComponent) value;

		if (c instanceof JButton)
		{
			JButton b = (JButton) c;
			if (e.getID() == MouseEvent.MOUSE_PRESSED)
			{
				b.doClick(100);
				b.setFocusPainted(true);
			}
		}
		c.dispatchEvent(new MouseEvent(c, e.getID(), e.getWhen(), e.getModifiers(), 0, 0, 1, e.isPopupTrigger(), e.getButton()));
	}

	public TableMouseListener(JTable table)
	{
		_table = table;
	}

	public void mouseEntered(MouseEvent e)
	{
		forwardEvent(e);
	}

	public void mouseExited(MouseEvent e)
	{
		forwardEvent(e);
	}

	public void mousePressed(MouseEvent e)
	{
		forwardEvent(e);
	}

	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() instanceof JButton)
		{
			forwardEvent(e);
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if (e.getSource() instanceof JButton)
		{
			forwardEvent(e);
		}
	}
}
