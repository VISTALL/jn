package com.jds.jn.gui.models.packetlist;

import java.lang.ref.WeakReference;

import javax.swing.JLabel;

import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.renders.PacketTableRenderer;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.ImageStatic;
import com.jds.jn.util.Util;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 17:20:11
 */
public class DecryptedPacketListModel extends PacketListModel<DecryptedPacket> implements PacketTableRenderer.TooltipTable
{
	private static final String[] columnNames = {
			"S/C",
			Bundle.getString("Time"),
			Bundle.getString("Id"),
			Bundle.getString("Length"),
			Bundle.getString("Name")
	};


	public DecryptedPacketListModel()
	{

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

	public void setName(int row, String name)
	{
		String OPCODE = (String) _currentTable.get(row)[1];
		DecryptedPacket p = (DecryptedPacket) _currentTable.get(row)[7];

		if(p == null)
		{
			System.out.println("packet null");
			return;
		}

		if(p.getPacketInfo() == null)
		{
			int id = Integer.decode(String.valueOf(_currentTable.get(row)[1]));

			//Protocol prot = p.getProtocol();
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
			p.getPacketInfo().setName(name);
		}

		/*for (Object[] objs : _currentTable)
					{
						String st = (String) objs[1];
						DecryptedPacket packet = (DecryptedPacket) objs[7];

						if (st.equalsIgnoreCase(OPCODE) && p.getPacketType() == packet.getPacketType())
						{
							DecryptedPacket newPacket = new DecryptedPacket(packet.getCryptedData().clone(), packet.getPacketType(), packet.getProtocol());
							objs[2] = String.valueOf(newPacket.getSize());
							objs[3] = newPacket.getName();
							objs[6] = newPacket;
						}
					}  */
	}

	public void deleteFormat(int row)
	{
		String OPCODE = (String) _currentTable.get(row)[1];
		DecryptedPacket p = (DecryptedPacket) _currentTable.get(row)[7];

		if(p.getPacketInfo() == null)
		{
			return;
		}
		else
		{
			//Protocol prot = p.getProtocol();
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

		/*for (Object[] objs : _currentTable)
					{
						String st = (String) objs[1];
						DecryptedPacket packet = (DecryptedPacket) objs[7];

						if (st.equalsIgnoreCase(OPCODE) && p.getPacketType() == packet.getPacketType())
						{
							DecryptedPacket newPacket = new DecryptedPacket(packet.getCryptedData(), packet.getPacketType(), packet.getProtocol());
							objs[3] = String.valueOf(newPacket.getSize());
							objs[4] = "";
							objs[7] = newPacket;
						}
					} */
	}

	public void updatePacket(int row, DecryptedPacket packet)
	{
		Object[] objs = _currentTable.get(row);

		objs[2] = String.valueOf(packet.getAllData().length);
		objs[3] = packet.getName();
		objs[6] = packet;
	}

	public void updatePackets(DecryptedPacket packet)
	{
		/*for (Object[] obj : _currentTable)
					{
						DecryptedPacket pa = (DecryptedPacket) obj[76];
						if (pa.getPacketInfo() == null)
						{
							continue;
						}

						if ((pa.getPacketType() == packet.getPacketType()) && (packet.getPacketInfo().getOpcodeStr().equals(pa.getPacketInfo().getOpcodeStr())))
						{
							DecryptedPacket newPacket = new DecryptedPacket(pa.getCryptedData().clone(), pa.getPacketType(), pa.getProtocol());
							obj[3] = String.valueOf(newPacket.getSize());
							obj[4] = newPacket.getName();
							obj[7] = newPacket;
						}
					}  */
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

	public boolean searchPacket(ViewPane viewPane, String findPacket)
	{
		int last = viewPane.getDecryptedPacketListPane().getPacketTable().getSelectionModel().getMinSelectionIndex();

		int index = 0;

		while(viewPane.getDecryptedPacketListPane().getPacketTable().getModel().getRowCount() != index)
		{
			String packet = (String) viewPane.getDecryptedPacketListPane().getPacketTable().getModel().getValueAt(index, 4);

			if(packet == null)
			{
				index += 1;
				continue;
			}

			if(index > last)
			{
				if(packet.startsWith(findPacket))
				{
					viewPane.getDecryptedPacketListPane().getPacketTable().setAutoscrolls(true);
					viewPane.getDecryptedPacketListPane().getPacketTable().getSelectionModel().setSelectionInterval(index, index);
					viewPane.getDecryptedPacketListPane().getPacketTable().scrollRectToVisible(viewPane.getDecryptedPacketListPane().getPacketTable().getCellRect(index, 0, true));
					return true;
				}
			}

			index += 1;
		}

		return false;
	}
}
