package com.jds.jn.network.profiles;

import java.util.*;

import com.jds.jn.config.RValues;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 20:15:26
 */
public class NetworkProfiles
{
	private Map<String, NetworkProfile> _map = new HashMap<String, NetworkProfile>();

	private static NetworkProfiles _instance;

	public static NetworkProfiles getInstance()
	{
		if (_instance == null)
		{
			_instance = new NetworkProfiles();
		}
		return _instance;
	}

	public NetworkProfile getProfile(String name)
	{
		if(name == null)
		{
			return null;
		}
		return _map.get(name);
	}

	public void addProfile(NetworkProfile prof)
	{
		_map.put(prof.getName(), prof);
	}

	public NetworkProfile newProfile(String name, ReceiveType type)
	{
		NetworkProfile prof = new NetworkProfile(name, type);
		prof.addPart(new NetworkProfilePart(ListenerType.Auth_Server, prof));
		prof.addPart(new NetworkProfilePart(ListenerType.Game_Server, prof));
		addProfile(prof);

		return prof;
	}

	public void removeProfile(NetworkProfile f)
	{
		_map.remove(f.getName());
	}

	public NetworkProfile active()
	{
		return RValues.ACTIVE_PROFILE.getVal() != null ? _map.get(RValues.ACTIVE_PROFILE.asString()) : null;
	}

	public Collection<NetworkProfile> profiles()
	{
		return _map.values();
	}
}
