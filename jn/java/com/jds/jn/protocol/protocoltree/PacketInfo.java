package com.jds.jn.protocol.protocoltree;

import com.jds.jn.parser.formattree.Format;
import com.jds.jn.parser.packetreader.PacketReader;

/**
 * This class represents a packet format in the protocol definition tree.
 * It is a container for
 *
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class PacketInfo //extends ProtocolNode
{
	private String _id;
	private String _name;
	private Format _format;
	private boolean _isKey;
	private boolean _serverList;
	private PacketReader _packetReader;

	public PacketInfo(String id, String name, boolean key, boolean serverList, Class<PacketReader> reader)
	{
		_id = id.toUpperCase();
		_name = name;
		_isKey = key;
		_serverList = serverList;

		if(reader != null)
		{
			try
			{
				_packetReader = reader.newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		_format = new Format(this);
	}

	public Format getDataFormat()
	{
		return _format;
	}

	public PacketReader getPacketReader()
	{
		return _packetReader;
	}

	public void setName(String s)
	{
		_name = s;
	}

	public String getName()
	{
		return _name;
	}

	public boolean isKey()
	{
		return _isKey;
	}

	public int sizeId()
	{
		return _id.split(";").length;
	}

	public String getHexForIndex(int index)
	{
		return _id.split(";")[index];
	}

	public String getOpcodeStr()
	{
		return _id.replace(";", "");
	}

	public boolean isServerList()
	{
		return _serverList;
	}

	public String getId()
	{
		return _id;
	}	

	@Override
	public String toString()
	{
		return getOpcodeStr() + " " + _name;
	}
}