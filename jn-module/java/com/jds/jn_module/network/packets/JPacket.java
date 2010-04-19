package com.jds.jn_module.network.packets;

import com.jds.jn_module.network.packets.PacketType;

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

	public JPacket(PacketType type, byte[] content)
	{
		_type = type;
		_buff = ByteBuffer.wrap(content).order(ByteOrder.LITTLE_ENDIAN);
	}

	public JPacket(PacketType type, ByteBuffer content)
	{
		_type = type;
		_buff = content;
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
}

