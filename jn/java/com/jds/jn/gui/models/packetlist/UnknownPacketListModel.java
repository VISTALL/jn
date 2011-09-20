package com.jds.jn.gui.models.packetlist;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import com.jds.jn.gui.panels.ViewPane;
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
public class UnknownPacketListModel extends AbstractTableModel implements PacketTableRenderer.TooltipTable
{
	private static final String[] columnNames =
	{
			"S/C",
			Bundle.getString("Time"),
			Bundle.getString("Id"),
			Bundle.getString("Length"),
			Bundle.getString("Name")
	};

	private List<Object[]> _currentTable = new ArrayList<Object[]>();

	public UnknownPacketListModel(ViewPane pane)
	{

	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public int getRowCount()
	{
		return _currentTable.size();
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		Object[] tableRow = _currentTable.get(row);
		if(tableRow != null)
		{
			return tableRow[col];
		}
		return "";
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

	public DecryptedPacket getPacket(int row)
	{
		return (DecryptedPacket) _currentTable.get(row)[7];
	}

	public void clear()
	{
		_currentTable.clear();
	}

	public void addRow(DecryptedPacket packet)
	{
		addRow(packet, -1);
	}

	public void addRow(DecryptedPacket packet, int row)
	{
		if(packet.getBuffer().array().length == 0)
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
			opcode = Util.zeropad(Integer.toHexString(packet.getBuffer().array()[0] & 0xFF), 2).toUpperCase();

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
				String.valueOf(packet.getSize()),
				packet.getName(),
				toolTip,
				false,
				packet
		};

		if(row == -1)
			_currentTable.add(temp);
		else
			_currentTable.add(row, temp);

		fireTableRowsInserted(row == -1 ? _currentTable.size() : row, row == -1 ? _currentTable.size() : row);
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