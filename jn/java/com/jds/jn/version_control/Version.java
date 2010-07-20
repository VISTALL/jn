package com.jds.jn.version_control;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:18:39/20.06.2010
 */
public class Version
{
	public static final byte ALPHA 	= 0;
	public static final byte BETA 	= 1;
	public static final byte RC 	= 2;
	public static final byte STABLE = 3;

	public static final Version UNKNOWN = new Version(Programs.UNKNOWN, 0, 0, ALPHA, 0);
	public static final Version CURRENT = new Version(Programs.JN, 2, 0, ALPHA, 1);

	private final Programs _program;
	private final int _major;
	private final int _minor;
	private final byte _type; // 0 - alpha 1 - beta 2 - rc 3 - stable
	private final int _number;

	public Version(Programs p, int major, int minor, byte type, int number)
	{
		_program = p;
		_major = major;
		_minor = minor;
		_type = type;
		_number = number;
	}

	@Override
	public String toString()
	{
		if(_program == Programs.UNKNOWN)
			return _program.getName();
		else
			return _program.getName() + " " + _major + "." + _minor + " " + getTypeAsString();
	}

	public String getTypeAsString()
	{
		switch (_type)
		{
			case ALPHA:
				return "Alpha " + _number;
			case 1:
				return "Beta " + _number;
			case 2:
				return "RC " + _number;
			default:
				return "Stable";
		}
	}

	public int getMajor()
	{
		return _major;
	}

	public int getMinor()
	{
		return _minor;
	}

	public byte getType()
	{
		return _type;
	}

	public int getNumber()
	{
		return _number;
	}
}
