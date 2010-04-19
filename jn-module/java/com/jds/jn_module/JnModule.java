package com.jds.jn_module;

import com.jds.jn_module.gui.MainForm;

import javax.swing.*;
import java.io.File;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  2:34:34/04.04.2010
 */
public class JnModule
{
	private static MainForm _form;

	public static int PORT = 7777;

	public static void main(String...ar) throws Exception
	{
		if(ar.length > 0)
		{
			try
			{
				PORT = Integer.parseInt(ar[0]);
			}
			catch (NumberFormatException e)
			{
				//e.printStackTrace();
			}
		}
		
		File file = new File("./logs");
		if(!file.exists())
			file.mkdir();

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		_form = new MainForm();
		_form.setVisible(true);

		Runtime.getRuntime().addShutdownHook(new Thread(_form));
	}

	public static MainForm getInstance()
	{
		return _form;
	}

}
