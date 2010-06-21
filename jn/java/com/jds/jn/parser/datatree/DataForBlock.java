package com.jds.jn.parser.datatree;

import com.jds.jn.parser.formattree.PartContainer;

/**
 * @author Gilles Duboscq
 */
public class DataForBlock extends DataTreeNodeContainer
{
	private int _iteration;
	private int _size;

	public DataForBlock(DataTreeNodeContainer container, PartContainer part, int iteration, int size)
	{
		super(container, part);
		_iteration = iteration;
		_size = size;
	}

	@Override
	public String toString()
	{
		return "Iteration " + (_iteration + 1) + "/" + _size;
	}
}