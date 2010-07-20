package com.jds.jn.protocol.protocoltree;

import java.util.HashMap;
import java.util.Map;

import com.jds.jn.Jn;
import com.jds.jn.network.packets.PacketType;

/**
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class PacketFamilly// extends ProtocolNode
{
	private final PacketType _type;
	private Map<String, PacketInfo> _formats = new HashMap<String, PacketInfo>();

	public PacketFamilly(PacketType t)
	{
		_type = t;
	}

	public void addPacket(PacketInfo format)
	{
		if ((format = _formats.put(format.getId(), format)) != null)
		{
			Jn.getForm().info("More than 1 packet register for 1 packet id: " + format.getId());
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