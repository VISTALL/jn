package com.jds.jn.gui.forms;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JWindow;

import com.jds.swing.JPicturePanel;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 31.08.2009
 * Time: 22:04:15
 */
public class SplashWindow extends JWindow
{
	private static SplashWindow _splash;

	public static void showSplash()
	{
		if (_splash == null)
		{
			new SplashWindow();
		}
	}

	public static void hideSplash()
	{
		if (_splash == null)
		{
			return;
		}

		_splash.dispose();

		_splash = null;
	}

	public SplashWindow()
	{
		_splash = this;
		initComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initComponents()
	{
		JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
		JPicturePanel pp = new JPicturePanel();
		try
		{
			pp.setImage(ImageIO.read(getClass().getResource("/com/jds/jn/resources/images/logo.png")));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		pp.setPreferredSize(pp.getImageSize());
		mainPanel.add(pp, BorderLayout.CENTER);
		setContentPane(mainPanel);
	}
}