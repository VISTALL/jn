package com.jds.jn.rconfig;

import javolution.util.FastList;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 19:51:47
 */
public class LastFiles
{
	private static FastList<String> _list = new FastList<String>();

	public static void addLastFile(String last)
	{
		if (!_list.contains(last))
		{
			_list.add(last);
		}
	}

	public static void clearFiles()
	{
		_list.clear();
	}

	public static FastList<String> getLastFiles()
	{
		return _list;
	}
}
