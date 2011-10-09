package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:52:51/07.04.2010
 */
public class JUnsignedByte extends ParserValue.DigitalValueParser<Short>
{
	@Override
	public Short getValue(NioBuffer b, Part part)
	{
		return b.getUnsigned();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_UBYTE;
	}

	@Override
	public Color getColor()
	{
		return Color.MAGENTA;
	}

	@Override
	public int length()
	{
		return 1;
	}
}
