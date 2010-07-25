package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.statics.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:56:15/07.04.2010
 */
public class JFloat implements ParserValue<Float>
{
	@Override
	public Float getValue(NioBuffer b, Object... arg)
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
}
