package com.jds.jn.parser.formattree;

import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * Used to represent the structure, it does not contain data
 *
 * @author Ulysses R. Ribeiro, Gilles Duboscq
 */
public class Part
{
	private PartType _type;
	private int _id = -1;
	private String _name = "";
	private PartContainer _parentContainer;
	private Format _containingFormat;
	private int _bxSizeId;
	private int _bxSize;
	private boolean _dynamicBSize = false;
	private boolean _isRoot;
	private boolean _isRealPart;
	private ValueReader _reader;
	private boolean _invert;

	public Part(PartType type, int id, String name, boolean invert)
	{
		setType(type);
		setId(id);
		setName(name);
		_isRealPart = true;
		_invert = invert;
	}

	public Part(PartType type)
	{
		this(type, false);
	}

	public Part(boolean isRoot)
	{
		this(PartType.block, isRoot);
	}

	public Part(PartType type, boolean isRoot)
	{
		this.setType(type);
		_isRoot = isRoot;
		_isRealPart = false;
	}

	public void setType(PartType type)
	{
		_type = type;
	}

	public PartType getType()
	{
		return _type;
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		_id = id;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	/**
	 * @return The parent container or null if this is the top level part.
	 */
	public PartContainer getParentContainer()
	{
		return _parentContainer;
	}

	public void setParentContainer(PartContainer pc)
	{
		_parentContainer = pc;
	}

	public void setContainingFormat(Format format)
	{
		_containingFormat = format;
	}

	public Format getContainingFormat()
	{
		return _containingFormat;
	}

	@Override
	public String toString()
	{
		return this.getType().getName();
	}

	public void setBSizeId(int sizeid)
	{
		_bxSizeId = sizeid;
	}

	public int getBSizeId()
	{
		return _bxSizeId;
	}

	public boolean isDynamicBSize()
	{
		return _dynamicBSize;
	}

	public void setDynamicBSize(boolean b)
	{
		_dynamicBSize = b;
	}

	public void setBSize(int size)
	{
		_bxSize = size;
	}

	public int getBSize()
	{
		return _bxSize;
	}

	public boolean isRoot()
	{
		return _isRoot;
	}

	public boolean isRealPart()
	{
		return _isRealPart;
	}

	public void setReader(ValueReader r)
	{
		_reader = r;
	}

	public ValueReader getReader()
	{
		return _reader;
	}

	public String treeString()
	{
		return "";
	}

	public boolean isInvert()
	{
		return _invert;
	}

}
