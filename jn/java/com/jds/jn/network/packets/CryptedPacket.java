package com.jds.jn.network.packets;

import java.nio.ByteOrder;

import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 14:55:53
 */
public class CryptedPacket implements IPacketData
{
	private final PacketType _type;
	private final NioBuffer _buff;
	private final long _time;

	private boolean _isDecrypted;

	public CryptedPacket(PacketType type, byte[] content, long t, ByteOrder order)
	{
		_time = t;
		_type = type;
		_buff = NioBuffer.wrap(content).order(order);
	}

	public CryptedPacket(PacketType type, NioBuffer content, long t)
	{
		_time = t;
		_type = type;
		_buff = content;
	}

	public NioBuffer getBuffer()
	{
		return _buff;
	}

	public int length()
	{
		return getAllData().length;
	}

	@Override
	public byte[] getAllData()
	{
		return getBuffer().array();
	}

	@Override
	public PacketType getPacketType()
	{
		return _type;
	}

	@Override
	public long getTime()
	{
		return _time;
	}

	@Override
	public String toString()
	{
		return "CryptedPacket: type - " + _type.name();
	}

	public boolean isDecrypted()
	{
		return _isDecrypted;
	}

	public void setDecrypted(boolean decrypted)
	{
		_isDecrypted = decrypted;
	}
}
