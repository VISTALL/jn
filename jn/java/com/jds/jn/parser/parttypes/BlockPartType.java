package com.jds.jn.parser.parttypes;


import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;

/**
 * @author Gilles Duboscq
 */
public class BlockPartType extends PartType
{
	public BlockPartType(String name)
	{
		super(name);
	}

	@Override
	public ValuePart getValuePart(DataTreeNodeContainer parent, Part part)
	{
		return null;
	}

	@Override
	public boolean isReadableType()
	{
		return false;
	}

	@Override
	public PartValueType getValueType()
	{
		return PartValueType.BLOB;
	}



	@Override
	public int getTypeByteNumber()
	{
		return -1;
	}

}
