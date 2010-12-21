package com.jds.jn.gui2.FindPacket.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jds.jn.gui2.FindPacket.FindPacket;

/**
 * Author: VISTALL
 * Date:  22:43/20.12.2010
 */
public class FPOpenListener implements ActionListener
{
	public static final ActionListener STATIC = new FPOpenListener();

	@Override
	public void actionPerformed(ActionEvent e)
	{
		new FindPacket();
	}
}
