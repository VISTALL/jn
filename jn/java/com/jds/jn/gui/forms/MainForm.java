package com.jds.jn.gui.forms;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Arrays;
import java.util.concurrent.ScheduledFuture;

import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.Jn;
import com.jds.jn.config.RValues;
import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.gui.dialogs.ExceptionDialog;
import com.jds.jn.gui.listeners.HideWatcher;
import com.jds.jn.gui.listeners.WindowsAdapter;
import com.jds.jn.gui.panels.*;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.session.Session;
import com.jds.jn.statics.RibbonActions;
import com.jds.jn.statics.TabRibbonActions;
import com.jds.jn.util.*;
import com.jds.swing.JTrayIcon;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sun.awt.AWTUtilities;
import org.jvnet.flamingo.ribbon.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 03/01/2010
 * Time: 22:31:09
 */
public class MainForm extends JRibbonFrame
{
	private JPanel _panel1;
	private JTabbedPane _manTab;
	private JProgressBar _memoryBar;
	private JButton _gcButton;
	private JButton _exceptionBtn;
	private JProgressBar _progressBar1;

	private ConsolePane _consolePane = new ConsolePane();
	private ViewTabbedPane _viewPane = new ViewTabbedPane();

	private JTrayIcon _trayIcon;
	private ScheduledFuture _future;

	private RibbonContextualTaskGroup _sessionGroup;

