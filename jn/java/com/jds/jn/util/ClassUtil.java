package com.jds.jn.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author VISTALL
 * @date 12:38/15.02.2011
 */
public class ClassUtil
{
	private static final Logger LOGGER = Logger.getLogger(ClassUtil.class);

	public static <T> List<T> newInstancesFrom(List<Class<T>> classes)
	{
		List<T> list = new ArrayList<T>(classes.size());
		for(Class<T> c : classes)
			try
			{
				list.add(c.newInstance());
			}
			catch(Exception e)
			{
				LOGGER.info("Fail to init clazz: " + c.getName());
			}
		return list;
	}
}
