package com.jds.jn.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.accessibility.AccessibleContext;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import com.jds.jn.gui.models.packetlist.CryptedPacketListModel;
import com.jds.jn.gui.models.packetlist.DecryptedPacketListModel;
import com.jds.jn.gui.models.packetlist.UnknownPacketListModel;
import com.jds.jn.gui.panels.viewpane.FilterPane;
import com.jds.jn.gui.panels.viewpane.HiddenPanel;
import com.jds.jn.gui.panels.viewpane.InfoPane;
import com.jds.jn.gui.panels.viewpane.PacketListPane;
import com.jds.jn.gui.panels.viewpane.SearchPane;
import com.jds.jn.gui.panels.viewpane.packetlist.CryptedPacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.DecryptedPacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.UnknownPacketListPane;
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
	public Session _session;

	private DecryptedPacketListModel _decryptPacketListModel = new DecryptedPacketListModel(this);
	private CryptedPacketListModel _cryptedPacketListModel = new CryptedPacketListModel(this);
	private UnknownPacketListModel _unknownPacketListModel = new UnknownPacketListModel(this);

	private PacketListPane _packetListPane;
	private SearchPane _searchPane;
	private FilterPane _filterPane;
	private InfoPane _infoPane;

	public ViewPane(Session session)
	{
		_session = session;

		setTabPlacement(LEFT);

		_infoPane = new InfoPane();
		_searchPane = new SearchPane(this);
		_filterPane = new FilterPane(this);
		_packetListPane = new PacketListPane(this);

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_packetListPane);
				_packetListPane.showPane(_packetListPane.getDecryptedPacketListPane());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_packetListPane);
				_packetListPane.showPane(_packetListPane.getCryptedPacketListPane());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_packetListPane);
				_packetListPane.showPane(_packetListPane.getUnknownPacketListPane());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKeyboardAction(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSelectedComponent(_searchPane);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void drawThis()
	{
		_filterPane.drawThis();

		addTab("PacketList", _packetListPane, true);
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

	public DecryptedPacketListModel getDecryptPacketListModel()
	{
		return _decryptPacketListModel;
	}

	public CryptedPacketListModel getCryptedPacketListModel()
	{
		return _cryptedPacketListModel;
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

	public UnknownPacketListModel getUnknownPacketListModel()
	{
		return _unknownPacketListModel;
	}
}
