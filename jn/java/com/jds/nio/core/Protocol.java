package com.jds.nio.core;

import com.jds.nio.NioSession;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 23:49:27
 */
public class Protocol
{
	/**
	 * Simple implemention of decode
	 *
	 * @param session
	 * @param buf
	 * @return
	 */
	public NioBuffer decode(NioSession session, NioBuffer buf)
	{
		return buf;
	}

	/**
	 * Simple implementiom of encode, in start of buffer is putting SIZE of packet
	 *
	 * @param session
	 * @param buf
	 * @return
	 */
	public NioBuffer encode(NioSession session, NioBuffer buf)
	{
		return buf;
	}
}