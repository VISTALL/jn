package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:45:36/07.04.2010
 */
public class JInteger implements JIParserValue<Integer>
{
	@Override
	public java.lang.Integer getValue(NioBuffer b, Object... arg)
	{
		return b.getInt();
	}
}
