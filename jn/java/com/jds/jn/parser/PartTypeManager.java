package com.jds.jn.parser;

import com.jds.jn.parser.parttypes.NumberPartType;
import com.jds.jn.statics.ImageStatic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Gilles Duboscq
 */
public class PartTypeManager
{
	private Map<String, PartType> _map;

	private static PartTypeManager _instance;

	public static PartTypeManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new PartTypeManager();
		}

		return _instance;
	}

	private PartTypeManager()
	{
		_map = new HashMap<String, PartType>();

		initBaseTypes();
	}

	public PartType getType(String name)
	{
		return _map.get(name);
	}

	public void registerPartType(PartType type)
	{
		_map.put(type.getName(), type);
	}

	public Collection<PartType> getTypes()
	{
		return _map.values();
	}

	public void initBaseTypes()
	{
		for(Types type : Types.values())
		{
			NumberPartType t = new NumberPartType(type.name(), type);

			registerPartType(t);
			ImageStatic.getInstance().registerIcon(t, type.getIcon());
		}

		registerPartType(PartType.S);
		registerPartType(PartType.SS);
		registerPartType(PartType.sS);
		registerPartType(PartType.s);
		registerPartType(PartType.b);
		registerPartType(PartType.forBlock);
		registerPartType(PartType.swicthBlock);
		registerPartType(PartType.block);

		ImageStatic.getInstance().registerIcon(PartType.S, ImageStatic.PART_NORMAL_STRING);
		ImageStatic.getInstance().registerIcon(PartType.SS, ImageStatic.PART_NORMAL_STRING);
		ImageStatic.getInstance().registerIcon(PartType.s, ImageStatic.PART_NORMAL_STRING);
		ImageStatic.getInstance().registerIcon(PartType.b, ImageStatic.PART_BYTE_ARRAY);
	}
}