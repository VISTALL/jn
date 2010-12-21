package com.jds.jn.gui.forms;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.concurrent.ScheduledFuture;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.config.RValues;
import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.gui.listeners.HideWatcher;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui.panels.ViewTabbedPane;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.session.Session;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.statics.RibbonActions;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.RunnableImpl;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.util.version_control.Version;
import com.jds.swing.JTrayIcon;
import com.sun.awt.AWTUtilities;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 03/01/2010
 * Time: 22:31:09
 */
public class MainForm extends JRibbonFrame
{
	private static MainForm _instance;
	private static final Logger _log = Logger.getLogger(MainForm.class);

	public static void init() throws Exception
	{
		_instance = new MainForm();
	}

	public static MainForm getInstance()
	{
		return _instance;
	}

	private JPanel _panel1;

	private JProgressBar _memoryBar;
	private JButton _gcButton;
	private JButton _consoleBtn;
	private JProgressBar _readerProgress;
	private ViewTabbedPane _sessionTabbedPane;

	private JTrayIcon _trayIcon;

	private ScheduledFuture<?> _memoryBarTask;

	//private RibbonContextualTaskGroup _sessionGroup;

	public MainForm() throws Exception
	{
		super("Jn");

		$$$setupUI$$$();
		add($$$getRootComponent$$$());
		//initTray();
		setResizable(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE); //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(500, 800));
		setExtendedState(JFrame.MAXIMIZED_HORIZ);

		setMainAppIcon(ImageStatic.JN_ICON_32);
		setIconImage(ImageIO.read(getClass().getResource("/com/jds/jn/resources/nimg/icon_64.png")));

		RibbonApplicationMenu menu = new RibbonApplicationMenu();
		RibbonActions.ribbonMenu(menu);
		getRibbon().setApplicationMenu(menu);


		getRibbon().addTask(new RibbonTask(Bundle.getString("Main"), RibbonActions.files(), RibbonActions.listeners()));
		getRibbon().addTask(new RibbonTask(Bundle.getString("Settings"), RibbonActions.settings()));

		//TabRibbonActions tabs = new TabRibbonActions();
		//_sessionGroup = tabs.getGroup();
		//getRibbon().addContextualTaskGroup(_sessionGroup);

		//addWindowListener(new WindowsAdapter());

		_gcButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.GC, _gcButton);
			}
		});

		_consoleBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.CONSOLE_WINDOW, e.getSource());
			}
		});

	}

	private void initTray()
	{
		if(!SystemTray.isSupported())
		{
			return;
		}

		try
		{
			SystemTray st = SystemTray.getSystemTray();
			_trayIcon = new JTrayIcon(ImageIO.read(getClass().getResource("/com/jds/jn/resources/nimg/icon_16.png")));
			_trayIcon.setImageAutoSize(true);
			_trayIcon.setToolTip(Version.CURRENT.toString());


			JPopupMenu pm = new JPopupMenu();

			final JMenuItem restore = new JMenuItem("Hide");
			restore.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					JActionListener.handle(JActionEvent.HIDE_SHOW, restore);
				}
			});
			pm.add(restore);

			pm.addSeparator();

			JMenuItem itemAbout = new JMenuItem("About");
			//itemAbout.setActionCommand(MainForm.MainAction.ABOUT.name());
			//itemAbout.addActionListener(_listener);
			pm.add(itemAbout);


			final JMenuItem exit = new JMenuItem("Exit");
			exit.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					JActionListener.handle(JActionEvent.EXIT, exit);
				}
			});
			pm.add(exit);

			_trayIcon.setJPopupMenu(pm);

			_trayIcon.addMouseListener(new HideWatcher(restore));

			st.add(_trayIcon);
		}
		catch(Exception e)
		{
			_log.info("Exception: " + e, e);
		}
	}

	public void startMemoryBarTask()
	{
		_memoryBarTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				updateMemoryBar();
			}
		}, 5000, 5000);
	}

	public void stopMemoryBarTask()
	{
		if(_memoryBarTask != null)
		{
			_memoryBarTask.cancel(true);
			_memoryBarTask = null;
		}
	}

	public void updateMemoryBar()
	{
		MemoryUsage hm = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

		long use = hm.getUsed() / 1048576;
		long max = hm.getMax() / 1048576;
		byte persents = (byte) ((use * 100) / max);

		_memoryBar.setString(use + " MB of " + max + " MB");
		_memoryBar.setValue(persents);
		_memoryBar.setToolTipText("Total heap size: " + max + " MB Used: " + use + "MB");
	}

	//TODO
	public void showSession(Session s)
	{
		getViewTabbedPane().addTab(s);

		s.onShow();
		///
		getRibbon().addContextualTaskGroup(s.getRibbonGroup());
		getViewTabbedPane().fireTabsChanged();
	}

	public void closeSessionTab(ViewPane vp)
	{
		if(vp == null || vp.getSession() == null)
		{
			return;
		}

		vp.getSession().close();
		getRibbon().removeContextualTaskGroup(vp.getSession().getRibbonGroup());
		getViewTabbedPane().remove(vp);
		getViewTabbedPane().fireTabsChanged();
	}

	public void info(String text)
	{
		_log.info(text);
	}

	public void warn(String text)
	{
		warn(text, null);
	}

	public void warn(String text, Throwable e)
	{
		_log.info(text, e);
	}

	public ViewTabbedPane getViewTabbedPane()
	{
		return _sessionTabbedPane;
	}

	public void updateTitle()
	{
		NetworkProfile prof = NetworkProfiles.getInstance().active();

		if(prof != null)
		{
			setTitle(String.format("%s - [%s]", Version.CURRENT, prof.getName()));
		}
		else
		{
			setTitle(Version.CURRENT.toString());
		}
	}

	public JProgressBar getProgressBar()
	{
		return _readerProgress;
	}

	public void updateVisible()
	{
		AWTUtilities.setWindowOpacity(this, RValues.MAIN_VISIBLE.asFloat());
	}


	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$()
	{
		_panel1 = new JPanel();
		_panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		final JToolBar toolBar1 = new JToolBar();
		toolBar1.setEnabled(true);
		toolBar1.setFloatable(false);
		toolBar1.setMargin(new Insets(2, 2, 2, 2));
		_panel1.add(toolBar1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
		_consoleBtn = new JButton();
		_consoleBtn.setBorderPainted(false);
		_consoleBtn.setFocusPainted(false);
		_consoleBtn.setHorizontalTextPosition(0);
		_consoleBtn.setIcon(new ImageIcon(getClass().getResource("/com/jds/jn/resources/images/file.png")));
		_consoleBtn.setText("");
		_consoleBtn.setVisible(true);
		toolBar1.add(_consoleBtn);
		final Spacer spacer1 = new Spacer();
		toolBar1.add(spacer1);
		_readerProgress = new JProgressBar();
		_readerProgress.setEnabled(true);
		_readerProgress.setMaximumSize(new Dimension(200, 20));
		_readerProgress.setMinimumSize(new Dimension(200, 20));
		_readerProgress.setPreferredSize(new Dimension(200, 20));
		_readerProgress.setStringPainted(true);
		_readerProgress.setVerifyInputWhenFocusTarget(true);
		_readerProgress.setVisible(false);
		toolBar1.add(_readerProgress);
		final Spacer spacer2 = new Spacer();
		toolBar1.add(spacer2);
		_memoryBar = new JProgressBar();
		_memoryBar.setMaximumSize(new Dimension(200, 20));
		_memoryBar.setMinimumSize(new Dimension(200, 20));
		_memoryBar.setPreferredSize(new Dimension(200, 20));
		_memoryBar.setStringPainted(true);
		toolBar1.add(_memoryBar);
		_gcButton = new JButton();
		_gcButton.setBorderPainted(false);
		_gcButton.setContentAreaFilled(true);
		_gcButton.setDefaultCapable(false);
		_gcButton.setFocusPainted(false);
		_gcButton.setIcon(new ImageIcon(getClass().getResource("/com/jds/jn/resources/nimg/gc.png")));
		_gcButton.setRequestFocusEnabled(false);
		_gcButton.setRolloverEnabled(true);
		_gcButton.setText("");
		toolBar1.add(_gcButton);
		_sessionTabbedPane = new ViewTabbedPane();
		_panel1.add(_sessionTabbedPane.$$$getRootComponent$$$(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return _panel1;
	}
}
