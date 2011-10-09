package com.jds.jn.parser.parttypes;

import com.jds.jn.parser.Type;
import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.formattree.Part;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:03:42/07.04.2010
 */
public class VisualPartType extends PartType
{
	private Type _type;

	public VisualPartType(String name, Type t)
	{
		super(name);
		_type = t;
	}

	public ValuePart getValuePart(DataTreeNodeContainer parent, Part part)
	{
		return new VisualValuePart(parent, part, _type);
	}

	@Override
	public boolean isReadableType()
	{
		return true;
	}

	@Override
	public PartValueType getValueType()
	{
		return _type.getInstance().getValueType();
	}

	public Type getType()
	{
		return _type;
	}
}