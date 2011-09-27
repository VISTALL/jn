package com.jds.jn.gui.models.packetlist;

import java.lang.ref.WeakReference;

import javax.swing.JLabel;

import com.jds.jn.gui.renders.PacketTableRenderer;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.ImageStatic;
import com.jds.jn.util.Util;

/**
 * @author VISTALL
 * @date 17:15/20.09.2011
 */
public class UnknownPacketListModel extends PacketListModel<DecryptedPacket> implements PacketTableRenderer.TooltipTable
{
	private static final String[] columnNames =
	{
			"S/C",
			Bundle.getString("Time"),
			Bundle.getString("Id"),
			Bundle.getString("Length"),
			Bundle.getString("Name")
	};


	public UnknownPacketListModel()
	{
		//
	}

	public DecryptedPacket getPacket(int row)
	{
		WeakReference ref = (WeakReference)_currentTable.get(row)[7];
		return (DecryptedPacket) ref.get();
	}

	@Override
	protected String[] getColumnNames()
	{
		return columnNames;
	}

	@Override
	public void addRow(int row, DecryptedPacket packet, boolean fireInsertRow)
	{
		if(packet.getAllData().length == 0)
			return;

		JLabel icon = null;

		if(packet.getPacketType() == PacketType.CLIENT)
		{
			if(packet.hasError())
				icon = ImageStatic.ICON_FROM_CLIENT_ERROR;
			else
				icon = ImageStatic.ICON_FROM_CLIENT;

			if(packet.getPacketInfo() != null)
			{
				if(packet.getPacketInfo().isKey())
					icon = ImageStatic.ICON_KEY_PACKET;
				else if(packet.getPacketInfo().isServerList())
					icon = ImageStatic.ICON_SERVER_LIST_PACKET;
			}
		}
		else
		{
			if(packet.hasError())
				icon = ImageStatic.ICON_FROM_SERVER_ERROR;
			else
				icon = ImageStatic.ICON_FROM_SERVER;

			if(packet.getPacketInfo() != null)
			{
				if(packet.getPacketInfo().isKey())
					icon = ImageStatic.ICON_KEY_PACKET;
				else if(packet.getPacketInfo().isServerList())
					icon = ImageStatic.ICON_SERVER_LIST_PACKET;
			}
		}
		String opcode = null;
		if(packet.getPacketInfo() != null)
			opcode = packet.getPacketInfo().getId();
		else
			opcode = Util.zeropad(Integer.toHexString(packet.getAllData()[0] & 0xFF), 2).toUpperCase();

		String toolTip = null;
		if(packet.hasError())
		{
			String color = (packet.hasError() ? "red" : "gray");
			toolTip = "<br><font color=\"" + color + "\">" + packet.getErrorMessage() + "</font></html>";
		}

		Object[] temp = {
				icon,
				Util.formatPacketTime(packet.getTime()),
				opcode,
				packet.getAllData().length,
				packet.getName(),
				toolTip,
				false,
				new WeakReference<DecryptedPacket>(packet)
		};

		if(row == -1)
			_currentTable.add(temp);
		else
			_currentTable.add(row, temp);

		super.addRow(row, packet, fireInsertRow);
	}

	@Override
	public String getToolTip(int row, int col)
	{
		String toolTip = "<html>Packet: " + row;
		Object msg = _currentTable.get(row)[5];
		if(msg != null)
			toolTip += msg;
		return toolTip;
	}

	@Override
	public boolean getIsMarked(int row)
	{
		return false;
	}
}