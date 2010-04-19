package com.jds.jn.network.packets;

import com.jds.nio.buffer.NioBuffer;

import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 14:55:53
 */
public class JPacket
{
	private final PacketType _type;
	private final NioBuffer _buff;
	private boolean _isShow = false;

	public JPacket(PacketType type, byte[] content)
	{
		_type = type;
		_buff = NioBuffer.wrap(content).order(ByteOrder.LITTLE_ENDIAN);
	}

	public JPacket(PacketType type, NioBuffer content)
	{
		_type = type;
		_buff = content;
	}

	public NioBuffer getBuffer()
	{
		return _buff;
	}

	public PacketType getType()
	{
		return _type;
	}

	public boolean isShow()
	{
		if (!_isShow)
		{
			_isShow = true;
			return false;
		}

		return _isShow;
	}

	@Override
	public String toString()
	{
		return "JPacket: type - " + _type.name();
	}
}
