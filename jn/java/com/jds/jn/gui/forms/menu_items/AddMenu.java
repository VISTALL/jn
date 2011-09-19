package com.jds.jn.gui.forms.menu_items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.util.ImageStatic;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21/11/2009
 * Time: 15:03:34
 */
public class AddMenu extends JMenu
{
	public AddMenu(final PacketForm form)
	{
		super(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("AddPart"));

		setIcon(ImageStatic.ICON_PLUS);

		for (PartType type : PartTypeManager.getInstance().getTypes())
		{
			if (type.getName().endsWith("lock"))//ахахахахха, но работает типы switchBlock и другие не показываются
			{
				continue;
			}

			JMenuItem i = new JMenuItem(type.getName(), ImageStatic.getInstance().getIconForPartType(type));
			i.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					JMenuItem item = (JMenuItem) e.getSource();
					Object node = form.getPacketStructure().getModel().getValueAt(form.getPacketStructure().getSelectedRow(), 0);
					if (node == null)
					{
						return;
					}

					if (node instanceof ValuePart)
					{
						EnterNameDialog dialog = new EnterNameDialog(form, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("EnterName"));
						if (!dialog.showToWrite())
						{
							return;
						}

						ValuePart part = (ValuePart) node;
						PartContainer pC = part.getModelPart().getParentContainer();
						PartType partType = PartTypeManager.getInstance().getType(item.getText());
						Part p = new Part(partType);
						p.setName(dialog.getText());
						if (pC != null)
						{
							pC.addPart(p);

							/*form.setPacket(new DecryptedPacket(form.getPacket().getCryptedData().clone(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
							form.getPane().getDecryptPacketTableModel().updatePacket(form.getRow(), form.getPacket());

							form.updateCurrentPacket();   */
						}
					}
					else if (node instanceof DataSwitchBlock)
					{
						EnterNameDialog dialog = new EnterNameDialog(form, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("EnterName"));
						if (!dialog.showToWrite())
						{
							return;
						}
						DataSwitchBlock switchBlock = (DataSwitchBlock) node;
						SwitchCaseBlock block = (SwitchCaseBlock) switchBlock.getModelPart();

						PartType partType = PartTypeManager.getInstance().getType(item.getText());
						Part p = new Part(partType);
						p.setName(dialog.getText());
						if (block != null)
						{
							block.addPart(p);

							/*form.setPacket(new DecryptedPacket(form.getPacket().getCryptedData().clone(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
							form.getPane().getDecryptPacketTableModel().updatePacket(form.getRow(), form.getPacket());

							form.updateCurrentPacket(); */
						}
					}
				}
			});
			add(i);

		}

		JMenuItem addSwitch = new JMenuItem("Add switch");
		addSwitch.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object node = form.getPacketStructure().getModel().getValueAt(form.getPacketStructure().getSelectedRow(), 0);
				if (node == null)
				{
					return;
				}
				/*DataTreeNodeContainer n = (DataTreeNodeContainer)getInstance.getPacketStructure().getModel().getValueAt(getInstance.getPacketStructure().getSelectedRow(), 0);
				n.addNode();   */


				EnterNameDialog dialog = new EnterNameDialog(form, "Enter id to use");
				if (!dialog.showToWrite())
				{
					return;
				}
			}
		});

		add(addSwitch);

	}
}
