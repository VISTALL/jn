package com.jds.jn.gui.panels.viewpane.packetlist;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.jds.jn.gui.models.packetlist.PacketListModel;
import com.jds.jn.network.packets.IPacket;

/**
 * @author VISTALL
 * @date 20:14/26.09.2011
 */
public abstract class PacketListPanel<P extends IPacket> extends JPanel
{
	private JRadioButton _radioButton;

	public abstract PacketListModel<P> getModel();

	public JRadioButton getRadioButton()
	{
		return _radioButton;
	}

	public void setRadioButton(JRadioButton radioButton)
	{
		_radioButton = radioButton;
	}
}
