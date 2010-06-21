package com.jds.jn.gui.panels.viewpane.packetlist;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

import com.intellij.uiDesigner.core.*;
import com.jds.jn.gui.listeners.panels.packetlist.DecodeAllActionListener;
import com.jds.jn.gui.models.DecPacketTableModel;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.renders.PacketTableRender2;
import com.jds.jn.helpers.PacketStructureParser;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.methods.proxy.Proxy;
import com.jds.jn.network.packets.*;
import com.jds.jn.network.profiles.*;
import com.jds.jn.session.Session;
import com.jds.jn.util.Util;
import com.jds.nio.buffer.NioBuffer;

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
	private JButton _decodeButton;
	private JButton _decodeAllButton;
	private JButton _sendServerListButton;
	private ViewPane _pane;

	public NotDecPacketListPane(ViewPane pane)
	{
		_pane = pane;
		$$$setupUI$$$();

		_decodeAllButton.addActionListener(new DecodeAllActionListener(this));

		_sendServerListButton.addActionListener(new ActionListener()
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

				Session session = getViewPane().getSession();

				DecPacketTableModel model = getViewPane().getPacketTableModel();

				byte[] bytes = session.getCrypt().encrypt(buf.array(), PacketType.SERVER);
				if (bytes == null)
				{
					return;
				}

				NioBuffer buff = NioBuffer.wrap(bytes);

				final DecPacketListPane pane = getViewPane().getPacketListPane();

				DecryptPacket datapacket = session.decode(new NotDecryptPacket(PacketType.SERVER, buff, System.currentTimeMillis()));
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

				getViewPane().updateInfo(session);

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


		_decodeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Session session = getViewPane().getSession();
				NetworkProfile profile = NetworkProfiles.getInstance().active();

				if (session.getProtocol() == null || profile == null)
				{
					return;
				}

				final DecPacketListPane pane = getViewPane().getPacketListPane();
				DecPacketTableModel model = getViewPane().getPacketTableModel();
				if (_packetList.getSelectedRow() == -1)
				{
					return;
				}
				NotDecryptPacket packet = getViewPane().getPacketTableModel2().getPacket(_packetList.getSelectedRow());


				if (!packet.isShow())
				{
					DecryptPacket datapacket = session.decode(packet);
					if (datapacket.getName() != null && datapacket.getName().equals("SM_SERVER_LIST"))
					{
						_sendServerListButton.setEnabled(true);
						_sendServerListButton.setVisible(true);
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


				getViewPane().updateInfo(session);

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

		_packetList = new JTable(getViewPane().getPacketTableModel2());
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
		_decodeButton = new JButton();
		this.$$$loadButtonText$$$(_decodeButton, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Decode"));
		panel2.add(_decodeButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		_decodeAllButton = new JButton();
		this.$$$loadButtonText$$$(_decodeAllButton, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("DecodeAll"));
		panel2.add(_decodeAllButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_sendServerListButton = new JButton();
		_sendServerListButton.setFocusPainted(false);
		_sendServerListButton.setIcon(new ImageIcon(getClass().getResource("/com/jds/jn/resources/images/serverlist.png")));
		_sendServerListButton.setText("");
		_sendServerListButton.setVisible(false);
		panel2.add(_sendServerListButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
				NotDecryptPacket packet = getViewPane().getPacketTableModel2().getPacket(row);
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

	@Override
	public void setEnabled(boolean b)
	{
		for(Component c : getComponents())
		{
			if(c != null)
			{
				c.setEnabled(b);
			}
		}

		super.setEnabled(b);
	}

	public void setEnableServerListButton(boolean b)
	{
		_sendServerListButton.setEnabled(b);
		_sendServerListButton.setVisible(b);
	}

	public JTable getPacketTable()
	{
		return _packetList;
	}

	public JScrollPane getScroll()
	{
		return _packetScrollPane;
	}

	public ViewPane getViewPane()
	{
		return _pane;
	}
}
