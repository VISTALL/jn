package com.jds.jn.parser.formattree;

import java.util.*;
import java.util.Map.Entry;

import com.jds.jn.parser.parttypes.PartType;

/**
 * @author Gilles Duboscq
 */
public class SwitchPart extends Part
{
	private Map<Integer, SwitchCaseBlock> _casesMap = new HashMap<Integer, SwitchCaseBlock>();
	private SwitchCaseBlock _default;
	private int _switchId;

	public SwitchPart(int id)
	{
		super(PartType.swicthBlock, -1, "SwitchPart", false);
		this.setSwitchId(id);
	}

	public SwitchCaseBlock getCase(int switchCase)
	{
		SwitchCaseBlock c = _casesMap.get(switchCase);
		if (c == null)
		{
			c = this.getDefaultCase();
		}
		return c;
	}

	public void setSwitchId(int id)
	{
		_switchId = id;
	}

	public int getSwitchId()
	{
		return _switchId;
	}

	/**
	 * switches can not have an id
	 */
	public int getId()
	{
		return -1;
	}

	public List<Integer> getCasesIds()
	{
		List<Integer> cases = new ArrayList<Integer>();
		for (Entry<Integer, SwitchCaseBlock> entry : _casesMap.entrySet())
		{
			cases.add(entry.getKey());
		}
		return cases;
	}

	public List<SwitchCaseBlock> getCases()
	{
		List<SwitchCaseBlock> cases = new ArrayList<SwitchCaseBlock>();
		for (Entry<Integer, SwitchCaseBlock> entry : _casesMap.entrySet())
		{
			cases.add(entry.getValue());
		}
		return cases;
	}

	public List<SwitchCaseBlock> getCases(boolean includeDefault)
	{
		List<SwitchCaseBlock> cases = getCases();
		if (includeDefault && _default != null)
		{
			cases.add(_default);
		}
		return cases;
	}

	public void addCase(SwitchCaseBlock iCase)
	{
		iCase.setParentContainer(this.getParentContainer()); // this can NOT be root
		iCase.setContainingFormat(this.getContainingFormat());
		if (iCase.isDefault())
		{
			_default = iCase;
		}
		else
		{
			_casesMap.put(iCase.getSwitchCase(), iCase);
		}
	}

	public Part getTestPart()
	{
		return this.getParentContainer().getPacketPartByIdInScope(this.getSwitchId(), this);
	}

	@Override
	public String treeString()
	{
		Part pp = getTestPart();
		if (pp != null)
		{
			return "Switch.. : " + pp.getName();
		}
		return "Switch..";
	}

	public boolean removeCase(int switchCase)
	{
		if (_casesMap.remove(switchCase) != null)
		{
			return true;
		}
		if (_default.getSwitchCase() == switchCase)
		{
			_default = null;
			return true;
		}
		return false;
	}

	public boolean removeCase(SwitchCaseBlock sCase)
	{
		return removeCase(sCase.getSwitchCase());
	}

	public void setDefaultCase(SwitchCaseBlock dcase)
	{
		dcase.setParentContainer(this.getParentContainer()); // this can NOT be root
		dcase.setContainingFormat(this.getContainingFormat());
		dcase.setDefault(true);
		_default = dcase;
	}

	public SwitchCaseBlock getDefaultCase()
	{
		return _default;
	}

	@Override
	public void setParentContainer(PartContainer pc)
	{
		super.setParentContainer(pc);
		for (SwitchCaseBlock block : getCases())
		{
			block.setParentContainer(pc);
		}
	}

	@Override
	public void setContainingFormat(Format format)
	{
		super.setContainingFormat(format);
		for (SwitchCaseBlock block : getCases())
		{
			block.setContainingFormat(format);
		}
	}
}