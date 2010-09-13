package com.jds.jn.parser.datatree;

import javax.swing.*;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.formattree.*;
import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.parser.valuereader.ValueReader;
import com.jds.nio.buffer.NioBuffer;


/**
 * This class represent a ValuePart (c, h, d...) used to parse data from a raw packet, thus it may contain the data parsed from the packet.
 *
 * @author Gilles Duboscq
 */
public abstract class ValuePart extends DataTreeNode
{
	protected int _startPosition;
	protected int _endPosition;
	private boolean _isSelected;

	public ValuePart(DataTreeNodeContainer parent, Part part)
	{
		super(parent, part);
		if (part instanceof ForPart || part instanceof SwitchPart || part instanceof MacroPart)
		{
			throw new IllegalArgumentException("The model of a value part must be a basic part not a for/switch or any other container");
		}
	}

	public PartType getType()
	{
		return getModelPart().getType();
	}


	public abstract String getValueAsString();
	public abstract void parse(NioBuffer buf, DecryptedPacket d);

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

	public void updateColor(DecryptedPacket a)
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
		return getModelPart().getName();
	}
}
