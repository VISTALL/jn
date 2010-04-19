package com.jds.jn.gui.models;

import com.jds.jn.parser.datatree.DataTreeNode;
import com.jds.jn.parser.datatree.ValuePart;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 22:24:19
 */
public class PacketViewTableModel extends DefaultTreeTableModel
{
	private final String[] columnNames;

	public PacketViewTableModel(TreeTableNode root)
	{
		super(root);

		columnNames = new String[]{
				ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Name"),
				ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Value"),
				ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("HexValue"),
				ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Reader")
		};
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	@Override
	public Object getValueAt(Object node, int column)
	{
		DataTreeNode part = ((DataPartNode) node).getPacketNode();
		switch (column)
		{
			case 0:
				return part;
			case 1:
				if (part instanceof ValuePart)
				{
					return ((ValuePart) part).getValueAsString();
				}
				return "";
			case 2:
				if (part instanceof ValuePart)
				{
					return ((ValuePart) part).getHexValueAsString();
				}
				return "";
			case 3:
				if (part instanceof ValuePart)
				{
					return ((ValuePart) part).readValueToComponent();
				}
				return "";
			default:
				return "";
		}
	}
}

