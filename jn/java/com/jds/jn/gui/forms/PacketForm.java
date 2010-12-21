package com.jds.jn.gui.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.jdesktop.swingx.JXTreeTable;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.Jn;
import com.jds.jn.config.RValues;
import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.forms.menu_listeners.MenuPopupMenuListener;
import com.jds.jn.gui.listeners.TableMouseListener;
import com.jds.jn.gui.models.DataPartNode;
import com.jds.jn.gui.models.PacketViewTableModel;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.renders.IconComboBoxRenderer;
import com.jds.jn.gui.renders.IconTableRenderer;
import com.jds.jn.gui.renders.PacketViewTreeRenderer;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.Types;
import com.jds.jn.parser.datatree.RawValuePart;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.Util;
import com.sun.awt.AWTUtilities;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 19:15:50
 */
public class PacketForm extends JFrame
{
	private DecryptedPacket _packet;
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

	public PacketForm(ViewPane pane, float persent, DecryptedPacket packet, int row)
	{
		setPacket(packet);
		_pane = pane;
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

				EnterNameDialog dialog = new EnterNameDialog(PacketForm.this, Bundle.getString("EnterName"));
				if(!dialog.showToWrite())
				{
					return;
				}

				PartContainer pC = getPacket().getPacketInfo().getDataFormat().getMainBlock();
				PartType partType = PartTypeManager.getInstance().getType(getPartBox().getSelectedItem().toString());
				Part p = new Part(partType);
				p.setName(dialog.getText());
				pC.addPart(p);

				setPacket(new DecryptedPacket(getPacket().getNotDecryptData().clone(), getPacket().getPacketType(), getPacket().getProtocol()));

				getPane().getDecryptPacketTableModel().updatePacket(getRow(), getPacket());

				updateCurrentPacket();
			}
		});

		addButton.setEnabled(getPacket().getPacketInfo() != null);

		setBytesPopupMenu(new JMenu("byte[] Menu"));

		JMenuItem changeSize = new JMenuItem("Change size");
		changeSize.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object node = getPacketStructure().getModel().getValueAt(getPacketStructure().getSelectedRow(), 0);
				if(node == null)
				{
					return;
				}

				if(!(node instanceof RawValuePart))
				{
					return;
				}

				RawValuePart part = (RawValuePart) node;

				if(!part.getClass().getSimpleName().equals("ValuePart"))
				{
					return;
				}
				EnterNameDialog dialog = new EnterNameDialog(PacketForm.this, "Enter size");
				if(!dialog.showToWrite())
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

				setPacket(new DecryptedPacket(getPacket().getNotDecryptData().clone(), getPacket().getPacketType(), getPacket().getProtocol()));
				getPane().getDecryptPacketTableModel().updatePacket(getRow(), getPacket());

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

				if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 && node instanceof ValuePart)
				{
					ValuePart part = (ValuePart) node;
					part.setSelected(!part.isSelected());
					part.updateColor(getPacket());
					updateHexDump();
					getPacketStructure().setRowSelectionInterval(index, index);
					setScroolBar();
				}
				else if(e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)
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
		setLocationRelativeTo(Jn.getForm());
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


		Style s = doc.addStyle("b", regular);
		StyleConstants.setBackground(s, new Color(100, 50, 50));

		s = doc.addStyle("op", regular);
		StyleConstants.setBackground(s, Color.YELLOW);

		s = doc.addStyle("chk", regular);
		StyleConstants.setBackground(s, Color.GREEN);

		for(Types t : Types.values())
		{
			s = doc.addStyle(t.name(), regular);
			StyleConstants.setBackground(s, t.getColor());
		}
	}

	public void addStyledText(String text, String style)
	{
		Style s = _hexStyledDoc.getStyle(style);
		if(s == null)
		{
			Jn.getForm().warn("Missing style for partType: " + style);
			style = "base";
		}

		try
		{
			_hexStyledDoc.insertString(_hexStyledDoc.getLength(), text, _hexStyledDoc.getStyle(style));
		}
		catch(BadLocationException e)
		{
			e.printStackTrace();
		}
	}

	public void updateHexDump()
	{
		_hexDumpPacket.setText("");

		int len = getPacket().getNotDecryptData().length;

		for(int i = 0; i < len; i++)
		{
			String color = getPacket().getColor(i);
			if(color == null)
			{
				color = "base";
			}

			byte b = getPacket().getNotDecryptData()[i];

			addStyledText(Util.zeropad(Long.toHexString(b & 0xff), 2).toUpperCase(), color);

			String nextColor = (i != (len - 1)) ? getPacket().getColor(i + 1) : null;

			if(nextColor != null && nextColor.equals(color))
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
		updateHexDump();

		if(getPacket().getPacketInfo() != null && !getPacket().hasError())
		{
			setTitle(Bundle.getString("Packet") + ": " + getPacket().getName());
			DataPartNode root = new DataPartNode(getPacket().getRootNode());

			getPacketViewTableModel().setRoot(root);
			getPacketStructure().revalidate();
			getPacketStructure().expandAll();
		}
		else
		{
			setTitle(Bundle.getString("Packet") + ": -");
			getPacketViewTableModel().setRoot(null);
		}
	}

	private void addLineBreaksToHexDump(byte[] data)
	{
		//add linefeeds to the dump
		int lnCount = _hexDumpPacket.getText().length() / 48;
		int rest = _hexDumpPacket.getText().length() % 48;
		for(int i = 1; i <= lnCount; i++)
		{
			int pos = i * 67 - 20;
			try
			{
				int idx = i - 1;
				String ansci = idx == 0 ? Util.toAnsci(data, 0, 16) : Util.toAnsci(data, idx * 16, idx * 16 + 16);
				_hexStyledDoc.replace(pos, 1, "   " + ansci + "\n", _hexStyledDoc.getStyle("base"));
			}
			catch(BadLocationException e1)
			{
				e1.printStackTrace();
			}
		}
		//rest
		if(rest != 0)
		{
			try
			{
				int pos = lnCount * 67 + rest;
				String space = "";
				int spaceCount = 48 - rest;
				while(spaceCount-- > 0)
				{
					space += " ";
				}
				String ansci = lnCount == 0 ? Util.toAnsci(data, 0, data.length) : Util.toAnsci(data, lnCount * 16, data.length);
				_hexStyledDoc.insertString(pos, space + "  " + ansci, _hexStyledDoc.getStyle("base"));
			}
			catch(BadLocationException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	private void createUIComponents()
	{
		_packetViewTableModel = new PacketViewTableModel(null);
		_packetStructure = new JXTreeTable(_packetViewTableModel);
		_packetStructure.setDefaultRenderer(Object.class, new IconTableRenderer());
		_packetStructure.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_packetStructure.addMouseListener(new TableMouseListener(getPacketStructure()));
		_packetStructure.setTreeCellRenderer(new PacketViewTreeRenderer());

		_packetStructure.setOpenIcon(ImageStatic.FOLDER_CLOSE);
		_packetStructure.setCollapsedIcon(ImageStatic.CLOSE_ICON);
		_packetStructure.setExpandedIcon(ImageStatic.OPEN_ICON);
		_packetStructure.setClosedIcon(ImageStatic.FOLDER_OPEN);

		setPartBox(new JComboBox(IconComboBoxRenderer._types));
		getPartBox().setRenderer(new IconComboBoxRenderer());
		_packetStructure.setEditable(false);
	}

	public DecryptedPacket getPacket()
	{
		return _packet;
	}

	public void setPacket(DecryptedPacket packet)
	{
		_packet = packet;
	}

	public JXTreeTable getPacketStructure()
	{
		return _packetStructure;
	}

	public ViewPane getPane()
	{
		return _pane;
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
		for(int i = 0; i < text.length(); i++)
		{
			if(text.charAt(i) == '&')
			{
				i++;
				if(i == text.length())
				{
					break;
				}
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

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return root;
	}
}
