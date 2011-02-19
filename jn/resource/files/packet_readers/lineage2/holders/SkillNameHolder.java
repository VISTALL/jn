package packet_readers.lineage2.holders;

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
public class SkillNameHolder
{
	private static final Logger _log = Logger.getLogger(SkillNameHolder.class);
	private static SkillNameHolder _instance;

	private IntObjectMap<String> _skillNames = new HashIntObjectMap<String>();

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
		InputStream stream = getClass().getResourceAsStream("/com/jds/jn/resources/datas/skillname-e.tsv");
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
				int id = Integer.parseInt(st[0]);
				int level = Integer.parseInt(st[1]);
				String name = st[2];

				_skillNames.put(hashCode(id, level), name);
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

		_log.info("Load skills names " + _skillNames.size());
	}

	public String name(int id, int level)
	{
		String name = _skillNames.get(hashCode(id, level));
		return name == null ? "None" : name;
	}

	public static int hashCode(int skillId, int skillLevel)
	{
		return skillId * 1021 + skillLevel;
	}
}

