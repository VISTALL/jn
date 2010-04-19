package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:45:06/07.04.2010
 */
public class JUnsignedInt implements JIParserValue<Long>
{
	@Override
	public Long getValue(NioBuffer b, Object... arg)
	{
		return b.getUnsignedInt();
	}
}
