package com.jds.jn.parser;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.parservalue.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:49:11/07.04.2010
 */
public enum Type
{
	c(JByte.class),
	uc(JUnsignedByte.class),
	h(JShort.class),
	uh(JUnsignedShort.class),
	bch(JBitCountShort.class),
	d(JInteger.class),
	ud(JUnsignedInt.class),
	Q(JLong.class),
	f(JFloat.class),
	D(JDouble.class),

	S(JUnicodeString.class),
	LS(JLimitedUnicodeString.class),
	s(JANSIString.class),
	ls(JLimitedANSIString.class);

	private ParserValue<?> _instance;

	Type(Class<? extends ParserValue<?>> c)
	{
		try
		{
			_instance = c.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ParserValue<?> getInstance()
	{
		return _instance;
	}

	public ImageIcon getIcon()
	{
		return _instance.getIcon();
	}

	public Color getColor()
	{
		return _instance.getColor();
	}
}
