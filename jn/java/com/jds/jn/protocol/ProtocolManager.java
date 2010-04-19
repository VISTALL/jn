package com.jds.jn.protocol;

import com.jds.jn.Jn;
import javolution.util.FastMap;
import com.jds.jn.loaders.ProtocolLoader;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.rconfig.RValues;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Map;

public class ProtocolManager
{
	private Map<String, Protocol> _protocolsByName;
	private String _protocolsDir;

	private static ProtocolManager _instance;

	public static ProtocolManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new ProtocolManager("./protocols/");
		}
		return _instance;
	}

	private ProtocolManager(String protocolsDir)
	{
		_protocolsDir = protocolsDir;
		_protocolsByName = new FastMap<String, Protocol>();
		if (_protocolsDir != null)
		{
			loadProtocols();
		}
	}

	public Protocol getProtocolByName(String name)
	{
		if (!_protocolsByName.containsKey(name))
		{
			Jn.getInstance().warn("Can not find protocol for name " + name);
		}

		return _protocolsByName.get(name);
	}

	public String getProtocolsDirectory()
	{
		return _protocolsDir;
	}

	public Collection<Protocol> getProtocols()
	{
		return _protocolsByName.values();
	}

	public Protocol getProtocol(ListenerType t) throws IllegalArgumentException
	{
		NetworkProfile prof = NetworkProfiles.getInstance().getProfile(RValues.ACTIVE_PROFILE.asString());
		NetworkProfilePart part = prof.getPart(t);

		Protocol prot = getProtocolByName(part.getProtocol());

		if (prot == null)
		{
			throw new IllegalArgumentException("Not find protocol by proxy id " + t.name());
		}

		return prot;
	}

	public void loadProtocols()
	{
		_protocolsByName.clear();
		File dir = new File(_protocolsDir);

		if (!dir.isDirectory())
		{
			Jn.getInstance().warn("Invalid Protocols directory (" + _protocolsDir + ")", null);
			return;
		}

		File[] files = dir.listFiles(new FilenameFilter()
		{

			public boolean accept(File dir, String name)
			{
				return name.endsWith(".xml");
			}

		});

		for (File f : files)
		{
			Protocol p = ProtocolLoader.restore(f);
			if (p == null)
			{
				return;
			}

			_protocolsByName.put(p.getName(), p);
		}
	}
}