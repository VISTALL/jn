package com.jds.jn.gui.renders;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.awt.*;

import com.jds.jn.config.RValues;
import com.jds.jn.gui.models.DataPartNode;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.util.ImageStatic;

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


			if(node.getPacketNode() instanceof ValuePart)
			{
				boolean isSelected = ((ValuePart)node.getPacketNode()).isSelected();

				setBackgroundNonSelectionColor(isSelected ? RValues.PACKET_FORM_SELECT_BACKGROUND_COLOR_2.asTColor() : RValues.PACKET_FORM_NOT_SELECT_BACKGROUND_COLOR_2.asTColor());
				setTextNonSelectionColor(isSelected ? RValues.PACKET_FORM_SELECT_FOREGROUND_COLOR_2.asTColor() : RValues.PACKET_FORM_NOT_SELECT_FOREGROUND_COLOR_2.asTColor());
			}

		}
		return this;
	}
}
