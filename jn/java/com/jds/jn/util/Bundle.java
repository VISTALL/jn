package com.jds.jn.util;

import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  0:22:23/20.06.2010
 */
public class Bundle
{
	private static ResourceBundle _bundle;

	static
	{
		_bundle = ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle");
	}

	public static String getString(String t, Object... at)
	{
		return String.format(_bundle.getString(t), at);
	}
}
