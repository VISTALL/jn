package com.jds.jn.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.accessibility.AccessibleContext;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jds.jn.gui.panels.viewpane.FilterPane;
import com.jds.jn.gui.panels.viewpane.HiddenPanel;
import com.jds.jn.gui.panels.viewpane.InfoPane;
import com.jds.jn.gui.panels.viewpane.PacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.CryptedPacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.DecryptedPacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.UnknownPacketListPane;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;

/**
 * @author VISTALL
 * @date 17:15:02/26.08.2009
 */
public class ViewPane extends JTabbedPane
{
	public Session _session;

	private PacketListPane _packetListPane;
	private FilterPane _filterPane;
	private InfoPane _infoPane;

	public ViewPane(Session session)
	{
		_session = session;

		setTabPlacement(LEFT);

		_infoPane = new InfoPane();
		_filterPane = new FilterPane(this);
		_packetListPane = new PacketListPane(this);

		addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				((HiddenPanel)getSelectedComponent()).refresh();
			}
		});

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_packetListPane);
				_packetListPane.setSelectedPanel(_packetListPane.getDecryptedPacketListPane());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_packetListPane);
				_packetListPane.setSelectedPanel(_packetListPane.getCryptedPacketListPane());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_packetListPane);
				_packetListPane.setSelectedPanel(_packetListPane.getUnknownPacketListPane());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void drawThis()
	{
		_filterPane.drawThis();

		addTab("PacketList", _packetListPane, true);
		addTab("Filter", _filterPane, true);
		addTab("Info", _infoPane, true);
	}

	public void addTab(String title, HiddenPanel pane, boolean bundle)
	{
		pane.setHidden(false);
		addTab(bundle ? Bundle.getString(title) : title, pane);
	}

	public void removeTab(HiddenPanel tab)
	{
		AccessibleJTabbedPane p = (AccessibleJTabbedPane)getAccessibleContext();
		int index = -1;
		for(int i = 0; i < p.getAccessibleChildrenCount(); i++)
		{
			//JTabbedPane.Page
			AccessibleContext child = (AccessibleContext)p.getAccessibleChild(i);
			if(child.getAccessibleChild(0) == tab)
				index = i;
		}

		if(index == -1)
			throw new IllegalArgumentException();

		removeTabAt(index);

		tab.setHidden(true);
	}

	public void close()
	{

	}

	public Session getSession()
	{
		return _session;
	}

	public DecryptedPacketListPane getDecryptedPacketListPane()
	{
		return _packetListPane.getDecryptedPacketListPane();
	}

	public CryptedPacketListPane getCryptedPacketListPane()
	{
		return _packetListPane.getCryptedPacketListPane();
	}

	public UnknownPacketListPane getUnknownPacketListPane()
	{
		return _packetListPane.getUnknownPacketListPane();
	}

	public FilterPane getFilterPane()
	{
		return _filterPane;
	}

	public void updateInfo(Session session)
	{
		_infoPane.update(session);
	}

	public void actionEnable(boolean e)
	{
		setEnabled(e);

		_packetListPane.setEnabled(e);
	}

	public PacketListPane getPacketListPane()
	{
		return _packetListPane;
	}

	public InfoPane getInfoPane()
	{
		return _infoPane;
	}
}
