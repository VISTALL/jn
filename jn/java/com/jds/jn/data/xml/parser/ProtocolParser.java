package com.jds.jn.data.xml.parser;

import java.io.File;

import org.dom4j.Element;
import com.jds.jn.config.RValues;
import com.jds.jn.data.xml.holder.ProtocolHolder;
import com.jds.jn.util.xml.AbstractDirParser;

/**
 * @author VISTALL
 * @date 23:48/26.09.2011
 */
public class ProtocolParser extends AbstractDirParser<ProtocolHolder>
{
	private static ProtocolParser _instance = new ProtocolParser();

	public static ProtocolParser getInstance()
	{
		return _instance;
	}

	private ProtocolParser()
	{
		super(ProtocolHolder.getInstance());
	}

	@Override
	public File getXMLDir()
	{
		return new File(RValues.PROTOCOL_DIR.asString());
	}

	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}

	@Override
	public String getDTDFileName()
	{
		return "template.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{

	}
}
