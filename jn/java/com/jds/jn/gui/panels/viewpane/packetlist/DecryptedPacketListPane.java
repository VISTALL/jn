package com.jds.jn.gui.panels.viewpane.packetlist;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jds.jn.gui.forms.PacketForm;
import com.jds.jn.gui.listeners.protocol_manipulation.packetlist.JPacketListPopup;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.renders.PacketTableRenderer;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetreader.PacketReader;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21.09.2009
 * Time: 13:54:45
 */
public class DecryptedPacketListPane extends JPanel
{
	private class MouseListenerImpl extends MouseAdapter
	{
       	@Override
		public void mouseClicked(MouseEvent e)
		{
			int row = _packetList.rowAtPoint(e.getPoint());
			/*if (row != -1)
			{
				_see.setEnabled(true);
			}
                */
			if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
			{
				if(row == -1)
					return;

				DecryptedPacket packet = getPane().getDecryptPacketListModel().getPacket(row);

				if(packet == null)
					return;
				float f = _transperyPacket.getValue() / 100F;
				new PacketForm(getPane(), f, packet, row);
			}

			else if(e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)
			{
				JTable table = (JTable) e.getSource();
				_menu.show(table, e.getX(), e.getY());
			}
		}
	}

	private JScrollPane _packetScrollPane;
	private JTable _packetList;
	private JSlider _transperyPacket;
	private JPanel main;
	private ViewPane _pane;
	private JPopupMenu _menu;
	private JMenuItem _searchItem;
	private JMenuItem _readItem;
	private JMenuItem _see;

