package com.jds.jn.gui.renders;

import com.jds.jn.gui.models.DataPartNode;
import com.jds.jn.statics.ImageStatic;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 23:20:13
 */
public class PacketViewTreeRenderer extends DefaultTreeCellRenderer
{
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value instanceof DataPartNode && leaf)
		{
			DataPartNode node = (DataPartNode) value;

			setText(node.getPacketNode().toString());
			setIcon(ImageStatic.getInstance().getIconForPartType(node.getPacketNode().getModelPart().getType()));
		}
		return this;
	}
}
