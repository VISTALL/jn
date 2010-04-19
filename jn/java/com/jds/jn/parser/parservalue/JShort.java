package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:46:21/07.04.2010
 */
public class JShort implements JIParserValue<Short>
{
	@Override
	public java.lang.Short getValue(NioBuffer b, Object... arg)
	{
		return b.getShort();
	}
}
