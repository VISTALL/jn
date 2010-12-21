package com.jds.jn.util;

import java.awt.Component;
import java.lang.reflect.Field;

import sun.awt.windows.WToolkit;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:53:29/20.07.2010
 */
public class OSUtils
{
	public static long getHWnd(Component comp)
	{
		Object peer = WToolkit.targetToPeer(comp);
		try
		{
			Class c = peer.getClass();
			Field f = getDeclaredField(c, "hwnd");
			f.setAccessible(true);
			Object result = f.get(peer);
			return (Long) result;
		}
		catch(Throwable t)
		{
			throw new RuntimeException(t.toString());
		}
	}

	public static Field getDeclaredField(Class clazz, String fieldName) throws NoSuchFieldException
	{
		Class c = clazz;
		while(c != null && c != Object.class)
		{
			try
			{
				return c.getDeclaredField(fieldName);
			}
			catch(NoSuchFieldException e)
			{
			}
			c = c.getSuperclass();
		}
		throw new NoSuchFieldException(fieldName);
	}

	public static String getLibName()
	{
		StringBuffer buf = new StringBuffer("jpcap-");
		boolean win = isWindows();
		boolean mac = isMac();
		boolean unix = isUnix();
		if(win)
			buf.append("win");
		else if(mac)
			buf.append("mac");
		else if(unix)
			buf.append("unix");

		buf.append("-");
		buf.append(System.getProperty("os.arch"));

		return buf.toString();
	}

	public static boolean isWindows()
	{
		String os = System.getProperty("os.name").toLowerCase();//windows
		return (os.indexOf("win") >= 0);
	}

	public static boolean isMac()
	{
		String os = System.getProperty("os.name").toLowerCase();//Mac
		return (os.indexOf("mac") >= 0);
	}

	public static boolean isUnix()
	{
		String os = System.getProperty("os.name").toLowerCase();//linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}
}
