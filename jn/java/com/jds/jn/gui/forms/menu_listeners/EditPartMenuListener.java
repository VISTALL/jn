package com.jds.jn.gui.forms.menu_listeners;

import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.gui.forms.menu_items.AddCasePartMenuItem;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.datatree.DataTreeNode;
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
 * Date: 21/11/2009
 * Time: 16:05:01
 */
public class EditPartMenuListener implements PopupMenuListener
{
	private PacketForm _form;

	private JMenu _partId;

	private JPopupMenu.Separator _popupPartSeparator;

	public EditPartMenuListener(PacketForm form)
	{
		_form = form;
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e)
	{
		Object node = _form.getPacketStructure().getModel().getValueAt(_form.getPacketStructure().getSelectedRow(), 0);
		if (node == null)
		{
			return;
		}

		if (!(node instanceof DataTreeNode))
		{
			return;
		}

		DataTreeNode datatree = (DataTreeNode) node;

		if (datatree.getModelPart().getId() != -1 && datatree instanceof ValuePart)
		{
			_partId = new JMenu("Part Id: " + datatree.getModelPart().getId());
			_partId.setIcon(ImageStatic.PART_ID);
			_popupPartSeparator = new JPopupMenu.Separator();
			_form.getMenu().add(_popupPartSeparator);
			_form.getMenu().add(_partId);
		}
		else if (datatree instanceof DataSwitchBlock)
		{
			DataSwitchBlock b = (DataSwitchBlock) datatree;
			SwitchCaseBlock block = (SwitchCaseBlock) b.getModelPart();

			final SwitchPart part = block.getContainingSwitch();

			if (block.isDefault())
			{
				_partId = new JMenu("Case Id: default");
				_partId.setIcon(ImageStatic.PART_ID);
				_popupPartSeparator = new JPopupMenu.Separator();

				JMenuItem item = new AddCasePartMenuItem(_form, part);

				_partId.add(item);

				_form.getMenu().add(_popupPartSeparator);
				_form.getMenu().add(_partId);
			}
			else
			{
				_partId = new JMenu("Case Id: " + b.getValuePart().readValue());
				_partId.setIcon(ImageStatic.PART_ID);
				_popupPartSeparator = new JPopupMenu.Separator();


				_form.getMenu().add(_popupPartSeparator);
				_form.getMenu().add(_partId);
			}
		}
		else
		{
			System.out.println(datatree.getClass().getSimpleName());
		}

		if (!(node instanceof ValuePart))
		{
			return;
		}

		ValuePart part = (ValuePart) node;

		/*if (part.getClass().getSimpleName().equals("ValuePart"))
		{
			_form.getMenu().add(_form.getBytesPopupMenu());
		}*/
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
	{

	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e)
	{
		clear();

		Object node = _form.getPacketStructure().getModel().getValueAt(_form.getPacketStructure().getSelectedRow(), 0);
		if (node == null)
		{
			return;
		}
	}


	public void clear()
	{
		if (_partId != null && _form.getMenu().getComponentIndex(_partId) != -1)
		{
			_form.getMenu().remove(_partId);
			_partId = null;
		}

		if (_popupPartSeparator != null && _form.getMenu().getComponentIndex(_popupPartSeparator) != -1)
		{
			_form.getMenu().remove(_popupPartSeparator);
			_popupPartSeparator = null;
		}
	}
}
