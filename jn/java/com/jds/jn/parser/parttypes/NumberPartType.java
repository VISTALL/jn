package com.jds.jn.parser.parttypes;

import com.jds.jn.parser.PartType;
import com.jds.jn.parser.Types;
import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.NumberValuePart;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:03:42/07.04.2010
 */
public class NumberPartType extends PartType
{
	private Types _type;

	public NumberPartType(String name, Types t)
	{
		super(name);
		_type = t;
	}

	public ValuePart getValuePart(DataTreeNodeContainer parent, Part part)
	{
		return new NumberValuePart(parent, part, _type);
	}

	@Override
	public boolean isReadableType()
	{
		return true;
	}
}