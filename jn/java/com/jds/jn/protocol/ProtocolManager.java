package com.jds.jn.protocol;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;

public class ProtocolManager
{
	private Map<String, Protocol> _protocolsByName;

	private static ProtocolManager _instance;

	public static ProtocolManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new ProtocolManager();
		}
		return _instance;
	}

	private ProtocolManager()
	{
		_protocolsByName = new HashMap<String, Protocol>();
		loadProtocols();
	}

	public Protocol getProtocolByName(String name)
	{
		if (!_protocolsByName.containsKey(name))
		{
			MainForm.getInstance().warn("Can not find protocol for name " + name);
		}

		return _protocolsByName.get(name);
	}

	public Collection<Protocol> getProtocols()
	{
		return _protocolsByName.values();
	}

	public Protocol getProtocol(ListenerType t) throws IllegalArgumentException
	{
		NetworkProfile prof = NetworkProfiles.getInstance().active();
		if(prof == null)
		{
			return null;
		}
		NetworkProfilePart part = prof.getPart(t);
		if(part == null)
		{
			return null;
		}

		return getProtocolByName(part.getProtocol());
	}

	public void loadProtocols()
	{
		_protocolsByName.clear();
		File dir = new File("./protocols/");

		if (!dir.isDirectory())
		{
			MainForm.getInstance().warn("Invalid Protocols directory (" + dir.getAbsolutePath() + ")");
			return;
		}

		File[] files = dir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".xml");
			}

		});

		for (File f : files)
		{
			Protocol p = ProtocolLoader.restore(f);
			if (p == null)
				return;

			_protocolsByName.put(p.getName(), p);
		}

		for(Protocol p : _protocolsByName.values())
		{
			if(p.getExtends() != null)
			{
				Protocol extendsProtocol = _protocolsByName.get(p.getExtends());
				if(extendsProtocol != null)
				{
					for(PacketFamilly extendsFamily : extendsProtocol.getFamilies())
						for(PacketInfo packetInfo : extendsFamily.getFormats().values())
							if(packetInfo.isExtended())
							{
								PacketFamilly familly = p.getFamilly(extendsFamily.getType());
								if(familly == null)
									p.setFamily(extendsFamily.getType(), familly = new PacketFamilly(extendsFamily.getType()));

								familly.addPacket(packetInfo, null);
							}
				}
				else
					MainForm.getInstance().warn("Not find extends protocol: " + p.getExtends() + " from " + p.getName());
			}
		}
	}
}