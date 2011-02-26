package com.jds.jn.gui.forms.menu_items;

import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.parser.formattree.SwitchPart;
import com.jds.jn.util.ImageStatic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23/11/2009
 * Time: 19:01:40
 */
public class DeleteSwitchMenuItem extends JMenuItem
{
	public DeleteSwitchMenuItem(final PacketForm form)
	{
		super("Delete Switch Part");

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

				if (node instanceof DataSwitchBlock)
				{
					DataSwitchBlock b = (DataSwitchBlock) node;
					SwitchCaseBlock block = (SwitchCaseBlock) b.getModelPart();
					final SwitchPart part = block.getContainingSwitch();
					PartContainer con = part.getParentContainer();
					if (con == null)
					{
						return;
					}
					/*if (block.isDefault())
					{
						return;
					} */

					/*if (Config.get(Values.DELETE_PART_CONFIRM, true))
					{
						ConfirmDialog dialog = new ConfirmDialog(form, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Message"), ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("ConfirmDeletePartMessage"));
						boolean[] result = dialog.showToConfirm();

						if (result[0])
						{
							Config.set(Values.DELETE_PART_CONFIRM, result[1]);

							con.removePart(part);
							//part.removeCase(block);

							form.setPacket(new DataPacket(form.getPacket().getFullBuffer().clone().array(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
							form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());

							form.updateCurrentPacket();
						}
					}
					else
					{
						con.removePart(part);

						form.setPacket(new DataPacket(form.getPacket().getFullBuffer().clone().array(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
						form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());

						form.updateCurrentPacket();
					} */
				}
			}
		});
	}
}
