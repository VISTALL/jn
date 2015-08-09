package com.jds.jn.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import com.jds.jn.Jn;
import com.jds.jn.config.LastFiles;
import com.jds.jn.config.RValues;
import com.jds.jn.gui.dialogs.EnterNameDialog;
import com.jds.jn.gui.dialogs.NetworkSettingsDialog;
import com.jds.jn.gui.dialogs.ProgramSettingsDialog;
import com.jds.jn.gui.forms.ConsoleForm;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.logs.Reader;
import com.jds.jn.logs.Writer;
import com.jds.jn.network.listener.ListenerSystem;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.session.Session;
import com.jds.jn.util.ImageStatic;
import com.jds.jn.util.RibbonActions;
import com.jds.jn.util.RunnableImpl;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.util.Util;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 9:00:28
 */
public class JActionListener
{
	private static final Map<ListenerType, Map<ReceiveType, Boolean>> _status = new HashMap<ListenerType, Map<ReceiveType, Boolean>>();

	static
	{
		_status.put(ListenerType.Auth_Server, new HashMap<ReceiveType, Boolean>());
		_status.put(ListenerType.Game_Server, new HashMap<ReceiveType, Boolean>());
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
					Jn.getForm().warn("Not can start/stop", e);
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
			case GC:
				System.gc();
				break;
			case PROGRAM_SETTINGS:
				ProgramSettingsDialog p = new ProgramSettingsDialog();
				p.setVisible(true);
				break;
			case NETWORK_SETTINGS:
				NetworkSettingsDialog di = new NetworkSettingsDialog();
				di.setVisible(true);
				break;
			case HIDE_SHOW:
				ThreadPoolManager.getInstance().execute(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						Jn.getForm().setVisible(!Jn.getForm().isVisible());
					}
				});
				break;
			case CONSOLE_WINDOW:
				ConsoleForm.getInstance().setVisible(!ConsoleForm.getInstance().isVisible());
				break;
			case CLEAR_LAST_FILES:
				LastFiles.clearFiles();
				break;
			case OPEN_SELECT_LOG_FILE:
				final File files = (File) arg[0];
				if (files == null)
				{
					return;
				}

				ThreadPoolManager.getInstance().execute(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						openSession(files);
					}
				});
				break;
			case OPEN_SELECT_FILE:
				openSessionAndAddFile(arg);
				break;
			case ADD_PROFILE:
				final NetworkSettingsDialog d = (NetworkSettingsDialog) arg[0];
				EnterNameDialog dial = new EnterNameDialog(d, "Enter profile name");
				if (dial.showToWrite())
				{
					NetworkProfiles.getInstance().newProfile(dial.getText(), ReceiveType.JPCAP);
					d.load();
				}
				break;
			case SAVE_SESSION:
				Writer.getInstance().chooseDialog();
				break;
		}
	}

	public static void openSession(File file)
	{
		try
		{
			LastFiles.addLastFile(file.getAbsolutePath());
			RValues.LAST_FOLDER.setVal(file.getAbsolutePath().replace(file.getName(), ""));
			Reader.getInstance().read(file, Reader.DEFAULT_READER_LISTENER);
		}
		catch (Exception e)
		{
			MainForm.getInstance().warn("Failed to open file " + e.getMessage(), e);
		}
	}

	public static void openSessionAndAddFile(Object[] args)
	{
		final File file = (File) args[0];
		if (file == null)
		{
			return;
		}

		try
		{
			LastFiles.addLastFile(file.getAbsolutePath());
			RValues.LAST_FOLDER.setVal(file.getAbsolutePath().replace(file.getName(), ""));

			Session session = new Session(ListenerType.Game_Server, Util.positiveRandom());

			FileInputStream fileInputStream = new FileInputStream(file);
			int available = fileInputStream.available();
			byte[] bytes = new byte[available];
			int read = fileInputStream.read(bytes);
			assert read == available;

			Protocol protocol = session.getProtocol();
			Collection<PacketInfo> famillyPackets = protocol.getFamillyPackets(PacketType.CLIENT);
			PacketInfo packetInfo = null;
			if(!famillyPackets.isEmpty())
			{
				packetInfo = famillyPackets.iterator().next();
			}
			DecryptedPacket packet = new DecryptedPacket(session, PacketType.CLIENT, bytes, System.currentTimeMillis(), protocol, true, packetInfo,
					file.getName());

			session.receiveQuitPacket(packet, true, true);

			Reader.DEFAULT_READER_LISTENER.onFinish(session, file);
		}
		catch (Exception e)
		{
			MainForm.getInstance().warn("Failed to open file " + e.getMessage(), e);
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
