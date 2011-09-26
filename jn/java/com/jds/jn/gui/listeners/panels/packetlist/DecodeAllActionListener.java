package com.jds.jn.gui.listeners.panels.packetlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.gui.panels.viewpane.packetlist.CryptedPacketListPane;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.parser.packetfactory.tasks.InvokeTask;
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
	private class DecryptTask extends RunnableImpl
	{
		@Override
		protected void runImpl() throws Throwable
		{
			Session session = _pane.getViewPane().getSession();
			NetworkProfile profile = NetworkProfiles.getInstance().active();

			if(session.getProtocol() == null || profile == null)
				return;

			List<CryptedPacket> packetList = session.getCryptedPackets();

			_pane.getViewPane().actionEnable(false);

			// ищем пакеты для обработки
			List<CryptedPacket> workPackets = new ArrayList<CryptedPacket>();
			for(CryptedPacket packet : packetList)
			{
				if(packet.isDecrypted())
					continue;

				workPackets.add(packet);
			}

			MainForm.getInstance().getProgressBar().setVisible(true);
			MainForm.getInstance().getProgressBar().setMaximum(workPackets.size());

			List<DecryptedPacket> packets = new ArrayList<DecryptedPacket>(workPackets.size());
			for(int i = 0; i < workPackets.size(); i++)
			{
				CryptedPacket packet = workPackets.get(i);

				DecryptedPacket datapacket = session.decode(packet);

				if(datapacket.getName() != null && datapacket.getPacketInfo().isServerList() && session.getMethod() != null && session.getListenerType() == ListenerType.Auth_Server)
					_pane.setEnableServerListButton(true);

				if(datapacket.getPacketInfo() != null)
				{
					NetworkProfilePart part = profile.getPart(session.getListenerType());
					if(part.isFiltredOpcode(datapacket.getPacketInfo().getOpcodeStr()))
						continue;
				}

				packets.add(datapacket);

				MainForm.getInstance().getProgressBar().setValue(i);
			}

			MainForm.getInstance().getProgressBar().setVisible(false);

			_pane.getViewPane().actionEnable(true);

			process(packets);
		}
	}

	private final CryptedPacketListPane _pane;
	private final DecryptTask _decryptTask = new DecryptTask();

	public DecodeAllActionListener(CryptedPacketListPane pane)
	{
		_pane = pane;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		ThreadPoolManager.getInstance().execute(_decryptTask);
	}

	private void process(List<DecryptedPacket> packets)
	{
		Session session = _pane.getViewPane().getSession();

		session.receiveQuitPackets(packets, true, false);

		_pane.getViewPane().updateInfo(session);

		ThreadPoolManager.getInstance().execute(new InvokeTask(session, packets));
	}
}
