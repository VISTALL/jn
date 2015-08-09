package com.jds.jn.parser.parttypes;


import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.parttypes.BlockPartType.blockType;


public abstract class PartType
{
	public static enum PartValueType
	{
		STRING,
		DIGITAL,
		BLOB
	}

	private final String _name;

	public static final PartType b = new RawPartType("b");

	public static final PartType order = new BlockPartType("order", blockType.block);
	public static final PartType ifBlock = new BlockPartType("ifBlock", blockType.forblock);
	public static final PartType forBlock = new BlockPartType("forblock", blockType.forblock);
	public static final PartType macroBlock = new BlockPartType("macroblock", blockType.macroBlock);
	public static final PartType swicthBlock = new BlockPartType("switchblock", blockType.switchblock);
	public static final PartType block = new BlockPartType("block", blockType.block);

	public PartType(String name)
	{
		_name = name;
	}

	public String getName()
	{
		return _name;
	}

	public abstract ValuePart getValuePart(DataTreeNodeContainer parent, Part part);

	public abstract boolean isReadableType();

	public abstract PartValueType getValueType();

	//public abstract boolean isBlockType();

	public int getTypeByteNumber(){return -1;}

	@Override
	public String toString()
	{
		return _name;
	}
}