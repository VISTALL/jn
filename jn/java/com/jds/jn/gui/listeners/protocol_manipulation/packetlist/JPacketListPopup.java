package com.jds.jn.gui.listeners.protocol_manipulation.packetlist;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.gui.panels.viewpane.packetlist.DecryptedPacketListPane;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.util.Bundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  13:34:06/20.06.2010
 */
public class JPacketListPopup extends JPopupMenu
{
	private JMenuItem _searchItem = new JMenuItem(Bundle.getString("FindNext"));
	private JMenuItem _seeItem = new JMenuItem(Bundle.getString("Show"));

	private JMenu _editMenu = new JMenu(Bundle.getString("EditMenu"));


	private JMenuItem _addSubEditMenu = new JMenuItem(Bundle.getString("AddPart"));
	private JMenuItem _renameSubEditMenu = new JMenuItem(Bundle.getString("Rename"));

	private final DecryptedPacketListPane _pane;

	public JPacketListPopup(DecryptedPacketListPane pane)
	{
		super();
		_pane = pane;

		_seeItem.addActionListener(new ShowActionListener());
		_editMenu.addMenuListener(new MenuListener1());

		add(_seeItem);
		add(_editMenu);
	}

	private class ShowActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DecryptedPacket packet = _pane.getSelectedRow();

			if (packet == null)
			{
				return;
			}
			
			new PacketForm(_pane.getPane(), _pane.getTransientValue(), packet, _pane.getSelectedRowNumber());
		}
	}

	private class MenuListener1 implements MenuListener
	{

		@Override
		public void menuSelected(MenuEvent e)
		{
			_editMenu.removeAll();

			DecryptedPacket packet = _pane.getSelectedRow();
			if(packet == null)  //wtf
			{
				return;
			}

			if(packet.getPacketInfo() == null)
			{
				_editMenu.add(_addSubEditMenu);
			}
			else
			{
				_editMenu.add(_renameSubEditMenu);
			}
		}

		@Override
		public void menuDeselected(MenuEvent e)
		{

		}

		@Override
		public void menuCanceled(MenuEvent e)
		{

		}
	}

}
