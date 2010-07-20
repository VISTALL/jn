package com.jds.jn.network.listener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jds.jn.Jn;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.methods.jpcap.Jpcap;
import com.jds.jn.network.methods.proxy.Proxy;
import com.jds.jn.util.ThreadPoolManager;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23/12/2009
 * Time: 2:39:00
 */
public class ListenerSystem
{
	private static ListenerSystem _instance;

	private Map<ReceiveType, Map<ListenerType, IMethod>> _list = new HashMap<ReceiveType, Map<ListenerType, IMethod>>();

	public static ListenerSystem getInstance()
	{
		if (_instance == null)
		{
			_instance = new ListenerSystem();
		}
		return _instance;
	}

	private ListenerSystem()
	{
		_list.put(ReceiveType.PROXY, new HashMap<ListenerType, IMethod>());
		_list.put(ReceiveType.JPCAP, new HashMap<ListenerType, IMethod>());

		_list.get(ReceiveType.PROXY).put(ListenerType.Auth_Server, new Proxy(ListenerType.Auth_Server));
		_list.get(ReceiveType.PROXY).put(ListenerType.Game_Server, new Proxy(ListenerType.Game_Server));

		_list.get(ReceiveType.JPCAP).put(ListenerType.Auth_Server, new Jpcap(ListenerType.Auth_Server));
		_list.get(ReceiveType.JPCAP).put(ListenerType.Game_Server, new Jpcap(ListenerType.Game_Server));
	}

	public IMethod getMethod(ReceiveType t, ListenerType l)
	{
		return _list.get(t).get(l);
	}

	public void start(final ReceiveType receive, final ListenerType type)
	{
		ThreadPoolManager.getInstance().execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					_list.get(receive).get(type).start();
				}
				catch (Exception e)
				{
					Jn.getForm().warn("Not can start", e);
				}
			}
		});
	}

	public void stop(final ReceiveType receive, final ListenerType type)
	{
		//ThreadPoolManager.getInstance().execute(new Runnable()
		{
			//@Override
			//public void run()
			{
				try
				{
					_list.get(receive).get(type).stop();
				}
				catch (Exception e)
				{
					Jn.getForm().warn("Not can stop", e);
				}
			}
		}//);
	}

	public void init() throws IOException
	{
		for (ReceiveType type : ReceiveType.values())
		{
			Map<ListenerType, IMethod> list = _list.get(type);
			for (IMethod method : list.values())
			{
				method.init();
			}
		}
	}
}
