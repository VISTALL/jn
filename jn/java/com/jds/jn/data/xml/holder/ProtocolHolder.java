package com.jds.jn.data.xml.holder;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.MacroInfo;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.util.xml.AbstractHolder;

public class ProtocolHolder extends AbstractHolder
{
	private Map<String, Protocol> _protocolsByName = new TreeMap<String, Protocol>();

	private static ProtocolHolder _instance = new ProtocolHolder();

	public static ProtocolHolder getInstance()
	{
		return _instance;
	}

	private ProtocolHolder()
	{
		//
	}

	public void addProtocol(Protocol protocol)
	{
		_protocolsByName.put(protocol.getName(), protocol);
	}

	public Protocol getProtocolByName(String name)
	{
		if(!_protocolsByName.containsKey(name))
			MainForm.getInstance().warn("Can not find protocol for name " + name);

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
			return null;

		NetworkProfilePart part = prof.getPart(t);
		if(part == null)
			return null;

		return getProtocolByName(part.getProtocol());
	}


	private void initExtendsProtocol(Protocol child)
	{
		if(child.getExtends() == null || child.getSuperProtocol() != null)
			return;

		Protocol parent = _protocolsByName.get(child.getExtends());
		if(parent == null)
		{
			MainForm.getInstance().warn("Not find extends protocol: " + child.getExtends() + " from " + child.getName());
			return;
		}

		if(parent.getExtends() != null)
			initExtendsProtocol(parent);

		for(PacketFamilly extendsFamily : parent.getFamilies())
			for(PacketInfo packetInfo : extendsFamily.getFormats().values())
			{
				PacketFamilly familly = child.getFamilly(extendsFamily.getType());
				if(familly == null)
					child.setFamily(familly = new PacketFamilly(extendsFamily.getType()));

				familly.addPacket(packetInfo, null);
			}

		for(MacroInfo m : parent.getMacros().values())
		{
			if(child.getMacroInfo(m.getId()) == null)
				child.addMacro(m);
		}

		child.setSuperProtocol(parent);
	}


	@Override
	protected void process()
	{
		for(Protocol p : _protocolsByName.values())
			initExtendsProtocol(p);
	}

	@Override
	public int size()
	{
		return _protocolsByName.size();
	}

	@Override
	public void clear()
	{
		_protocolsByName.clear();
	}
}