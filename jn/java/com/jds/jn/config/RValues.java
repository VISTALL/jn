package com.jds.jn.config;

import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.properties.PropertyValue;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 21:35:08
 */

public enum RValues
{
	@PropertyValue("UseTray")
	USE_TRAY(false),
	@PropertyValue("LastFolder")
	LAST_FOLDER("./logs/"),
	@PropertyValue("ActiveProfile")
	ACTIVE_PROFILE(null, String.class),
	@PropertyValue("MainVisible")
	MAIN_VISIBLE(1F),
	@PropertyValue("ActiveType")
	ACTIVE_TYPE(ReceiveType.JPCAP),
	@PropertyValue("SaveAsDecode")
	SAVE_AS_DECODE(false);

	private Object _val;
	private Class<?> _type;

	RValues(Object dval)
	{
		_type = dval.getClass();	
	}

	RValues(Object dval, Class<?> type)
	{
		_val = dval;
		_type = type;
	}

	public void setVal(Object val)
	{
		_val = val;
	}

	public Class<?> getType()
	{
		return _type;
	}  	

	public float asFloat()
	{
		return (Float)_val;
	}

	public boolean asBoolean()
	{
		return (Boolean)_val;
	}

	public String asString()
	{
		return (String)_val;
	}

	public ReceiveType asReceiveType()
	{
		return (ReceiveType)_val;
	}

	public Object getVal()
	{
		return _val;
	}
}
