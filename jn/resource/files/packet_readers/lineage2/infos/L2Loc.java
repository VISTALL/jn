/*
 * Jn (Java sNiffer)
 * Copyright (C) 2011 napile.org
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

import com.jds.jn.network.packets.DecryptedPacket;

/**
 * @author VISTALL
 * @date 21:06/13.10.2011
 */
public class L2Loc
{
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _h;

	public L2Loc(int x, int y, int z, int h)
	{
		_h = h;
		_z = z;
		_y = y;
		_x = x;
	}

	public L2Loc(DecryptedPacket p)
	{
		_x = p.getInt("x");
		_y = p.getInt("y");
		_z = p.getInt("z");
		_h = p.getInt("h");
	}

	public int getX()
	{
		return _x;
	}

	public int getY()
	{
		return _y;
	}

	public int getZ()
	{
		return _z;
	}

	public int getH()
	{
		return _h;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof L2Loc)
		{
			L2Loc o1 = (L2Loc) o;
			if(o1._x == _x && o1._y == _y && o1._z == _z)
				return true;
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return _x ^ _y ^ _z;
	}
}
