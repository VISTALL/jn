package com.jds.jn.parser.datatree;

import com.jds.jn.parser.formattree.IfPart;

/**
 * @author VISTALL
 * @since 09.08.2015
 */
public class DataIfPart extends DataTreeNodeContainer
{
	public DataIfPart(DataTreeNodeContainer container, IfPart part)
	{
		super(container, part);
	}

	@Override
	public String toString()
	{
		IfPart modelPart = (IfPart) getModelPart();
		return "If: " + modelPart.getFieldName() + " " + modelPart.getOperator() + " " + modelPart.getValue();
	}
}
