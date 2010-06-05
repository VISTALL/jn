package com.jds.jn.util;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  9:25:14/04.06.2010
 */
public class UncaughtExceptionHandlerImpl  implements UncaughtExceptionHandler
{
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		e.printStackTrace();
	}
}
