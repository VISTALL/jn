package com.jds.jn.network.methods.proxy;

import com.jds.jn.Jn;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.methods.proxy.handlers.ClientToProxyHandler;
import com.jds.jn.network.methods.proxy.handlers.ServerToProxyHandler;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.nio.NioAcceptor;
import com.jds.nio.NioConnector;
import com.jds.nio.NioSession;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05.09.2009
 * Time: 6:56:19
 */
public class Proxy implements IMethod
{
	private NioSession _clientSession;
	private NioSession _serverSession;

	private String _localHost;
	private String _remoteHost;

	private int _localPort;
	private int _remotePort;

	private NioAcceptor _acceptor;
	private NioConnector _connector;

	private final ListenerType _type;

	public Proxy(ListenerType type)
	{
		_type = type;
		_acceptor = new NioAcceptor(new ClientToProxyHandler(this), null);
		_connector = new NioConnector(new ServerToProxyHandler(this), null);
	}

	@Override
	public void start() throws Exception
	{
		Jn.getForm().info("Start Proxy: " + _type.name());
		_acceptor.bind(new InetSocketAddress(getLocalHost(), getLocalPort()));
	}

	@Override
	public void stop() throws Exception
	{
		Jn.getForm().info("Stop Proxy: " + _type.name());
		try
		{
			if (getClientSession() != null)
			{
				getClientSession().clear();
			}
			if (getServerSession() != null)
			{
				getServerSession().clear();
			}
			_acceptor.unbind();
		}
		catch (Exception e)
		{
			//
		}
	}

	@Override
	public void init() throws IOException
	{
		NetworkProfile profile = NetworkProfiles.getInstance().active();
		if (profile == null)
		{
			throw new IOException("Not set network profile");
		}
		NetworkProfilePart part = profile.getPart(_type);

		_localHost = part.getLocalHost();
		_localPort = part.getLocalPort();
		_remoteHost = part.getRemoteHost();
		_remotePort = part.getRemotePort();
	}

	public NioConnector getConnector()
	{
		return _connector;
	}

	public NioSession getClientSession()
	{
		return _clientSession;
	}

	public void setClientSession(NioSession clientSession)
	{
		_clientSession = clientSession;
	}

	public NioSession getServerSession()
	{
		return _serverSession;
	}

	public void setServerSession(NioSession serverSession)
	{
		_serverSession = serverSession;
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

	@Override
	public ListenerType getListenerType()
	{
		return _type;
	}

	@Override
	public ReceiveType getType()
	{
		return ReceiveType.PROXY;
	}

	@Override
	public long getSessionId()
	{
		return getClientSession().getId();
	}
}
