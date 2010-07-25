package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.statics.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:56:56/07.04.2010
 */
public class JDouble  implements ParserValue<Double>
{
	@Override
	public Double getValue(NioBuffer b, Object... arg)
	{
		return b.getDouble();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_DOUBLE;
	}

	@Override
	public Color getColor()
	{
		return Color.DARK_GRAY;
	}
}
