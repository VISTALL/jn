package com.jds.jn.gui.models.packetlist;

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
public class CryptedPacketListModel extends PacketListModel<CryptedPacket>
{
	private static final String[] columnNames =
	{
			"S/C",
			Bundle.getString("Time"),
			Bundle.getString("Length")
	};


	public CryptedPacketListModel()
	{

	}

	@Override
	protected String[] getColumnNames()
	{
		return columnNames;
	}

	@Override
	public void addRow(int index, final CryptedPacket packet, boolean fireInsertRow)
	{
		Object[] temp =
		{
			(packet.getPacketType() == PacketType.SERVER ? ImageStatic.ICON_FROM_SERVER : ImageStatic.ICON_FROM_CLIENT),
			Util.formatPacketTime(packet.getTime()),
			packet.length(),
			packet
		};

		_currentTable.add(temp);

		super.addRow(index, packet, fireInsertRow);
	}

	public CryptedPacket getPacket(int index)
	{
		return (CryptedPacket) _currentTable.get(index)[3];
	}
}

