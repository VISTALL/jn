package com.jds.jn.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * @author VISTALL
 * @date 22:43/06.10.2011
 */
public class JTabs extends JComponent
{
	public static interface PanelFactory
	{
		JPanel newInstance();
	}

	private List<JTab> _tabs = new CopyOnWriteArrayList<JTab>();

	public JTabs()
	{
		//
	}

	public void updateUI()
	{

	}

	public void addTab(JTab t)
	{

	}

	public void registerTopPanel(int key, int mod, PanelFactory factory)
	{
		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Test");
			}
		}, KeyStroke.getKeyStroke(key, mod), 0);
	}
}
