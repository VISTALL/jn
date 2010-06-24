package com.jds.jn.parser.packetfactory.lineage2.npc;

import com.jds.jn.network.packets.DecryptPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  13:54:01/22.06.2010
 */
public class SpawnLoc
{
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _h;

	public SpawnLoc(DecryptPacket p)
	{
		_x = p.getInt("x");
		_y = p.getInt("y");
		_z = p.getInt("z");
		_h = p.getInt("h");
	}

	public SpawnLoc(int x, int y, int z, int h)
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
		if(o instanceof SpawnLoc)
		{
			SpawnLoc o1 = (SpawnLoc)o;
			if(o1._x == _x && o1._y == _y && o1._z == _z)
			{
				return true;
			}
		}

		return false;
	}
}
