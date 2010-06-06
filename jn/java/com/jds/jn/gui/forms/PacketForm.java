package com.jds.jn.gui.forms;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

import com.intellij.uiDesigner.core.*;
import com.jds.jn.Jn;
import com.jds.jn.config.RValues;
import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.forms.menu_listeners.MenuPopupMenuListener;
import com.jds.jn.gui.listeners.TableMouseListener;
import com.jds.jn.gui.models.DataPartNode;
import com.jds.jn.gui.models.PacketViewTableModel;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.renders.*;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.parser.PartType;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.util.Util;
import com.sun.awt.AWTUtilities;
import org.jdesktop.swingx.JXTreeTable;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 19:15:50
 */
public class PacketForm extends JFrame
{
	private DataPacket _packet;
	private JPanel root;
	private PacketViewTableModel _packetViewTableModel;
	private JTextPane _hexDumpPacket;
	private JXTreeTable _packetStructure;
	private JButton addButton;
	private JComboBox _partBox;
	private JScrollPane _scrollPane;
	private DefaultStyledDocument _hexStyledDoc;
	private JPopupMenu _menu;
	private ViewPane _pane;
	private int _row;
	private JMenu _bytesPopupMenu;

	private int _verticalScroll;
	private int _horizontalScroll;

	public PacketForm(ViewPane pane, float persent, DataPacket packet, int row)
	{
		setPacket(packet);
		setPane(pane);
		setRow(row);

		$$$setupUI$$$();
		add($$$getRootComponent$$$());
		setIconImage(ImageStatic.ICON_PACKET.getImage());

		_hexStyledDoc = (DefaultStyledDocument) _hexDumpPacket.getStyledDocument();
		addStylesToHexDump(_hexStyledDoc);

		addButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				EnterNameDialog dialog = new EnterNameDialog(PacketForm.this, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("EnterName"));
				if (!dialog.showToWrite())
				{
					return;
				}

				PartContainer pC = getPacket().getPacketFormat().getDataFormat().getMainBlock();
				PartType partType = PartTypeManager.getInstance().getType(getPartBox().getSelectedItem().toString());
				Part p = new Part(partType);
				p.setName(dialog.getText());
				pC.addPart(p);
				setPacket(new DataPacket(getPacket().getFullBuffer().clone().array(), getPacket().getPacketType(), getPacket().getProtocol()));
				getPane().getPacketTableModel().updatePacket(getRow(), getPacket());

				updateCurrentPacket();
			}
		});

		addButton.setEnabled(getPacket().getPacketFormat() != null);

		setBytesPopupMenu(new JMenu("byte[] Menu"));

		JMenuItem changeSize = new JMenuItem("Change size");
		changeSize.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object node = getPacketStructure().getModel().getValueAt(getPacketStructure().getSelectedRow(), 0);
				if (node == null)
				{
					return;
				}

				if (!(node instanceof ValuePart))
				{
					return;
				}

				ValuePart part = (ValuePart) node;

				if (!part.getClass().getSimpleName().equals("ValuePart"))
				{
					return;
				}
				EnterNameDialog dialog = new EnterNameDialog(PacketForm.this, "Enter size");
				if (!dialog.showToWrite())
				{
					return;
				}
				int size = Integer.parseInt(dialog.getText());
				part.setBSize(size);

				PartContainer pC = part.getModelPart().getParentContainer();
				PartType partType = PartTypeManager.getInstance().getType(part.getModelPart().getType().getName());

				Part p = new Part(partType);
				p.setName(part.toString());
				p.setBSize(size);

				pC.replace(part.getModelPart(), p);

				setPacket(new DataPacket(getPacket().getFullBuffer().clone().array(), getPacket().getPacketType(), getPacket().getProtocol()));
				getPane().getPacketTableModel().updatePacket(getRow(), getPacket());

				updateCurrentPacket();
			}
		});
		getBytesPopupMenu().add(changeSize);

		setMenu(new JPopupMenu());
		//EditPartMenuListener listener = new EditPartMenuListener(this);

		getMenu().addPopupMenuListener(new MenuPopupMenuListener(this, getMenu()));

		/**JMenuItem rename = new RenameMenuItem(this, listener);
		 JMenu addMenu = new AddMenu(this);
		 JMenu addAfterMenu = new AddAfterMenu(this);
		 JMenuItem changeMenu = new ChangeMenu(this);
		 JMenuItem delete = new DeleteMenuItem(this);

		 getMenu().add(rename);
		 getMenu().add(addMenu);
		 getMenu().add(addAfterMenu);
		 getMenu().add(changeMenu);
		 getMenu().add(delete); */

		getPacketStructure().addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				int index = getPacketStructure().getSelectedRow();
				Object node = getPacketStructure().getModel().getValueAt(index, 0);

				_verticalScroll = _scrollPane.getVerticalScrollBar().getValue();
				_horizontalScroll = _scrollPane.getHorizontalScrollBar().getValue();

				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 && node instanceof ValuePart)
				{
					ValuePart part = (ValuePart) node;
					part.setSelected(!part.isSelected());
					part.updateColor(getPacket());
					updateHexDump();
					getPacketStructure().setRowSelectionInterval(index, index);
					setScroolBar();
				}
				else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)
				{
					getMenu().show(getPacketStructure(), e.getX(), e.getY());
				}
			}

		});

		$$$getRootComponent$$$().registerKeyboardAction(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				PacketForm.this.dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

		updateCurrentPacket();
		setSize(976, 634);
		AWTUtilities.setWindowOpacity(this, persent);
		setLocationRelativeTo(Jn.getInstance());
		setResizable(true);
		setVisible(true);
	}


	private void addStylesToHexDump(StyledDocument doc)
	{
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style base = doc.addStyle("base", def);

		Style selected = doc.addStyle("selected", def);
		StyleConstants.setFontFamily(selected, "Monospaced");
		StyleConstants.setForeground(selected, RValues.PACKET_FORM_SELECT_FOREGROUND_COLOR.asTColor());
		StyleConstants.setBackground(selected, RValues.PACKET_FORM_SELECT_BACKGROUND_COLOR.asTColor());

		StyleConstants.setFontFamily(base, "Monospaced");
		StyleConstants.setForeground(base, Color.BLACK);

		Style regular = doc.addStyle("regular", base);
		//StyleConstants.setUnderline(regular , true);

		Style s = doc.addStyle("d", regular);
		StyleConstants.setBackground(s, new Color(72, 164, 255));

		s = doc.addStyle("ud", regular);
		StyleConstants.setBackground(s, new Color(72, 164, 255));

		s = doc.addStyle("Q", regular);
		StyleConstants.setBackground(s, new Color(255, 255, 128));

		s = doc.addStyle("h", regular);
		StyleConstants.setBackground(s, Color.ORANGE);

		s = doc.addStyle("uh", regular);
		StyleConstants.setBackground(s, new Color(255, 200, 10));

		s = doc.addStyle("bch", regular);
		StyleConstants.setBackground(s, new Color(189, 148, 6));

		s = doc.addStyle("s", regular);
		StyleConstants.setBackground(s, new Color(100, 255, 100));

		s = doc.addStyle("S", regular);
		StyleConstants.setBackground(s, new Color(100, 255, 100));

		s = doc.addStyle("uc", regular);
		StyleConstants.setBackground(s, Color.MAGENTA);

		s = doc.addStyle("c", regular);
		StyleConstants.setBackground(s, Color.PINK);

		s = doc.addStyle("SS", regular);
		StyleConstants.setBackground(s, new Color(100, 255, 100));

		s = doc.addStyle("sS", regular);
		StyleConstants.setBackground(s, new Color(100, 255, 100));

		s = doc.addStyle("f", regular);
		StyleConstants.setBackground(s, Color.LIGHT_GRAY);

		s = doc.addStyle("D", regular);
		StyleConstants.setBackground(s, Color.DARK_GRAY);

		Color bxColor = new Color(100, 50, 50);
		s = doc.addStyle("b", regular);
		StyleConstants.setBackground(s, bxColor);

		s = doc.addStyle("op", regular);
		StyleConstants.setBackground(s, Color.YELLOW);

		//s = doc.addStyle("selected", regular);
		//StyleConstants.setBackground(s, Color.BLUE);

		s = doc.addStyle("chk", regular);
		StyleConstants.setBackground(s, Color.GREEN);
	}

	public void addStyledText(String text, String style)
	{
		Style s = _hexStyledDoc.getStyle(style);
		if (s == null)
		{
			Jn.getInstance().warn("Missing style for partType: " + style);
			style = "base";
		}

		try
		{
			_hexStyledDoc.insertString(_hexStyledDoc.getLength(), text, _hexStyledDoc.getStyle(style));
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}

	public void updateHexDump()
	{
		_hexDumpPacket.setText("");

		int len = getPacket().getFullBuffer().array().length;

		for (int i = 0; i < len; i++)
		{
			String color = getPacket().getColor(i);
			if (color == null)
			{
				color = "base";
			}

			byte b = getPacket().getFullBuffer().array()[i];

			addStyledText(Util.zeropad(Long.toHexString(b & 0xff), 2).toUpperCase(), color);

			String nextColor = (i != (len - 1)) ? getPacket().getColor(i + 1) : null;

			if (nextColor != null && nextColor.equals(color))
			{
				addStyledText(" ", color);
			}
			else
			{
				addStyledText(" ", "base");
			}
		}

		addLineBreaksToHexDump(getPacket().getBuffer().array());
	}

	public void setScroolBar()
	{
		_scrollPane.getVerticalScrollBar().setValue(_verticalScroll);
		_scrollPane.getHorizontalScrollBar().setValue(_horizontalScroll);
	}

	public void updateCurrentPacket()
	{
		String name = (String) getPane().getPacketTableModel().getValueAt(_row, 4);

		if (name == null)
		{
			setTitle(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Packet") + ": -");
		}
		else
		{
			setTitle(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Packet") + ": " + name);
		}

		updateHexDump();

		if (getPacket().getPacketFormat() != null && !getPacket().hasError())
		{
			/*int opSize = 0;
			for (PartType pt : getPacket().getPacketFormat().sizeId())
			{
				opSize += pt.getTypeByteNumber() * 3;
			}   */

			DataPartNode root = new DataPartNode(getPacket().getRootNode(), getPacket().getProtocol().getChecksumSize() * 3);

			getPacketViewTableModel().setRoot(root);
			getPacketStructure().revalidate();
			getPacketStructure().expandAll();
		}
		else
		{
			getPacketViewTableModel().setRoot(null);
		}
	}

	private void addLineBreaksToHexDump(byte[] data)
	{
		//add linefeeds to the dump
		int lnCount = _hexDumpPacket.getText().length() / 48;
		int rest = _hexDumpPacket.getText().length() % 48;
		for (int i = 1; i <= lnCount; i++)
		{
			int pos = i * 67 - 20;
			try
			{
				int idx = i - 1;
				String ansci = idx == 0 ? Util.toAnsci(data, 0, 16) : Util.toAnsci(data, idx * 16, idx * 16 + 16);
				_hexStyledDoc.replace(pos, 1, "   " + ansci + "\n", _hexStyledDoc.getStyle("base"));
			}
			catch (BadLocationException e1)
			{
				e1.printStackTrace();
			}
		}
		//rest
		if (rest != 0)
		{
			try
			{
				int pos = lnCount * 67 + rest;
				String space = "";
				int spaceCount = 48 - rest;
				while (spaceCount-- > 0)
				{
					space += " ";
				}
				String ansci = lnCount == 0 ? Util.toAnsci(data, 0, data.length) : Util.toAnsci(data, lnCount * 16, data.length);
				_hexStyledDoc.insertString(pos, space + "  " + ansci, _hexStyledDoc.getStyle("base"));
			}
			catch (BadLocationException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	private void createUIComponents()
	{
		setPacketViewTableModel(new PacketViewTableModel(null));
		setPacketStructure(new JXTreeTable(getPacketViewTableModel()));
		getPacketStructure().setDefaultRenderer(Object.class, new IconTableRenderer());
		getPacketStructure().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getPacketStructure().addMouseListener(new TableMouseListener(getPacketStructure()));
		getPacketStructure().setTreeCellRenderer(new PacketViewTreeRenderer());
		//getPacketStructure().getColumn(0).setPreferredWidth(50);

		getPacketStructure().setOpenIcon(ImageStatic.FOLDER_CLOSE);
		getPacketStructure().setCollapsedIcon(ImageStatic.CLOSE_ICON);
		getPacketStructure().setExpandedIcon(ImageStatic.OPEN_ICON);
		getPacketStructure().setClosedIcon(ImageStatic.FOLDER_OPEN);

		setPartBox(new JComboBox(IconComboBoxRenderer.types));
		getPartBox().setRenderer(new IconComboBoxRenderer());
		getPacketStructure().setEditable(false);
	}

	public DataPacket getPacket()
	{
		return _packet;
	}

	public void setPacket(DataPacket packet)
	{
		_packet = packet;
	}

	public JXTreeTable getPacketStructure()
	{
		return _packetStructure;
	}

	public void setPacketStructure(JXTreeTable packetStructure)
	{
		_packetStructure = packetStructure;
	}

	public ViewPane getPane()
	{
		return _pane;
	}

	public void setPane(ViewPane pane)
	{
		_pane = pane;
	}

	public int getRow()
	{
		return _row;
	}

	public void setRow(int row)
	{
		_row = row;
	}

	public PacketViewTableModel getPacketViewTableModel()
	{
		return _packetViewTableModel;
	}

	public void setPacketViewTableModel(PacketViewTableModel packetViewTableModel)
	{
		_packetViewTableModel = packetViewTableModel;
	}

	public JComboBox getPartBox()
	{
		return _partBox;
	}

	public void setPartBox(JComboBox partBox)
	{
		_partBox = partBox;
	}

	public JPopupMenu getMenu()
	{
		return _menu;
	}

	public void setMenu(JPopupMenu menu)
	{
		_menu = menu;
	}

	public JMenu getBytesPopupMenu()
	{
		return _bytesPopupMenu;
	}

	public void setBytesPopupMenu(JMenu bytesPopupMenu)
	{
		_bytesPopupMenu = bytesPopupMenu;
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
		root = new JPanel();
		root.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		final JSplitPane splitPane1 = new JSplitPane();
		splitPane1.setEnabled(true);
		root.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_scrollPane = new JScrollPane();
		_scrollPane.setVerticalScrollBarPolicy(22);
		splitPane1.setLeftComponent(_scrollPane);
		_hexDumpPacket = new JTextPane();
		_hexDumpPacket.setEditable(false);
		_hexDumpPacket.putClientProperty("charset", "UTF-8");
		_scrollPane.setViewportView(_hexDumpPacket);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		splitPane1.setRightComponent(panel1);
		final JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setVerticalScrollBarPolicy(22);
		panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		_packetStructure.setAutoResizeMode(4);
		_packetStructure.setAutoStartEditOnKeyStroke(false);
		_packetStructure.setEnabled(true);
		_packetStructure.setExpandsSelectedPaths(false);
		_packetStructure.setFillsViewportHeight(false);
		_packetStructure.setHorizontalScrollEnabled(false);
		_packetStructure.setPreferredScrollableViewportSize(new Dimension(-1, -1));
		_packetStructure.setRolloverEnabled(false);
		_packetStructure.setRootVisible(false);
		_packetStructure.setRowHeightEnabled(false);
		_packetStructure.setScrollsOnExpand(false);
		_packetStructure.setShowHorizontalLines(false);
		_packetStructure.setShowVerticalLines(false);
		_packetStructure.setShowsRootHandles(false);
		_packetStructure.setUpdateSelectionOnSort(false);
		_packetStructure.setVerifyInputWhenFocusTarget(false);
		_packetStructure.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		_packetStructure.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		scrollPane1.setViewportView(_packetStructure);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		addButton = new JButton();
		this.$$$loadButtonText$$$(addButton, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("AddPart"));
		panel2.add(addButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		panel2.add(_partBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
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

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return root;
	}
}
