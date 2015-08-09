package com.jds.jn.parser.formattree;

import java.nio.ByteOrder;

import com.jds.jn.parser.parttypes.PartType;

/**
 * @author VISTALL
 * @since 09.08.2015
 */
public class ChangeOrderPart extends Part
{
	private ByteOrder myByteOrder;

	public ChangeOrderPart(ByteOrder byteOrder)
	{
		super(PartType.order);
		myByteOrder = byteOrder;
	}

	public ByteOrder getByteOrder()
	{
		return myByteOrder;
	}
}
