package com.jds.jn.util;

import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  0:22:23/20.06.2010
 */
public class Bundle
{
	public static String getString(String t, Object... at)
	{
		return String.format(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString(t), at);
	}
}
