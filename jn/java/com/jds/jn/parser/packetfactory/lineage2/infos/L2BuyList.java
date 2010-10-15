package com.jds.jn.parser.packetfactory.lineage2.infos;

import java.util.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  12:18:35/06.08.2010
 */
public class L2BuyList
{
	private final int _listId;
	private Map<Integer, L2ItemComponent> _items = new HashMap<Integer, L2ItemComponent>();

	public L2BuyList(int listId)
	{
		_listId = listId;
	}

	public void addItem(L2ItemComponent comp)
	{
		_items.put(comp.getItemId(), comp);
	}

	public Collection<L2ItemComponent> getItems()
	{
		return _items.values();
	}

	public int getListId()
	{
		return _listId;
	}
}
