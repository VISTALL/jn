package com.jds.jn.network.methods.proxy;

import javolution.util.FastMap;
import com.jds.jn.network.listener.types.ListenerType;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 25.08.2009
 * Time: 17:30:35
 */
public class ProxyList
{
	private final FastMap<ListenerType, Proxy> _proxyList = new FastMap<ListenerType, Proxy>();
	private static ProxyList _instance;

	public static ProxyList getInstance()
	{
		if (_instance == null)
		{
			_instance = new ProxyList();
		}
		return _instance;
	}

	public ProxyList()
	{
		_proxyList.put(ListenerType.Auth_Server, new Proxy(ListenerType.Auth_Server));
		_proxyList.put(ListenerType.Game_Server, new Proxy(ListenerType.Game_Server));
	}

	public Proxy getProxy(ListenerType id)
	{
		return _proxyList.get(id);
	}
}
