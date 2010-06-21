package com.jds.jn.parser.datatree;

import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.DecryptPacket.DataPacketMode;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.parttypes.StringPartType;
import com.jds.nio.buffer.NioBuffer;


/**
 * @author Gilles Duboscq
 */
public class StringValuePart extends ValuePart
{
	protected String _string;
	protected StringPartType.StringType _type;

	public StringValuePart(DataTreeNodeContainer parent, Part part, StringPartType.StringType type)
	{
		super(parent, part);
		_type = type;
	}

	@Override
	public void parse(NioBuffer buf, DecryptPacket s)
	{
		if (this.getMode() == DataPacketMode.FORGING)
		{
			throw new IllegalStateException("Can not parse on a Forging mode Data JPacket Tree element");
		}
		int pos = buf.position();
		int size = 0;
		switch (_type)
		{
			case s:
				StringBuffer sb = new StringBuffer();
				byte b;
				while ((b = buf.get()) != 0)
				{
					sb.append((char) b);
				}
				_string = sb.toString();
				size = sb.length() + 1;
				break;
			case S:
				StringBuffer sb2 = new StringBuffer();
				char ch;
				while ((ch = buf.getChar()) != 0)
				{
					sb2.append(ch);
				}
				_string = sb2.toString();
				size = sb2.length() * 2 + 2;
				break;
			case sS:
				StringBuffer sb4 = new StringBuffer();
				char ch2;
				while ((ch2 = buf.getChar()) != 0)
				{
					sb4.append(ch2);
				}
				_string = sb4.toString();
				size = sb4.length() * 2;
				break;
			case SS:
				StringBuffer sb3 = new StringBuffer();
				char ch1;
				while ((ch1 = buf.getChar()) != 0)
				{
					sb3.append(ch1);
				}
				_string = sb3.toString();
				size = getBSize();
				break;
		}
		// sets the raw bytes
		_bytes = new byte[size];
		buf.position(pos);
		buf.get(_bytes);

		for(int i = pos; i < buf.position(); i++)
		{
			s.setColor(i, "b");
		}
	}

	public String getStringValue()
	{
		return _string;
	}

	@Override
	public String getValueAsString()
	{
		return _string;
	}

	@Override
	public String getHexValueAsString()
	{
		return _string;
	}
}