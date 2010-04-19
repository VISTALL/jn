package com.jds.jn.gui.renders;

import com.jds.jn.statics.ImageStatic;

import javax.swing.*;
import java.awt.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 25.09.2009
 * Time: 16:41:34
 */
public class IconComboBoxRenderer extends JLabel implements ListCellRenderer
{
	private static final ImageIcon[] images = {
			ImageStatic.PART_BYTE,
			ImageStatic.PART_UBYTE,
			ImageStatic.PART_SHORT,
			ImageStatic.PART_USHORT,

	};
	public static final String[] types = {
			"c",
			"uc",
			"h",
			"uh"
	};

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		int selectedIndex = searchIndex(value);
		if (selectedIndex < 0 || selectedIndex >= types.length)
		{
			selectedIndex = index;
		}
		if (selectedIndex < 0 || selectedIndex >= types.length)
		{
			selectedIndex = 0;
		}

		//	if (isSelected)
		//		{
		//			setBackground(list.getSelectionBackground());
		setForeground(Color.BLACK);
		//		}
		//	else
		//{
		//		setBackground(list.getBackground());
		setForeground(Color.BLACK);
		//	}
		// Set the icon and text. If icon was null, say so.
		ImageIcon icon = images[selectedIndex];
		setText(types[selectedIndex]);
		setIcon(icon);
		/*	if (icon != null)
				{
					setFont(list.getFont());
				} */

		return this;
	}

	private int searchIndex(Object value)
	{
		if (value instanceof String)
		{
			String str = (String) value;
			int i = 0;
			for (String type : types)
			{
				if (type.equals(str))
				{
					return i;
				}
				i++;
			}
		}
		return -1;
	}

}
