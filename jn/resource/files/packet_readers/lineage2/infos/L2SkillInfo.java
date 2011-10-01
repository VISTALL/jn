package packet_readers.lineage2.infos;

/**
 * @author VISTALL
 * @date  21:14:19/17.06.2010
 */
public class L2SkillInfo
{
	private final int _id;
	private final int _level;
	private final int _reuse;
	private final int _hitTime;
	private final int _reuseGroup;

	public L2SkillInfo(int id, int level, int reuse, int hitTime, int reuseGroup)
	{
		_id = id;
		_level = level;
		_reuse = reuse;
		_hitTime = hitTime;
		_reuseGroup = reuseGroup;
	}

	public int getId()
	{
		return _id;
	}

	public int getLevel()
	{
		return _level;
	}

	public int getReuse()
	{
		return _reuse;
	}

	public int getHitTime()
	{
		return _hitTime;
	}

	public int getReuseGroup()
	{
		return _reuseGroup;
	}
}