	public DecryptedPacketListPane(ViewPane pane)
	{
		_pane = pane;
		_menu = new JPacketListPopup(this);
		$$$setupUI$$$();

		/*	_searchItem = new JMenuItem(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("FindNext"));
				_searchItem.setEnabled(false);
				_searchItem.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						String findPacket = _pane.getSearchPane().getFindText();
						_pane.getPacketTableModel().searchPacket(findPacket);
					}
				});

				_menu.add(_searchItem);

				_see = new JMenuItem(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Show"));
				_see.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						int row = _packetList.getSelectedRow();
						if (row == -1)
						{
							return;
						}
						DecryptPacket packet = _pane.getPacketTableModel().getPacket(row);
						if (packet == null)
						{
							return;
						}
						float f = _transperyPacket.getValue() / 100F;
						new PacketForm(_pane, f, packet, row);
					}
				});
				_see.setEnabled(false);
				_menu.add(_see);

				_readItem = new JMenuItem(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Reader"));
				_readItem.setEnabled(true);
				_readItem.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						int row = _packetList.getSelectedRow();
						DecryptPacket packet = _pane.getPacketTableModel().getPacket(row);
						if (packet == null || packet.getPacketFormat() == null)
						{
							return;
						}

						PacketReader read = packet.getPacketFormat().getPacketReader();

						if (read == null || !read.read(packet))
						{
							return;
						}

						read.show();
					}
				});
				_menu.add(_readItem);

				JMenu edit = new JMenu(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("EditMenu"));

				JMenuItem rename = new JMenuItem(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Rename"));
				rename.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						int row = _packetList.getSelectedRow();
						if (row == -1)
						{
							return;
						}

						DecryptPacket packet = _pane.getPacketTableModel().getPacket(row);
						if (packet == null)
						{
							return;
						}

						EnterNameDialog dialog = new EnterNameDialog(Jn.getInstance(), ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("EnterName"), packet.getName());
						if (dialog.showToWrite())
						{
							_pane.getPacketTableModel().setName(row, dialog.getText());
							_packetList.updateUI();
						}
					}
				});

				JMenuItem delete = new JMenuItem(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Delete"));
				delete.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						int row = _packetList.getSelectedRow();
						if (row == -1)
						{
							return;
						}

						DecryptPacket packet = _pane.getPacketTableModel().getPacket(row);
						if (packet == null)
						{
							return;
						}

						if (Config.get(Values.DELETE_PACKET_CONFIRM, true))
						{
							ConfirmDialog dialog = new ConfirmDialog(Jn.getInstance(), ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Message"), ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("YouRealyWantToyDeletePacketInfo"));
							boolean[] result = dialog.showToConfirm();

							Config.set(Values.DELETE_PACKET_CONFIRM, result[1]);

							if (!result[0])
							{
								return;
							}
						}

						_pane.getPacketTableModel().deleteFormat(row);
						_packetList.updateUI();
					}
				});

				edit.add(rename);
				edit.add(delete);


				_menu.add(edit);  */


		_transperyPacket.setToolTipText(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Visible") + ": " + _transperyPacket.getValue());

		_transperyPacket.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{
				_transperyPacket.setToolTipText(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Visible") + ": " + _transperyPacket.getValue());
			}
		});

		registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				getPane().getSearchPane().search();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


		_packetList.registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int row = _packetList.getSelectedRow();
				if(row == -1)
				{
					return;
				}
				DecryptedPacket packet = getPane().getDecryptPacketListModel().getPacket(row);
				if(packet == null)
				{
					return;
				}
				float f = _transperyPacket.getValue() / 100F;
				new PacketForm(getPane(), f, packet, row);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	public JMenuItem getSearchItem()
	{
		return _searchItem;
	}

	private void createUIComponents()
	{
		main = this;

		setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

		_packetList = new JTable(getPane().getDecryptPacketListModel());
		_packetList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_packetList.setDefaultRenderer(Object.class, new PacketTableRenderer(getPane().getDecryptPacketListModel()));
		_packetList.getColumnModel().getColumn(0).setMaxWidth(30); //type
		_packetList.getColumnModel().getColumn(1).setMaxWidth(115); //time
		_packetList.getColumnModel().getColumn(2).setMaxWidth(50); //id
		_packetList.getColumnModel().getColumn(3).setMaxWidth(50);  //lenght
		_packetList.getColumnModel().getColumn(4).setMaxWidth(180); //name

		_packetList.addMouseListener(new MouseListenerImpl());
	}

	public void activeReadItem()
	{
		_readItem.setEnabled(false);

		DecryptedPacket packet = getSelectedRow();
		if(packet == null || packet.getPacketInfo() == null)
		{
			return;
		}

		PacketReader read = packet.getPacketInfo().getPacketReader();

		if(read == null || !read.read(packet))
		{
			return;
		}

		_readItem.setEnabled(true);
	}

	public int getSelectedRowNumber()
	{
		return _packetList.getSelectedRow();
	}

	public DecryptedPacket getSelectedRow()
	{
		int row = _packetList.getSelectedRow();

		if(row == -1)
		{
			return null;
		}

		return getPane().getDecryptPacketListModel().getPacket(row);
	}

	public float getTransientValue()
	{
		return _transperyPacket.getValue() / 100F;
	}

	public JTable getPacketTable()
	{
		return _packetList;
	}

	public JScrollPane getScroll()
	{
		return _packetScrollPane;
	}

	public ViewPane getPane()
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
		main.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		_packetScrollPane = new JScrollPane();
		_packetScrollPane.setAutoscrolls(true);
		_packetScrollPane.setHorizontalScrollBarPolicy(31);
		_packetScrollPane.setVerticalScrollBarPolicy(22);
		main.add(_packetScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		_packetList.setAutoCreateRowSorter(false);
		_packetList.setPreferredScrollableViewportSize(new Dimension(-1, -1));
		_packetScrollPane.setViewportView(_packetList);
		_transperyPacket = new JSlider();
		_transperyPacket.setMinimum(20);
		_transperyPacket.setOrientation(0);
		_transperyPacket.setValue(100);
		main.add(_transperyPacket, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return main;
	}
}
