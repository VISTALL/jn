package com.jds.jn_module.network.session;

import com.jds.jn_module.JnModule;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:04:14/04.04.2010
 */
public class SessionTable implements Iterable<Session>
{
	private HashMap<Long, Session> _sessions = new  HashMap<Long, Session>();
	private static SessionTable _instance;

	public static SessionTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new SessionTable();
		}
		return _instance;
	}

	private SessionTable()
	{
	}

	public Session newGameSession(long s)
	{
		Session session = new Session(s);
		_sessions.put(s, session);

		JnModule.getInstance().setSessionId(s);
		return session;
	}

	public Session getSession(long d)
	{
		return _sessions.get(d);
	}

	@Override
	public Iterator<Session> iterator()
	{
		return _sessions.values().iterator();
	}
}
