package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:54:31/07.04.2010
 */
public class JBitCountShort extends ParserValue.DigitalValueParser<Integer>
{
	private static final Color _color = new Color(189, 148, 6);

	@Override
	public Integer getValue(NioBuffer b, Part part)
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

	@Override
	public int length()
	{
		return 2;
	}
}
