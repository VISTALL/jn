package com.jds.jn.parser.datatree;

import java.util.ArrayList;
import java.util.List;

import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import com.jds.jn.parser.formattree.ForPart;
import com.jds.jn.parser.formattree.MacroPart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.parser.parttypes.PartType;


/**
 * @author Gilles Duboscq
 */
public class DataTreeNodeContainer extends DataTreeNode
{
	private IntObjectMap<ValuePart> _partIdMap = Containers.emptyIntObjectMap();
	private List<DataTreeNode> _nodes = new ArrayList<DataTreeNode>();
	private boolean _isRoot;

	protected DataTreeNodeContainer(DataTreeNodeContainer container, Part part)
	{
		super(container, part);
		if (!(part instanceof PartContainer || part instanceof ForPart || part instanceof MacroPart))
		{
			throw new IllegalArgumentException("The model of a packet node container must be a blockpart/switchcase/forpart/macro");
		}
		_isRoot = false;
	}

	public DataTreeNodeContainer()
	{
		super();
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
			if(_partIdMap == Containers.<ValuePart>emptyIntObjectMap())
				_partIdMap = new HashIntObjectMap<ValuePart>(4);

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
			return getParentContainer().getPacketValuePartById(id);
		return vp;
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

	public DataTreeNode getPartByNameDeep(String name, boolean enterSwitch)
	{
		for (DataTreeNode node : getNodes())
		{
			if (name.equals(node.getModelPart().getName()))
			{
				return node;
			}

			if (enterSwitch && node instanceof DataTreeNodeContainer && node.getModelPart().getType() == PartType.swicthBlock)
			{
				DataTreeNode partByName = ((DataTreeNodeContainer) node).getPartByNameDeep(name, true);
				if(partByName != null)
				{
					return partByName;
				}
			}
		}

		DataTreeNodeContainer parentContainer = getParentContainer();
		if(parentContainer != null)
		{
			DataTreeNode partByNameDeep = parentContainer.getPartByNameDeep(name, enterSwitch);
			if(partByNameDeep != null)
			{
				return partByNameDeep;
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

	public DataForPart getFirstForById(int id)
	{
		for (DataTreeNode node : this.getNodes())
		{
			if (node.getModelPart() instanceof ForPart && ((ForPart) node.getModelPart()).getForId() == id)
				return (DataForPart)node;
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

	public float getFloat(String s)
	{
		return ((VisualValuePart)getPartByName(s)).getValueAsFloat();
	}

	public double getDouble(String s)
	{
		return ((VisualValuePart)getPartByName(s)).getValueAsDouble();
	}

	public int getInt(String s)
	{
		return ((VisualValuePart)getPartByName(s)).getValueAsInt();
	}

	public long getLong(String s)
	{
		return ((VisualValuePart)getPartByName(s)).getValueAsLong();
	}

	public String getString(String s)
	{
		return ((VisualValuePart)getPartByName(s)).getValueAsString();
	}

	public byte[] getBytes(String s)
	{
		return ((RawValuePart)getPartByName(s)).getBytes();
	}
}