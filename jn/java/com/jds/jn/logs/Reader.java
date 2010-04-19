package com.jds.jn.logs;

import com.jds.jn.logs.readers.JNLReader;

import java.io.File;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:40:56
 */
public class Reader
{
	public static void read(String fileName) throws Exception
	{
		if (fileName.endsWith(".jnl"))
		{
			File file = new File(fileName);
			if (!file.exists())
			{
				return;
			}
			JNLReader reader = new JNLReader(file);
			reader.read();
		}
	}
}
