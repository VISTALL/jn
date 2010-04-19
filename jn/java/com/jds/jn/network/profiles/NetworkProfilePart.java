package com.jds.jn.network.profiles;

import javolution.util.FastList;
import com.jds.jn.network.listener.types.ListenerType;

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
	private FastList<String> _filterList = new FastList<String>();


	public NetworkProfilePart(ListenerType type, NetworkProfile m)
	{
		_type = type;
		_mother = m;
	}

	public String getLocalHost()
	{
		return _localHost;
	}

	public void setLocalHost(String localHost)
	{
		_localHost = localHost;
	}

	public String getRemoteHost()
	{
		return _remoteHost;
	}

	public void setRemoteHost(String remoteHost)
	{
		_remoteHost = remoteHost;
	}

	public int getLocalPort()
	{
		return _localPort;
	}

	public void setLocalPort(int localPort)
	{
		_localPort = localPort;
	}

	public int getRemotePort()
	{
		return _remotePort;
	}

	public void setRemotePort(int remotePort)
	{
		_remotePort = remotePort;
	}

	public int getDeviceId()
	{
		return _deviceId;
	}

	public void setDeviceId(int deviceName)
	{
		_deviceId = deviceName;
	}

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

	public String getProtocol()
	{
		return _protocol;
	}

	public void setProtocol(String protocol)
	{
		_protocol = protocol;
	}

	public int getPacketId()
	{
		return _packetId;
	}

	public void setPacketId(int packetId)
	{
		_packetId = packetId;
	}

	public int getServerId()
	{
		return _serverId;
	}

	public void setServerId(int serverId)
	{
		_serverId = serverId;
	}

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
		_filterList.add(s);
	}

	public void removeFilterOpcode(String a)
	{
		_filterList.remove(a);
	}

	public String getFilterListAsString()
	{
		String filterList = "";

		for (String str : _filterList)
		{
			filterList += str + ";";
		}

		return filterList;
	}

	public void fromStringToFilterList(String filterList)
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
		return _filterList.contains(s);
	}
}
