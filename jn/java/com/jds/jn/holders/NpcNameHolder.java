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
public class NpcNameHolder
{
	private static final Logger _log = Logger.getLogger(NpcNameHolder.class);
	private static NpcNameHolder _instance;

	private IntObjectMap<String> _npcNames = new HashIntObjectMap<String>();

	public static NpcNameHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new NpcNameHolder();
		}
		return _instance;
	}

	private NpcNameHolder()
	{
		InputStream stream = getClass().getResourceAsStream("/com/jds/jn/resources/datas/npcname-e.tsv");
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
				try
				{
					String[] st = line.split("\t");
					int npcId = Integer.parseInt(st[0]);
					String npcName = "none";
					if(st.length > 1)
						npcName = st[1];

					_npcNames.put(npcId, npcName);
				}
				catch (Exception e)
				{
					_log.info("Line: " + line, e);
				}
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

		_log.info("Load npc names " + _npcNames.size());
	}

	public String name(int npcId)
	{
		return _npcNames.get(npcId) == null ? "None" : _npcNames.get(npcId);
	}
}

