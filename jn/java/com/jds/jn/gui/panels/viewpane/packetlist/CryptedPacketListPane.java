package com.jds.jn.gui.panels.viewpane.packetlist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.gui.listeners.panels.packetlist.DecodeAllActionListener;
import com.jds.jn.gui.models.DecryptedPacketTableModel;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.renders.CryptedPacketTableRender;
import com.jds.jn.helpers.PacketStructureParser;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.methods.proxy.Proxy;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfilePart;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.session.Session;
import com.jds.jn.util.Util;
import com.jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 25.09.2009
 * Time: 17:42:01
 */
public class CryptedPacketListPane extends JPanel
{
	private JScrollPane _packetScrollPane;
	private JTable _packetList;
	private JPanel _rootPane;
	private JButton _decodeButton;
	private JButton _decodeAllButton;
	private JButton _sendServerListButton;
	private ViewPane _pane;

	public CryptedPacketListPane(ViewPane pane)
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
				if(prof == null)
				{
					return;
				}

				NetworkProfilePart p = prof.getPart(ListenerType.Auth_Server);
				if(p.getServerList() == null)
				{
					return;
				}
				PacketStructureParser parser = new PacketStructureParser(p.getServerList());
				parser.parse();

				NioBuffer buf = parser.getBuffer();

				Session session = getViewPane().getSession();

				DecryptedPacketTableModel model = getViewPane().getDecryptPacketTableModel();

				byte[] bytes = session.getCrypt().encrypt(buf.array(), PacketType.SERVER);
				if(bytes == null)
				{
					return;
				}

				NioBuffer buff = NioBuffer.wrap(bytes);

				final DecryptedPacketListPane pane = getViewPane().getPacketListPane();

				DecryptedPacket datapacket = session.decode(new CryptedPacket(PacketType.SERVER, buff, System.currentTimeMillis()));
				model.addRow(datapacket);
				session.receiveQuitPacket(datapacket, true, true);

				try
				{
					Proxy proxy = (Proxy) ListenerSystem.getInstance().getMethod(ReceiveType.PROXY, ListenerType.Auth_Server);
					proxy.getClientSession().put(buff);
				}
				catch(Exception e1)
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

				if(session.getProtocol() == null || profile == null)
				{
					return;
				}

				final DecryptedPacketListPane pane = getViewPane().getPacketListPane();
				DecryptedPacketTableModel model = getViewPane().getDecryptPacketTableModel();
				if(_packetList.getSelectedRow() == -1)
				{
					return;
				}
				CryptedPacket packet = getViewPane().getCryptPacketTableModel().getPacket(_packetList.getSelectedRow());
				if(packet.isDecrypted())
					return;

				DecryptedPacket datapacket = session.decode(packet);

				if(datapacket.getName() != null && datapacket.getPacketInfo().isServerList() && session.getMethod() != null && session.getListenerType() == ListenerType.Auth_Server)
					setEnableServerListButton(true);

				if(datapacket.getPacketInfo() != null)
				{
					NetworkProfilePart part = profile.getPart(session.getListenerType());
					if(part.isFiltredOpcode(datapacket.getPacketInfo().getOpcodeStr()))
						return;
				}

				session.receiveQuitPacket(datapacket, true, true);

				model.addRow(datapacket);

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
		_rootPane = this;
		setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		_packetList = new JTable(getViewPane().getCryptPacketTableModel());
		_packetList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_packetList.setDefaultRenderer(Object.class, new CryptedPacketTableRender());
		_packetList.getColumnModel().getColumn(0).setMaxWidth(50); //type
		_packetList.getColumnModel().getColumn(1).setMaxWidth(115); //time
		_packetList.getColumnModel().getColumn(2).setMaxWidth(300);  //
		_packetList.addMouseListener(new MouseListenerImpl());
	}

	private class MouseListenerImpl implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
			{
				int row = _packetList.getSelectedRow();
				CryptedPacket packet = getViewPane().getCryptPacketTableModel().getPacket(row);
				if(packet == null)
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
		_rootPane.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		_rootPane.setEnabled(true);
		panel1.add(_rootPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		_packetScrollPane = new JScrollPane();
		_packetScrollPane.setEnabled(true);
		_packetScrollPane.setHorizontalScrollBarPolicy(31);
		_packetScrollPane.setVerticalScrollBarPolicy(22);
		_rootPane.add(_packetScrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		_packetList.setPreferredScrollableViewportSize(new Dimension(-1, -1));
		_packetScrollPane.setViewportView(_packetList);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
		_rootPane.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_decodeButton = new JButton();
		this.$$$loadButtonText$$$(_decodeButton, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Decode"));
		panel2.add(_decodeButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		_decodeAllButton = new JButton();
		_decodeAllButton.setEnabled(true);
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
		for(int i = 0; i < text.length(); i++)
		{
			if(text.charAt(i) == '&')
			{
				i++;
				if(i == text.length())
					break;
				if(!haveMnemonic && text.charAt(i) != '&')
				{
					haveMnemonic = true;
					mnemonic = text.charAt(i);
					mnemonicIndex = result.length();
				}
			}
			result.append(text.charAt(i));
		}
		component.setText(result.toString());
		if(haveMnemonic)
		{
			component.setMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
	}
}
