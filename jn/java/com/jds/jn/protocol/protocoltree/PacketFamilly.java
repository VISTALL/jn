package com.jds.jn.protocol.protocoltree;

import java.util.Map;
import java.util.TreeMap;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.Protocol;

/**
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class PacketFamilly// extends ProtocolNode
{
	private final PacketType _type;
	private Map<String, PacketInfo> _formats = new TreeMap<String, PacketInfo>();

	public PacketFamilly(PacketType t)
	{
		_type = t;
	}

	public void addPacket(PacketInfo format, Protocol p)
	{
		if(p != null)
		{
			if ((format = _formats.put(format.getId(), format)) != null)
			MainForm.getInstance().info("Duplicate packet for one opcode: " + format.getId() + "; protocol:" + p.getName());
		}
		else if(!_formats.containsKey(format.getId()))
			_formats.put(format.getId(), format);
	}

	public Map<String, PacketInfo> getFormats()
	{
		return _formats;
	}

	public PacketType getType()
	{
		return _type;
	}
}