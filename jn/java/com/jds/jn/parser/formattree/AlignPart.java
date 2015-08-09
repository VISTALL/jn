package com.jds.jn.parser.formattree;

import com.jds.jn.parser.parttypes.PartType;

/**
 * @author VISTALL
 * @since 09.08.2015
 */
public class AlignPart extends Part
{
	private int myAlign;

	public AlignPart(int align)
	{
		super(PartType.align);
		myAlign = align;
	}

	public int getAlign()
	{
		return myAlign;
	}
}
