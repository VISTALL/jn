package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:45:06/07.04.2010
 */
public class JUnsignedInt extends ParserValue.DigitalValueParser<Long>
{
	private static final Color _color = new Color(72, 164, 255);

	@Override
	public Long getValue(NioBuffer b, Part part)
	{
		return b.getUnsignedInt();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_UINT;
	}

	@Override
	public Color getColor()
	{
		return _color;
	}

	@Override
	public int length()
	{
		return 4;
	}
}
