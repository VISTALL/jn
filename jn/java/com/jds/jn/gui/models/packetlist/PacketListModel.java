package com.jds.jn.gui.models.packetlist;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.jds.jn.network.packets.IPacket;

/**
 * @author VISTALL
 * @date 20:11/26.09.2011
 */
public abstract class PacketListModel<P extends IPacket> extends AbstractTableModel
{
	protected List<Object[]> _currentTable = new ArrayList<Object[]>();

	protected int _needUpdateIndex, _lastUpdateIndex;

	protected abstract String[] getColumnNames();

	@Override
	public int getColumnCount()
	{
		return getColumnNames().length;
	}

	@Override
	public String getColumnName(int col)
	{
		return getColumnNames()[col];
	}

	public void addRow(int row, P packet, boolean fireInsertRow)
	{
		if(fireInsertRow)
			fireTableRowsInserted(_currentTable.size(), _currentTable.size());
		else
			_needUpdateIndex = _currentTable.size();
	}

	public void refresh()
	{
		int oldValue1 = _lastUpdateIndex, oldValue2 = _needUpdateIndex;
		_lastUpdateIndex = _needUpdateIndex = _currentTable.size();

		fireTableRowsInserted(oldValue1, oldValue2);
	}

	public int size()
	{
		return _currentTable.size();
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		Object[] tableRow = _currentTable.get(row);
		if(tableRow != null)
			return tableRow[col];

		return "";
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

	@Override
	public int getRowCount()
	{
		return _currentTable.size();
	}

	public void clear()
	{
		_currentTable.clear();
	}
}
