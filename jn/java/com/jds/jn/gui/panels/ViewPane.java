package com.jds.jn.gui.panels;

import java.awt.Component;
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

import com.jds.jn.gui.models.CryptedPacketTableModel;
import com.jds.jn.gui.models.DecryptedPacketTableModel;
import com.jds.jn.gui.panels.viewpane.FilterPane;
import com.jds.jn.gui.panels.viewpane.HiddenPanel;
import com.jds.jn.gui.panels.viewpane.InfoPane;
import com.jds.jn.gui.panels.viewpane.PacketList;
import com.jds.jn.gui.panels.viewpane.SearchPane;
import com.jds.jn.gui.panels.viewpane.packetlist.CryptedPacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.DecryptedPacketListPane;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 17:15:02
 */
public class ViewPane extends JTabbedPane
{
	private class SelectionPaneListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e)
		{
			if(e.getSource() == getModel())
			{
				Component c = getSelectedComponent();
				if(c != null)
				{
					c.invalidate();
					c.repaint();
				}
			}
		}
	}

	public Session _session;

	private DecryptedPacketTableModel _decryptPacketTableModel = new DecryptedPacketTableModel(this);
	private CryptedPacketTableModel _cryptedPacketTableModel = new CryptedPacketTableModel(this);

	private PacketList _packetList;
	private SearchPane _searchPane;
	private FilterPane _filterPane;
	private InfoPane _infoPane;
	public ViewPane(Session session)
	{
		_session = session;

		setTabPlacement(2);

		addChangeListener(new SelectionPaneListener());

		_infoPane = new InfoPane();
		_searchPane = new SearchPane(this);
		_filterPane = new FilterPane(this);
		_packetList = new PacketList(this);

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_packetList);
				_packetList.showPane(false);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_packetList);
				_packetList.showPane(true);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void drawThis()
	{
		_filterPane.drawThis();

		addTab("PacketList", _packetList, true);
		addTab("FindPanel", _searchPane, true);
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

	public DecryptedPacketTableModel getDecryptPacketTableModel()
	{
		return _decryptPacketTableModel;
	}

	public CryptedPacketTableModel getCryptPacketTableModel()
	{
		return _cryptedPacketTableModel;
	}

	public Session getSession()
	{
		return _session;
	}

	public DecryptedPacketListPane getPacketListPane()
	{
		return _packetList.getDecryptedPacketListPane();
	}

	public CryptedPacketListPane getCryptedPacketListPane()
	{
		return _packetList.getCryptedPacketListPane();
	}

	public SearchPane getSearchPane()
	{
		return _searchPane;
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

		_packetList.setEnabled(e);
	}

	public JTabbedPane getTabbedPane()
	{
		return this;
	}

	public PacketList getPacketList()
	{
		return _packetList;
	}

	public InfoPane getInfoPane()
	{
		return _infoPane;
	}
}
