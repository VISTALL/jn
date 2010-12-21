package com.jds.jn.network.listener.types;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 17/12/2009
 * Time: 14:16:17
 */
public enum ListenerType
{
	Auth_Server(1),
	Game_Server(2);
	public static final ListenerType[] VALUES = values();

	private final int _id;

	ListenerType(int id)
	{
		_id = id;
	}

	public static ListenerType valuesOf(int i)
	{
		for (ListenerType p : values())
		{
			if (p.getId() == i)
			{
				return p;
			}
		}

		return null;
	}

	public int getId()
	{
		return _id;
	}
}
