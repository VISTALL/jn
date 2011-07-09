package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:56:56/07.04.2010
 */
public class JDouble  implements ParserValue<Double>
{
	@Override
	public Double getValue(NioBuffer b, Part part, Object... arg)
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

	@Override
	public int length()
	{
		return 8;
	}
}
