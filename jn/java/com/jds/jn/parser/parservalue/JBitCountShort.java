package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:54:31/07.04.2010
 */
public class JBitCountShort implements JIParserValue<Integer>
{
	@Override
	public Integer getValue(NioBuffer b, Object... arg)
	{
		return Integer.bitCount(b.getShort());
	}
}
