package com.jds.jn.parser.parttypes;


import com.jds.jn.parser.PartType;
import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;

/**
 * @author Gilles Duboscq
 */
public class BlockPartType extends PartType
{
	public enum blockType
	{
		forblock,
		switchblock,
		block
	}

	private blockType _type;

	public BlockPartType(String name, blockType type)
	{
		super(name);
		_type = type;
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

	public blockType getType()
	{
		return _type;
	}

	@Override
	public int getTypeByteNumber()
	{
		return -1;
	}

}
