package com.jds.jn.util;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:18:39/20.06.2010
 */
public enum Version
{
	UNKNOWN("Unknown Version"),   //0
	JN_2_0_M_1("Jn 2.0 M1"),      //1
	JN_2_0_M_2("Jn 2.0 M2");      //2

	private final String _name;

	Version(String name)
	{
		_name = name;
	}

	public static Version get(String vv)
	{
		if(vv == null)
		{
			return UNKNOWN;
		}

		for (Version v : values())
		{
			if(v.getName().equalsIgnoreCase(vv))
			{
				return v;
			}
		}

		return UNKNOWN;
	}

	public static Version currentEnum()
	{
		return JN_2_0_M_2;
	}

	public static String current()
	{
		return JN_2_0_M_2.getName();
	}

	public String getName()
	{
		return _name;
	}
}
