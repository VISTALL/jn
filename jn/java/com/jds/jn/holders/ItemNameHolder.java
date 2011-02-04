package com.jds.jn.holders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.log4j.Logger;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  19:57:29/31.07.2010
 */
public class ItemNameHolder
{
	private static final Logger _log = Logger.getLogger(ItemNameHolder.class);
	private static ItemNameHolder _instance;

	private IntObjectMap<String> _itemNames = new HashIntObjectMap<String>();

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
		InputStream stream = getClass().getResourceAsStream("/com/jds/jn/resources/datas/itemname-e.tsv");
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
			_log.info("Exception: " + e, e);
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
		_itemNames.put(-100, "Clan Reputation");
		_itemNames.put(-200, "Pc Bang Point");
		_itemNames.put(-300, "Fame");
		_itemNames.put(-400, "Hellbound Point");

		_log.info("Load item names " + _itemNames.size());
	}

	public String name(int itemId)
	{
		String name = _itemNames.get(itemId);
		return name == null ? "None" : name;
	}
}

