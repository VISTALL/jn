package com.jds.jn.gui.forms.menu_items;

import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.util.ImageStatic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21/11/2009
 * Time: 15:51:07
 */
public class RenameMenuItem extends JMenuItem
{
	public RenameMenuItem(final PacketForm form)
	{
		super(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Rename"));

		setIcon(ImageStatic.ICON_EDIT);

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
					EnterNameDialog dialog = new EnterNameDialog(form, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("EnterName"), part.toString());

					if (dialog.showToWrite())
					{
						part.getModelPart().setName(dialog.getText());
						form.getPacketStructure().updateUI();
					}
				}
				else if (node instanceof DataSwitchBlock)
				{
					DataSwitchBlock b = (DataSwitchBlock) node;
					SwitchCaseBlock block = (SwitchCaseBlock) b.getModelPart();

					/*if(block.isDefault())
					{
						//listener.clear();
						return;
					}*/

					EnterNameDialog dialog = new EnterNameDialog(form, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("EnterName"), block.getName());

					if (dialog.showToWrite())
					{
						block.setName(dialog.getText());
						form.getPane().getDecryptPacketTableModel().updatePacket(form.getRow(), form.getPacket());
						form.getPane().getDecryptPacketTableModel().updatePackets(form.getPacket());
						form.updateCurrentPacket();
						form.getPacketStructure().updateUI();
					}

					//listener.clear();
				}
			}
		});
	}
}
