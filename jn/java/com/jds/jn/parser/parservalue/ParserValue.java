package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.parttypes.PartType;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:43:06/07.04.2010
 */
public interface ParserValue<T>
{
	public T getValue(NioBuffer b, Part part);

	public ImageIcon getIcon();

	public Color getColor();

	public int length();

	public PartType.PartValueType getValueType();

	public static abstract class DigitalValueParser<T extends Number> implements ParserValue<T>
	{
		public PartType.PartValueType getValueType()
		{
			return PartType.PartValueType.DIGITAL;
		}
	}

	public static abstract class StringValueParser implements ParserValue<String>
	{
		public PartType.PartValueType getValueType()
		{
			return PartType.PartValueType.STRING;
		}
	}
}
