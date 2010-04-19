package com.jds.jn.gui.panels;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jds.jn.gui.models.DecPacketTableModel;
import com.jds.jn.gui.models.NotDecPacketTableModel;
import com.jds.jn.gui.panels.viewpane.FilterPane;
import com.jds.jn.gui.panels.viewpane.InfoPane;
import com.jds.jn.gui.panels.viewpane.PacketList;
import com.jds.jn.gui.panels.viewpane.SearchPane;
import com.jds.jn.gui.panels.viewpane.packetlist.DecPacketListPane;
import com.jds.jn.gui.panels.viewpane.packetlist.NotDecPacketListPane;
import com.jds.jn.session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 26.08.2009
 * Time: 17:15:02
 */
public class ViewPane extends JPanel
{
	public Session _session;

	private JPanel mainPane;
	public JTabbedPane _packetAndSearch;
	private DecPacketTableModel _packetTableModel;
	private NotDecPacketTableModel _packetTableModel2;

	public PacketList _packetList;
	public SearchPane _searchPane;
	public FilterPane _filterPane;
	public InfoPane _infoPane;

	//private JCheckBox _packetListShow;
	//private JCheckBox _searchShow;
	//private JCheckBox _filterShow;
	//private JCheckBox _infoShow;

	private JPopupMenu _tabMenu;

	public ViewPane(Session session)
	{
		$$$setupUI$$$();

		_session = session;
		_session.setViewPane(this);

		_packetTableModel = new DecPacketTableModel(this);
		_packetTableModel2 = new NotDecPacketTableModel(this);

		_tabMenu = new JPopupMenu();

		/*	_packetListShow = new JCheckBox(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("PacketList"));
		_packetListShow.addActionListener(new ActionListener()
		{

		@Override
		public void actionPerformed(ActionEvent e)
		{
		if(!_packetList.IS_HIDE)
		{
		int index = getIndex(_packetList);
		_packetAndSearch.removeTabAt(index);
		_packetList.IS_HIDE = true;
		}
		else
		{
		_packetAndSearch.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("PacketList"), _packetList);
		_packetList.IS_HIDE = false;
		}
		}
		});

		_tabMenu.add(_packetListShow);


		_searchShow = new JCheckBox(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("FindPanel"));
		_searchShow.addActionListener(new ActionListener()
		{

		@Override
		public void actionPerformed(ActionEvent e)
		{
		if(!_searchPane.IS_HIDE)
		{
		int index = getIndex(_searchPane);
		_packetAndSearch.removeTabAt(index);
		_searchPane.IS_HIDE = true;
		}
		else
		{
		_packetAndSearch.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("FindPanel"), _searchPane);
		_searchPane.IS_HIDE = false;
		}
		}
		});

		_tabMenu.add(_searchShow);
				*/
		/*_filterShow = new JCheckBox(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Filter"));
				  _filterShow.addActionListener(new ActionListener()
				  {

					  @Override
					  public void actionPerformed(ActionEvent e)
					  {
						  if(!_filterPane.IS_HIDE)
						  {
							  int index = getIndex(_filterPane);
							  _packetAndSearch.removeTabAt(index);
							  _filterPane.IS_HIDE = true;
						  }
						  else
						  {
							  _packetAndSearch.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Filter"), _filterPane);
							  _filterPane.IS_HIDE = false;
						  }
					  }
				  });

				  _tabMenu.add(_filterShow); */

		//_infoShow = new JCheckBox(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Info"));
		/*	_infoShow.addActionListener(new ActionListener()
				  {

					  @Override
					  public void actionPerformed(ActionEvent e)
					  {
						  if(!_infoPane.IS_HIDE)
						  {
							  int index = getIndex(_infoPane);
							  _packetAndSearch.removeTabAt(index);
							  _infoPane.IS_HIDE = true;
						  }
						  else
						  {
							  _packetAndSearch.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Info"), _infoPane);
							  _infoPane.IS_HIDE = false;
						  }
					  }
				  });

				  _tabMenu.add(_infoShow);*/


		_packetAndSearch.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)
				{
					_tabMenu.show(_packetAndSearch, e.getX(), e.getY());
				}
			}

			@Override
			public void mousePressed(MouseEvent e)
			{

			}

			@Override
			public void mouseReleased(MouseEvent e)
			{

			}

			@Override
			public void mouseEntered(MouseEvent e)
			{

			}

			@Override
			public void mouseExited(MouseEvent e)
			{

			}
		});


		_searchPane = new SearchPane(this);
		_filterPane = new FilterPane(this);
		_packetList = new PacketList(this);
		_infoPane = new InfoPane(this);
		//_packetList.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		//

		_packetAndSearch.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("PacketList"), _packetList);
		//_packetListShow.setSelected(true);

		_packetAndSearch.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("FindPanel"), _searchPane);
		//_searchShow.setSelected(true);

		_packetAndSearch.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Filter"), _filterPane);
		//_filterShow.setSelected(true);

		_packetAndSearch.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Info"), _infoPane);
		//_infoShow.setSelected(true);

		updateInfo(_session);
	}

	public int getIndex(Component comp)
	{
		int size = _packetAndSearch.getComponentCount();

		for (int i = 0; i < size; i++)
		{
			Component com = _packetAndSearch.getComponent(i);
			if (com.equals(comp))
			{
				return i;
			}
		}

		return -1;
	}

	public void autoScroll()
	{
		//_packetScrollPane.validate();
		//JScrollBar vScroll = _packetScrollPane.getVerticalScrollBar();
		//vScroll.setValue(vScroll.getMaximum());
	}

	private void createUIComponents()
	{
		mainPane = this;

		setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
	}

	public void close()
	{

	}

	public DecPacketTableModel getPacketTableModel()
	{
		return _packetTableModel;
	}

	public NotDecPacketTableModel getPacketTableModel2()
	{
		return _packetTableModel2;
	}

	public Session getSession()
	{
		return _session;
	}

	public DecPacketListPane get_packetListPane()
	{
		return _packetList.get_packetListPane();//_Dec_packetListPane;
	}

	public NotDecPacketListPane getNotDecPacketListPane()
	{
		return _packetList.getNotDecPacketListPane();//_notdec_packetListPane;
	}

	public SearchPane get_searchPane()
	{
		return _searchPane;
	}

	public FilterPane get_filterPane()
	{
		return _filterPane;
	}

	public void updateInfo(Session session)
	{
		_infoPane.update(session);
	}


	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$()
	{
		createUIComponents();
		mainPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		_packetAndSearch = new JTabbedPane();
		_packetAndSearch.setTabPlacement(2);
		mainPane.add(_packetAndSearch, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return mainPane;
	}
}
