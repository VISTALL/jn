package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * @author VISTALL
 * @date 22:59/04.07.2011
 */
public class JLimitedUnicodeString extends ParserValue.StringValueParser
{
	private static Color _color = new Color(100, 255, 100);

	@Override
	public String getValue(NioBuffer b, Part part)
	{
		StringBuilder sb2 = new StringBuilder();
		for(int i = 0; i < part.getBSize(); i++)
			sb2.append(b.getChar());
		return sb2.toString().trim();
	}

	@Override
	public ImageIcon getIcon()
	{
		return ImageStatic.PART_NORMAL_STRING;
	}

	@Override
	public Color getColor()
	{
		return _color;
	}

	@Override
	public int length()
	{
		return 0;
	}
}
