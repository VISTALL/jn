package com.jds.jn;

import javax.swing.*;

import java.awt.*;

import com.jds.jn.config.ConfigParser;
import com.jds.jn.gui.dialogs.ExceptionDialog;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.gui.forms.SplashWindow;
import com.jds.jn.helpers.Shutdown;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.Types;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.ProtocolManager;
import com.jds.jn.remotefiles.FileLoader;
import com.jds.jn.runnable.GCUpdate;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.version_control.Programs;
import com.jds.jn.version_control.Version;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 03/01/2010
 * Time: 22:32:18
 */
public class Jn
{
	public  static final Version CURRENT = new Version(Programs.JN, 2, 0, Version.ALPHA, 1);
	private static MainForm _form;

	public static void main(String... arg) throws Exception
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SplashWindow.showSplash();
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);

			ConfigParser.getInstance();

			_form = new MainForm();
			_form.setMinimumSize(new Dimension(500, 800));
			_form.setExtendedState(JFrame.MAXIMIZED_HORIZ);
			_form.info("Program started.");

			NetworkProfiles.getInstance();

			ListenerSystem.getInstance();

			ThreadPoolManager.getInstance();

			ImageStatic.getInstance();
			FileLoader.getInstance();

			Types.newInstance();
			PartTypeManager.getInstance();
			ProtocolManager.getInstance();

			ExceptionDialog.getInstance();

			_form.updateTitle();

			ThreadPoolManager.getInstance().scheduleAtFixedRate(new GCUpdate(), 500, 5000);

			Runtime.getRuntime().addShutdownHook(new Shutdown());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(_form == null)
			{
				throw new Error("Form is Null");
			}

			_form.setVisible(true);
			_form.updateVisible();

			_form.info(String.format("Load %d classes.", FileLoader.getInstance().size()));

			for (Protocol p : ProtocolManager.getInstance().getProtocols())
			{
				_form.info(String.format("Load %s protocol.", p.getName()));
			}
			SplashWindow.hideSplash();
		}
	}

	public static MainForm getForm()
	{
		return _form;
	}
}
