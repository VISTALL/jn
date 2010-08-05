package com.jds.jn.parser.valuereader;

import javax.swing.*;

import com.jds.jn.parser.datatree.ValuePart;

/**
 * @author Gilles Duboscq
 */
public interface ValueReader
{
	public JComponent readToComponent(ValuePart part);

	public String read(ValuePart part);
}