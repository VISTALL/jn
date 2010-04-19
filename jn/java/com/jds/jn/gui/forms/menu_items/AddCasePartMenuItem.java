package com.jds.jn.gui.forms.menu_items;

import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.parser.formattree.SwitchPart;
import com.jds.jn.statics.ImageStatic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21/11/2009
 * Time: 17:59:48
 */
public class AddCasePartMenuItem extends JMenuItem
{
	public AddCasePartMenuItem(final PacketForm form, final SwitchPart part)
	{
		super("Add Case Block");

		setIcon(ImageStatic.ICON_PLUS);

		addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				EnterNameDialog dialog = new EnterNameDialog(form, "Enter id");
				if (!dialog.showToWrite())
				{
					return;
				}
				if (dialog.getText().equalsIgnoreCase("default"))
				{
					SwitchCaseBlock newBlock = new SwitchCaseBlock(part);
					part.addCase(newBlock);
				}
				else
				{
					try
					{
						int id = Integer.parseInt(dialog.getText());
						SwitchCaseBlock newBlock = new SwitchCaseBlock(part, id);
						part.addCase(newBlock);
					}
					catch (NumberFormatException e1)
					{
						e1.printStackTrace();
					}
				}


				form.setPacket(new DataPacket(form.getPacket().getFullBuffer().clone().array(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
				form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());

				form.updateCurrentPacket();
			}
		});
	}
}
