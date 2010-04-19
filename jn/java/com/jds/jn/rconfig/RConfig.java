package com.jds.jn.rconfig;

import ca.beq.util.win32.registry.RegistryKey;
import ca.beq.util.win32.registry.RegistryValue;
import ca.beq.util.win32.registry.RootKey;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;

import java.util.Iterator;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 20:26:45
 */
public class RConfig
{
	private static RConfig _instance;
	private static final String MAIN_ROOT = "Software\\J Develop Station\\Jn";
	private static final String PROFILES_ROOT = "Software\\J Develop Station\\Jn\\Profiles";
	private static final String LAST_FILES_ROOT = "Software\\J Develop Station\\Jn\\LastFiles";

	public static RConfig getInstance()
	{
		if (_instance == null)
		{
			_instance = new RConfig();
		}
		return _instance;
	}

	private RConfig()
	{
		RegistryKey key = new RegistryKey(RootKey.HKEY_CURRENT_USER, MAIN_ROOT);

		if (!key.exists())
		{
			return;
		}

		for (RValues val : RValues.values())
		{
			if (key.hasValue(val.getName()))
			{
				val.setVal(key.getValue(val.getName()).getData());
			}
		}

		if (key.hasSubkey("LastFiles"))
		{
			RegistryKey lKey = new RegistryKey(RootKey.HKEY_CURRENT_USER, LAST_FILES_ROOT);
			Iterator<RegistryValue> iterator = lKey.values();

			while (iterator.hasNext())
			{
				final RegistryValue keyValue = iterator.next();
				LastFiles.addLastFile(keyValue.getName());
			}
		}

		if (key.hasSubkey("Profiles"))
		{
			RegistryKey prof = new RegistryKey(RootKey.HKEY_CURRENT_USER, PROFILES_ROOT);
			Iterator<RegistryKey> iterator = prof.subkeys();

			while (iterator.hasNext())
			{
				final RegistryKey skey = iterator.next();
				final NetworkProfile profile = NetworkProfiles.getInstance().newProfile(skey.getName());

				final Iterator<RegistryKey> pathKeyIterator = skey.subkeys();
				while (pathKeyIterator.hasNext())
				{
					final RegistryKey p = pathKeyIterator.next();
					ListenerType type = ListenerType.valueOf(p.getName());
					NetworkProfilePart part = profile.getPart(type);

					part.setLocalHost(p.getValue("LocalHost").asString());
					part.setLocalPort(p.getValue("LocalPort").asInteger());
					part.setRemoteHost(p.getValue("RemoteHost").asString());
					part.setRemotePort(p.getValue("RemotePort").asInteger());
					part.setDevicePort(p.getValue("DevicePort").asInteger());

					if (p.hasValue("ServerId"))
					{
						part.setServerId(p.getValue("ServerId").asInteger());
					}
					if (p.hasValue("PacketId"))
					{
						part.setPacketId(p.getValue("PacketId").asInteger());
					}
					if (p.hasValue("ServerList"))
					{
						part.setServerList(p.getValue("ServerList").asString());
					}
					if (p.hasValue("DeviceId"))
					{
						part.setDeviceId(p.getValue("DeviceId").asInteger());
					}

					part.setProtocol(p.getValue("Protocol").asString());

					if (p.hasValue("FilterList"))
					{
						part.fromStringToFilterList(p.getValue("FilterList").asString());
					}
				}
			}
		}
	}

	public void shutdown()
	{
		final RegistryKey key = new RegistryKey(RootKey.HKEY_CURRENT_USER, MAIN_ROOT);

		if (!key.exists())
		{
			key.create();
		}

		for (RValues val : RValues.values())
		{
			key.setValue(new RegistryValue(val.getName(), val.getVal()));
		}

		final RegistryKey prof = new RegistryKey(RootKey.HKEY_CURRENT_USER, PROFILES_ROOT);

		if (prof.exists())
		{
			prof.delete();
		}

		prof.create();

		final RegistryKey lastKey = new RegistryKey(RootKey.HKEY_CURRENT_USER, LAST_FILES_ROOT);

		if (lastKey.exists())
		{
			lastKey.delete();
		}

		lastKey.create();

		for (String st : LastFiles.getLastFiles())
		{
			lastKey.setValue(new RegistryValue(st, ""));
		}

		for (NetworkProfile b : NetworkProfiles.getInstance().profiles())
		{
			final String currentRoot = PROFILES_ROOT + "\\" + b.getName();
			RegistryKey profile = new RegistryKey(RootKey.HKEY_CURRENT_USER, currentRoot);
			profile.create();

			profile.setValue(new RegistryValue("type", b.getActive().name()));

			for (NetworkProfilePart $ : b.parts())
			{
				final String root = currentRoot + "\\" + $.getType().name();
				RegistryKey part = new RegistryKey(RootKey.HKEY_CURRENT_USER, root);
				part.create();

				part.setValue(new RegistryValue("LocalHost", $.getLocalHost()));
				part.setValue(new RegistryValue("LocalPort", $.getLocalPort()));
				part.setValue(new RegistryValue("RemoteHost", $.getRemoteHost()));
				part.setValue(new RegistryValue("RemotePort", $.getRemotePort()));
				part.setValue(new RegistryValue("DeviceId", $.getDeviceId()));
				part.setValue(new RegistryValue("DevicePort", $.getDevicePort()));
				part.setValue(new RegistryValue("Protocol", $.getProtocol()));
				part.setValue(new RegistryValue("ServerId", $.getServerId()));
				part.setValue(new RegistryValue("PacketId", $.getPacketId()));
				part.setValue(new RegistryValue("ServerList", $.getServerList()));
				part.setValue(new RegistryValue("FilterList", $.getFilterListAsString()));
			}
		}
	}
}
