package com.jds.jn.parser.parservalue;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  13:31:55/23.07.2010
 */
public class JLimitedANSIString extends ParserValue.StringValueParser
{
	private static Color _color = new Color(100, 255, 100);

	@Override
	public String getValue(NioBuffer buffer, Part part)
	{
		ByteBuffer bb = ByteBuffer.allocate(part.getBSize());

		byte b;
		while((b = buffer.get()) != 0)
		{
			if(bb.hasRemaining())
			{
				bb.put(b);
			}
		}

		bb.flip();

		try
		{
			return new String(bb.array(), "ASCII");
		}
		catch(UnsupportedEncodingException e)
		{
			throw new Error(e);
		}
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