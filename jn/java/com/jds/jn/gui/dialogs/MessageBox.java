package com.jds.jn.gui.dialogs;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBox extends JDialog
{
	private JPanel contentPane;
	private JButton buttonOK;
	private JLabel _text;

	public MessageBox(String t)
	{
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);
		setSize(460, 55);
		_text.setText(t);

		buttonOK.addActionListener(new ActionListener()
		{
			@Deprecated
			public void actionPerformed(ActionEvent e)
			{
				onOK();
			}
		});

		setVisible(true);
	}

	private void onOK()
	{
		// add your code here
		dispose();
	}
}
