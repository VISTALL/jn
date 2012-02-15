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
public class NpcNameHolder
{
	private static final Logger _log = Logger.getLogger(NpcNameHolder.class);
	private static NpcNameHolder _instance;

	private IntObjectMap<String> _npcNames = new HashIntObjectMap<String>();
	private IntObjectMap<String> _npcTitles = new HashIntObjectMap<String>();

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

			Document document = reader.read(zipFile.getInputStream(zipFile.getEntry("NpcName.xml")));
			for(Iterator<Element> iterator = document.getRootElement().elementIterator(); iterator.hasNext();)
			{
				Element e = iterator.next();

				int id = Integer.parseInt(e.element("id").getText());
				String name = e.element("name").getText();
				String title = e.element("description").getText();

				_npcNames.put(id, name);
				_npcTitles.put(id, title);
			}
		}
		catch(Exception e)
		{
			_log.warn("Exception:" + e, e);
		}

		_log.info("Load npc names " + _npcNames.size());
	}

	public String name(int npcId)
	{
		return _npcNames.containsKey(npcId) ? _npcNames.get(npcId) : "";
	}

	public String title(int npcId)
	{
		return _npcTitles.containsKey(npcId) ? _npcTitles.get(npcId) : "";
	}
}

