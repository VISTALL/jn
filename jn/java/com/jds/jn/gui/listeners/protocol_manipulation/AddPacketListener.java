package com.jds.jn.gui.listeners.protocol_manipulation;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jds.jn.gui.panels.viewpane.packetlist.DecPacketListPane;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  13:11:56/20.06.2010
 */
public class AddPacketListener extends JMenuItem implements ActionListener
{
	private final DecPacketListPane _pane;

	public AddPacketListener(DecPacketListPane pane)
	{
		_pane = pane;

		addActionListener(this); 	
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

	}
}
