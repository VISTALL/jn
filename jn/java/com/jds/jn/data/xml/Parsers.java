package com.jds.jn.data.xml;

import com.jds.jn.data.xml.parser.ProtocolParser;

/**
 * @author VISTALL
 * @date 12:15/27.09.2011
 */
public abstract class Parsers
{
	public static void parseAll()
	{
		ProtocolParser.getInstance().load();
	}
}
