package com.jds.jn.parser.datatree;

import com.jds.jn.parser.DataStructure;
import com.jds.jn.parser.Types;
import com.jds.jn.parser.formattree.Part;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:07:25/07.04.2010
 */
public class NumberValuePart extends ValuePart
{
	private Number _value;
	private Types _type;

	public NumberValuePart(DataTreeNodeContainer parent, Part part, Types type)
	{
		super(parent, part);
		_type = type;
	}

	@Override
	public void parse(NioBuffer buf, DataStructure s)
	{
		if (getMode() == DataStructure.DataPacketMode.FORGING)
		{
			throw new IllegalStateException("Can not parse on a Forging mode Data JPacket Tree element");
		}

		final int position = buf.position();

		_value = (Number) _type.getInstance().getValue(buf);

		_startPosition = position;
		_endPosition = buf.position();

		updateColor(s);
	}

	@Override
	public String getNormalColor()
	{
		return _type.name();
	}

	public double getValueAsDouble()
	{
		if (getModelPart().isInvert())
		{
			return -_value.doubleValue();
		}
		else
		{
			return _value.doubleValue();
		}
	}

	public float getValueAsFloat()
	{
		if (getModelPart().isInvert())
		{
			return -_value.floatValue();
		}
		else
		{
			return _value.floatValue();
		}
	}

	public long getValueAsLong()
	{
		if (getModelPart().isInvert())
		{
			return -_value.longValue();
		}
		else
		{
			return _value.longValue();
		}
	}

	public int getValueAsInt()
	{
		if (getModelPart().isInvert())
		{
			return -_value.intValue();
		}
		else
		{
			return _value.intValue();
		}
	}

	public short getValueAsShort()
	{
		if (getModelPart().isInvert())
		{
			return (short) (-_value.shortValue());
		}
		else
		{
			return _value.shortValue();
		}
	}

	public byte getValueAsByte()
	{
		if (getModelPart().isInvert())
		{
			return (byte) -_value.byteValue();
		}
		else
		{
			return _value.byteValue();
		}
	}

	@Override
	public String getValueAsString()
	{
		return String.valueOf(getValueAsLong());
	}

	@Override
	public String getHexValueAsString()
	{
		if (_type == Types.D || _type == Types.f)
		{
			return Double.toHexString(getValueAsLong()).toUpperCase();
		}
		else
		{
			return "0x" + Long.toHexString(getValueAsLong()).toUpperCase();
		}
	}
}
