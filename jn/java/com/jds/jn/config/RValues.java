package com.jds.jn.config;

import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.config.properties.PropertyValue;
import com.jds.swing.JColor;

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
	@PropertyValue("PrintUnknownPacket")
	PRINT_UNKNOWN_PACKET(true),
	@PropertyValue("LastFolder")
	LAST_FOLDER("../logs/"),
	@PropertyValue("ProtocolDir")
	PROTOCOL_DIR("../protocols/"),
	@PropertyValue("ActiveProfile")
	ACTIVE_PROFILE(null, String.class),
	@PropertyValue("MainVisible")
	MAIN_VISIBLE(1F),
	@PropertyValue("ActiveType")
	ACTIVE_TYPE(ReceiveType.JPCAP),

	//colors hex dump
	@PropertyValue("PacketForm_Select_BackgroundColor")
	PACKET_FORM_SELECT_BACKGROUND_COLOR(new JColor(JColor.YELLOW)),
	@PropertyValue("PacketForm_Select_ForegroundColor")
	PACKET_FORM_SELECT_FOREGROUND_COLOR(new JColor(JColor.BLACK)),
	//colors parts
	@PropertyValue("PacketForm_Select_BackgroundColor2")
	PACKET_FORM_SELECT_BACKGROUND_COLOR_2(new JColor(JColor.YELLOW)),
	@PropertyValue("PacketForm_Select_ForegroundColor2")
	PACKET_FORM_SELECT_FOREGROUND_COLOR_2(new JColor(JColor.BLACK)),
	@PropertyValue("PacketForm_NotSelect_BackgroundColor2")
	PACKET_FORM_NOT_SELECT_BACKGROUND_COLOR_2(new JColor(JColor.WHITE)),
	@PropertyValue("PacketForm_NotSelect_ForegroundColor2")
	PACKET_FORM_NOT_SELECT_FOREGROUND_COLOR_2(new JColor(JColor.BLACK)),

	@PropertyValue("LastWindowPositionX")
	LAST_WINDOW_POSITION_X(0),
	@PropertyValue("LastWindowPositionY")
	LAST_WINDOW_POSITION_Y(0),
	@PropertyValue("LastSearch")
	LAST_SEARCH(""),

	@PropertyValue("SaveAsDecode")
	SAVE_AS_DECODE(false);

	private Object _val;
	private Class<?> _type;

	RValues(Object dval)
	{
		this(dval, dval.getClass());
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

	public int asInt()
	{
		return (Integer)_val;
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
		if(_val instanceof ReceiveType)
			return (ReceiveType)_val;
		else
			return ReceiveType.valueOf((String)_val);
	}

	public JColor asTColor()
	{
		return (JColor)_val;
	}

	public Object getVal()
	{
		return _val;
	}
}
