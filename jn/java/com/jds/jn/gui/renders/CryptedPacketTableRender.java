package com.jds.jn.gui.renders;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 20:57:40
 */
public class CryptedPacketTableRender extends DefaultTableCellRenderer implements TableCellRenderer
{
	@Override
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