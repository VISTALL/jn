package com.jds.nio.core;

import com.jds.nio.NioSession;
import com.jds.nio.buffer.NioBuffer;

import java.nio.ByteOrder;

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
		//System.out.println("decode buf \n" + NetworkUtil.printData(buf.array()));
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
		NioBuffer OUT = NioBuffer.allocate(1);
		OUT.order(ByteOrder.LITTLE_ENDIAN);
		OUT.setAutoExpand(true);

		OUT.putShort((short) (buf.limit() + 2));
		OUT.put(buf);
		OUT.flip();

		//System.out.println("encode buf \n" + NetworkUtil.printData(OUT.array()));
		return OUT;
	}
}