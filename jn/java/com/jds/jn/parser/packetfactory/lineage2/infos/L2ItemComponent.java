package com.jds.jn.parser.packetfactory.lineage2.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  23:02:56/31.07.2010
 */
public class L2ItemComponent
{
	private final int _itemId;
	private final long _count;

	public L2ItemComponent(int itemId, long count)
	{
		_itemId = itemId;
		_count = count;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public long getCount()
	{
		return _count;
	}
}
