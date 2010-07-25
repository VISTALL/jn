package com.jds.jn.protocol.protocoltree;

import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.parser.formattree.PartContainer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  14:02:40/21.07.2010
 */
public class MacroInfo
{
	private final String _id;
	private PartContainer _modelBlock;

	public MacroInfo(String id)
	{
		_id = id;
		_modelBlock = new PartContainer(PartType.block, true);
	}

	public String getId()
	{
		return _id;
	}

	public PartContainer getModelBlock()
	{
		return _modelBlock;
	}
}
