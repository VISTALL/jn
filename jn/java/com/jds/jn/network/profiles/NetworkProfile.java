package com.jds.jn.network.profiles;

import javolution.util.FastMap;

import java.util.Collection;

import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 20:26:52
 */
public class NetworkProfile
{
	private final String _name;
	private ReceiveType _type;

	private FastMap<ListenerType, NetworkProfilePart> TYPES = new FastMap<ListenerType, NetworkProfilePart>();

	public NetworkProfile(String name, ReceiveType type)
	{
		_name = name;
		_type = type;
	}

	public void addPart(NetworkProfilePart part)
	{
		TYPES.put(part.getType(), part);
	}

	public NetworkProfilePart getPart(ListenerType tye)
	{
		return TYPES.get(tye);
	}

	public Collection<NetworkProfilePart> parts()
	{
		return TYPES.values();
	}

	public String getName()
	{
		return _name;
	}

	public ReceiveType getType()
	{
		return _type;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
