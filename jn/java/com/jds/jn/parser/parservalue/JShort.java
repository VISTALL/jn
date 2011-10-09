package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:46:21/07.04.2010
 */
public class JShort extends ParserValue.DigitalValueParser<Short>
{
	@Override
	public java.lang.Short getValue(NioBuffer b, Part part)
	{
		return b.getShort();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_SHORT;
	}

	@Override
	public Color getColor()
	{
		return Color.ORANGE;
	}

	@Override
	public int length()
	{
		return 2;
	}
}
