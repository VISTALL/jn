package packet_readers.aion.holders;

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
public class ClientStringHolder
{
	private static final Logger _log = Logger.getLogger(ClientStringHolder.class);
	private static ClientStringHolder _instance;

	private IntObjectMap<String> _strings = new HashIntObjectMap<String>();

	public static ClientStringHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new ClientStringHolder();
		}
		return _instance;
	}

	private ClientStringHolder()
	{
		InputStream stream = getClass().getResourceAsStream("/com/jds/jn/resources/datas/client_strings.csv");
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

				_strings.put(itemId, itemName);
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

		_log.info("Load client strings " + _strings.size());
	}

	public String name(int itemId)
	{
		String name = _strings.get(itemId);
		return name == null ? "None" : name;
	}
}

