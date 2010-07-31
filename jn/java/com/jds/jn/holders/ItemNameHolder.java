package com.jds.jn.holders;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  19:57:29/31.07.2010
 */
public class ItemNameHolder
{
	private static final Logger _log = Logger.getLogger(ItemNameHolder.class);
	private static ItemNameHolder _instance;

	private Map<Integer, String> _itemNames = new HashMap<Integer, String>();

	public static ItemNameHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new ItemNameHolder();
		}
		return _instance;
	}

	private ItemNameHolder()
	{
		InputStream stream = getClass().getResourceAsStream("/com/jds/jn/resources/datas/itemname.tsv");
		if(stream == null)
		{
			_log.info("Not exists");
			return;
		}

		LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(stream));

		try
		{
			String line = null;
			while((line = lineReader.readLine()) != null)
			{
				if(line.contains("#"))
					continue;

				String[] st = line.split("\t");
				int itemId = Integer.parseInt(st[0]);
				String itemName = st[1];

				_itemNames.put(itemId, itemName);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				lineReader.close();
			}
			catch (IOException e)
			{
				//
			}
		}

		_log.info("Load item names " + _itemNames.size());
	}

	public String name(int npcId)
	{
		return _itemNames.get(npcId) == null ? "None" : _itemNames.get(npcId);
	}
}

