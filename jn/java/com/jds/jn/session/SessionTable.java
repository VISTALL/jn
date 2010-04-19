package com.jds.jn.session;

import com.jds.jn.Jn;
import javolution.util.FastMap;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.ProtocolManager;

import java.util.Map;

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
		_sessionList = new FastMap<Long, Session>();
	}

	public CaptorSession newGameSession(IMethod iMethod, long session)
	{
		Protocol protocol;

		protocol = ProtocolManager.getInstance().getProtocol(iMethod.getListenerType());

		if (protocol != null)
		{
			CaptorSession gameSession = new CaptorSession(iMethod, protocol);
			_sessionList.put(session, gameSession);
			Jn.getInstance().showSession(gameSession);

			return gameSession;
		}

		return null;
	}

	public Session newGameSession(IMethod iMethod)
	{
		Protocol protocol;

		protocol = ProtocolManager.getInstance().getProtocol(iMethod.getListenerType());

		if (protocol != null)
		{
			Session gameSession = new Session(iMethod, protocol);
			_sessionList.put(iMethod.getSessionId(), gameSession);
			Jn.getInstance().showSession(gameSession);

			return gameSession;
		}

		return null;
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
