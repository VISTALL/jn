package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:53:13/07.04.2010
 */
public class JByte implements ParserValue<Byte>
{
	@Override
	public Byte getValue(NioBuffer b, Object... arg)
	{
		return b.get();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_BYTE;
	}

	@Override
	public Color getColor()
	{
		return Color.PINK;
	}
}
