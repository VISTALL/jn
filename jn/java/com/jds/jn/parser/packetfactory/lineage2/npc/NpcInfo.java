package com.jds.jn.parser.packetfactory.lineage2.npc;

import gnu.trove.TIntObjectHashMap;

import com.jds.jn.network.packets.DecryptPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:58:15/05.06.2010
 */
public class NpcInfo
{
	private final int _npcId;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _h;
	private final int _pAttackSpeed;
	private final int _mAttackSpeed;
	private final int _runSpd;
	private final int _walkSpd;
	private final double _collisionRadius;
	private final double _collisionHeight;

	private int _hp;
	private int _mp;
	private int _level;

	private TIntObjectHashMap<SkillInfo> _skills = new TIntObjectHashMap<SkillInfo>();
	//private final int _rhand;
	//private final int _armor;
	//private final int _lhand;
	
	public NpcInfo(DecryptPacket p)
	{
		_npcId = p.getInt("npcId");
		_x = p.getInt("x");
		_y = p.getInt("y");
		_z = p.getInt("z");
		_h = p.getInt("h");
		_mAttackSpeed = p.getInt("mAttackSpeed");
		_pAttackSpeed = p.getInt("pAttackSpeed");
		_runSpd = p.getInt("run_spd");
		_walkSpd = p.getInt("walk_spd");
		_collisionRadius = p.getDouble("col_radius");
		_collisionHeight = p.getDouble("col_height");
		//_rhand = p.getInt("rhand");
		//_armor = p.getInt("armor");
		//_lhand = p.getInt("lhand");
	}

	public int getNpcId()
	{
		return _npcId;
	}

	public int getHp()
	{
		return _hp;
	}

	public void setHp(int hp)
	{
		_hp = hp;
	}

	public int getMp()
	{
		return _mp;
	}

	public void setMp(int mp)
	{
		_mp = mp;
	}

	public int getLevel()
	{
		return _level;
	}

	public void setLevel(int level)
	{
		_level = level;
	}

	public boolean hasSkill(int f)
	{
		return _skills.containsKey(f);
	}

	public void addSkill(SkillInfo f)
	{
		_skills.put(f.getId(), f);
	}
}
