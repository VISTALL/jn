package com.jds.jn.gui.panels.viewpane;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import com.intellij.uiDesigner.core.*;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.profiles.*;
import com.jds.jn.parser.PartType;
import com.jds.jn.parser.datatree.NumberValuePart;
import com.jds.jn.parser.datatree.StringValuePart;
import com.jds.jn.parser.formattree.ForPart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21.09.2009
 * Time: 14:00:20
 * Thanks: Ulysses R. Ribeiro
 */
public class SearchPane extends JPanel
{
	private JPanel main;
	private JTextField _findText;
	private JButton _searchBtn;
	private JRadioButton simpleSearchRadioButton;
	private JRadioButton advancedSearchRadioButton;
	private JComboBox packetSelect;
	private JComboBox partselect;
	private JComboBox operatorSelect;
	private JTextField findT;
	private JLabel statusLable;
	private ViewPane _pane;

	public boolean IS_HIDE = false;

	private PacketInfo _currentFormat;
	private Part _currentPart;
	private int _currentIndex;
	private boolean _stringPart;

	private static final String[] MATH_OPERATORS = {
			"==",
			"!=",
			">",
			">=",
			"<",
			"<="
	};
	private static final String[] STRING_OPERATORS = {
			"==",
			"!="
	};

	private List<PacketInfo> _formats = new ArrayList<PacketInfo>();

