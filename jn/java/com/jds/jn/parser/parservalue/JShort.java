package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.statics.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:46:21/07.04.2010
 */
public class JShort implements ParserValue<Short>
{
	@Override
	public java.lang.Short getValue(NioBuffer b, Object... arg)
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
}
