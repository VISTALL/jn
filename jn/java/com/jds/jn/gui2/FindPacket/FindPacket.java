package com.jds.jn.gui2.FindPacket;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.gui2.FindPacket.models.FPTableModel;
import com.jds.jn.gui2.FindPacket.renderers.FPRenderer;
import com.jds.jn.logs.Reader;
import com.jds.jn.logs.listeners.ReaderListener;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.ProtocolManager;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.RunnableImpl;
import com.jds.jn.util.ThreadPoolManager;

public class FindPacket extends JDialog
{
	private static final Logger _log = Logger.getLogger(FindPacket.class);

	private final ReaderListener LISTENER = new ReaderListener()
	{
		@Override
		public DecryptedPacket newPacket(Session session, CryptedPacket p)
		{
			return new DecryptedPacket(session, p.getPacketType(), p.getAllData(), p.getTime(), session.getProtocol(), false);
		}

		@Override
		public void readPacket(Session session, DecryptedPacket p)
		{
			session.receiveQuitPacket(p, false, false);
		}

		@Override
		public void readPacket(Session session, CryptedPacket p)
		{
			session.receiveQuitPacket(p);
		}

		@Override
		public void onFinish(Session session, File file)
		{
			if(session == null)
				return;

			PacketInfo packetInfo = (PacketInfo) _packetList.getSelectedItem();
			List<DecryptedPacket> packets = session.getDecryptPackets();
			for(DecryptedPacket packet : packets)
			{
				if(packet.getPacketInfo() == packetInfo)
					((FPTableModel) _packetTable.getModel()).addRow(file.getName(), packet);
			}
		}
	};

	private JPanel contentPane;
	private JComboBox _packetList;
	private JButton _chooseFiles;
	private JComboBox _listeners;
	private JTable _packetTable;

	public FindPacket()
	{
		super(MainForm.getInstance());
		$$$setupUI$$$();
		setTitle(Bundle.getString("SearchPacket"));
		setContentPane(contentPane);

		_chooseFiles.setText(Bundle.getString("SelectFiles"));
		for(ListenerType type : ListenerType.VALUES)
		{
			_listeners.addItem(type);
		}
		_listeners.setSelectedItem(null);
		_listeners.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				_packetList.removeAllItems();
				ListenerType type = (ListenerType) _listeners.getSelectedItem();
				Protocol protocol = ProtocolManager.getInstance().getProtocol(type);
				if(protocol == null)
				{
					return;
				}
				for(PacketFamilly familly : protocol.getFamilies())
				{
					for(PacketInfo info : familly.getFormats().values())
					{
						_packetList.addItem(info);
					}
				}
			}
		});

		_chooseFiles.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ThreadPoolManager.getInstance().execute(new RunnableImpl()
				{
					@Override
					protected void runImpl() throws Exception
					{
						JFileChooser chooser = Reader.getInstance().getFileChooser();
						chooser.setMultiSelectionEnabled(true);

						int returnVal = chooser.showOpenDialog(FindPacket.this);
						if(returnVal == JFileChooser.APPROVE_OPTION)
						{
							File[] files = chooser.getSelectedFiles();
							for(File f : files)
							{
								try
								{
									Reader.getInstance().read(f, LISTENER);
								}
								catch(Exception e1)
								{
									_log.info("Exception: " + e1, e1);
								}
							}

							ThreadPoolManager.getInstance().execute(new RunnableImpl()
							{
								@Override
								protected void runImpl() throws Throwable
								{
									_packetTable.invalidate();
								}
							});
						}
					}
				});
			}
		});

		_packetTable.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int row = _packetTable.rowAtPoint(e.getPoint());
				/*if (row != -1)
				{
					_see.setEnabled(true);
				}
					*/
				if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
				{
					if(row == -1)
					{
						return;
					}

					DecryptedPacket packet = ((FPTableModel) _packetTable.getModel()).getPacket(row);

					if(packet == null)
					{
						return;
					}
					new PacketForm(null, 1F, packet, row);
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
		});

		setResizable(false);
		setSize(450, 600);
		setVisible(true);
	}

	private void createUIComponents()
	{
		_packetTable = new JXTable(new FPTableModel());
		_packetTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_packetTable.setDefaultRenderer(Object.class, new FPRenderer());
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
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
		_packetList = new JComboBox();
		panel1.add(_packetList, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_chooseFiles = new JButton();
		_chooseFiles.setText("Button");
		panel2.add(_chooseFiles, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_listeners = new JComboBox();
		panel2.add(_listeners, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(279, 24), null, 0, false));
		final JScrollPane scrollPane1 = new JScrollPane();
		panel1.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane1.setViewportView(_packetTable);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return contentPane;
	}
}
