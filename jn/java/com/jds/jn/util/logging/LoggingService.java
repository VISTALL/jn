package com.jds.jn.util.logging;

import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;
import java.util.logging.*;

import com.jds.jn.util.logging.log4j.JuliToLog4JHandler;

/**
 * @author VISTALL
 */
public class LoggingService
{
	public static void load()
	{
		try
		{
			URL url = LoggingService.class.getResource("log4j.xml");
			DOMConfigurator.configure(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		Logger logger = LogManager.getLogManager().getLogger("");
		for (Handler h : logger.getHandlers())
		{
			logger.removeHandler(h);
		}

		logger.addHandler(new JuliToLog4JHandler());
	}
}
