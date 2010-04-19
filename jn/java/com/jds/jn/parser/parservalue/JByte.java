package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:53:13/07.04.2010
 */
public class JByte implements JIParserValue<Byte>
{
	@Override
	public Byte getValue(NioBuffer b, Object... arg)
	{
		return b.get();
	}
}
