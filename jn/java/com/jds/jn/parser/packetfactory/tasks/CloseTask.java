package com.jds.jn.parser.packetfactory.tasks;

import com.jds.jn.session.Session;
import com.jds.jn.util.RunnableImpl;

/**
 * @author VISTALL
 * @date 14:32/18.09.2011
 */
public class CloseTask extends RunnableImpl
{
	private Session _session;

	public CloseTask(Session session)
	{
		_session = session;
	}

	@Override
	protected void runImpl() throws Throwable
	{
		_session.fireClose();
	}
}
