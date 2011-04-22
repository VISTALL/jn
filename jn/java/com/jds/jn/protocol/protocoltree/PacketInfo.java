package com.jds.jn.protocol.protocoltree;

import java.util.AbstractMap;
import java.util.Map;

import com.jds.jn.parser.Type;
import com.jds.jn.parser.formattree.Format;
import com.jds.jn.parser.packetreader.PacketReader;

/**
 * This class represents a packet format in the protocol definition tree.
 * It is a container for
 *
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class PacketInfo
{
	private final Map.Entry<Type, Long>[] _opcode;

	private String _id;
	private String _name;
	private Format _format;
	private boolean _isKey;
	private boolean _serverList;
	private PacketReader _packetReader;

	public PacketInfo(String id, String name, boolean key, boolean serverList, Class<PacketReader> reader)
	{
		_id = id.toUpperCase();

		String[] sl = hexArray();
		_opcode = new Map.Entry[sl.length];
		for(int i = 0; i < _opcode.length; i++)
		{
			Type t = getType(sl[i]);
			long v = Long.decode("0x" + sl[i]);

			_opcode[i] = new AbstractMap.SimpleEntry<Type, Long>(t, v);
		}

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

	public static Type getType(String v)
	{
		int t = v.length();
		if (t >= 1 && t <= 2)  //c
		{
			return Type.uc;
		}
		if (t >= 3 && t <= 4) //h
		{
			return Type.uh;
		}
		if (t >= 5 && t <= 8) //d
		{
			return Type.ud;
		}
		if (t >= 9 && t <= 12) //q
		{
			return Type.Q;
		}
		throw new IllegalArgumentException("Unknown type of opcode: " + v);
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

	public String[] hexArray()
	{
		return _id.split(";");
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

	public Map.Entry<Type, Long>[] getOpcode()
	{
		return _opcode;
	}
}