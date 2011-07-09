package com.jds.jn.parser.parservalue;

import java.awt.Color;

import javax.swing.ImageIcon;

import com.jds.jn.parser.formattree.Part;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:43:06/07.04.2010
 */
public interface ParserValue<T>
{
	public T getValue(NioBuffer b, Part part, Object... arg);

	public ImageIcon getIcon();

	public Color getColor();

	public int length();
}
