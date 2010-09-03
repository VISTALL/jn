package com.jds.jn.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:02:53/03.09.2010
 */
public class StringHexBuffer
{
	private final char[] _chars;
	private int _position;

	public StringHexBuffer(String t)
	{
		_chars = t.toCharArray();
	}

	public byte nextByte()
	{
		StringBuffer f = new StringBuffer();
		f.append(_chars[_position++]);
		f.append(_chars[_position++]);

		return Long.decode("0x" + f.toString()).byteValue();
	}

	public int nextInt()
	{
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.put(nextByte());
		buff.put(nextByte());
		buff.put(nextByte());
		buff.put(nextByte());
		buff.position(0);
		return buff.getInt();
	}

	public short nextShort()
	{
		ByteBuffer buff = ByteBuffer.allocate(2);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.put(nextByte());
		buff.put(nextByte());
		buff.position(0);
		return buff.getShort();
	}

	public long nextLong()
	{
		ByteBuffer buff = ByteBuffer.allocate(8);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.put(nextByte());
		buff.put(nextByte());
		buff.put(nextByte());
		buff.put(nextByte());
		buff.put(nextByte());
		buff.put(nextByte());
		buff.put(nextByte());
		buff.put(nextByte());
		buff.position(0);
		return buff.getLong();
	}
}