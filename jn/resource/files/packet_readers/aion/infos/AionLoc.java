package packet_readers.aion.infos;

/**
 * @author VISTALL
 * @date 23:04/03.03.2011
 */
public class AionLoc
{
	private final int _worldId;
	private final float _x;
	private final float _y;
	private final float _z;
	private final int _h;
	private final int _staticId;

	public AionLoc(int worldId, float x, float y, float z, int h, int staticId)
	{
		_worldId = worldId;
		_x = x;
		_y = y;
		_z = z;
		_h = h;
		_staticId = staticId;
	}

	@Override
	public int hashCode()
	{
		return (int)(_worldId + _x + _y + _z + _staticId);
	}

	public float getX()
	{
		return _x;
	}

	public float getY()
	{
		return _y;
	}

	public float getZ()
	{
		return _z;
	}

	public int getH()
	{
		return _h;
	}

	public int getStaticId()
	{
		return _staticId;
	}

	public int getWorldId()
	{
		return _worldId;
	}
}
