package com.jds.jn.gui;

import com.jds.jn.Jn;
import javolution.util.FastMap;
import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.dialogs.ExceptionDialog;
import com.jds.jn.gui.dialogs.NetworkSettingsDialog;
import com.jds.jn.gui.dialogs.ProgramSettingsDialog;
import com.jds.jn.gui.panels.ConsolePane;
import com.jds.jn.gui.panels.ViewTabbedPane;
import com.jds.jn.logs.Reader;
import com.jds.jn.logs.Writer;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.rconfig.LastFiles;
import com.jds.jn.rconfig.RValues;
import com.jds.jn.session.Session;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.statics.RibbonActions;
import com.jds.jn.util.ThreadPoolManager;
import org.jvnet.flamingo.common.JCommandButton;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 9:00:28
 */
public class JActionListener
{
	private static final FastMap<ListenerType, FastMap<ReceiveType, Boolean>> _status = new FastMap<ListenerType, FastMap<ReceiveType, Boolean>>();

	static
	{
		_status.put(ListenerType.Auth_Server, new FastMap<ReceiveType, Boolean>());
		_status.put(ListenerType.Game_Server, new FastMap<ReceiveType, Boolean>());
	}

	public static void active(ReceiveType type)
	{
		RValues.ACTIVE_TYPE.setVal(type.name());

		boolean bool = getStatus(ListenerType.Auth_Server, type);

		if (!bool)
		{
			RibbonActions.LISTENER_1.setIcon(ImageStatic.START_48x48);
		}
		else
		{
			RibbonActions.LISTENER_1.setIcon(ImageStatic.STOP_48x48);
		}

		bool = getStatus(ListenerType.Game_Server, type);

		if (!bool)
		{
			RibbonActions.LISTENER_2.setIcon(ImageStatic.START_48x48);
		}
		else
		{
			RibbonActions.LISTENER_2.setIcon(ImageStatic.STOP_48x48);
		}
	}

	public static void handle(JActionEvent event, Object sender, Object... arg)
	{
		switch (event)
		{
			case LISTENER_1:
			case LISTENER_2:
				ListenerType type = event.type();
				JCommandButton b1 = (JCommandButton) sender;

				ReceiveType receive = RValues.ACTIVE_TYPE.asReceiveType();

				try
				{
					ListenerSystem.getInstance().init();
				}
				catch (IOException e)
				{
					Jn.getInstance().warn("Not can start/stop", e);
					return;
				}

				if (getStatus(type, receive))
				{
					ListenerSystem.getInstance().stop(receive, type);

					b1.setIcon(ImageStatic.START_48x48);
					setStatus(receive, type, false);
				}
				else
				{
					ListenerSystem.getInstance().start(receive, type);

					b1.setIcon(ImageStatic.STOP_48x48);
					setStatus(receive, type, true);
				}

				break;
			case CONSOLE_TAB:
				ConsolePane pane = Jn.getInstance().getConsolePane();
				JTabbedPane tabs = Jn.getInstance().getTabs();
				JCheckBox cbox = (JCheckBox) sender;
				if (!cbox.isSelected())
				{
					tabs.remove(pane);
				}
				else
				{
					tabs.addTab("Console", pane);
				}
				break;
			case VIEW_TAB:
				ViewTabbedPane p2 = Jn.getInstance().getViewTabbedPane();
				JTabbedPane t2 = Jn.getInstance().getTabs();
				JCheckBox b2 = (JCheckBox) sender;
				if (!b2.isSelected())
				{
					t2.remove(p2);
				}
				else
				{
					t2.addTab("View", p2);
				}
				break;
			case GC:
				System.gc();
				break;
			case PROGRAM_SETTINGS:
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						ProgramSettingsDialog di = new ProgramSettingsDialog();
						di.setVisible(true);
					}
				});
				break;
			case NETWORK_SETTINGS:
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						NetworkSettingsDialog di = new NetworkSettingsDialog();
						di.setVisible(true);
					}
				});
				break;
			case HIDE_SHOW:
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						Jn.getInstance().setVisible(!Jn.getInstance().isVisible());
					}
				});
				break;
			case EXCEPTION_WINDOW:
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						ExceptionDialog.getInstance().setVisible(true);
					}
				});
				break;
			case CLEAR_LAST_FILES:
				LastFiles.clearFiles();
				break;
			case OPEN_FILE:
				final JFileChooser chooser = new JFileChooser(RValues.LAST_FOLDER.asString());

				chooser.setFileFilter(new FileFilter()
				{

					@Override
					public boolean accept(File f)
					{
						return f.isDirectory() || f.isFile() && f.getName().endsWith(".jnl");
					}

					@Override
					public String getDescription()
					{
						return "Jn log format (.jnl)";
					}
				});
				final int returnVal = chooser.showOpenDialog(Jn.getInstance());

				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					ThreadPoolManager.getInstance().execute(new Runnable()
					{
						public void run()
						{
							openSession(chooser.getSelectedFile());
						}
					});
				}
				break;
			case OPEN_SELECT_FILE:
				final File files = (File) arg[0];
				if (files == null)
				{
					return;
				}

				ThreadPoolManager.getInstance().execute(new Runnable()
				{
					public void run()
					{
						openSession(files);
					}
				});
				break;
			case ADD_PROFILE:
				final NetworkSettingsDialog d = (NetworkSettingsDialog) arg[0];
				EnterNameDialog dial = new EnterNameDialog(d, "Enter profile name");
				if (dial.showToWrite())
				{
					NetworkProfiles.getInstance().newProfile(dial.getText());
					d.load();
				}
				break;
			case SAVE_SESSION:
				final JFileChooser c = new JFileChooser(RValues.LAST_FOLDER.asString());

				c.setFileFilter(new FileFilter()
				{

					@Override
					public boolean accept(File f)
					{
						return f.isDirectory() || f.isFile() && f.getName().endsWith(".jnl");
					}

					@Override
					public String getDescription()
					{
						return "Jn log format (.jnl)";
					}
				});

				final int r = c.showSaveDialog(Jn.getInstance());

				if (r == JFileChooser.APPROVE_OPTION)
				{
					Thread t = new Thread()
					{
						@Override
						public void run()
						{
							saveSession(c.getSelectedFile(), c.getFileFilter());
						}
					};
					t.start();
				}
				break;
		}
	}

	public static void saveSession(File file, FileFilter filter)
	{

		try
		{
			Session session = Jn.getInstance().getViewTabbedPane().getCurrentViewPane().getSession();

			if (session == null)
			{
				return;
			}

			Writer.write(file, filter, session);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void openSession(File file)
	{
		try
		{
			LastFiles.addLastFile(file.getAbsolutePath());
			RValues.LAST_FOLDER.setVal(file.getAbsolutePath().replace(file.getName(), ""));
			Reader.read(file.getAbsolutePath());
		}
		catch (Exception e)
		{
			Jn.getInstance().warn("Failed to open file " + e.getMessage(), e);
		}
	}

	public static void setStatus(ReceiveType receive, ListenerType type, boolean s)
	{
		_status.get(type).put(receive, s);
	}

	public static boolean getStatus(ListenerType type, ReceiveType receive)
	{
		Boolean s = _status.get(type).get(receive);
		if (s == null)
		{
			setStatus(receive, type, false);
			return false;
		}
		else
		{
			return _status.get(type).get(receive);
		}
	}
}
