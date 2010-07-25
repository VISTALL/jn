package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.statics.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:57:20/07.04.2010
 */
public class JLong implements ParserValue<Long>
{
	private static final Color _color = new Color(255, 255, 128);

	@Override
	public Long getValue(NioBuffer b, Object... arg)
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
}
