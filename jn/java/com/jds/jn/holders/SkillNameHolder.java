package com.jds.jn.holders;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  19:57:29/31.07.2010
 */
public class SkillNameHolder
{
	private static final Logger _log = Logger.getLogger(SkillNameHolder.class);
	private static SkillNameHolder _instance;

	private Map<Integer, String> _skillNames = new HashMap<Integer, String>();

	public static SkillNameHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new SkillNameHolder();
		}
		return _instance;
	}

	private SkillNameHolder()
	{
		InputStream stream = getClass().getResourceAsStream("/com/jds/jn/resources/datas/skillname.tsv");
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

				_skillNames.put(itemId, itemName);
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

		_log.info("Load skills names " + _skillNames.size());
	}

	public String name(int skillId)
	{
		String name = _skillNames.get(skillId);
		return name == null ? "None" : name;
	}
}

