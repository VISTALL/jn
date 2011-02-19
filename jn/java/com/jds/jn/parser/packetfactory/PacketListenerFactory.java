package com.jds.jn.parser.packetfactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.jds.jn.classes.CLoader;

/**
 * @author VISTALL
 * @date 12:38/15.02.2011
 */
public class PacketListenerFactory
{
	private static final Logger _log = Logger.getLogger(PacketListenerFactory.class);

	public static List<IPacketListener> listListeners(List<Class<IPacketListener>> classes)
	{
		List<IPacketListener> list = new ArrayList<IPacketListener>(classes.size());
		for(Class<IPacketListener> c : classes)
		{
			try
			{
				list.add(c.newInstance());
			}
			catch(Exception e)
			{
				_log.info("Fail to init clazz: " + c.getName());
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List<Class<IPacketListener>> listClasses(Node root)
	{
		List<Class<IPacketListener>> classes = new ArrayList<Class<IPacketListener>>(2);
		for (Node n = root.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if(n.getNodeName().equalsIgnoreCase("listener"))
			{
				NamedNodeMap map = n.getAttributes();
				Node value = map.getNamedItem("name");
				if(value != null)
				{
					try
					{
						Class<?> clazz = CLoader.getInstance().forName("packet_readers." + value.getNodeValue());
						if(IPacketListener.class.isAssignableFrom(clazz))
							classes.add((Class<IPacketListener>)clazz);
						else
							_log.info("Class: " + value.getNodeValue() + " is not instanceof IPacketListener");
					}
					catch(Exception e)
					{
						_log.info("Not find listener: " + value.getNodeValue());
					}
				}

			}
		}
		return classes.isEmpty() ? Collections.<Class<IPacketListener>>emptyList() : classes;
	}
}
