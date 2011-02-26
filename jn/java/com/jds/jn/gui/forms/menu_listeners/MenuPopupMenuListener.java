package com.jds.jn.gui.forms.menu_listeners;

import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.gui.forms.menu_items.*;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.parser.formattree.SwitchPart;
import com.jds.jn.util.ImageStatic;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 22/11/2009
 * Time: 20:48:04
 */
public class MenuPopupMenuListener implements PopupMenuListener
{
	private final PacketForm _form;
	private final JPopupMenu _menu;

	public MenuPopupMenuListener(PacketForm form, JPopupMenu menu)
	{
		_form = form;
		_menu = menu;
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e)
	{
		Object node = _form.getPacketStructure().getModel().getValueAt(_form.getPacketStructure().getSelectedRow(), 0);
		if (node == null)
		{
			return;
		}

		if (node instanceof ValuePart)
		{
			ValuePart part = (ValuePart) node;

			_menu.add(new RenameMenuItem(_form));
			_menu.add(new AddMenu(_form));
			_menu.add(new AddAfterMenu(_form));
			_menu.add(new ChangeMenu(_form));
			_menu.add(new DeleteMenuItem(_form));

			JMenu item = new JMenu("Id: " + part.getModelPart().getId());
			ChangeIdItem i = new ChangeIdItem(_form);
			item.add(i);

			item.setIcon(ImageStatic.PART_ID);

			_menu.add(item);
		}
		else if (node instanceof DataSwitchBlock)
		{
			DataSwitchBlock b = (DataSwitchBlock) node;
			SwitchCaseBlock block = (SwitchCaseBlock) b.getModelPart();
			final SwitchPart part = block.getContainingSwitch();

			_menu.add(new RenameMenuItem(_form));
			_menu.add(new AddMenu(_form));
			_menu.add(new AddCasePartMenuItem(_form, part));
			_menu.add(new DeleteMenuItem(_form));
			_menu.add(new DeleteSwitchMenuItem(_form));

			if (block.isDefault())
			{

				_menu.addSeparator();
				JMenuItem item = new JMenuItem("Case for: " + b.getValuePart() + " [default]");
				item.setIcon(ImageStatic.PART_ID);
				_menu.add(item);
			}
			else
			{
				_menu.addSeparator();
				JMenuItem item = new JMenuItem("Case for: " + b.getValuePart() + " [" + b.getValuePart().readValue() + "]");
				item.setIcon(ImageStatic.PART_ID);
				_menu.add(item);
			}
		}
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
	{
		_menu.removeAll();
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e)
	{
		_menu.removeAll();
	}
}
