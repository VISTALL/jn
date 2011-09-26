package com.jds.jn.util;

import java.io.File;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21 лист 2009
 * Time: 10:37:01
 */
public class FileUtils
{
	public static String getFileExtension(File f)
	{
		String name = f.getName();
		int dot = name.lastIndexOf('.');
		return name.substring(dot + 1);
	}
}
