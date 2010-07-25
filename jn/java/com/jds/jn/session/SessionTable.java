package com.jds.jn.session;

import java.util.HashMap;
import java.util.Map;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.methods.IMethod;

/**
 * @author Ulysses R. Ribeiro
 */
public class SessionTable
{
	private Map<Long, Session> _sessionList;
	private static SessionTable _instance = new SessionTable();

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
		_sessionList = new HashMap<Long, Session>();
	}

	public CaptorSession newCaptorSession(IMethod iMethod, long sessionId)
	{
		CaptorSession session = new CaptorSession(iMethod, sessionId);
		_sessionList.put(sessionId, session);
		MainForm.getInstance().showSession(session);

		return session;
	}

	public Session newSession(IMethod iMethod)
	{
		Session session = new Session(iMethod, iMethod.getSessionId());
		_sessionList.put(iMethod.getSessionId(), session);
		MainForm.getInstance().showSession(session);

		return session;
	}

	public void removeGameSession(long sessionId)
	{
		_sessionList.remove(sessionId);
	}

	public Session getSession(long sessionId)
	{
		return _sessionList.get(sessionId);
	}
}
