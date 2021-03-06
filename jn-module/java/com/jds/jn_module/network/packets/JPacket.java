package com.jds.jn_module.network.packets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:10:50/04.04.2010
 */
public class JPacket
{
	private final PacketType _type;
	private final ByteBuffer _buff;
	private final long _time;

	public JPacket(PacketType type, byte[] content)
	{
		_type = type;
		_buff = ByteBuffer.wrap(content).order(ByteOrder.LITTLE_ENDIAN);
		_time = System.currentTimeMillis();
	}

	public JPacket(PacketType type, ByteBuffer content)
	{
		_type = type;
		_buff = content;
		_time = System.currentTimeMillis();
	}

	public ByteBuffer getBuffer()
	{
		return _buff;
	}

	public PacketType getType()
	{
		return _type;
	}

	@Override
	public String toString()
	{
		return "JPacket: type - " + _type.name();
	}

	public long getTime()
	{
		return _time;
	}
}

