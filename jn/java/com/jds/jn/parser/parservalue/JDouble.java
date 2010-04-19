package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:56:56/07.04.2010
 */
public class JDouble  implements JIParserValue<Double>
{
	@Override
	public Double getValue(NioBuffer b, Object... arg)
	{
		return b.getDouble();
	}
}
