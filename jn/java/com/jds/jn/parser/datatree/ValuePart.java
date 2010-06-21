package com.jds.jn.parser.datatree;

import com.jds.jn.Jn;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.DecryptPacket.DataPacketMode;
import com.jds.jn.parser.PartType;
import com.jds.jn.parser.formattree.ForPart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.SwitchPart;
import com.jds.jn.parser.valuereader.ValueReader;
import com.jds.jn.util.Util;
import com.jds.nio.buffer.NioBuffer;

import javax.swing.*;


/**
 * This class represent a ValuePart (c, h, d...) used to parse data from a raw packet, thus it may contain the data parsed from the packet.
 *
 * @author Gilles Duboscq
 */
public class ValuePart extends DataTreeNode
{
	protected byte[] _bytes;
	private int _bxSize = -1;

	protected int _startPosition;
	protected int _endPosition;
	private boolean _isSelected;

	public ValuePart(DataTreeNodeContainer parent, Part part)
	{
		super(parent, part);
		if (part instanceof ForPart || part instanceof SwitchPart)
		{
			throw new IllegalArgumentException("The model of a value part must be a basic part not a for/switch or any other container");
		}
	}

	public void parse(NioBuffer buf, DecryptPacket d)
	{
		if (getMode() == DataPacketMode.FORGING)
		{
			throw new IllegalStateException("Can not parse on a Forging mode Data JPacket Tree element");
		}

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

	public PartType getType()
	{
		return getModelPart().getType();
	}

	public String getHexDump()
	{
		return Util.hexDump(_bytes);
	}

	public byte[] getBytes()
	{
		if (!this.getType().isReadableType())
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

	public int getBSize()
	{
		if (_bxSize < 0)
		{
			if (getModelPart().isDynamicBSize())
			{
				ValuePart vp = this.getParentContainer().getPacketValuePartById(getModelPart().getBSizeId());
				if (vp == null || !(vp instanceof NumberValuePart))
				{
					Jn.getInstance().warn("ValuePart: Invalid SizeId for bx: part can not be found or is not an integer");
				}
				else
				{
					setBSize(((NumberValuePart) vp).getValueAsInt());
				}
			}
			else
			{
				setBSize(getModelPart().getBSize());
			}
		}
		return _bxSize;
	}

	public String getValueAsString()
	{
		return new String(_bytes);
	}


	public String getHexValueAsString()
	{
		return "";
	}

	public String readValue()
	{
		ValueReader r = this.getModelPart().getReader();
		if (r != null)
		{
			return r.read(this);
		}
		return getValueAsString();
	}

	public JComponent readValueToComponent()
	{
		ValueReader r = getModelPart().getReader();
		if (r != null)
		{
			return r.readToComponent(this);
		}
		return new JLabel("");
	}

	public String getNormalColor()
	{
		return "b";
	}

	public String getColor()
	{
		if(_isSelected)
		{
			return "selected";
		}
		else
		{
			return getNormalColor();
		}
	}

	public void updateColor(DecryptPacket a)
	{
		for(int index = _startPosition; index < _endPosition; index ++)
		{
			a.setColor(index,getColor());
		}
	}

	public boolean isSelected()
	{
		return _isSelected;
	}

	public void setSelected(boolean selected)
	{
		_isSelected = selected;
	}

	@Override
	public String toString()
	{
		return this.getModelPart().getName();
	}
}
