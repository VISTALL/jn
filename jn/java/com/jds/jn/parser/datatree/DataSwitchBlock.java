package com.jds.jn.parser.datatree;

import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.parser.formattree.SwitchPart;

public class DataSwitchBlock extends DataTreeNodeContainer
{
	private ValuePart _valuePart;

	public DataSwitchBlock(DataTreeNodeContainer container, SwitchCaseBlock part, ValuePart vp)
	{
		super(container, part);
		_valuePart = vp;
	}

	@Override
	public String toString()
	{
		SwitchCaseBlock block = (SwitchCaseBlock) getModelPart();
		SwitchPart part = block.getContainingSwitch();
		if (block.isDefault())
		{
			return "Switch on '" + part.getTestPart().getName() + "' - default case";
		}
		return "Switch on '" + part.getTestPart().getName() + "' - case '" + getValuePart().readValue() + "'";
	}

	public ValuePart getValuePart()
	{
		return _valuePart;
	}
}