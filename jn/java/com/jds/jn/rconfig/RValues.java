package com.jds.jn.rconfig;

import com.jds.jn.network.listener.types.ReceiveType;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 21:35:08
 */
public enum RValues
{
	USE_TRAY("UseTray", false),
	LAST_FOLDER("LastFolder", "./logs/"),
	ACTIVE_PROFILE("ActiveProfile", null),
	MAIN_VISIBLE("MainVisible", 1F),
	ACTIVE_TYPE("ActiveType", ReceiveType.JPCAP.name()),
	SAVE_AS_DECODE("SaveAsDecode", false);

	private String _name;
	private Object _default;
	private Object _val;

	RValues(String name, Object dval)
	{
		_name = name;
		_default = dval;
	}

	public String getName()
	{
		return _name;
	}

	protected String getVal()
	{
		return String.valueOf(_val == null ? (_val = _default) : _val);
	}

	public void setVal(Object val)
	{
		_val = val;
	}

	public float asFloat()
	{
		return Float.parseFloat(getVal());
	}

	public boolean asBoolean()
	{
		return Boolean.parseBoolean(getVal());
	}

	public String asString()
	{
		return getVal();
	}

	public ReceiveType asReceiveType()
	{
		return ReceiveType.valueOf(getVal());
	}
}
