package com.jds.jn.parser.packetfactory.lineage2.npc;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:14:19/17.06.2010
 */
public class SkillInfo
{
	private final int _id;
	private final int _level;
	private final int _reuse;
	private final int _hitTime;

	public SkillInfo(int id, int level, int reuse, int hitTime)
	{
		_id = id;
		_level = level;
		_reuse = reuse;
		_hitTime = hitTime;
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
}
