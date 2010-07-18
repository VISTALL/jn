package com.jds.jn.version_control;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  22:44:19/18.07.2010
 */
public enum Programs
{
	UNKNOWN("Unknown"),
	JN("Jn"),
	JN_MODULE("Jn Module");
	
	private final String _name;

	Programs(String name)
	{
		_name = name;
	}

	public String getName()
	{
		return _name;
	}
}
