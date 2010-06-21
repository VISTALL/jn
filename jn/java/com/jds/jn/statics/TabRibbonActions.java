package com.jds.jn.statics;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jds.jn.Jn;
import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.util.Bundle;
import com.jds.swing.SimpleResizableIcon;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.ribbon.resize.CoreRibbonResizePolicies;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 13:51:00
 */
public class TabRibbonActions
{
	public RibbonContextualTaskGroup getGroup()
	{
		RibbonContextualTaskGroup group = new RibbonContextualTaskGroup("", Color.green, getTask1());

		return group;
	}

	public RibbonTask getTask1()
	{
		JRibbonBand viewBand = new JRibbonBand(Bundle.getString("View"), new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 8, 8));
		viewBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(viewBand));


		final JCheckBox box = new JCheckBox(Bundle.getString("PacketList"));
		box.setSelected(true);
		box.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ViewPane pane = Jn.getInstance().getViewTabbedPane().getCurrentViewPane();
				if (pane == null)
				{
					return;
				}
				if (!pane._packetList.IS_HIDE)
				{
					int index = pane.getIndex(pane._packetList);
					pane._packetAndSearch.removeTabAt(index);
					pane._packetList.IS_HIDE = true;
				}
				else
				{
					pane._packetAndSearch.addTab(Bundle.getString("PacketList"), pane._packetList);
					pane._packetList.IS_HIDE = false;
				}
			}
		});

		final JCheckBox box2 = new JCheckBox(Bundle.getString("FindPanel"));
		box2.setSelected(true);
		box2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ViewPane pane = Jn.getInstance().getViewTabbedPane().getCurrentViewPane();
				if (pane == null)
				{
					return;
				}
				if (!pane._searchPane.IS_HIDE)
				{
					int index = pane.getIndex(pane._searchPane);
					pane._packetAndSearch.removeTabAt(index);
					pane._searchPane.IS_HIDE = true;
				}
				else
				{
					pane._packetAndSearch.addTab(Bundle.getString("FindPanel"), pane._searchPane);
					pane._searchPane.IS_HIDE = false;
				}
			}
		});

		final JCheckBox box3 = new JCheckBox(Bundle.getString("Filter"));
		box3.setSelected(true);
		box3.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ViewPane pane = Jn.getInstance().getViewTabbedPane().getCurrentViewPane();
				if (pane == null)
				{
					return;
				}
				if (!pane._filterPane.IS_HIDE)
				{
					int index = pane.getIndex(pane._filterPane);
					pane._packetAndSearch.removeTabAt(index);
					pane._filterPane.IS_HIDE = true;
				}
				else
				{
					pane._packetAndSearch.addTab(Bundle.getString("Filter"), pane._filterPane);
					pane._filterPane.IS_HIDE = false;
				}
			}
		});

		final JCheckBox box4 = new JCheckBox(Bundle.getString("Info"));
		box4.setSelected(true);
		box4.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ViewPane pane = Jn.getInstance().getViewTabbedPane().getCurrentViewPane();
				if (pane == null)
				{
					return;
				}
				if (!pane._infoPane.IS_HIDE)
				{
					int index = pane.getIndex(pane._infoPane);
					pane._packetAndSearch.removeTabAt(index);
					pane._infoPane.IS_HIDE = true;
				}
				else
				{
					pane._packetAndSearch.addTab(Bundle.getString("Info"), pane._infoPane);
					pane._infoPane.IS_HIDE = false;
				}
			}
		});

		JRibbonComponent c;

		c = new JRibbonComponent(box);
		viewBand.addRibbonComponent(c);

		c = new JRibbonComponent(box2);
		viewBand.addRibbonComponent(c);

		c = new JRibbonComponent(box3);
		viewBand.addRibbonComponent(c);

		c = new JRibbonComponent(box4);
		viewBand.addRibbonComponent(c);


		JRibbonBand actionsBand = new JRibbonBand(Bundle.getString("Actions"), new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 15, 15));
		actionsBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(actionsBand));

		final JCommandButton jCommandButton = new JCommandButton(Bundle.getString("Save"), ImageStatic.SAVE_48x48);
		jCommandButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.SAVE_SESSION, jCommandButton);
			}
		});
		actionsBand.addCommandButton(jCommandButton, RibbonElementPriority.TOP);

		final JCommandButton jCommandButton2 = new JCommandButton(Bundle.getString("Close"), ImageStatic.EXIT_48x48);
		jCommandButton2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Jn.getInstance().closeSessionTab(Jn.getInstance().getViewTabbedPane().getCurrentViewPane());
			}
		});
		actionsBand.addCommandButton(jCommandButton2, RibbonElementPriority.TOP);

		RibbonTask task = new RibbonTask(Bundle.getString("SessionMenu"), viewBand, actionsBand);

		return task;
	}

}
