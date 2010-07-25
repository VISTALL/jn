package com.jds.jn.parser;

import java.util.*;

import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.parser.parttypes.VisualPartType;
import com.jds.jn.statics.ImageStatic;


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
			VisualPartType t = new VisualPartType(type.name(), type);

			registerPartType(t);
			ImageStatic.getInstance().registerIcon(t, type.getIcon());
		}

		registerPartType(PartType.b);
		registerPartType(PartType.forBlock);
		registerPartType(PartType.swicthBlock);
		registerPartType(PartType.block);

		ImageStatic.getInstance().registerIcon(PartType.b, ImageStatic.PART_BYTE_ARRAY);
	}
}