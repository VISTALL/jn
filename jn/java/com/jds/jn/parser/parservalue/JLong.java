package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:57:20/07.04.2010
 */
public class JLong implements ParserValue<Long>
{
	private static final Color _color = new Color(127, 127, 127);

	@Override
	public Long getValue(NioBuffer b, Part part, Object... arg)
	{
		return b.getLong();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_LONG;
	}

	@Override
	public Color getColor()
	{
		return _color;
	}

	@Override
	public int length()
	{
		return 8;
	}
}
