package com.jds.jn.gui.listeners;

import com.jds.jn.Jn;
import com.jds.jn.config.RValues;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 19:46:02
 */
public class WindowsAdapter extends WindowAdapter
{
	@Override
	public void windowClosed(WindowEvent e)
	{
		//Jn.stop();
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		//_listener.actionPerformed(new ActionEvent(this, 0, MainForm.MainAction.EXIT.name()));
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		if (RValues.USE_TRAY.asBoolean())
		{
			Jn.getInstance().setVisible(!Jn.getInstance().isVisible());
		}
	}
}
