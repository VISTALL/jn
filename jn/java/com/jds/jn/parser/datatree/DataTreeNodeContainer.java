package com.jds.jn.parser.datatree;

import java.util.*;

import com.jds.jn.network.packets.DecryptPacket.DataPacketMode;
import com.jds.jn.parser.PartType;
import com.jds.jn.parser.formattree.*;


/**
 * @author Gilles Duboscq
 */
public class DataTreeNodeContainer extends DataTreeNode
{
	private Map<Integer, ValuePart> _partIdMap = new HashMap<Integer, ValuePart>();
	private List<DataTreeNode> _nodes = new LinkedList<DataTreeNode>();
	private boolean _isRoot;

	// can not intantiate non-root container

	protected DataTreeNodeContainer(DataTreeNodeContainer container, Part part)
	{
		super(container, part);
		if (!(part instanceof PartContainer || part instanceof ForPart))
		{
			throw new IllegalArgumentException("The model of a packet node container must be a blockpart/switchcase/forpart/has");
		}
		_isRoot = false;
	}

	public DataTreeNodeContainer()
	{
		super();
		_isRoot = true;
	}

	public DataTreeNodeContainer(DataPacketMode mode)
	{
		super(mode);
		_isRoot = true;
	}

	public boolean isRoot()
	{
		return _isRoot;
	}

	public void addNode(DataTreeNode node)
	{
		_nodes.add(node);
		if (node instanceof ValuePart && node.getModelPart().getId() != -1)
		{
			_partIdMap.put(node.getModelPart().getId(), (ValuePart) node);
		}
	}

	/**
	 * Searches into parent containers until the part is found or the top level container is reached.
	 *
	 * @param id The Id of the PacketPart to be retrieved
	 * @return The PacketPart with the given Id if found, null otherwise
	 */
	public ValuePart getPacketValuePartById(int id)
	{
		ValuePart vp = _partIdMap.get(id);
		if (vp == null && !this.isRoot())
		{
			return this.getParentContainer().getPacketValuePartById(id);
		}
		return vp;
	}

	public Map<Integer, ValuePart> getPartIdMap()
	{
		return Collections.unmodifiableMap(_partIdMap);
	}

	public List<? extends DataTreeNode> getNodes()
	{
		return _nodes;
	}

	public DataTreeNode getPartByName(String name)
	{
		return getPartByName(name, false);
	}

	public DataTreeNode getPartByName(String name, boolean enterSwitch)
	{
		for (DataTreeNode node : getNodes())
		{
			if (name.equals(node.getModelPart().getName()))
			{
				return node;
			}
			if (enterSwitch && node instanceof DataTreeNodeContainer && node.getModelPart().getType() == PartType.swicthBlock)
			{
				((DataTreeNodeContainer) node).getPartByName(name, true);
			}
		}
		return null;
	}

	public DataTreeNode getPartById(int id)
	{
		return getPartById(id, false);
	}

	public DataTreeNode getPartById(int id, boolean enterSwitch)
	{
		for (DataTreeNode node : this.getNodes())
		{
			if (node.getModelPart().getId() == id)
			{
				return node;
			}
			if (enterSwitch && node instanceof DataTreeNodeContainer && node.getModelPart().getType() == PartType.swicthBlock)
			{
				((DataTreeNodeContainer) node).getPartById(id, true);
			}
		}
		return null;
	}


	@Override
	public int getBytesSize()
	{
		int size = 0;
		for (DataTreeNode dpn : this.getNodes())
		{
			size += dpn.getBytesSize();
		}
		return size;
	}
}