package com.jds.jn_module.utils;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:53:29/20.07.2010
 */
public class OSUtils
{
	public static String getLibName()
	{
		StringBuilder buf = new StringBuilder("jpcap-");
		boolean win = isWindows();
		boolean mac = isMac();
		boolean unix = isUnix();
		if(win)
			buf.append("win");
		else if(mac)
			buf.append("mac");
		else if(unix)
			buf.append("unix");

		buf.append("-");
		buf.append(System.getProperty("os.arch"));

		return buf.toString();
	}

	public static boolean isWindows()
	{
		String os = System.getProperty("os.name").toLowerCase();//windows
		return (os.contains("win"));
	}

	public static boolean isMac()
	{
		String os = System.getProperty("os.name").toLowerCase();//Mac
		return (os.contains("mac"));
	}

	public static boolean isUnix()
	{
		String os = System.getProperty("os.name").toLowerCase();//linux or unix
		return (os.contains("nix") || os.contains("nux"));
	}
}
