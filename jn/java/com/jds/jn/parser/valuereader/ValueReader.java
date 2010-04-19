package com.jds.jn.parser.valuereader;

import com.jds.jn.parser.datatree.ValuePart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.*;

/**
 * @author Gilles Duboscq
 */
public interface ValueReader
{
	public boolean loadReaderFromXML(Node n);

	public boolean saveReaderToXML(Element element, Document doc);

	public JComponent readToComponent(ValuePart part);

	public String read(ValuePart part);

	public boolean supportsEnum();

	public <T extends Enum<T>> T getEnum(ValuePart part);
}