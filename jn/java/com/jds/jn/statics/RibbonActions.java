package com.jds.jn.statics;

import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.config.LastFiles;
import com.jds.jn.config.RValues;
import com.jds.swing.SimpleResizableIcon;
import org.jvnet.flamingo.common.CommandButtonDisplayState;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandButtonPanel;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.ribbon.resize.CoreRibbonResizePolicies;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 03/01/2010
 * Time: 23:57:41
 */
public abstract class RibbonActions
{
	public static JRibbonBand view()
	{
		JRibbonBand viewBand = new JRibbonBand("Tabs", new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 8, 8));
		viewBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(viewBand));

		final JCheckBox box = new JCheckBox("Console");
		box.setSelected(true);
		box.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.CONSOLE_TAB, box);
			}
		});

		JRibbonComponent c = new JRibbonComponent(box);
		viewBand.addRibbonComponent(c);

		final JCheckBox bb2 = new JCheckBox("View");
		bb2.setSelected(true);
		bb2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.VIEW_TAB, bb2);
			}
		});

		c = new JRibbonComponent(bb2);
		viewBand.addRibbonComponent(c);

		return viewBand;
	}

	public static JRibbonBand settings()
	{
		JRibbonBand animationBand = new JRibbonBand("Settings", new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 50, 50));
		animationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(animationBand));

		final JCommandButton jCommandButton = new JCommandButton("Program", ImageStatic.PROGRAM_SET_48x48);
		animationBand.addCommandButton(jCommandButton, RibbonElementPriority.TOP);
		jCommandButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.PROGRAM_SETTINGS, jCommandButton);
			}
		});

		final JCommandButton networkS = new JCommandButton("Network", ImageStatic.NETWORK_SET_48x48);
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

	public static JRibbonBand files()
	{
		JRibbonBand animationBand = new JRibbonBand("Files", new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 30, 30));
		animationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(animationBand));

		JCommandButton opnFile = new JCommandButton("Open", ImageStatic.FILE_48x48);
		opnFile.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.OPEN_FILE, e.getSource());
			}
		});
		animationBand.addCommandButton(opnFile, RibbonElementPriority.TOP);


		//ico = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/jds/jn/resources/nimg/fsave.png"), new Dimension(15, 15));
		//JCommandButton saveFile = new JCommandButton("Save", ico);
		//animationBand.addCommandButton(saveFile, RibbonElementPriority.TOP);

		return animationBand;
	}

	public static JCommandButton LISTENER_1;
	public static JCommandButton LISTENER_2;

	public static JRibbonBand listeners()
	{
		JRibbonBand animationBand = new JRibbonBand("Listeners", new SimpleResizableIcon(RibbonElementPriority.TOP, 60, 60));
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

		JRadioButton proxy = new JRadioButton(" - Proxy");
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

		animationBand.addRibbonComponent(new JRibbonComponent(new JLabel("Type:")));
		animationBand.addRibbonComponent(new JRibbonComponent(jpcap));
		animationBand.addRibbonComponent(new JRibbonComponent(proxy));

		return animationBand;
	}

	public static void ribbonMenu(RibbonApplicationMenu m)
	{
		final RibbonApplicationMenuEntryPrimary fopn = new RibbonApplicationMenuEntryPrimary(ImageStatic.FILE_48x48, "Open", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.OPEN_FILE, e.getSource());
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		RibbonApplicationMenuEntryPrimary h = new RibbonApplicationMenuEntryPrimary(ImageStatic.HELP_48x48, "Help", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//TODO HELP
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		RibbonApplicationMenuEntryPrimary io = new RibbonApplicationMenuEntryPrimary(ImageStatic.INFO_48x48, "Info", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//TODO INFO
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		RibbonApplicationMenuEntryPrimary amEntryExit = new RibbonApplicationMenuEntryPrimary(ImageStatic.EXIT_48x48, "Exit", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		}, JCommandButton.CommandButtonKind.ACTION_ONLY);

		m.addMenuEntry(fopn);
		m.addMenuEntry(h);
		m.addMenuEntry(io);
		m.addMenuEntry(amEntryExit);

		m.setDefaultCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback()
		{
			public void menuEntryActivated(JPanel targetPanel)
			{
				targetPanel.removeAll();

				JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(CommandButtonDisplayState.MEDIUM);
				openHistoryPanel.addButtonGroup("Last Files");

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
