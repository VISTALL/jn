package packet_readers.lineage2.holders;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
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
		URL url = getClass().getResource("/com/jds/jn/resources/datas/l2-data.zip");

		if(url == null)
		{
			_log.info("Not exists");
			return;
		}

		ZipFile zipFile = null;
		try
		{
			zipFile = new ZipFile(new File(url.toURI()));
		}
		catch(Exception e)
		{
			_log.info(e, e);
			return;
		}

		try
		{
			SAXReader reader = new SAXReader();

			Document document = reader.read(zipFile.getInputStream(zipFile.getEntry("SkillName.xml")));
			for(Iterator<Element> iterator = document.getRootElement().elementIterator(); iterator.hasNext();)
			{
				Element e = iterator.next();

				int id = Integer.parseInt(e.element("id").getText());
				int level = Integer.parseInt(e.element("level").getText());
				String name = e.element("name").getText();

				_skillNames.put(hashCode(id, level), name);
			}
		}
		catch(Exception e)
		{
			_log.warn("Exception:" + e, e);
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

