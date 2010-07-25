package com.jds.jn.util;

import java.awt.*;
import java.lang.reflect.Field;

import sun.awt.windows.WToolkit;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:53:29/20.07.2010
 */
public class WindowsUtils
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
		catch (Throwable t)
		{
			throw new RuntimeException(t.toString());
		}
	}


	public static Field getDeclaredField(Class clazz, String fieldName) throws NoSuchFieldException
	{
		Class c = clazz;
		while (c != null && c != Object.class)
		{
			try
			{
				return c.getDeclaredField(fieldName);
			}
			catch (NoSuchFieldException e)
			{
			}
			c = c.getSuperclass();
		}
		throw new NoSuchFieldException(fieldName);
	}

}
