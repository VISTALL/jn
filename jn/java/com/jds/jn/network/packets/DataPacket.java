package com.jds.jn.network.packets;

import java.nio.BufferUnderflowException;

import com.jds.jn.Jn;
import com.jds.jn.parser.DataStructure;
import com.jds.jn.parser.datatree.NumberValuePart;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.PacketInfo;


/**
 * @author Gilles Duboscq
 */
public class DataPacket extends DataStructure
{
	private PacketType _packetType;
	private Protocol _protocol;
	private PacketInfo _packetFormat;
	private int _size;

	public DataPacket(JPacket packet, Protocol proto)
	{
		this(packet.getBuffer().array(), packet.getType(), proto, true);
	}

	public DataPacket(byte[] data, PacketType dir, Protocol proto)
	{
		this(data, dir, proto, true);
	}

	public DataPacket(byte[] data, PacketType direction, Protocol protocol, boolean parse)
	{
		super(data, null);

		_packetType = direction;
		_protocol = protocol;

		addData(parse);
	}

	public void addData(boolean parse)
	{
		_packetFormat = _protocol.getFormat(this);

		if (_packetFormat == null)
		{
			_buf = getFullBuffer().clone();
		}

		_size = getBuffer().array().length;

		if (_packetFormat != null)
		{
			setFormat(_packetFormat.getDataFormat());
		}

		if (parse)
		{
			try
			{
				parse();
			}
			catch (BufferUnderflowException e)
			{
				_error = "Insuficient data for the specified format";
				Jn.getInstance().warn("Parsing packet (" + getName() + "), insuficient data for the specified format. Please verify the format.");
			}
		}
	}

	public Protocol getProtocol()
	{
		return _protocol;
	}

	public PacketType getPacketType()
	{
		return _packetType;
	}

	public PacketInfo getPacketFormat()
	{
		return _packetFormat;
	}

	public String getName()
	{
		if (getPacketFormat() == null)
		{
			return null;
		}
		return getPacketFormat().getName();
	}

	public int getRawSize()
	{
		return _size;
	}

	public boolean isKey()
	{
		return _packetFormat != null && _packetFormat.isKey();
	}

	public double getDouble(String s)
	{
		return ((NumberValuePart)getRootNode().getPartByName(s)).getValueAsDouble();
	}

	public int getInt(String s)
	{
		return ((NumberValuePart)getRootNode().getPartByName(s)).getValueAsInt();
	}
}