package com.jds.jn.network.packets;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 14:55:53
 */
public class CryptedPacket implements IPacketData
{
	private final PacketType _type;
	private final byte[] _data;
	private final long _time;

	private boolean _isDecrypted;

	public CryptedPacket(PacketType type, byte[] content, long t)
	{
		_time = t;
		_type = type;
		_data = content;
	}

	public int length()
	{
		return _data.length;
	}

	@Override
	public byte[] getAllData()
	{
		return _data;
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
