package com.jds.jn.parser.parttypes;


import com.jds.jn.parser.PartType;
import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.ValuePart;
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
		return new ValuePart(parent, part);
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
