package com.jds.jn.util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonPanel;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import com.jds.jn.config.LastFiles;
import com.jds.jn.config.RValues;
import com.jds.jn.data.xml.parser.ProtocolParser;
import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.gui2.FindPacket.listeners.FPOpenListener;
import com.jds.jn.gui2.PacketMassAnallize.PacketMassAnalysisDialog;
import com.jds.jn.logs.Reader;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.swing.SimpleResizableIcon;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 03/01/2010
 * Time: 23:57:41
 */
public abstract class RibbonActions
{
	public static JRibbonBand settings()
	{
		JRibbonBand animationBand = new JRibbonBand(Bundle.getString("Settings"), new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 50, 50));
		animationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(animationBand));

		final JCommandButton jCommandButton = new JCommandButton(Bundle.getString("Program"), ImageStatic.PROGRAM_SET_48x48);
		animationBand.addCommandButton(jCommandButton, RibbonElementPriority.TOP);
		jCommandButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.PROGRAM_SETTINGS, jCommandButton);
			}
		});

		final JCommandButton networkS = new JCommandButton(Bundle.getString("Network"), ImageStatic.NETWORK_SET_48x48);
		networkS.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.NETWORK_SETTINGS, networkS);
			}
		});

		animationBand.addCommandButton(networkS, RibbonElementPriority.TOP);

		return animationBand;
	}

	public static JRibbonBand protocolActions()
	{
		JRibbonBand animationBand = new JRibbonBand(Bundle.getString("Protocols"), new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 50, 50));
		animationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(animationBand));

		final JCommandButton jCommandButton = new JCommandButton(Bundle.getString("ReloadProtocol"), ImageStatic.REFRESH_48x48);
		animationBand.addCommandButton(jCommandButton, RibbonElementPriority.TOP);
		jCommandButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ProtocolParser.getInstance().reload();
			}
		});

		return animationBand;
	}

	public static JRibbonBand files()
	{
		JRibbonBand animationBand = new JRibbonBand(Bundle.getString("Files"), new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 30, 30));
		animationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(animationBand));

		JCommandButton opnFile = new JCommandButton(Bundle.getString("Open"), ImageStatic.FILE_48x48);
		opnFile.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Reader.getInstance().showChooseDialog();
			}
		});

		JCommandButton searchPacket = new JCommandButton(Bundle.getString("SearchPacket"), ImageStatic.SEARCH_PACKET_48x48);
		searchPacket.addActionListener(FPOpenListener.STATIC);

		animationBand.addCommandButton(opnFile, RibbonElementPriority.TOP);
		animationBand.addCommandButton(searchPacket, RibbonElementPriority.TOP);
		return animationBand;
	}


	public static JCommandButton LISTENER_1;
	public static JCommandButton LISTENER_2;

	public static JRibbonBand listeners()
	{
		JRibbonBand animationBand = new JRibbonBand(Bundle.getString("Listeners"), new SimpleResizableIcon(RibbonElementPriority.TOP, 60, 60));
		animationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(animationBand));

		LISTENER_1 = new JCommandButton("N.1", ImageStatic.START_48x48);
		LISTENER_1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.LISTENER_1, LISTENER_1);
			}
		});

		LISTENER_2 = new JCommandButton("N.2", ImageStatic.START_48x48);
		LISTENER_2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.LISTENER_2, LISTENER_2);
			}
		});

		animationBand.addCommandButton(LISTENER_1, RibbonElementPriority.TOP);
		animationBand.addCommandButton(LISTENER_2, RibbonElementPriority.TOP);

		animationBand.startGroup();


		JRadioButton jpcap = new JRadioButton(" - Jpcap");
		jpcap.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.active(ReceiveType.JPCAP);
			}
		});

		JRadioButton proxy = new JRadioButton(" - " + Bundle.getString("ProxyTab"));
		proxy.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.active(ReceiveType.PROXY);
			}
		});

		if (RValues.ACTIVE_TYPE.asReceiveType() == ReceiveType.PROXY)
		{
			proxy.setSelected(true);
		}
		else
		{
			jpcap.setSelected(true);
		}

		ButtonGroup b = new ButtonGroup();
		b.add(jpcap);
		b.add(proxy);

		animationBand.addRibbonComponent(new JRibbonComponent(new JLabel(Bundle.getString("Type"))));
		animationBand.addRibbonComponent(new JRibbonComponent(jpcap));
		animationBand.addRibbonComponent(new JRibbonComponent(proxy));

		return animationBand;
	}

	public static void ribbonMenu(RibbonApplicationMenu m)
	{
		final RibbonApplicationMenuEntryPrimary fopn = new RibbonApplicationMenuEntryPrimary(ImageStatic.FILE_48x48, Bundle.getString("Open"), new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Reader.getInstance().showChooseDialog();
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		final RibbonApplicationMenuEntryPrimary analysis = new RibbonApplicationMenuEntryPrimary(ImageStatic.FILE_48x48, "Mass Analysis", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new PacketMassAnalysisDialog();
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		RibbonApplicationMenuEntryPrimary h = new RibbonApplicationMenuEntryPrimary(ImageStatic.HELP_48x48, Bundle.getString("Help"), new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//TODO HELP
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		RibbonApplicationMenuEntryPrimary io = new RibbonApplicationMenuEntryPrimary(ImageStatic.INFO_48x48, Bundle.getString("Info"), new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//TODO INFO
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		RibbonApplicationMenuEntryPrimary amEntryExit = new RibbonApplicationMenuEntryPrimary(ImageStatic.EXIT_48x48, Bundle.getString("Exit"), new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		m.addMenuEntry(fopn);
		m.addMenuEntry(analysis);
		m.addMenuEntry(h);
		m.addMenuEntry(io);
		m.addMenuEntry(amEntryExit);

		m.setDefaultCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback()
		{
			public void menuEntryActivated(JPanel targetPanel)
			{
				targetPanel.removeAll();

				JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(CommandButtonDisplayState.MEDIUM);
				openHistoryPanel.addButtonGroup(Bundle.getString("LastFiles"));

				for (String st : LastFiles.getLastFiles())
				{
					final File file = new File(st);
					if (!file.exists())
					{
						continue;
					}

					JCommandButton historyButton = new JCommandButton(file.getName(), ImageStatic.DOC_24x24);
					historyButton.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							JActionListener.handle(JActionEvent.OPEN_SELECT_FILE, e.getSource(), file);
						}
					});

					historyButton.setHorizontalAlignment(2);
					openHistoryPanel.addButtonToLastGroup(historyButton);
				}

				openHistoryPanel.setMaxButtonColumns(1);
				//openHistoryPanel.setMaxButtonRows(10);
				targetPanel.setLayout(new BorderLayout());
				JScrollPane pane = new JScrollPane(openHistoryPanel);
				targetPanel.add(pane, "Center");
			}
		});

		RibbonApplicationMenuEntryFooter f = new RibbonApplicationMenuEntryFooter(ImageStatic.PROGRAM_SET_24x24, "Program Settings", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.PROGRAM_SETTINGS, e.getSource());
			}
		});

		final RibbonApplicationMenuEntryFooter f2 = new RibbonApplicationMenuEntryFooter(ImageStatic.NETWORK_SET_24x24, "Network Settings", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.NETWORK_SETTINGS, e.getSource());
			}
		});

		final RibbonApplicationMenuEntryFooter clear = new RibbonApplicationMenuEntryFooter(ImageStatic.NETWORK_SET_24x24, "Clear", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.CLEAR_LAST_FILES, e.getSource());
			}
		});

		m.addFooterEntry(clear);
		m.addFooterEntry(f);
		m.addFooterEntry(f2);
	}

}
