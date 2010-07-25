package com.jds.jn.parser.parttypes;


import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.formattree.Part;

/**
 * @author Gilles Duboscq
 */
public class RawPartType extends PartType
{
	public RawPartType(String name)
	{
		super(name);
	}

	@Override
	public ValuePart getValuePart(DataTreeNodeContainer parent, Part part)
	{
		return new RawValuePart(parent, part);
	}

	@Override
	public boolean isReadableType()
	{
		return true;
	}

	@Override
	public int getTypeByteNumber()
	{
		return 0;
	}

}
