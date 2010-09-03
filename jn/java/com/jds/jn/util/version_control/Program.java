package com.jds.jn.util.version_control;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  22:44:19/18.07.2010
 */
public enum Program
{
	UNKNOWN("Unknown"),
	JN("Jn"),
	JN_MODULE("Jn Module"),
	PACKET_SAMURAI("Packet Samurai"),
	L2PHX("L2Phx");

	private final String _name;

	Program(String name)
	{
		_name = name;
	}

	public String getName()
	{
		return _name;
	}
}
