package com.jds.nio.core;

import com.jds.nio.NioSession;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 18:54:22
 */
public interface NioHandler
{
	/**
	 * Is called when is session is create
	 *
	 * @param nioSession
	 */
	public void sessionCreate(NioSession nioSession);

	/**
	 * Is called when is session is closing, closeType is type of closing force or normal
	 *
	 * @param nioSession
	 * @param closeType
	 */
	public void sessionClose(NioSession nioSession, CloseType closeType);

	/**
	 * is called when exception is throw
	 *
	 * @param nioSession
	 * @param throwable
	 */
	public void catchException(NioSession nioSession, Throwable throwable);

	/**
	 * is called if message is receive
	 *
	 * @param nioSession
	 * @param buffer
	 */
	public void receive(NioSession nioSession, NioBuffer buffer);
}
