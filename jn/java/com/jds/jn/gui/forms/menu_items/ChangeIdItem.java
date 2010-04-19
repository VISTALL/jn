package com.jds.jn.gui.forms.menu_items;

import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.parser.datatree.ValuePart;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 22/11/2009
 * Time: 23:07:02
 */
public class ChangeIdItem extends JMenuItem
{
	public ChangeIdItem(final PacketForm form)
	{
		super("Change Id");

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
					ValuePart part = (ValuePart) node;
					EnterNameDialog dialog = new EnterNameDialog(form, "Enter id", String.valueOf(part.getModelPart().getId()));

					if (dialog.showToWrite())
					{
						try
						{
							part.getModelPart().setId(Integer.parseInt(dialog.getText()));
							//getInstance.getPane().getPacketTableModel().updatePacket(getInstance.getRow(), getInstance.getPacket());
							//getInstance.getPane().getPacketTableModel().updatePackets(getInstance.getPacket());
							form.updateCurrentPacket();
							form.getPacketStructure().updateUI();
						}
						catch (NumberFormatException e1)
						{
							//e1.printStackTrace();
						}
					}
				}
			}
		});
	}
}
