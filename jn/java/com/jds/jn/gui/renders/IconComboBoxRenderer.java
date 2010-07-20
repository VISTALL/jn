package com.jds.jn.gui.renders;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.jds.jn.parser.PartType;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.statics.ImageStatic;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 25.09.2009
 * Time: 16:41:34
 */
public class IconComboBoxRenderer extends JLabel implements ListCellRenderer
{
	private static final ImageIcon[] _images;
	public static final String[] _types;

	static
	{
		Collection<PartType> t = PartTypeManager.getInstance().getTypes();
		List<PartType> types = new ArrayList<PartType>(t.size());
		for (PartType type : t)
		{
			if (ImageStatic.getInstance().getIconForPartType(type) != null)
			{
				types.add(type);
			}
		}

		_types = new String[types.size()];
		_images = new ImageIcon[types.size()];

		for (int i = 0; i < types.size(); i++)
		{
			_types[i] = types.get(i).getName();
			_images[i] = ImageStatic.getInstance().getIconForPartType(types.get(i));
		}
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		int selectedIndex = searchIndex(value);
		if (selectedIndex < 0 || selectedIndex >= _types.length)
		{
			selectedIndex = index;
		}
		if (selectedIndex < 0 || selectedIndex >= _types.length)
		{
			selectedIndex = 0;
		}

		setForeground(Color.BLACK);
		setBackground(Color.WHITE);

		ImageIcon icon = _images[selectedIndex];
		setText(_types[selectedIndex]);
		setIcon(icon);

		return this;
	}

	private int searchIndex(Object value)
	{
		if (value instanceof String)
		{
			String str = (String) value;
			int i = 0;
			for (String type : _types)
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
