package com.jds.jn.parser.packetfactory.lineage2.infos;

import com.jds.jn.network.packets.DecryptPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  13:54:01/22.06.2010
 */
public class L2SpawnLocInfo
{
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _h;

	public L2SpawnLocInfo(DecryptPacket p)
	{
		_x = p.getInt("x");
		_y = p.getInt("y");
		_z = p.getInt("z");
		_h = p.getInt("h");
	}

	public L2SpawnLocInfo(int x, int y, int z, int h)
	{
		_x = x;
		_y = y;
		_z = z;
		_h = h;
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
		if(o instanceof L2SpawnLocInfo)
		{
			L2SpawnLocInfo o1 = (L2SpawnLocInfo)o;
			if(o1._x == _x && o1._y == _y && o1._z == _z)
			{
				return true;
			}
		}

		return false;
	}
}
