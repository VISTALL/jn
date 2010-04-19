package com.jds.jn.gui.panels.viewpane.packetlist;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.Jn;
import javolution.util.FastList;
import com.jds.jn.gui.models.DecPacketTableModel;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.renders.PacketTableRender2;
import com.jds.jn.helpers.PacketStructureParser;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.methods.proxy.Proxy;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.network.packets.JPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.session.Session;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.util.Util;
import com.jds.nio.buffer.NioBuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 25.09.2009
 * Time: 17:42:01
 */
public class NotDecPacketListPane extends JPanel
{
	private JScrollPane _packetScrollPane;
	private JTable _packetList;
	private JPanel main;
	private JButton decodeButton;
	private JButton decodeAllButton;
	private JButton sendServerListButton;
	protected ViewPane _pane;

	public NotDecPacketListPane(ViewPane pane)
	{
		_pane = pane;
		$$$setupUI$$$();

		decodeAllButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Session session = _pane.getSession();
				NetworkProfile profile = NetworkProfiles.getInstance().active();

				if (session.getProtocol() == null || profile == null)
				{
					return;
				}

				FastList<JPacket> packetList = session.getNotDecryptPackets();

				final DecPacketListPane pane = _pane.get_packetListPane();
				DecPacketTableModel model = _pane.getPacketTableModel();

				Jn.getInstance().getProgressBar().setVisible(true);
				Jn.getInstance().getProgressBar().setValue(0);

				int i = 1;
				int size = packetList.size();

				for (JPacket packet : packetList)
				{
					if (!packet.isShow())
					{
						DataPacket datapacket = session.decode(packet);

						if (datapacket.getName() != null && datapacket.getPacketFormat().isServerList() && session.getMethod() != null && session.getListenerType() == ListenerType.Auth_Server)
						{
							sendServerListButton.setEnabled(true);
							sendServerListButton.setVisible(true);
						}

						if (datapacket.getPacketFormat() != null)
						{
							NetworkProfilePart part = profile.getPart(session.getListenerType());
							if (part.isFiltredOpcode(datapacket.getPacketFormat().getOpcodeStr()))
							{
								continue;
							}
						}

						session.addDecryptPacket(datapacket);
						model.addRow(datapacket);
					}

					Jn.getInstance().getProgressBar().setValue((int) ((i * 100D) / size));
					i++;
				}

				Jn.getInstance().getProgressBar().setVisible(false);
				Jn.getInstance().getProgressBar().setValue(0);

				_pane.updateInfo(session);

				ThreadPoolManager.getInstance().execute(new Runnable()
				{

					@Override
					public void run()
					{
						pane.getPacketTable().updateUI();
					}
				});
			}
		});

		sendServerListButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				NetworkProfile prof = NetworkProfiles.getInstance().active();
				if (prof == null)
				{
					return;
				}

				NetworkProfilePart p = prof.getPart(ListenerType.Auth_Server);
				if (p.getServerList() == null)
				{
					return;
				}
				PacketStructureParser parser = new PacketStructureParser(p.getServerList());
				parser.parse();

				NioBuffer buf = parser.getBuffer();

				Session session = _pane.getSession();

				DecPacketTableModel model = _pane.getPacketTableModel();

				byte[] bytes = session.getCrypt().encrypt(buf.array(), PacketType.SERVER);
				if (bytes == null)
				{
					return;
				}

				NioBuffer buff = NioBuffer.wrap(bytes);

				final DecPacketListPane pane = _pane.get_packetListPane();

				DataPacket datapacket = session.decode(new JPacket(PacketType.SERVER, buff));
				model.addRow(datapacket);
				session.addDecryptPacket(datapacket);

				try
				{
					Proxy proxy = (Proxy) ListenerSystem.getInstance().getMethod(ReceiveType.PROXY, ListenerType.Auth_Server);
					proxy.getClientSession().put(buff);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}

				_pane.updateInfo(session);

				SwingUtilities.invokeLater(new Runnable()
				{

					@Override
					public void run()
					{
						pane.getPacketTable().updateUI();
					}
				});
			}
		});


		decodeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Session session = _pane.getSession();
				NetworkProfile profile = NetworkProfiles.getInstance().active();

				if (session.getProtocol() == null || profile == null)
				{
					return;
				}

				final DecPacketListPane pane = _pane.get_packetListPane();
				DecPacketTableModel model = _pane.getPacketTableModel();
				if (_packetList.getSelectedRow() == -1)
				{
					return;
				}
				JPacket packet = _pane.getPacketTableModel2().getPacket(_packetList.getSelectedRow());


				if (!packet.isShow())
				{
					DataPacket datapacket = session.decode(packet);
					if (datapacket.getName() != null && datapacket.getName().equals("SM_SERVER_LIST"))
					{
						sendServerListButton.setEnabled(true);
						sendServerListButton.setVisible(true);
					}
					if (datapacket.getPacketFormat() != null)
					{
						NetworkProfilePart part = profile.getPart(session.getListenerType());
						if (part.isFiltredOpcode(datapacket.getPacketFormat().getOpcodeStr()))
						{
							return;
						}
					}

					session.addDecryptPacket(datapacket);
					model.addRow(datapacket);
				}


				_pane.updateInfo(session);

				SwingUtilities.invokeLater(new Runnable()
				{

					@Override
					public void run()
					{
						pane.getPacketTable().updateUI();
					}
				});
			}
		});
	}


	private void createUIComponents()
	{
		main = this;

		setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

		_packetList = new JTable(_pane.getPacketTableModel2());
		_packetList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_packetList.setDefaultRenderer(Object.class, new PacketTableRender2());
		_packetList.getColumnModel().getColumn(0).setMaxWidth(50); //type
		_packetList.getColumnModel().getColumn(1).setMaxWidth(115); //time
		_packetList.getColumnModel().getColumn(2).setMaxWidth(300);  //
		_packetList.addMouseListener(new MouseL());
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$()
	{
		createUIComponents();
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		main.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(main, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		_packetScrollPane = new JScrollPane();
		_packetScrollPane.setHorizontalScrollBarPolicy(31);
		_packetScrollPane.setVerticalScrollBarPolicy(22);
		main.add(_packetScrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		_packetList.setPreferredScrollableViewportSize(new Dimension(-1, -1));
		_packetScrollPane.setViewportView(_packetList);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
		main.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		decodeButton = new JButton();
		this.$$$loadButtonText$$$(decodeButton, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Decode"));
		panel2.add(decodeButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		decodeAllButton = new JButton();
		this.$$$loadButtonText$$$(decodeAllButton, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("DecodeAll"));
		panel2.add(decodeAllButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		sendServerListButton = new JButton();
		sendServerListButton.setFocusPainted(false);
		sendServerListButton.setIcon(new ImageIcon(getClass().getResource("/com/jds/jn/resources/images/serverlist.png")));
		sendServerListButton.setText("");
		sendServerListButton.setVisible(false);
		panel2.add(sendServerListButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	private void $$$loadButtonText$$$(AbstractButton component, String text)
	{
		StringBuffer result = new StringBuffer();
		boolean haveMnemonic = false;
		char mnemonic = '\0';
		int mnemonicIndex = -1;
		for (int i = 0; i < text.length(); i++)
		{
			if (text.charAt(i) == '&')
			{
				i++;
				if (i == text.length())
				{
					break;
				}
				if (!haveMnemonic && text.charAt(i) != '&')
				{
					haveMnemonic = true;
					mnemonic = text.charAt(i);
					mnemonicIndex = result.length();
				}
			}
			result.append(text.charAt(i));
		}
		component.setText(result.toString());
		if (haveMnemonic)
		{
			component.setMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
	}

	public class MouseL implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
			{
				int row = _packetList.getSelectedRow();
				JPacket packet = _pane.getPacketTableModel2().getPacket(row);
				if (packet == null)
				{
					return;
				}

				JTextPane pane = new JTextPane();
				pane.setText(Util.printData(packet.getBuffer().array()));

				JPopupMenu m = new JPopupMenu();

				m.add(pane);

				m.show(_packetList, e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e)
		{

		}

		@Override
		public void mouseReleased(MouseEvent e)
		{

		}

		@Override
		public void mouseEntered(MouseEvent e)
		{

		}

		@Override
		public void mouseExited(MouseEvent e)
		{

		}
	}

	public JTable getPacketTable()
	{
		return _packetList;
	}

	public JScrollPane getScroll()
	{
		return _packetScrollPane;
	}
}
