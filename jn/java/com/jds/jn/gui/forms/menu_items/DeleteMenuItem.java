package com.jds.jn.gui.forms.menu_items;

import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.parser.formattree.SwitchPart;
import com.jds.jn.statics.ImageStatic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21/11/2009
 * Time: 15:14:12
 */
public class DeleteMenuItem extends JMenuItem
{
	public DeleteMenuItem(final PacketForm form)
	{
		super(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Delete"));

		setIcon(ImageStatic.ICON_DEL);

		addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object node = form.getPacketStructure().getModel().getValueAt(form.getPacketStructure().getSelectedRow(), 0);
				if (node == null)
				{
					return;
				}

				if (node instanceof ValuePart)
				{

					/* TODO if (Config.get(Values.DELETE_PART_CONFIRM, true))
					{
						ConfirmDialog dialog = new ConfirmDialog(form, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Message"), ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("ConfirmDeletePartMessage"));
						boolean[] result = dialog.showToConfirm();
						if (result[0])
						{
							Config.set(Values.DELETE_PART_CONFIRM, result[1]);

							ValuePart part = (ValuePart) node;
							form.getPacket().getPacketFormat().getDataFormat().getMainBlock().removePart(part.getModelPart());
							form.setPacket(new DataPacket(form.getPacket().getFullBuffer().clone().array(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
							form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());
							form.updateCurrentPacket();
						}
					}
					else
					{
						ValuePart part = (ValuePart) node;
						form.getPacket().getPacketFormat().getDataFormat().getMainBlock().removePart(part.getModelPart());
						form.setPacket(new DataPacket(form.getPacket().getFullBuffer().clone().array(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
						form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());
						form.updateCurrentPacket();
					} */
				}
				else if (node instanceof DataSwitchBlock)
				{
					DataSwitchBlock b = (DataSwitchBlock) node;
					SwitchCaseBlock block = (SwitchCaseBlock) b.getModelPart();
					final SwitchPart part = block.getContainingSwitch();

					/*if (block.isDefault())
					{
						return;
					} */

				/* TODO	if (Config.get(Values.DELETE_PART_CONFIRM, true))
					{
						ConfirmDialog dialog = new ConfirmDialog(form, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Message"), ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("ConfirmDeletePartMessage"));
						boolean[] result = dialog.showToConfirm();

						if (result[0])
						{
							Config.set(Values.DELETE_PART_CONFIRM, result[1]);

							part.removeCase(block);

							form.setPacket(new DataPacket(form.getPacket().getFullBuffer().clone().array(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
							form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());

							form.updateCurrentPacket();
						}
					}
					else
					{
						part.removeCase(block);

						form.setPacket(new DataPacket(form.getPacket().getFullBuffer().clone().array(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
						form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());

						form.updateCurrentPacket();
					} */
				}
			}
		});
	}
}