	public MainForm()
	{
		super("Jn");

		$$$setupUI$$$();
		add($$$getRootComponent$$$());
		initTray();
		setResizable(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE); //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		try
		{
			setIconImages(Arrays.asList(ImageIO.read(getClass().getResource("/com/jds/jn/resources/nimg/Jn24.png")), ImageIO.read(getClass().getResource("/com/jds/jn/resources/nimg/Jn.png"))));
		}
		catch (IOException ignored)
		{
			ignored.printStackTrace();
		}

		RibbonApplicationMenu menu = new RibbonApplicationMenu();
		RibbonActions.ribbonMenu(menu);
		getRibbon().setApplicationMenu(menu);

		RibbonTask f = new RibbonTask(Bundle.getString("Main"), new AbstractRibbonBand[]{
				RibbonActions.files(),
				RibbonActions.listeners()
		});
		getRibbon().addTask(f);

		RibbonTask v = new RibbonTask(Bundle.getString("View"), new AbstractRibbonBand[]{RibbonActions.view()});
		getRibbon().addTask(v);

		RibbonTask s = new RibbonTask(Bundle.getString("Settings"), new AbstractRibbonBand[]{RibbonActions.settings()});
		getRibbon().addTask(s);

		TabRibbonActions tabs = new TabRibbonActions();
		_sessionGroup = tabs.getGroup();

		getRibbon().addContextualTaskGroup(_sessionGroup);

		addWindowListener(new WindowsAdapter());

		_gcButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.GC, _gcButton);
			}
		});

		_exceptionBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.EXCEPTION_WINDOW, e.getSource());
			}
		});

		_manTab.addTab(Bundle.getString("Console"), _consolePane);
		_manTab.addTab(Bundle.getString("View2"), _viewPane);
	}

	private void initTray()
	{
		if (!SystemTray.isSupported())
		{
			return;
		}

		try
		{
			SystemTray st = SystemTray.getSystemTray();
			_trayIcon = new JTrayIcon(ImageIO.read(getClass().getResource("/com/jds/jn/resources/nimg/Jn24.png")));
			_trayIcon.setImageAutoSize(true);
			_trayIcon.setToolTip(Version.current());


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
		catch (Exception e)
		{
			warn("Exception tray icon", e);
		}
	}

	public synchronized void updateMemoryBar()
	{
		//SwingUtilities.invokeLater(new Runnable()
		//{
		//	public void run()
		{
			MemoryUsage hm = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

			long use = hm.getUsed() / 1048576;
			long max = hm.getMax() / 1048576;
			byte persents = (byte) ((use * 100) / max);

			_memoryBar.setString(use + " MB of " + max + " MB");
			_memoryBar.setValue(persents);
			_memoryBar.setToolTipText("Total heap size: " + max + " MB Used: " + use + "MB");
		}
		//});
	}

	public void showSession(Session s)
	{
		sessionMenu(true);
		getViewTabbedPane().showSession(s);
	}

	public void sessionMenu(boolean b)
	{
		getRibbon().setVisible(_sessionGroup, b);
	}

	public void closeSessionTab(ViewPane vp)
	{
		if (vp == null || vp.getSession() == null)
		{
			return;
		}

		vp.getSession().close();
		getViewTabbedPane().remove(vp);

		if (getViewTabbedPane().sizeAll() == 0)
		{
			Jn.getInstance().sessionMenu(false);
		}
	}

	public ConsolePane getConsolePane()
	{
		return _consolePane;
	}

	public JTabbedPane getTabs()
	{
		return _manTab;
	}

	public void info(String text)
	{
		_consolePane.addLog("[" + Bundle.getString("Info") + "] " + text);
	}

	public void enableException()
	{
		_future = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{

			@Override
			public void run()
			{
				_exceptionBtn.setVisible(!_exceptionBtn.isVisible());
			}
		}, 1000, 1000);
	}

	public void disableException()
	{
		if (_future != null)
		{
			_future.cancel(true);
		}

		_exceptionBtn.setVisible(false);
	}

	public void warn(String text)
	{
		warn(text, null);
	}

	public void warn(String text, Throwable e)
	{
		if(e != null)
		{
			_consolePane.addLog("[" +  Bundle.getString("Error") +"] " + text + e);
		}
		else
		{
			_consolePane.addLog("[" +  Bundle.getString("Error") +"] "  + text);
		}

		if (e != null)
		{
			e.printStackTrace();

			enableException();
			ExceptionDialog.getInstance().addException(e);			
		}
	}

	public ViewTabbedPane getViewTabbedPane()
	{
		return _viewPane;
	}

	public void updateTitle()
	{
		NetworkProfile prof = NetworkProfiles.getInstance().active();

		if (prof != null)
		{
			setTitle( String.format("%s - [%s]", Version.current(), prof.getName()));
		}
		else
		{
			setTitle(Version.current());
		}
	}

	public JProgressBar getProgressBar()
	{
		return _progressBar1;
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
		_panel1.setLayout(new FormLayout("fill:d:grow", "center:394px:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		_manTab = new JTabbedPane();
		_manTab.setRequestFocusEnabled(true);
		_manTab.setTabLayoutPolicy(1);
		_manTab.setTabPlacement(3);
		CellConstraints cc = new CellConstraints();
		_panel1.add(_manTab, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL, new Insets(5, 5, 5, 5)));
		final JToolBar toolBar1 = new JToolBar();
		toolBar1.setEnabled(true);
		toolBar1.setFloatable(false);
		toolBar1.setMargin(new Insets(2, 2, 2, 2));
		_panel1.add(toolBar1, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
		_exceptionBtn = new JButton();
		_exceptionBtn.setBorderPainted(false);
		_exceptionBtn.setFocusPainted(false);
		_exceptionBtn.setHorizontalTextPosition(0);
		_exceptionBtn.setIcon(new ImageIcon(getClass().getResource("/com/jds/jn/resources/nimg/error.png")));
		_exceptionBtn.setText("");
		_exceptionBtn.setVisible(false);
		toolBar1.add(_exceptionBtn);
		final Spacer spacer1 = new Spacer();
		toolBar1.add(spacer1);
		_progressBar1 = new JProgressBar();
		_progressBar1.setEnabled(true);
		_progressBar1.setMaximumSize(new Dimension(200, 20));
		_progressBar1.setMinimumSize(new Dimension(200, 20));
		_progressBar1.setPreferredSize(new Dimension(200, 20));
		_progressBar1.setStringPainted(true);
		_progressBar1.setVerifyInputWhenFocusTarget(true);
		_progressBar1.setVisible(false);
		toolBar1.add(_progressBar1);
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
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return _panel1;
	}
}
