package com.jds.jn.gui2.MainForm.ribbon.SessionMenu;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.session.Session;
import com.jds.jn.statics.ImageStatic;
import com.jds.jn.util.Bundle;
import com.jds.swing.SimpleResizableIcon;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:21:20/22.07.2010
 */
public class SessionRibbonTaskGroup  extends RibbonContextualTaskGroup
{
	private final Session _session;

	public SessionRibbonTaskGroup(final Session s)
	{
		this(s, "", Color.GREEN, mainSessionMenu(s));
	}

	private SessionRibbonTaskGroup(Session session, String title, Color hueColor, RibbonTask... tasks)
	{
		super(title, hueColor, tasks);
		_session = session;
	}

	public static RibbonTask mainSessionMenu(final Session session)
	{
		//------------------------- View Band -----------------------------------------------
		JRibbonBand viewBand = new JRibbonBand(Bundle.getString("View"), new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 8, 8));
		viewBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(viewBand));

		final JCheckBox packetList = new JCheckBox(Bundle.getString("PacketList"));
		packetList.setSelected(true);
		packetList.addActionListener(new PacketListActionListener(session));

	   	final JCheckBox search = new JCheckBox(Bundle.getString("FindPanel"));
		search.setSelected(true);
		search.addActionListener(new SearchActionListener(session));

		final JCheckBox filter = new JCheckBox(Bundle.getString("Filter"));
		filter.setSelected(true);
		filter.addActionListener(new FilterActionListener(session));

		final JCheckBox info = new JCheckBox(Bundle.getString("Info"));
		info.setSelected(true);
		info.addActionListener(new InfoActionListener(session));

		viewBand.addRibbonComponent(new JRibbonComponent(packetList));
		viewBand.addRibbonComponent(new JRibbonComponent(search));
		viewBand.addRibbonComponent(new JRibbonComponent(filter));
		viewBand.addRibbonComponent(new JRibbonComponent(info));

		//------------------------- Actions Band -----------------------------------------------
		JRibbonBand actionsBand = new JRibbonBand(Bundle.getString("Actions"), new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 15, 15));
		actionsBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(actionsBand));

		final JCommandButton saveButton = new JCommandButton(Bundle.getString("Save"), ImageStatic.SAVE_48x48);
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.handle(JActionEvent.SAVE_SESSION, saveButton);
			}
		});
		actionsBand.addCommandButton(saveButton, RibbonElementPriority.TOP);

		final JCommandButton closeButton = new JCommandButton(Bundle.getString("Close"), ImageStatic.EXIT_48x48);
		closeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainForm.getInstance().closeSessionTab(session.getViewPane());
			}
		});
		actionsBand.addCommandButton(closeButton, RibbonElementPriority.TOP);

		return new RibbonTask(Bundle.getString("SessionMenu"), viewBand, actionsBand);
	}


	public Session getSession()
	{
		return _session;
	}
}
