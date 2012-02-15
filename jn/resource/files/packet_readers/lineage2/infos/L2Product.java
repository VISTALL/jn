/*
 * Jn (Java sNiffer)
 * Copyright (C) 2012 napile.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package packet_readers.lineage2.infos;

import java.util.Set;
import java.util.TreeSet;

import com.jds.jn.parser.datatree.DataForBlock;

/**
 * @author VISTALL
 * @date 19:06/22.01.2012
 */
public class L2Product
{
	private final int _id;
	private final int _price;
	private final int _category;
	private final int _tabId;
	private final long _startSellDate;
	private final long _stopSellDate;
	private final int _weekOfDay;
	private final int _startSellHour;
	private final int _startSellMinute;
	private final int _stopSellHour;
	private final int _stopSellMinute;

	private final int _stock;

	private final Set<L2ItemWithCount> _items = new TreeSet<L2ItemWithCount>();

	public L2Product(DataForBlock packet)
	{
		_id = packet.getInt("product-id");
		_price = packet.getInt("price");
		_category = packet.getInt("category");
		_tabId = packet.getInt("tab-id");
		_stock = packet.getInt("max-stock");
		_startSellDate = packet.getInt("start-sale-time") * 1000L;
		_stopSellDate = packet.getInt("stop-sale-time") * 1000L;
		_weekOfDay = packet.getInt("week-of-day");
		_startSellHour = packet.getInt("start-sell-hour");
		_startSellMinute = packet.getInt("start-sell-minute");
		_stopSellHour = packet.getInt("stop-sell-hour");
		_stopSellMinute = packet.getInt("stop-sell-minute");
	}

	public int getId()
	{
		return _id;
	}

	public int getPrice()
	{
		return _price;
	}

	public long getStartSellDate()
	{
		return _startSellDate;
	}

	public long getStopSellDate()
	{
		return _stopSellDate;
	}

	public int getStock()
	{
		return _stock;
	}

	public Set<L2ItemWithCount> getItems()
	{
		return _items;
	}

	public int getWeekOfDay()
	{
		return _weekOfDay;
	}

	public int getStartSellHour()
	{
		return _startSellHour;
	}

	public int getStartSellMinute()
	{
		return _startSellMinute;
	}

	public int getStopSellHour()
	{
		return _stopSellHour;
	}

	public int getStopSellMinute()
	{
		return _stopSellMinute;
	}

	public int getCategory()
	{
		return _category;
	}

	public int getTabId()
	{
		return _tabId;
	}
}
