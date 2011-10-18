package com.jds.jn;

import com.jds.jn.util.logging.LoggingService;
import packet_readers.lineage2.holders.ItemNameHolder;

/**
 * Author: VISTALL
 * Date:  10:55/21.12.2010
 */
public class JnTest
{
	public static void main(String... arg) throws Exception
	{
		LoggingService.load();

		ItemNameHolder.getInstance();
	}
}
