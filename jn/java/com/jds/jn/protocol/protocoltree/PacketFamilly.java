package com.jds.jn.protocol.protocoltree;

import java.util.Map;
import java.util.TreeMap;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.packets.PacketType;

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

	public void addPacket(PacketInfo format)
	{
		if ((format = _formats.put(format.getId(), format)) != null)
		{
			MainForm.getInstance().info("More than 1 packet register for 1 packet id: " + format.getId());
		}
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