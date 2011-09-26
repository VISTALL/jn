package com.jds.jn.util.xml;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.jds.jn.util.xml.helpers.ErrorHandlerImpl;
import com.jds.jn.util.xml.helpers.SimpleDTDEntityResolver;

/**
 * Author: VISTALL
 * Date:  18:35/30.11.2010
 */
public abstract class AbstractParser<H extends AbstractHolder>
{
	protected final Logger _log = Logger.getLogger(getClass());

	protected final H _holder;

	protected String _currentFile;
	protected SAXReader _reader;

	protected AbstractParser(H holder)
	{
		_holder = holder;
		_reader = new SAXReader();
		_reader.setValidation(true);
		_reader.setErrorHandler(new ErrorHandlerImpl(this));
	}

	protected void initDTD(File f)
	{
		_reader.setEntityResolver(new SimpleDTDEntityResolver(f));
	}

	protected void parseDocument(InputStream f, String name) throws Exception
	{
		_currentFile = name;

		org.dom4j.Document document = _reader.read(f);

		readData(document.getRootElement());
	}

	protected abstract void readData(Element rootElement) throws Exception;

	protected abstract void parse();

	protected H getHolder()
	{
		return _holder;
	}

	public String getCurrentFileName()
	{
		return _currentFile;
	}

	public void load()
	{
		parse();
		_holder.process();
		_holder.log();
	}

	public void reload()
	{
		info("reload start...");
		_holder.clear();
		load();
	}

	public void error(String st, Exception e)
	{
		_log.error(st, e);
	}

	public void error(String st)
	{
		_log.error(st);
	}

	public void warn(String st, Exception e)
	{
		_log.warn(st, e);
	}

	public void warn(String st)
	{
		_log.warn(st);
	}

	public void info(String st, Exception e)
	{
		_log.info(st, e);
	}

	public void info(String st)
	{
		_log.info(st);
	}
}
