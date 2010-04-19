package com.jds.jn.parser.parservalue;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  15:43:06/07.04.2010
 */
public interface JIParserValue<T extends Object>
{
	public T getValue(NioBuffer b, Object... arg);
}
