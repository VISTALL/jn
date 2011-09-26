package com.jds.jn.gui.renders;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 20:57:40
 */
public class PacketTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer
{
	private TooltipTable _table;

	public PacketTableRenderer(TooltipTable table)
	{
		_table = table;
	}

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
			c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		if (c instanceof JComponent)
		{
			JComponent jc = (JComponent) c;
			jc.setToolTipText(_table.getToolTip(row, col));
		}

		if (_table.getIsMarked(row) && !isSelected)
			c.setBackground(Color.YELLOW);
		else if (!isSelected)
			c.setBackground(table.getBackground());


		return c;
	}

	public interface TooltipTable
	{
		public String getToolTip(int row, int col);

		public boolean getIsMarked(int row);
	}
}
