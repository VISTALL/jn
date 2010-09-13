package com.jds.jn.gui.listeners.panels.packetlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.jds.jn.Jn;
import com.jds.jn.gui.models.DecPacketTableModel;
import com.jds.jn.gui.panels.viewpane.packetlist.CryptedPacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.DecPacketListPane;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.profiles.*;
import com.jds.jn.session.Session;
import com.jds.jn.util.ThreadPoolManager;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  12:14:48/20.06.2010
 */
public class DecodeAllActionListener implements ActionListener
{
	private final CryptedPacketListPane _pane;

	public DecodeAllActionListener(CryptedPacketListPane pane)
	{
		_pane = pane;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		ThreadPoolManager.getInstance().execute(new Runnable()
		{

			@Override
			public void run()
			{
				Session session = _pane.getViewPane().getSession();
				NetworkProfile profile = NetworkProfiles.getInstance().active();

				if (session.getProtocol() == null || profile == null)
				{
					return;
				}

				List<CryptedPacket> packetList = session.getCryptedPackets();

				final DecPacketListPane pane = _pane.getViewPane().getPacketListPane();
				DecPacketTableModel model = _pane.getViewPane().getDecryptPacketTableModel();

				_pane.getViewPane().actionEnable(false);

				Jn.getForm().getProgressBar().setVisible(true);
				Jn.getForm().getProgressBar().setValue(0);

				int i = 1;
				int size = packetList.size();

				for (CryptedPacket packet : packetList)
				{
					AddPacket:
					{
						if (!packet.isShow())
						{
							DecryptedPacket datapacket = session.decode(packet);

							if (datapacket.getName() != null && datapacket.getPacketFormat().isServerList() && session.getMethod() != null && session.getListenerType() == ListenerType.Auth_Server)
							{
								_pane.setEnableServerListButton(true);
							}

							if (datapacket.getPacketFormat() != null)
							{
								NetworkProfilePart part = profile.getPart(session.getListenerType());
								if (part.isFiltredOpcode(datapacket.getPacketFormat().getOpcodeStr()))
								{
									break AddPacket;
								}
							}

							session.addDecryptPacket(datapacket);
							model.addRow(datapacket);
						}
					}


					int p = (int) ((100D * (i + 1)) / size);

					Jn.getForm().getProgressBar().setValue(p);
					i++;
				}

				Jn.getForm().getProgressBar().setVisible(false);
				Jn.getForm().getProgressBar().setValue(0);

				_pane.getViewPane().actionEnable(true);

				_pane.getViewPane().updateInfo(session);

				pane.getPacketTable().updateUI();
			}

		});
	}
}
