package com.jds.jn.gui.listeners;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.jds.jn.config.RValues;
import com.jds.swing.JColor;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:53:40/06.06.2010
 */
public class ColorChooseMouseListener extends MouseAdapter
{
	private final JPanel _panel;
	private final RValues _value;
	private final String _title;
	private final Component _component;

	public ColorChooseMouseListener(Component component, JPanel panel, RValues value, String title)
	{
		_component = component;
		_panel = panel;
		_value = value;
		_title = title;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		JColor color = _value.asTColor();
		Color c = JColorChooser.showDialog(_component, _title, color);

		if (c != null)
		{
			_value.setVal(new JColor(c));
			_panel.setBackground(c);
		}
	}
}
