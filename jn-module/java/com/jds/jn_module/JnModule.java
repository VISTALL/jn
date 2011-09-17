package com.jds.jn_module;

import java.io.File;

import javax.swing.UIManager;

import com.jds.jn.util.OSUtils;
import com.jds.jn_module.gui.MainForm;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  2:34:34/04.04.2010
 */
public class JnModule
{
	public static int PORT;
	private static MainForm _form;

	public static void main(String...ar) throws Exception
	{
		File file = new File("./logs");
		if(!file.exists())
			file.mkdir();

		System.loadLibrary(OSUtils.getLibName());

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
