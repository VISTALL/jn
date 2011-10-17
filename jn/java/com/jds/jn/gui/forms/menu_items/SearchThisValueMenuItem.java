/*
 * Jn (Java sNiffer)
 * Copyright (C) 2011 napile.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.jds.jn.gui.forms.menu_items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.ImageStatic;

/**
 * @author VISTALL
 * @date 20:40/13.10.2011
 */
public class SearchThisValueMenuItem extends JMenuItem
{
	public SearchThisValueMenuItem(final PacketForm form)
	{
		super(Bundle.getString("SearchThisValue"));

		setIcon(ImageStatic.FIND);

		addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object node = form.getPacketStructure().getModel().getValueAt(form.getPacketStructure().getSelectedRow(), 0);
				if(node == null)
					return;

				ValuePart part = (ValuePart) node;
				String value = null;
				switch(form.getPacketStructure().getSelectedColumn())
				{
					case 0:
						value = part.getModelPart().getName();
						break;
					case 1:
						value = part.getValueAsString();
						break;
					case 2:
						value = part.getHexValueAsString();
						break;
					default:
						return;
				}

				form.getPane().getDecryptedPacketListPane().getFormForSearch().getOperatorEqual().setText(value);
			}
		});
	}
}
