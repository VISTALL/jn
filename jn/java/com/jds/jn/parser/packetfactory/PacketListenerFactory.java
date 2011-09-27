package com.jds.jn.parser.packetfactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

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
}
