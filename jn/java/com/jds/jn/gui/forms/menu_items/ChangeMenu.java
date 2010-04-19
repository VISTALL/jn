package com.jds.jn.gui.forms.menu_items;

import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.parser.PartType;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.statics.ImageStatic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21/11/2009
 * Time: 15:10:57
 */
public class ChangeMenu extends JMenu
{
	public ChangeMenu(final PacketForm form)
	{
		super(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Change"));

		setIcon(ImageStatic.ICON_CHANGE);

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

					ValuePart part = (ValuePart) node;
					PartContainer pC = part.getModelPart().getParentContainer();
					PartType partType = PartTypeManager.getInstance().getType(item.getText());
					Part p = new Part(partType);
					p.setName(part.toString());

					pC.replace(part.getModelPart(), p);

					form.setPacket(new DataPacket(form.getPacket().getFullBuffer().clone().array(), form.getPacket().getPacketType(), form.getPacket().getProtocol()));
					form.getPane().getPacketTableModel().updatePacket(form.getRow(), form.getPacket());

					form.updateCurrentPacket();
				}
			});
			add(i);
		}
	}
}
