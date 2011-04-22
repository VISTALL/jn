package com.jds.jn;

import java.nio.ByteOrder;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Date:  10:55/21.12.2010
 */
public class JnTest
{
	public static void main(String... arg)
	{
		NioBuffer a = NioBuffer.allocate(2);
		a.put((byte)0xFE);
		a.put((byte)0xDA);
		a.position(0);
		a.order(ByteOrder.BIG_ENDIAN);


		System.out.println(a.getUnsignedShort());


	}
}
