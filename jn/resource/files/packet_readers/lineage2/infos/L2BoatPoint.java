package packet_readers.lineage2.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:16:25/27.08.2010
 */
public class L2BoatPoint
{
	private int _x;
	private int _y;
	private int _z;
	private int _fuel;

	public L2BoatPoint(int x, int y, int z, int fuel)
	{
		_x = x;
		_y = y;
		_z = z;
		_fuel = fuel;
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

	public int getFuel()
	{
		return _fuel;
	}
}
