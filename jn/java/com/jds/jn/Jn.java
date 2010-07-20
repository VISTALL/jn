package com.jds.jn;

import org.apache.log4j.Logger;

import javax.swing.*;

import com.jds.jn.config.ConfigParser;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.gui.forms.SplashWindow;
import com.jds.jn.helpers.Shutdown;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.Types;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.ProtocolManager;
import com.jds.jn.classes.CLoader;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.util.logging.LoggingService;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 03/01/2010
 * Time: 22:32:18
 */
public class Jn
{
	private static final Logger _log = Logger.getLogger(Jn.class);

	public static void main(String... arg) throws Exception
	{
		LoggingService.load();

		_log.info("Logger init - ok");

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		_log.info("GUI properties set - ok");

		SplashWindow.showSplash();

		_log.info("Loading config - start");

		ConfigParser.getInstance();

		_log.info("Loading config - ok");

		ThreadPoolManager.getInstance();

		ImageStatic.getInstance();

		NetworkProfiles.getInstance();

		ListenerSystem.getInstance();

		CLoader.getInstance();

		Types.newInstance();
		PartTypeManager.getInstance();
		ProtocolManager.getInstance();

		try
		{
			MainForm.init();
			_log.info("MainForm: init ok");
		}
		catch (Exception e)
		{
			_log.info("MainForm: init fail" + e, e);
		}

		Runtime.getRuntime().addShutdownHook(new Shutdown());

		MainForm.getInstance().startMemoryBarTask();

		MainForm.getInstance().updateTitle();

		MainForm.getInstance().setVisible(true);

		MainForm.getInstance().updateVisible();

		_log.info(String.format("Load %d classes.", CLoader.getInstance().size()));

		for (Protocol p : ProtocolManager.getInstance().getProtocols())
		{
			_log.info(String.format("Load %s protocol.", p.getName()));
		}

		SplashWindow.hideSplash();
	}

	@Deprecated
	public static MainForm getForm()
	{
		return MainForm.getInstance();
	}
}
