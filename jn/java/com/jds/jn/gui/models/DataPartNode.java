package com.jds.jn.gui.models;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import com.jds.jn.parser.datatree.DataForBlock;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.datatree.DataTreeNode;
import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.ForPart;
import com.jds.jn.parser.formattree.MacroPart;
import com.jds.jn.parser.formattree.SwitchCaseBlock;

public class DataPartNode extends DefaultMutableTreeTableNode
{
	private DataTreeNode _node;

	public DataPartNode(DataTreeNode node)
	{
		super();
		_node = node;

		if (_node instanceof DataTreeNodeContainer)
		{
			int i = 0;
			for (DataTreeNode n : ((DataTreeNodeContainer) _node).getNodes())
			{
				insert(new DataPartNode(n), i++);
			}
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
				return "For: " + part.getForId();
			}
			else if (_node.getModelPart() instanceof MacroPart)
			{
				MacroPart part = (MacroPart) _node.getModelPart();
				return part.getName() + "; Macro: " + part.getMacroId();
			}
			else if (_node instanceof DataForBlock)
			{
				return _node.toString();
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
		return _node instanceof ValuePart;
	}

	public DataTreeNode getPacketNode()
	{
		return _node;
	}

}
