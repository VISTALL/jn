package com.jds.jn.gui2.FindPacket.renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Author: VISTALL
 * Date:  0:08/21.12.2010
 */
public class FPRenderer extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		Component c;
		if (value instanceof Component)
		{
			c = (Component) value;
			if (isSelected)
			{
				c.setForeground(table.getSelectionForeground());
				c.setBackground(table.getSelectionBackground());
			}
		}
		else
		{
			c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		}
		return c;
	}
}
