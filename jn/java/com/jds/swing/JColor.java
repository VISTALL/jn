package com.jds.swing;

import java.awt.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:28:31/06.06.2010
 */
public class JColor extends Color
{
	public JColor(Color c)
	{
		super(c.getRed(), c.getGreen(), c.getBlue());
	}

	public JColor(int r, int g, int b)
	{
		super(r, g, b);
	}

	@Override
	public String toString()
	{
		return String.valueOf(getRGB());
	}
}
