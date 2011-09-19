package com.jds.jn.gui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.ImageStatic;
import com.jds.jn.util.Util;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 25.09.2009
 * Time: 17:55:13
 */
public class CryptedPacketTableModel extends AbstractTableModel
{
	private static final String[] columnNames =
	{
			"S/C",
			Bundle.getString("Time"),
			Bundle.getString("Length")
	};

	private List<Object[]> _currentTable = new ArrayList<Object[]>();

	public CryptedPacketTableModel(ViewPane pane)
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
		if (tableRow != null)
			return tableRow[col];

		return "";
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

	public void addRow(final CryptedPacket packet)
	{
		Object[] temp =
		{
				(packet.getPacketType() == PacketType.SERVER ? ImageStatic.ICON_FROM_SERVER : ImageStatic.ICON_FROM_CLIENT),
				Util.formatPacketTime(packet.getTime()),
				String.valueOf(packet.length()),
				packet
		};

		_currentTable.add(temp);
	}

	public CryptedPacket getPacket(int index)
	{
		return (CryptedPacket) _currentTable.get(index)[3];
	}
}

