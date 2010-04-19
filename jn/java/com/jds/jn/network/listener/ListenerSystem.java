package com.jds.jn.network.listener;

import javolution.util.FastMap;
import com.jds.jn.Jn;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.methods.jpcap.Jpcap;
import com.jds.jn.network.methods.proxy.Proxy;
import com.jds.jn.util.ThreadPoolManager;

import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23/12/2009
 * Time: 2:39:00
 */
public class ListenerSystem
{
	private static ListenerSystem _instance;

	private FastMap<ReceiveType, FastMap<ListenerType, IMethod>> _list = new FastMap<ReceiveType, FastMap<ListenerType, IMethod>>();

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
		_list.put(ReceiveType.PROXY, new FastMap<ListenerType, IMethod>());
		_list.put(ReceiveType.JPCAP, new FastMap<ListenerType, IMethod>());

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
					Jn.getInstance().warn("Not can start", e);
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
					Jn.getInstance().warn("Not can stop", e);
				}
			}
		}//);
	}

	public void init() throws IOException
	{
		for (ReceiveType type : ReceiveType.values())
		{
			FastMap<ListenerType, IMethod> list = _list.get(type);
			for (IMethod method : list.values())
			{
				method.init();
			}
		}
	}
}
