package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:45:36/07.04.2010
 */
public class JInteger implements ParserValue<Integer>
{
	private static final Color _color = new Color(72, 164, 255);

	@Override
	public java.lang.Integer getValue(NioBuffer b, Object... arg)
	{
		return b.getInt();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_INT;
	}

	@Override
	public Color getColor()
	{
		return _color;
	}
}
