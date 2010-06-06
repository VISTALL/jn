package com.jds.jn.util;

import java.awt.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:28:31/06.06.2010
 */
public class TColor extends Color
{
	public TColor(Color c)
	{
		super(c.getRed(), c.getGreen(), c.getBlue());
	}

	public TColor(int r, int g, int b)
	{
		super(r, g, b);
	}

	@Override
	public String toString()
	{
		return String.valueOf(getRGB());
	}
}