	public SearchPane(ViewPane pane)
	{
		_pane = pane;
		$$$setupUI$$$();

		simpleSearchRadioButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				packetSelect.setEnabled(false);
				partselect.setEnabled(false);
				operatorSelect.setEnabled(false);
				findT.setEnabled(false);

				_findText.setEnabled(true);
			}
		});

		advancedSearchRadioButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				setPackets();

				packetSelect.setEnabled(true);
				partselect.setEnabled(true);
				operatorSelect.setEnabled(true);
				findT.setEnabled(true);

				_findText.setEnabled(false);
			}
		});

		_findText.addKeyListener(new KeyListener()
		{

			@Override
			public void keyTyped(KeyEvent e)
			{

			}

			@Override
			public void keyPressed(KeyEvent e)
			{

			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				/*
				TODO _pane.getPacketListPane().getSearchItem().setEnabled(!_findText.getText().trim().equals(""));
				*/
				_searchBtn.setEnabled(!_findText.getText().trim().equals(""));
			}
		});

		findT.addKeyListener(new KeyListener()
		{

			@Override
			public void keyTyped(KeyEvent e)
			{

			}

			@Override
			public void keyPressed(KeyEvent e)
			{

			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				_pane.getPacketListPane().getSearchItem().setEnabled(!findT.getText().trim().equals(""));
				_searchBtn.setEnabled(!findT.getText().trim().equals(""));
			}
		});

		_searchBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				search();
			}
		});

		packetSelect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				_currentIndex = 0;
				_currentFormat = _formats.get(packetSelect.getSelectedIndex());
				partselect.removeAllItems();

				_currentPart = null;
				partselect.addItem("<none>");
				for (Part part : _currentFormat.getDataFormat().getMainBlock().getParts())
				{
					if (part instanceof ForPart)
					{
						continue;
					}
					partselect.addItem(part.getName());
				}
			}
		});

		partselect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				_currentIndex = 0;
				String name = (String) partselect.getSelectedItem();
				if (name == null)
				{
					return;
				}

				_currentPart = _currentFormat.getDataFormat().getMainBlock().getPartByName(name);
				if (_currentPart == null)
				{
					return;
				}

				if ((!_stringPart || operatorSelect.getItemCount() <= 0) && (_currentPart.getType() == PartType.S || _currentPart.getType() == PartType.s))
				{
					operatorSelect.removeAllItems();
					_stringPart = true;
					for (String str : STRING_OPERATORS)
					{
						operatorSelect.addItem(str);
					}
					operatorSelect.setSelectedIndex(0);
				}
				else if ((_stringPart || operatorSelect.getItemCount() <= 0) && !(_currentPart.getType() == PartType.S || _currentPart.getType() == PartType.s))
				{
					operatorSelect.removeAllItems();
					_stringPart = false;
					for (String str : MATH_OPERATORS)
					{
						operatorSelect.addItem(str);
					}
					operatorSelect.setSelectedIndex(0);
				}
			}
		});

		operatorSelect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				_currentIndex = 0;
			}
		});
	}

	public void search()
	{
		if (simpleSearchRadioButton.isSelected())
		{
			String findPacket = _findText.getText();

			if (findPacket.equals(""))
			{
				return;
			}

			if (_pane.getDecryptPacketTableModel().searchPacket(findPacket))
			{
				found();
			}
			else
			{
				notfound();
			}
		}
		else
		{
			int index = search(_currentIndex);

			if (index >= 0)
			{
				JTable pt = _pane.getPacketListPane().getPacketTable();
				pt.setAutoscrolls(true);
				pt.getSelectionModel().setSelectionInterval(index, index);
				pt.scrollRectToVisible(pt.getCellRect(index, 0, true));
				_currentIndex = index + 1;
				found();
			}
			else
			{
				notfound();
			}
		}
	}

	private void setPackets()
	{
		_formats.clear();

		if (_pane.getSession() == null)
		{
			return;
		}

		Protocol currentProto = _pane.getSession().getProtocol();
		getAllFormatsName(currentProto);

		for (PacketInfo format : _formats)
		{
			packetSelect.addItem(format.getOpcodeStr() + " " + format.getName());
		}
	}

	private void getAllFormatsName(Protocol p)
	{
		for (PacketFamilly a : p.getFamilies())
		{
			_formats.addAll(a.getFormats().values());
		}
	}

	public String getFindText()
	{
		return _findText.getText();
	}

	private void createUIComponents()
	{
		main = this;
	}

	public void found()
	{
		statusLable.setText(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Found"));
		statusLable.setForeground(Color.GREEN);
	}

	public void notfound()
	{
		statusLable.setText(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("NotFound"));
		statusLable.setForeground(Color.RED);
	}

	public int search(int startIndex)
	{
		Session session = _pane.getSession();
		NetworkProfile profile = NetworkProfiles.getInstance().active();

		if (session == null || profile == null)
		{
			return -1;
		}

		ListenerType type = session.getListenerType();
		NetworkProfilePart part = profile.getPart(type);
		List<DecryptPacket> packets = session.getDecryptPackets();

		if (packets == null)
		{
			return -1;
		}

		PacketInfo format;
		int size = packets.size();

		for (int i = startIndex; i < size; i++)
		{
			DecryptPacket gp = packets.get(i);
			format = gp.getPacketFormat();

			if (format != null)
			{
				if (part.isFiltredOpcode(format.getOpcodeStr()))
				{
					continue;
				}
				if (format == _currentFormat)
				{
					if (_currentPart != null)
					{
						switch (operatorSelect.getSelectedIndex())
						{
							case 0: // ==
								if ((_currentPart.getType() != PartType.S) && (_currentPart.getType() != PartType.s))
								{
									try
									{
										int value = Integer.decode(findT.getText());
										int partValue = (int) ((NumberValuePart) gp.getRootNode().getPartByName(_currentPart.getName())).getValueAsInt();
										if (value == partValue)
										{
											return i;
										}
									}
									catch (NumberFormatException nfe)
									{
										//nfe.printStackTrace();
									}
								}
								else
								{
									if (((StringValuePart) gp.getRootNode().getPartByName(_currentPart.getName())).getStringValue().equalsIgnoreCase(findT.getText()))
									{
										return i;
									}
								}
								break;
							case 1: // !=
								if ((_currentPart.getType() != PartType.S) && (_currentPart.getType() != PartType.s))
								{
									try
									{
										int value = Integer.decode(findT.getText());
										int partValue = ((NumberValuePart) gp.getRootNode().getPartByName(_currentPart.getName())).getValueAsInt();

										if (value != partValue)
										{
											return i;
										}
									}
									catch (NumberFormatException nfe)
									{
										//nfe.printStackTrace();
									}
								}
								else
								{
									if (!((StringValuePart) gp.getRootNode().getPartByName(_currentPart.getName())).getStringValue().equalsIgnoreCase(findT.getText()))
									{
										return i;
									}
								}
								break;
							case 2: // >
								try
								{
									int value = Integer.decode(findT.getText());
									int partValue = (int) ((NumberValuePart) gp.getRootNode().getPartByName(_currentPart.getName())).getValueAsInt();
									if (partValue > value)
									{
										return i;
									}
								}
								catch (NumberFormatException nfe)
								{
									//nfe.printStackTrace();
								}
								break;
							case 3: // >=
								try
								{
									int value = Integer.decode(findT.getText());
									int partValue = (int) ((NumberValuePart) gp.getRootNode().getPartByName(_currentPart.getName())).getValueAsInt();
									if (partValue >= value)
									{
										return i;
									}
								}
								catch (NumberFormatException nfe)
								{
									//nfe.printStackTrace();
								}
								break;
							case 4: // <
								try
								{
									int value = Integer.decode(findT.getText());
									int partValue = (int) ((NumberValuePart) gp.getRootNode().getPartByName(_currentPart.getName())).getValueAsInt();
									if (partValue < value)
									{
										return i;
									}
								}
								catch (NumberFormatException nfe)
								{
									//nfe.printStackTrace();
								}
								break;
							case 5: // <=
								try
								{
									int value = Integer.decode(findT.getText());
									int partValue = ((NumberValuePart) gp.getRootNode().getPartByName(_currentPart.getName())).getValueAsInt();
									if (partValue <= value)
									{
										return i;
									}
								}
								catch (NumberFormatException nfe)
								{
									//nfe.printStackTrace();
								}
								break;
						}
					}
					else
					{
						return i;
					}
				}
			}
		}

		return -1;
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
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 10), -1, -1));
		main.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel2.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
		_findText = new JTextField();
		panel3.add(_findText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		simpleSearchRadioButton = new JRadioButton();
		simpleSearchRadioButton.setSelected(true);
		this.$$$loadButtonText$$$(simpleSearchRadioButton, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("SimpleSearch"));
		panel2.add(simpleSearchRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		advancedSearchRadioButton = new JRadioButton();
		this.$$$loadButtonText$$$(advancedSearchRadioButton, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("AdvancedSearch"));
		panel4.add(advancedSearchRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel4.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
		final JPanel panel6 = new JPanel();
		panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		packetSelect = new JComboBox();
		packetSelect.setEnabled(false);
		panel6.add(packetSelect, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel7 = new JPanel();
		panel7.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
		panel5.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		partselect = new JComboBox();
		partselect.setEnabled(false);
		panel7.add(partselect, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
		operatorSelect = new JComboBox();
		operatorSelect.setEnabled(false);
		panel7.add(operatorSelect, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
		findT = new JTextField();
		findT.setEnabled(false);
		panel7.add(findT, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
		final JPanel panel8 = new JPanel();
		panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_searchBtn = new JButton();
		_searchBtn.setBorderPainted(true);
		_searchBtn.setEnabled(false);
		_searchBtn.setFocusPainted(true);
		_searchBtn.setIcon(new ImageIcon(getClass().getResource("/com/jds/jn/resources/images/find.png")));
		_searchBtn.setText("");
		panel8.add(_searchBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel8.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JPanel panel9 = new JPanel();
		panel9.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Status"));
		panel9.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		panel9.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		statusLable = new JLabel();
		statusLable.setForeground(new Color(-52429));
		this.$$$loadLabelText$$$(statusLable, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("NotFound"));
		panel9.add(statusLable, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer3 = new Spacer();
		main.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		ButtonGroup buttonGroup;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(simpleSearchRadioButton);
		buttonGroup.add(simpleSearchRadioButton);
		buttonGroup.add(advancedSearchRadioButton);
	}

	/**
	 * @noinspection ALL
	 */
	private void $$$loadLabelText$$$(JLabel component, String text)
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
			component.setDisplayedMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
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
		return main;
	}
}
