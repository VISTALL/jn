package com.jds.jn.parser;


import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.parttypes.*;
import com.jds.jn.parser.parttypes.BlockPartType.blockType;
import com.jds.jn.parser.parttypes.StringPartType.StringType;


public abstract class PartType
{
	private final String _name;

	public static final PartType S = new StringPartType("S", StringType.S);
	public static final PartType s = new StringPartType("s", StringType.s);
	public static final PartType SS = new StringPartType("SS", StringType.SS);
	public static final PartType sS = new StringPartType("sS", StringType.sS);

	public static final PartType b = new RawPartType("b");
	public static final PartType forBlock = new BlockPartType("forblock", blockType.forblock);
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

	//public abstract boolean isBlockType();

	public int getTypeByteNumber(){return -1;};

	@Override
	public String toString()
	{
		return _name;
	}
}