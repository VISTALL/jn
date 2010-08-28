package com.jds.jn.util.xml;

import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:05:56/28.08.2010
 */
public class SimpleErrorHandler implements ErrorHandler
{
	private static final Logger _log = Logger.getLogger(SimpleErrorHandler.class);
	private String _fileName;

	public SimpleErrorHandler(File f)
	{
		_fileName = f.getName();
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException
	{
		_log.info("Warn: line: " + exception.getLineNumber() + "; file: " + _fileName);
	}

	@Override
	public void error(SAXParseException exception) throws SAXException
	{
		_log.info("Error: line: " + exception.getLineNumber() + "; file: " + _fileName);
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException
	{
		_log.info("FatalError: line: " + exception.getLineNumber() + "; file: " + _fileName);
	}
}
