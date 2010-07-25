package com.jds.jn.parser.parservalue;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.statics.ImageStatic;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:45:06/07.04.2010
 */
public class JUnsignedInt implements ParserValue<Long>
{
	private static final Color _color = new Color(72, 164, 255);

	@Override
	public Long getValue(NioBuffer b, Object... arg)
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
}
