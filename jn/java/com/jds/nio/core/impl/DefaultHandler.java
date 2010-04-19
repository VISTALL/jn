package com.jds.nio.core.impl;

import com.jds.nio.NioSession;
import com.jds.nio.buffer.NioBuffer;
import com.jds.nio.core.CloseType;
import com.jds.nio.core.NioHandler;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 23:56:11
 */
public class DefaultHandler implements NioHandler
{

	/**
	 * Is called when is session is create
	 *
	 * @param nioSession
	 */
	@Override
	public void sessionCreate(NioSession nioSession)
	{

	}

	/**
	 * Is called when is session is closing, closeType is type of closing force or normal
	 *
	 * @param nioSession
	 * @param closeType
	 */
	@Override
	public void sessionClose(NioSession nioSession, CloseType closeType)
	{

	}

	/**
	 * is called when exception is throw
	 *
	 * @param nioSession
	 * @param throwable
	 */
	@Override
	public void catchException(NioSession nioSession, Throwable throwable)
	{
		throwable.printStackTrace();
	}

	/**
	 * is called if message is receive
	 *
	 * @param nioSession
	 * @param buffer
	 */
	@Override
	public void receive(NioSession nioSession, NioBuffer buffer)
	{

	}
}
