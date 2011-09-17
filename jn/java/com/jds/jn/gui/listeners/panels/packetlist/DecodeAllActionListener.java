package com.jds.jn.gui.listeners.panels.packetlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.gui.panels.viewpane.packetlist.CryptedPacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.DecPacketListPane;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.session.Session;
import com.jds.jn.util.RunnableImpl;
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

		ThreadPoolManager.getInstance().execute(new RunnableImpl()
		{

			@Override
			public void runImpl()
			{
				Session session = _pane.getViewPane().getSession();
				NetworkProfile profile = NetworkProfiles.getInstance().active();

				if (session.getProtocol() == null || profile == null)
				{
					return;
				}

				List<CryptedPacket> packetList = session.getCryptedPackets();

				final DecPacketListPane pane = _pane.getViewPane().getPacketListPane();

				_pane.getViewPane().actionEnable(false);

				MainForm.getInstance().getProgressBar().setVisible(true);
				MainForm.getInstance().getProgressBar().setValue(0);

				int i = 1;
				int size = packetList.size();

				for (CryptedPacket packet : packetList)
				{
					AddPacket:
					{
						if (!packet.isShow())
						{
							DecryptedPacket datapacket = session.decode(packet);

							if (datapacket.getName() != null && datapacket.getPacketInfo().isServerList() && session.getMethod() != null && session.getListenerType() == ListenerType.Auth_Server)
								_pane.setEnableServerListButton(true);

							if (datapacket.getPacketInfo() != null)
							{
								NetworkProfilePart part = profile.getPart(session.getListenerType());
								if (part.isFiltredOpcode(datapacket.getPacketInfo().getOpcodeStr()))
									break AddPacket;
							}

							session.receiveQuitPacket(datapacket, true, true);
						}
					}


					int p = (int) ((100D * (i + 1)) / size);

					MainForm.getInstance().getProgressBar().setValue(p);
					i++;
				}

				MainForm.getInstance().getProgressBar().setValue(0);
				MainForm.getInstance().getProgressBar().setVisible(false);

				_pane.getViewPane().actionEnable(true);

				_pane.getViewPane().updateInfo(session);

				pane.getPacketTable().updateUI();
			}

		});
	}
}
