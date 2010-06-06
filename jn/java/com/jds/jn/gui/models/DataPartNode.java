package com.jds.jn.gui.models;

import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.formattree.ForPart;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class DataPartNode extends DefaultMutableTreeTableNode
{
	private DataTreeNode _node;
	private int _offset;
	private int _length;

	public DataPartNode(DataTreeNode node, int offset)
	{
		super();
		_node = node;

		if (_node instanceof DataTreeNodeContainer)
		{
			int i = 0;
			for (DataTreeNode n : ((DataTreeNodeContainer) _node).getNodes())
			{
				insert(new DataPartNode(n, offset), i++);

				setOffset(offset);
				setLengtht(_node.getBytesSize() * 3 - 1);
				offset += n.getBytesSize() * 3;
			}
		}
		else
		{
			setOffset(offset);
			setLengtht(_node.getBytesSize() * 3 - 1);
			offset += this.getLength() + 1;
		}

	}

	@Override
	public String toString()
	{
		if (_node instanceof DataTreeNodeContainer)
		{
			if (_node instanceof DataSwitchBlock)
			{
				if (_node.getModelPart() instanceof SwitchCaseBlock)
				{
					SwitchCaseBlock block = (SwitchCaseBlock) _node.getModelPart();
					if (block.isDefault())
					{
						if (block.getName().equals(""))
						{
							return "Case: default";
						}
						else
						{
							return block.getName();
						}
					}
					else
					{
						if (block.getName().equals(""))
						{
							return "Case: " + block.getSwitchCase();
						}
						else
						{
							return block.getName();
						}
					}
				}
			}
			else if (_node.getModelPart() instanceof ForPart)
			{
				ForPart part = (ForPart) _node.getModelPart();
				return "id " + part.getForId();
			}
		}
		return "";
	}
	
	@Override
	public boolean getAllowsChildren()
	{
		return (_node instanceof DataTreeNodeContainer);
	}

	@Override
	public boolean isLeaf()
	{
		return (_node instanceof ValuePart);
	}

	public DataTreeNode getPacketNode()
	{
		return _node;
	}

	private void setOffset(int offset)
	{
		_offset = offset;
	}

	public int getOffset()
	{
		return _offset;
	}

	private void setLengtht(int length)
	{
		_length = length;
	}

	public int getLength()
	{
		return _length;
	}
}
