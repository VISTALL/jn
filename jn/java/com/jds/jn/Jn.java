package com.jds.jn;

import com.jds.jn.gui.dialogs.ExceptionDialog;
import com.jds.jn.gui.forms.NForm;
import com.jds.jn.gui.forms.SplashWindow;
import com.jds.jn.helpers.Shutdown;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.Types;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.ProtocolManager;
import com.jds.jn.rconfig.RConfig;
import com.jds.jn.remotefiles.FileLoader;
import com.jds.jn.runnable.GCUpdate;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.util.ThreadPoolManager;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 03/01/2010
 * Time: 22:32:18
 */
public class Jn implements Runnable
{
	private static Jn _instance;
	private NForm _form;
	public static String VERSION = ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Jn");

	public static void main(String... arg)
	{
		_instance = new Jn();

		//SwingUtilities.invokeLater(_instance);
		ThreadPoolManager.getInstance().execute(_instance);
	}

	public static NForm getInstance()
	{
		return _instance._form;
	}

	@Override
	public void run()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SplashWindow.showSplash();

			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);

			RConfig.getInstance();

			_form = new NForm();
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
}
