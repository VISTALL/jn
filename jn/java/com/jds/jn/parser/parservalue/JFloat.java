package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:56:15/07.04.2010
 */
public class JFloat implements JIParserValue<Float>
{
	@Override
	public Float getValue(NioBuffer b, Object... arg)
	{
		return b.getFloat();
	}
}
