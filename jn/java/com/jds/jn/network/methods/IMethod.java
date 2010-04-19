package com.jds.jn.network.methods;

import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;

import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23/12/2009
 * Time: 7:07:45
 */
public interface IMethod
{
	public void start() throws Exception;

	public void stop() throws Exception;

	public void init() throws IOException;

	public ListenerType getListenerType();

	public ReceiveType getType();

	public long getSessionId();
}
