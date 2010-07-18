package com.jds.jn.network.methods.jpcap.buffers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  22:27:18/18.07.2010
 */
public class BigEndianIntBuffer implements IPacketBuffer
{
	private ByteBuffer _buf;

	public BigEndianIntBuffer()
	{
		_buf = ByteBuffer.allocate(65535);
		_buf.order(ByteOrder.BIG_ENDIAN);
	}

	@Override
	public void putData(byte[] dat)
	{
		_buf.put(dat);
	}

	@Override
	public int nextAvaliablePacket()
	{
		if (_buf.position() < 4)
		{
			return 0;
		}

		int size = _buf.getInt(0);

		if (size == 4)
		{
			_buf.position(0);
			return 0;
		}

		if (size > _buf.position())
		{
			return 0;
		}

		return (size - 4);
	}

	@Override
	public void getNextPacket(byte[] header, byte[] data)
	{
		_buf.limit(_buf.position());

		_buf.position(0);

		_buf.get(header);

		_buf.get(data);

		_buf.compact();
	}
}
