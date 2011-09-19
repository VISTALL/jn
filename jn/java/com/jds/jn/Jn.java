package com.jds.jn;

import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import com.jds.jn.classes.CLoader;
import com.jds.jn.config.ConfigParser;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.gui.forms.SplashWindow;
import com.jds.jn.helpers.Shutdown;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.ProtocolManager;
import com.jds.jn.util.ImageStatic;
import com.jds.jn.util.OSUtils;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.util.logging.LoggingService;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 03/01/2010
 * Time: 22:32:18
 * <p/>
 * Java sNiffer
 */
public class Jn
{
	private static final Logger _log = Logger.getLogger(Jn.class);

	public static void main(String... arg) throws Exception
	{
		Locale[] l = Locale.getAvailableLocales();
		for(Locale locale : l)
			if(locale.getLanguage().equals("ru"))
				Locale.setDefault(locale);

		LoggingService.load();

		_log.info("Logger init - ok");

		System.loadLibrary(OSUtils.getLibName());

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		_log.info("GUI properties set - ok");

		SplashWindow.showSplash();

		_log.info("Loading config - start");

		ConfigParser.getInstance();

		_log.info("Loading config - ok");

		ThreadPoolManager.getInstance();

		NetworkProfiles.getInstance();

		ImageStatic.getInstance();

		ListenerSystem.getInstance();

		CLoader.getInstance();

		try
		{
			MainForm.init();
			_log.info("MainForm: init ok");
		}
		catch(Exception e)
		{
			_log.info("MainForm: init fail" + e, e);
		}

		PartTypeManager.getInstance();
		ProtocolManager.getInstance();

		Runtime.getRuntime().addShutdownHook(new Shutdown());

		MainForm.getInstance().startMemoryBarTask();

		MainForm.getInstance().updateTitle();

		MainForm.getInstance().setVisible(true);

		MainForm.getInstance().updateVisible();

		_log.info(String.format("Load %d classes.", CLoader.getInstance().size()));

		for(Protocol p : ProtocolManager.getInstance().getProtocols())
			_log.info(String.format("Load %s protocol.", p.getName()));

		SplashWindow.hideSplash();
	}

	@Deprecated
	public static MainForm getForm()
	{
		return MainForm.getInstance();
	}
}
