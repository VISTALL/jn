package com.jds.jn.protocol.protocoltree;

import com.jds.jn.Jn;
import com.jds.jn.network.packets.PacketType;
import javolution.util.FastMap;

/**
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class PacketFamilly// extends ProtocolNode
{
	private final PacketType _type;
	private FastMap<String, PacketInfo> _formats = new FastMap<String, PacketInfo>();

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

	public FastMap<String, PacketInfo> getFormats()
	{
		return _formats;
	}

	public PacketType getType()
	{
		return _type;
	}
}