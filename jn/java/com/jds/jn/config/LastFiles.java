package com.jds.jn.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 19:51:47
 */
public class LastFiles
{
	private static List<String> _list = new ArrayList<String>();

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

	public static List<String> getLastFiles()
	{
		return _list;
	}
}
