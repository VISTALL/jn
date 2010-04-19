package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:52:51/07.04.2010
 */
public class JUnsignedByte implements JIParserValue<Short>
{
	@Override
	public Short getValue(NioBuffer b, Object... arg)
	{
		return b.getUnsigned();
	}
}
