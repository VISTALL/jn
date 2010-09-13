package com.jds.jn.parser.datatree;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.util.Util;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  13:44:00/23.07.2010
 */
public class RawValuePart extends ValuePart
{
	protected byte[] _bytes;
	protected int _bxSize = -1;

	public RawValuePart(DataTreeNodeContainer parent, Part part)
	{
		super(parent, part);
	}

	@Override
	public void parse(NioBuffer buf, DecryptedPacket d)
	{
		int pos = buf.position();
		int size = 0;
		size = getBSize();
		if (size > 65536)
		{
			size = 1;
		}
		// sets the raw bytes
		_bytes = new byte[size];
		buf.position(pos);
		buf.get(_bytes);

		_startPosition = pos;
		_endPosition = buf.position();

		updateColor(d);
	}

	public int getBSize()
	{
		if (_bxSize < 0)
		{
			if (getModelPart().isDynamicBSize())
			{
				ValuePart vp = getParentContainer().getPacketValuePartById(getModelPart().getBSizeId());
				if (vp == null || !(vp instanceof VisualValuePart))
				{
					MainForm.getInstance().info("ValuePart: Invalid SizeId for bx: part can not be found or is not an integer");
				}
				else
				{
					setBSize(((VisualValuePart) vp).getValueAsInt());
				}
			}
			else
			{
				setBSize(getModelPart().getBSize());
			}
		}
		return _bxSize;
	}

	public byte[] getBytes()
	{
		if (!getType().isReadableType())
		{
			throw new IllegalStateException("Trying to get bytes from a " + this.getType().getName());
		}
		return _bytes;
	}

	@Override
	public int getBytesSize()
	{
		if (_bytes == null)
		{
			return 0;
		}
		return _bytes.length;
	}

	public void setBSize(int size)
	{
		if (size < 0)
		{
			_bxSize = 0;
		}
		else
		{
			_bxSize = size;
		}
	}

	@Override
	public String getValueAsString()
	{
		return new String(_bytes);
	}

	public String getHexDump()
	{
		return Util.hexDump(_bytes);
	}
}
