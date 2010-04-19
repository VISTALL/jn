package com.jds.jn.gui.models;

import javolution.util.FastTable;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.network.packets.JPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.statics.ImageStatic;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 25.09.2009
 * Time: 17:55:13
 */
@SuppressWarnings("serial")
public class NotDecPacketTableModel extends AbstractTableModel
{
	private static final String[] columnNames = {
			"S/C",
			"Time",
			ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Length")
	};

	private FastTable<Object[]> _currentTable = new FastTable<Object[]>();

	private final ViewPane _pane;

	public NotDecPacketTableModel(ViewPane pane)
	{
		_pane = pane;
	}

	@Deprecated
	public Object[] getRow(int id)
	{
		return _currentTable.get(id);
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

	public void addRow(final JPacket packet)
	{
		ImageIcon icon = null;

		if (packet.getType() == PacketType.CLIENT)
		{
			icon = ImageStatic.ICON_FROM_CLIENT;
		}
		else
		{
			icon = ImageStatic.ICON_FROM_SERVER;
		}

		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss SSSS");

		Object[] temp = {
				new JLabel(icon),
				time.format(new Date()),
				String.valueOf(packet.getBuffer().limit()),
				packet
		};

		_currentTable.add(temp);

		_pane.getNotDecPacketListPane().getPacketTable().updateUI();
	}

	public JPacket getPacket(int index)
	{
		return (JPacket) _currentTable.get(index)[3];
	}
}

