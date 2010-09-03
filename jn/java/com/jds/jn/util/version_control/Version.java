package com.jds.jn.util.version_control;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:18:39/20.06.2010
 */
public class Version
{
	public static final byte M 		= 0;
	public static final byte RC 	= 1;
	public static final byte STABLE = 2;

	public static final Version UNKNOWN = new Version(Program.UNKNOWN, 0, 0, M, 0);
	public static final Version CURRENT = new Version(Program.JN, 2, 0, M, 1);

	private final Program _program;
	private final int _major;
	private final int _minor;
	private final byte _type; //
	private final int _number;

	public Version(Program p, int major, int minor, byte type, int number)
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
		if(_program == Program.UNKNOWN)
			return _program.getName();
		else
			return _program.getName() + " " + _major + "." + _minor + " " + getTypeAsString();
	}

	public String getTypeAsString()
	{
		switch (_type)
		{
			case M:
				return "M" + _number;
			case RC:
				return "RC" + _number;
		}

		return "";
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
