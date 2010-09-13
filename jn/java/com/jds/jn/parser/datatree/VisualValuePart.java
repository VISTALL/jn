package com.jds.jn.parser.datatree;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.Types;
import com.jds.jn.parser.formattree.Part;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:07:25/07.04.2010
 */
public class VisualValuePart extends ValuePart
{
	private Object _value;
	private Types _type;

	public VisualValuePart(DataTreeNodeContainer parent, Part part, Types type)
	{
		super(parent, part);
		_type = type;
	}

	@Override
	public void parse(NioBuffer buf, DecryptedPacket s)
	{
		final int position = buf.position();

		_value = _type.getInstance().getValue(buf);

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
			return -((Number)_value).doubleValue();
		}
		else
		{
			return ((Number)_value).doubleValue();
		}
	}

	public float getValueAsFloat()
	{
		if (getModelPart().isInvert())
		{
			return -((Number)_value).floatValue();
		}
		else
		{
			return ((Number)_value).floatValue();
		}
	}

	public long getValueAsLong()
	{
		if (getModelPart().isInvert())
		{
			return -((Number)_value).longValue();
		}
		else
		{
			return ((Number)_value).longValue();
		}
	}

	public int getValueAsInt()
	{
		if (getModelPart().isInvert())
		{
			return -((Number)_value).intValue();
		}
		else
		{
			return ((Number)_value).intValue();
		}
	}

	public short getValueAsShort()
	{
		if (getModelPart().isInvert())
		{
			return (short) (-((Number)_value).shortValue());
		}
		else
		{
			return ((Number)_value).shortValue();
		}
	}

	public byte getValueAsByte()
	{
		if (getModelPart().isInvert())
		{
			return (byte) -((Number)_value).byteValue();
		}
		else
		{
			return ((Number)_value).byteValue();
		}
	}

	@Override
	public String getValueAsString()
	{
		return String.valueOf(_value);
	}

	@Override
	public String getHexValueAsString()
	{
		if(_value instanceof String)
		{
			return "";
		}
		else if (_type == Types.D || _type == Types.f)
		{
			return Double.toHexString(getValueAsLong()).toUpperCase();
		}
		else
		{
			return "0x" + Long.toHexString(getValueAsLong()).toUpperCase();
		}
	}

	@Override
	public int getBytesSize()
	{
		return 0;
	}
}
