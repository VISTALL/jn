package com.jds.jn.network.profiles;

import java.util.ArrayList;

import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.config.properties.PropertyValue;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 20:12:24
 */
public class NetworkProfilePart
{
	private final NetworkProfile _mother;
	private final ListenerType _type;

	//proxy
	private String _localHost = "127.0.0.1";
	private String _remoteHost = "127.0.0.1";
	private int _localPort = 0;
	private int _remotePort = 0;
	private int _packetId;
	private int _serverId;
	private String _serverList;
	//pcap
	private int _deviceId = 0;
	private int _devicePort = 0;

	// protocol
	private String _protocol = "";

	// filter
	private ArrayList<String> _filterList;


	public NetworkProfilePart(ListenerType type, NetworkProfile m)
	{
		_type = type;
		_mother = m;
	}

	@PropertyValue("LocalHost")
	public String getLocalHost()
	{
		return _localHost;
	}

	public void setLocalHost(String localHost)
	{
		_localHost = localHost;
	}

	@PropertyValue("RemoteHost")
	public String getRemoteHost()
	{
		return _remoteHost;
	}

	public void setRemoteHost(String remoteHost)
	{
		_remoteHost = remoteHost;
	}

	@PropertyValue("LocalPort")
	public int getLocalPort()
	{
		return _localPort;
	}

	public void setLocalPort(int localPort)
	{
		_localPort = localPort;
	}

	@PropertyValue("RemotePort")
	public int getRemotePort()
	{
		return _remotePort;
	}

	public void setRemotePort(int remotePort)
	{
		_remotePort = remotePort;
	}

	@PropertyValue("DeviceId")
	public int getDeviceId()
	{
		return _deviceId;
	}

	public void setDeviceId(int deviceName)
	{
		_deviceId = deviceName;
	}

	@PropertyValue("DevicePort")
	public int getDevicePort()
	{
		return _devicePort;
	}

	public void setDevicePort(int devicePort)
	{
		_devicePort = devicePort;
	}

	public ListenerType getType()
	{
		return _type;
	}

	public NetworkProfile getMother()
	{
		return _mother;
	}

	@PropertyValue("Protocol")
	public String getProtocol()
	{
		return _protocol;
	}

	public void setProtocol(String protocol)
	{
		_protocol = protocol;
	}

	@PropertyValue("ProxyPacketId")
	public int getPacketId()
	{
		return _packetId;
	}

	public void setPacketId(int packetId)
	{
		_packetId = packetId;
	}

	@PropertyValue("ProxyServerId")
	public int getServerId()
	{
		return _serverId;
	}

	public void setServerId(int serverId)
	{
		_serverId = serverId;
	}

	@PropertyValue("ProxyServerList")
	public String getServerList()
	{
		return _serverList;
	}

	public void setServerList(String serverList)
	{
		_serverList = serverList;
	}

	public void addFilterOpcode(String s)
	{
		if(_filterList == null)
		{
			_filterList = new ArrayList<String>();
		}
		_filterList.add(s);
	}

	public void removeFilterOpcode(String a)
	{
		if(_filterList != null)
		{
			_filterList.remove(a);
		}
	}

	@PropertyValue("FilterOpcodeList")
	public String getFilterListAsString()
	{
		String filterList = "";

		if(_filterList != null)
			for (String str : _filterList)
			{
				filterList += str + ";";
			}

		return filterList;
	}

	public void setFilterListAsString(String filterList)
	{
		for (String st : filterList.split(";"))
		{
			if (!st.trim().equals(""))
			{
				addFilterOpcode(st);
			}
		}
	}

	public boolean isFiltredOpcode(String s)
	{
		return _filterList != null && _filterList.contains(s);
	}
}
