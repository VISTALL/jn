package com.jds.jn.parser.formattree;

import com.jds.jn.parser.PartType;


/**
 * @author Gilles Duboscq
 */
public class SwitchCaseBlock extends PartContainer
{
	private int _case;
	private boolean _isDefault;
	private SwitchPart _switch;

	/**
	 * This constructor creates a new non-default SwitchCase
	 *
	 * @param sw the containing SwithBlock
	 * @param id the case of this SwichCase
	 */
	public SwitchCaseBlock(SwitchPart sw, int id)
	{
		super(PartType.swicthBlock);
		_case = id;
		_switch = sw;
	}

	/**
	 * This constructor creates a default SwitchCase
	 *
	 * @param sw the containing SwithBlock
	 */
	public SwitchCaseBlock(SwitchPart sw)
	{
		super(PartType.swicthBlock);
		_isDefault = true;
		_switch = sw;

	}

	/**
	 * Returns the case of this SwitchCase. This is irrelevant if this is a default case.
	 *
	 * @return the case of this SwitchCase
	 */
	public int getSwitchCase()
	{
		return _case;
	}

	public SwitchPart getContainingSwitch()
	{
		return _switch;
	}

	public boolean isDefault()
	{
		return _isDefault;
	}

	@Override
	public String treeString()
	{
		if (_isDefault)
		{
			return "Default";
		}
		return "Case 0x" + Integer.toHexString(_case) + (_case > 9 ? " (" + _case + ")" : "");
	}

	public void setDefault(boolean b)
	{
		_isDefault = b;
	}

	public void setSwitchCase(int id)
	{
		_switch.removeCase(_case);
		_case = id;
		_switch.addCase(this);
	}

	@Override
	public String toString()
	{
		return _isDefault ? "default" : _switch.getTestPart().getName() + " = " + Integer.toString(_case);
	}
}