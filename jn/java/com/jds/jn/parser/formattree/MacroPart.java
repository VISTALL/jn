package com.jds.jn.parser.formattree;

import com.jds.jn.parser.parttypes.PartType;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  13:57:22/21.07.2010
 */
public class MacroPart extends Part
{
	private String _id;

	public MacroPart(String id, String name)
	{
		super(PartType.macroBlock);
		_id = id;
		setName(name);
	}

	public String getMacroId()
	{
		return _id;
	}
}
