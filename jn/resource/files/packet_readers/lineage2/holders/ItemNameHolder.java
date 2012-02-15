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
 * @author VISTALL
 * @date 19:57:29/31.07.2010
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

			Document document = reader.read(zipFile.getInputStream(zipFile.getEntry("ItemName.xml")));
			for(Iterator<Element> iterator = document.getRootElement().elementIterator(); iterator.hasNext();)
			{
				Element e = iterator.next();

				int itemId = Integer.parseInt(e.element("item_id").getText());
				String name = e.element("name").getText();

				_itemNames.put(itemId, name);
			}
		}
		catch(Exception e)
		{
			_log.warn("Exception:" + e, e);
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

