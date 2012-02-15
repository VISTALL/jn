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

/**
 * @author VISTALL
 * @date 19:37/22.01.2012
 */
public class L2ItemWithCount implements Comparable<L2ItemWithCount>
{
	private int _itemId;
	private long _count;

	public L2ItemWithCount(int itemId, long count)
	{
		_itemId = itemId;
		_count = count;
	}

	@Override
	public int compareTo(L2ItemWithCount o)
	{
		if(_itemId == o._itemId)
			return 0;
		return o._itemId - _itemId;
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
