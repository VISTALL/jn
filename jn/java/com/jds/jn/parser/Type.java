package com.jds.jn.parser;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.parservalue.JANSIString;
import com.jds.jn.parser.parservalue.JBitCountShort;
import com.jds.jn.parser.parservalue.JByte;
import com.jds.jn.parser.parservalue.JDouble;
import com.jds.jn.parser.parservalue.JFloat;
import com.jds.jn.parser.parservalue.JInteger;
import com.jds.jn.parser.parservalue.JLimitedUnicodeString;
import com.jds.jn.parser.parservalue.JLong;
import com.jds.jn.parser.parservalue.JShort;
import com.jds.jn.parser.parservalue.JUnicodeString;
import com.jds.jn.parser.parservalue.JUnsignedByte;
import com.jds.jn.parser.parservalue.JUnsignedInt;
import com.jds.jn.parser.parservalue.JUnsignedShort;
import com.jds.jn.parser.parservalue.ParserValue;

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
	s(JANSIString.class);

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
