package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:57:20/07.04.2010
 */
public class JLong implements JIParserValue<Long>
{
	@Override
	public Long getValue(NioBuffer b, Object... arg)
	{
		return b.getLong();
	}
}
