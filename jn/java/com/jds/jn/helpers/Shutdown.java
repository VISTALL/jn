package com.jds.jn.helpers;

import com.jds.jn.config.ConfigParser;
import com.jds.jn.config.RValues;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.util.ThreadPoolManager;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21.08.2009
 * Time: 3:01:37
 */
public class Shutdown extends Thread
{
	@Override
	public void run()
	{
		RValues.LAST_WINDOW_POSITION_X.setVal(MainForm.getInstance().getLocation().x);
		RValues.LAST_WINDOW_POSITION_Y.setVal(MainForm.getInstance().getLocation().y);

		try
		{
			MainForm.getInstance().stopMemoryBarTask();
			ThreadPoolManager.getInstance().shutdown();
			ConfigParser.getInstance().shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
