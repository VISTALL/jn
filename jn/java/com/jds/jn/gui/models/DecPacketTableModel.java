package com.jds.jn.gui.models;

import javolution.util.FastTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.renders.PacketTableRenderer;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.Util;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 17:20:11
 */
@SuppressWarnings("serial")
public class DecPacketTableModel extends AbstractTableModel implements PacketTableRenderer.TooltipTable
{
	private static final String[] columnNames =
	{
			"S/C",
			Bundle.getString("Time"),
			Bundle.getString("Id"),
			Bundle.getString("Length"),
			Bundle.getString("Name")
	};

	private FastTable<Object[]> _currentTable = new FastTable<Object[]>();
	private final ViewPane _pane;

	public DecPacketTableModel(ViewPane pane)
	{
		_pane = pane;
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

	public DecryptPacket getPacket(int row)
	{
		return (DecryptPacket) _currentTable.get(row)[7];
	}

	public void clear()
	{
		_currentTable.clear();
	}

	public void addRow(DecryptPacket packet)
	{
		addRow(packet, -1);
	}

	public void addRow(DecryptPacket packet, int row)
	{
		if (packet.getBuffer().array().length == 0)
		{
			return;
		}

		ImageIcon icon = null;

		if (packet.getPacketType() == PacketType.CLIENT)
		{
			if (packet.hasError())
			{
				icon = ImageStatic.ICON_FROM_CLIENT_ERROR;
			}
			else
			{
				icon = ImageStatic.ICON_FROM_CLIENT;
			}

			if (packet.getPacketFormat() != null)
			{
				if (packet.getPacketFormat().isKey())
				{
					icon = ImageStatic.ICON_KEY_PACKET;
				}
				else if (packet.getPacketFormat().isServerList())
				{
					icon = ImageStatic.ICON_SERVER_LIST_PACKET;
				}
			}
		}
		else
		{
			if (packet.hasError())
			{
				icon = ImageStatic.ICON_FROM_SERVER_ERROR;
			}
			else
			{
				icon = ImageStatic.ICON_FROM_SERVER;
			}

			if (packet.getPacketFormat() != null)
			{
				if (packet.getPacketFormat().isKey())
				{
					icon = ImageStatic.ICON_KEY_PACKET;
				}
				else if (packet.getPacketFormat().isServerList())
				{
					icon = ImageStatic.ICON_SERVER_LIST_PACKET;
				}
			}
		}
		String opcode = null;
		if (packet.getPacketFormat() != null)
		{
			opcode = packet.getPacketFormat().getId().toUpperCase();
		}
		else
		{
			opcode = Util.zeropad(Integer.toHexString(packet.getBuffer().array()[0] & 0xFF), 2).toUpperCase();
		}

		String toolTip = null;
		if (packet.hasError())
		{
			String color = (packet.hasError() ? "red" : "gray");
			toolTip = "<br><font color=\"" + color + "\">" + packet.getErrorMessage() + "</font></html>";
		}

		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss SSSS");

		Object[] temp =
		{
				new JLabel(icon),
				time.format(new Date()),
				opcode,
				String.valueOf(packet.getSize()),
				packet.getName(),
				toolTip,
				false,
				packet
		};

		if (row == -1)
		{
			_currentTable.add(temp);
		}
		else
		{
			_currentTable.add(row, temp);
		}
	}

	public void setName(int row, String name)
	{
		String OPCODE = (String) _currentTable.get(row)[1];
		DecryptPacket p = (DecryptPacket) _currentTable.get(row)[7];

		if (p == null)
		{
			System.out.println("packet null");
			return;
		}

		if (p.getPacketFormat() == null)
		{
			int id = Integer.decode(String.valueOf(_currentTable.get(row)[1]));

			Protocol prot = p.getProtocol();
			PacketFamilly fam;
			/*
			if (p.getPacketType() == PacketType.CLIENT)
			{
				fam = prot.getClientPacketsFamilly();
			}
			else
			{
				fam = prot.getServerPacketsFamilly();
			} */

			//PacketFormat forma = new PacketFormat(id, name);
			//fam.addNode(forma);
		}
		else
		{
			p.getPacketFormat().setName(name);
		}

		for (Object[] objs : _currentTable)
		{
			String st = (String) objs[1];
			DecryptPacket packet = (DecryptPacket) objs[7];

			if (st.equalsIgnoreCase(OPCODE) && p.getPacketType() == packet.getPacketType())
			{
				DecryptPacket newPacket = new DecryptPacket(packet.getNotDecryptData().clone(), packet.getPacketType(), packet.getProtocol());
				objs[2] = String.valueOf(newPacket.getSize());
				objs[3] = newPacket.getName();
				objs[6] = newPacket;
			}
		}
	}

	public void deleteFormat(int row)
	{
		String OPCODE = (String) _currentTable.get(row)[1];
		DecryptPacket p = (DecryptPacket) _currentTable.get(row)[7];

		if (p.getDataFormat() == null)
		{
			return;
		}
		else
		{
			Protocol prot = p.getProtocol();
			PacketFamilly fam;

			/*if (p.getPacketType() == PacketType.CLIENT)
			{
				fam = prot.getClientPacketsFamilly();
			}
			else
			{
				fam = prot.getServerPacketsFamilly();
			}

			fam.remove(p.getPacketFormat()); */
		}

		for (Object[] objs : _currentTable)
		{
			String st = (String) objs[1];
			DecryptPacket packet = (DecryptPacket) objs[7];

			if (st.equalsIgnoreCase(OPCODE) && p.getPacketType() == packet.getPacketType())
			{
				DecryptPacket newPacket = new DecryptPacket(packet.getNotDecryptData().clone(), packet.getPacketType(), packet.getProtocol());
				objs[3] = String.valueOf(newPacket.getSize());
				objs[4] = "";
				objs[7] = newPacket;
			}
		}
	}

	public void updatePacket(int row, DecryptPacket packet)
	{
		Object[] objs = _currentTable.get(row);

		objs[2] = String.valueOf(packet.getSize());
		objs[3] = packet.getName();
		objs[6] = packet;
	}

	public void updatePackets(DecryptPacket packet)
	{
		for (Object[] obj : _currentTable)
		{
			DecryptPacket pa = (DecryptPacket) obj[76];
			if (pa.getPacketFormat() == null)
			{
				continue;
			}

			if ((pa.getPacketType() == packet.getPacketType()) && (packet.getPacketFormat().getOpcodeStr().equals(pa.getPacketFormat().getOpcodeStr())))
			{
				DecryptPacket newPacket = new DecryptPacket(pa.getNotDecryptData().clone(), pa.getPacketType(), pa.getProtocol());
				obj[3] = String.valueOf(newPacket.getSize());
				obj[4] = newPacket.getName();
				obj[7] = newPacket;
			}
		}
	}

	@Override
	public String getToolTip(int row, int col)
	{
		String toolTip = "<html>JPacket: " + row;
		Object msg = _currentTable.get(row)[5];
		if (msg != null)
		{
			toolTip += msg;
		}
		return toolTip;
	}

	@Override
	public boolean getIsMarked(int row)
	{
		return false;
	}

	public boolean searchPacket(String findPacket)
	{
		int last = _pane.getPacketListPane().getPacketTable().getSelectionModel().getMinSelectionIndex();

		int index = 0;

		while (_pane.getPacketListPane().getPacketTable().getModel().getRowCount() != index)
		{
			String packet = (String) _pane.getPacketListPane().getPacketTable().getModel().getValueAt(index, 4);

			if (packet == null)
			{
				index += 1;
				continue;
			}

			if (index > last)
			{
				if (packet.startsWith(findPacket))
				{
					_pane.getPacketListPane().getPacketTable().setAutoscrolls(true);
					_pane.getPacketListPane().getPacketTable().getSelectionModel().setSelectionInterval(index, index);
					_pane.getPacketListPane().getPacketTable().scrollRectToVisible(_pane.getPacketListPane().getPacketTable().getCellRect(index, 0, true));
					return true;
				}
			}

			index += 1;
		}

		return false;
	}
}

