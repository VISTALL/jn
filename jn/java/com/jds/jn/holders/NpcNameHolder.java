package com.jds.jn.holders;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  19:57:29/31.07.2010
 */
public class NpcNameHolder
{
	private static final Logger _log = Logger.getLogger(NpcNameHolder.class);
	private static NpcNameHolder _instance;

	private Map<Integer, String> _npcNames = new HashMap<Integer, String>();

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
		InputStream stream = getClass().getResourceAsStream("/com/jds/jn/resources/datas/npcname.tsv");
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
					System.out.println("Line: " + line);
				}
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

		_log.info("Load npc names " + _npcNames.size());
	}

	public String name(int npcId)
	{
		return _npcNames.get(npcId) == null ? "None" : _npcNames.get(npcId);
	}
}

