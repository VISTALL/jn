package com.jds.jn;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.jds.jn.ui.JTab;
import com.jds.jn.ui.JTabs;

/**
 * Author: VISTALL
 * Date:  10:55/21.12.2010
 */
public class JnTest
{
	public static void main(String... arg) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabs t = new JTabs();
		t.registerTopPanel(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK, new JTabs.PanelFactory()
		{
			@Override
			public JPanel newInstance()
			{
				JPanel p = new JPanel();
				p.add(new JLabel("test"));
				return p;
			}
		});
		f.add(t);

		t.addTab(new JTab(new JTextArea("dsadsad")).setTitle("AAAAAAAAAA"));
		t.addTab(new JTab(new JTextArea("adasd")).setTitle("BBBBBBBBBB"));

		f.setSize(500, 500);
		f.setVisible(true);
	}
}
