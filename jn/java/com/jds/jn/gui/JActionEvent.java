package com.jds.jn.gui;

import com.jds.jn.network.listener.types.ListenerType;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 9:00:44
 */
public enum JActionEvent
{
	LISTENER_1(ListenerType.Auth_Server),
	LISTENER_2(ListenerType.Game_Server),
	ADD_PROFILE,
	//REMOVE_PROFILE,
	PROGRAM_SETTINGS,
	NETWORK_SETTINGS,
	HIDE_SHOW,
	GC,
	EXCEPTION_WINDOW,
	EXIT,
	OPEN_FILE,
	OPEN_SELECT_FILE,
	CLEAR_LAST_FILES,
	CONSOLE_TAB,
	SAVE_SESSION,
	VIEW_TAB;

	private final ListenerType _type;

	JActionEvent()
	{
		this(null);
	}

	JActionEvent(ListenerType type)
	{
		_type = type;
	}

	public ListenerType type()
	{
		return _type;
	}
}
