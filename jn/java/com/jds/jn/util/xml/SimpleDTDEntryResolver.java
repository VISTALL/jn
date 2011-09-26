package com.jds.jn.util.xml;

import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  14:59:08/28.08.2010
 */
@Deprecated
public class SimpleDTDEntryResolver implements EntityResolver
{
	public static final SimpleDTDEntryResolver PROTOCOL_DTD = new SimpleDTDEntryResolver("./protocols/protocol.dtd");

	private String _fileName;

	public SimpleDTDEntryResolver(String fileName)
	{
		_fileName = fileName;
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
	{
		return new InputSource(new FileReader(_fileName));
	}
}
