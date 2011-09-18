package com.jds.jn.gui2.MainForm.ribbon.SessionMenu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.ImageStatic;
import com.jds.swing.SimpleResizableIcon;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:21:20/22.07.2010
 */
public class SessionRibbonTaskGroup extends RibbonContextualTaskGroup
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

	public static RibbonTask[] mainSessionMenu(final Session session)
	{
		//------------------------- View Band -----------------------------------------------
		JRibbonBand viewBand = new JRibbonBand(Bundle.getString("View"), new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 8, 8));
		viewBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(viewBand));

		final JCheckBox packetList = new JCheckBox(Bundle.getString("PacketList"));
		packetList.setSelected(true);
		packetList.addActionListener(new HidePaneListener(session, session.getViewPane().getPacketList()));

	   	final JCheckBox search = new JCheckBox(Bundle.getString("FindPanel"));
		search.setSelected(true);
		search.addActionListener(new HidePaneListener(session, session.getViewPane().getSearchPane()));

		final JCheckBox filter = new JCheckBox(Bundle.getString("Filter"));
		filter.setSelected(true);
		filter.addActionListener(new HidePaneListener(session, session.getViewPane().getFilterPane()));

		final JCheckBox info = new JCheckBox(Bundle.getString("Info"));
		info.setSelected(true);
		info.addActionListener(new HidePaneListener(session, session.getViewPane().getInfoPane()));

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

		RibbonTask mainTask = new RibbonTask(Bundle.getString("SessionMenu"), viewBand, actionsBand);
		List<JRibbonBand> bands = new ArrayList<JRibbonBand>(4);
		for(IPacketListener l : session.getInvokes())
			bands.addAll(l.getRibbonBands());

		if(!bands.isEmpty())
		{
			RibbonTask listenerTask = new RibbonTask(Bundle.getString("Listeners"), bands.toArray(new JRibbonBand[bands.size()]));
			return new RibbonTask[]{mainTask, listenerTask};
		}
		else
			return new RibbonTask[]{mainTask};
	}


	public Session getSession()
	{
		return _session;
	}
}

