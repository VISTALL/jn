package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:56:15/07.04.2010
 */
public class JFloat extends ParserValue.DigitalValueParser<Float>
{
	@Override
	public Float getValue(NioBuffer b, Part part)
	{
		return b.getFloat();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_FLOAT;
	}

	@Override
	public Color getColor()
	{
		return Color.LIGHT_GRAY;
	}

	@Override
	public int length()
	{
		return 4;
	}
}
