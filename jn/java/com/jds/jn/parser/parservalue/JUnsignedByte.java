package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.statics.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:52:51/07.04.2010
 */
public class JUnsignedByte implements ParserValue<Short>
{
	@Override
	public Short getValue(NioBuffer b, Object... arg)
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
}
