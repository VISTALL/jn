package com.jds.jn.parser.parttypes;


import com.jds.jn.parser.PartType;
import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.StringValuePart;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;

/**
 * @author Gilles Duboscq  && VISTALL
 */
public class StringPartType extends PartType
{
	public enum StringType
	{
		S,
		s,
		sS, //short String
		SS  // Stupied String
	}

	private StringType _type;

	public StringPartType(String name, StringType type)
	{
		super(name);
		_type = type;
	}

	@Override
	public ValuePart getValuePart(DataTreeNodeContainer parent, Part part)
	{
		return new StringValuePart(parent, part, _type);
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
