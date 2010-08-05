package com.jds.jn.parser.datatree;

import com.jds.jn.parser.formattree.Part;

/**
 * @author Gilles Duboscq
 */
public abstract class DataTreeNode
{
	private DataTreeNodeContainer _container;
	private Part _modelPart;

	public DataTreeNode(DataTreeNodeContainer container, Part part)
	{
		_container = container;
		_modelPart = part;
		_container.addNode(this);
	}

	public DataTreeNode()
	{
	}

	public DataTreeNodeContainer getParentContainer()
	{
		return _container;
	}

	public Part getModelPart()
	{
		return _modelPart;
	}

	public abstract int getBytesSize();

	@Override
	public String toString()
	{
		return "";
	}
}