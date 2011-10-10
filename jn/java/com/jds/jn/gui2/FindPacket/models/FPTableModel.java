package com.jds.jn.gui2.FindPacket.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.ImageStatic;
import com.jds.jn.util.Util;

/**
 * Author: VISTALL
 * Date:  23:49/20.12.2010
 */
public class FPTableModel  extends AbstractTableModel
{
 	private static final String[] columnNames =
	{
			"S/C",
			Bundle.getString("Time"),
			Bundle.getString("Length"),
			Bundle.getString("File")
	};

	private List<Object[]> _currentTable = new ArrayList<Object[]>();

	@Override
	public int getRowCount()
	{
		return _currentTable.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
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
		if (tableRow != null)
			return tableRow[col];
		return "";
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

	public void addRow(final String fileName, final DecryptedPacket packet)
	{
		JLabel icon = null;

		if (packet.getPacketType() == PacketType.CLIENT)
			icon = ImageStatic.ICON_FROM_CLIENT;
		else
			icon = ImageStatic.ICON_FROM_SERVER;

		Object[] temp =
		{
				icon,
				Util.formatPacketTime(packet.getTime()),
				String.valueOf(packet.getAllData().length),
				fileName,
				packet
		};

		_currentTable.add(temp);

		fireTableRowsInserted(_currentTable.size(), _currentTable.size());
	}

	public DecryptedPacket getPacket(int index)
	{
		return (DecryptedPacket) _currentTable.get(index)[4];
	}
}
