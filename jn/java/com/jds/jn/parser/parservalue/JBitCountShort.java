package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:54:31/07.04.2010
 */
public class JBitCountShort implements ParserValue<Integer>
{
	private static final Color _color = new Color(189, 148, 6);

	@Override
	public Integer getValue(NioBuffer b, Object... arg)
	{
		return Integer.bitCount(b.getShort());
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_SHORT;
	}

	@Override
	public Color getColor()
	{
		return _color;
	}
}
