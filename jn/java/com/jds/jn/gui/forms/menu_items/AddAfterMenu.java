package com.jds.jn.gui.forms.menu_items;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.parser.PartType;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.statics.ImageStatic;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21/11/2009
 * Time: 15:07:37
 */
public class AddAfterMenu extends JMenu
{
	public AddAfterMenu(final PacketForm form)
	{
		super(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("AddAfterPart"));

		setIcon(ImageStatic.ICON_PLUS);

		for (PartType type : PartTypeManager.getInstance().getTypes())
		{
			if (type.getName().endsWith("lock")) //ахахахахха, но работает типы switchBlock и другие не показываются
			{
				continue;
			}

			JMenuItem i = new JMenuItem(type.getName(), ImageStatic.getInstance().getIconForPartType(type));
			i.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.out.println(e.getSource().getClass());
					JMenuItem item = (JMenuItem) e.getSource();
					Object node = form.getPacketStructure().getModel().getValueAt(form.getPacketStructure().getSelectedRow(), 0);
					if (node == null)
					{
						return;
					}

					if (!(node instanceof ValuePart))
					{
						return;
					}

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

					if (pC == null)
					{
						return;
					}

					pC.addPartAfter(p, part.getModelPart());

					form.setPacket(new DecryptPacket(form.getPacket().getNotDecryptData().clone(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
					form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());

					form.updateCurrentPacket();
				}
			});
			add(i);
		}
	}
}
