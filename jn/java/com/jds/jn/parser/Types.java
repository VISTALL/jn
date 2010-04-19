package com.jds.jn.parser;

import com.jds.jn.parser.parservalue.*;
import com.jds.jn.statics.ImageStatic;

import javax.swing.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:49:11/07.04.2010
 */
public enum Types
{
	c(JByte.class, ImageStatic.PART_BYTE),
	uc(JUnsignedByte.class, ImageStatic.PART_UBYTE),
	h(JShort.class, ImageStatic.PART_SHORT),
	uh(JUnsignedShort.class, ImageStatic.PART_USHORT),
	bch(JBitCountShort.class, ImageStatic.PART_SHORT),
	d(JInteger.class, ImageStatic.PART_INT),
	ud(JUnsignedInt.class, ImageStatic.PART_UINT),
	Q(JLong.class, ImageStatic.PART_LONG),
	f(JFloat.class, ImageStatic.PART_FLOAT),
	D(JDouble.class, ImageStatic.PART_DOUBLE);

	private final Class<? extends JIParserValue<?>> _class;
	private JIParserValue<?> _instance;
	private ImageIcon _icon;

	Types(Class<? extends JIParserValue<?>> c, ImageIcon ico)
	{
		_class = c;
		_icon = ico;
	}

	public static void newInstance()
	{
		for (Types t : values())
		{
			try
			{
				t._instance = t._class.newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public JIParserValue<?> getInstance()
	{
		return _instance;
	}

	public ImageIcon getIcon()
	{
		return _icon;
	}
}
