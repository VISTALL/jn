package com.jds.jn.parser.packetfactory.lineage2.infos;

import java.lang.reflect.Field;
import java.util.*;

import com.jds.jn.holders.NpcNameHolder;
import com.jds.jn.network.packets.DecryptPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:58:15/05.06.2010
 */
public class L2NpcInfo
{
	private final String _name;
	private final int _npcId;

	private final int _pAttackSpeed;
	private final int _mAttackSpeed;
	private final int _runSpd;
	private final int _walkSpd;
	private final double _collisionRadius;
	private final double _collisionHeight;

	private int _hp = 100;
	private int _mp = 100;
	private int _level = 1;

	private final int _rhand;
	private final int _armor;
	private final int _lhand;

	private final Map<Integer, L2SkillInfo> _skills = new HashMap<Integer, L2SkillInfo>();
	private final List<L2DialogInfo> _dialogs = new ArrayList<L2DialogInfo>();
	private final List<L2SpawnLocInfo> _spawns = new ArrayList<L2SpawnLocInfo>();


	public L2NpcInfo(DecryptPacket p)
	{
		_npcId = p.getInt("npcId") - 1000000;
		_name = NpcNameHolder.getInstance().name(_npcId);
		_mAttackSpeed = p.getInt("mAttackSpeed");
		_pAttackSpeed = p.getInt("pAttackSpeed");
		_runSpd = p.getInt("run_spd");
		_walkSpd = p.getInt("walk_spd");
		_collisionRadius = p.getDouble("col_radius");
		_collisionHeight = p.getDouble("col_height");
		_rhand = p.getInt("rhand");
		_armor = p.getInt("armor");
		_lhand = p.getInt("lhand");

		addSpawnLoc(p);
	}


	public void addSpawnLoc(DecryptPacket p)
	{
		L2SpawnLocInfo loc =  new L2SpawnLocInfo(p);
		if(!hasSpawn(loc))
		{
			_spawns.add(loc);
		}
	}

	public String toXML()
	{
		String xml =
				"\t<npc id=\"%npcId%\" templateId=\"0\" name=\"%name%\" title=\"\">\n" +
				"\t\t<set name=\"collision_radius\" val=\"%collisionRadius%\" />\n" +
				"\t\t<set name=\"collision_height\" val=\"%collisionHeight%\" />\n" +
				"\t\t<set name=\"level\" val=\"%level%\" />\n" +
				"\t\t<set name=\"nameServerSide\" val=\"false\" />\n" +
				"\t\t<set name=\"titleServerSide\" val=\"false\" />\n" +
				"\t\t<set name=\"type\" val=\"L2Npc\" />\n" +
				"\t\t<set name=\"ai_type\" val=\"NpcAI\" />\n" +
				"\t\t<set name=\"baseAtkRange\" val=\"40\" />\n" +
				"\t\t<set name=\"baseHpMax\" val=\"%hp%\" />\n" +
				"\t\t<set name=\"baseMpMax\" val=\"%mp%\" />\n" +
				"\t\t<set name=\"baseHpReg\" val=\"7.5\" />\n" +
				"\t\t<set name=\"baseMpReg\" val=\"2.7\" />\n" +
				"\t\t<set name=\"baseSTR\" val=\"40\" />\n" +
				"\t\t<set name=\"baseCON\" val=\"43\" />\n" +
				"\t\t<set name=\"baseDEX\" val=\"30\" />\n" +
				"\t\t<set name=\"baseINT\" val=\"21\" />\n" +
				"\t\t<set name=\"baseWIT\" val=\"20\" />\n" +
				"\t\t<set name=\"baseMEN\" val=\"10\" />\n" +
				"\t\t<set name=\"revardExp\" val=\"0\" />\n" +
				"\t\t<set name=\"revardSp\" val=\"0\" />\n" +
				"\t\t<set name=\"basePAtk\" val=\"500\" />\n" +
				"\t\t<set name=\"basePDef\" val=\"500\" />\n" +
				"\t\t<set name=\"baseMAtk\" val=\"500\" />\n" +
				"\t\t<set name=\"baseMDef\" val=\"500\" />\n" +
				"\t\t<set name=\"basePAtkSpd\" val=\"%pAttackSpeed%\" />\n" +
				"\t\t<set name=\"baseMAtkSpd\" val=\"%mAttackSpeed%\" />\n" +
				"\t\t<set name=\"aggroRange\" val=\"0\" />\n" +
				"\t\t<set name=\"baseWalkSpd\" val=\"%walkSpd%\" />\n" + 
				"\t\t<set name=\"baseRunSpd\" val=\"%runSpd%\" />\n" +
				"\t\t<set name=\"baseShldDef\" val=\"0\" />\n" +
				"\t\t<set name=\"baseShldRate\" val=\"0\" />\n" +
				"\t\t<set name=\"baseCrit\" val=\"40\" />\n" +
				"\t\t<set name=\"dropHerbs\" val=\"true\" />\n" +
				"\t\t<set name=\"shots\" val=\"NONE\" />\n";

		if(_armor != 0 || _lhand != 0 || _rhand != 0)
		{
			xml += "\t\t<equip>\n";

			if(_armor != 0)
			{
				xml += "\t\t\t<armor id=\"%armor%\" />\n";
			}

			if(_lhand != 0)
			{
				xml += "\t\t\t<lhand id=\"%lhand%\" />\n";
			}

			if(_rhand != 0)
			{
				xml += "\t\t\t<rhand id=\"%rhand%\" />\n";
			}

			xml += "\t\t</equip>\n";
		}

		if(_spawns.size() != 0)
		{
			xml += "\t\t<spawnlist>\n";
			for(L2SpawnLocInfo loc : _spawns)
			{
				xml += String.format("\t\t\t<spawn x=\"%d\" y=\"%d\" z=\"%d\" h=\"%d\" />\n", loc.getX(), loc.getY(), loc.getZ(), loc.getH());
			}
			xml += "\t\t</spawnlist>\n";
		}

		if(_skills.size() != 0)
		{
			xml += "\t\t<skills>\n";
			for(L2SkillInfo info : _skills.values())
			{
				xml += String.format("\t\t\t<!--Hit time: %d; Reuse: %d-->\n", info.getHitTime(), info.getReuse());
				xml += String.format("\t\t\t<skill id=\"%d\" level=\"%d\" />\n", info.getId(), info.getLevel());
			}
			xml += "\t\t</skills>\n";
		}

		xml += "\t</npc>\n";

		for(Field d : L2NpcInfo.class.getDeclaredFields())
		{
			String name = "%" + d.getName().replace("_", "") + "%";
			if(xml.contains(name))
			{
				try
				{
					xml = xml.replace(name, String.valueOf(d.get(this)));
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}

		return xml;
	}


	public void setHp(int hp)
	{
		_hp = hp;
	}

	public void setMp(int mp)
	{
		_mp = mp;
	}

	public void setLevel(int level)
	{
		_level = level;
	}

	public boolean hasSkill(int f)
	{
		return _skills.containsKey(f);
	}

	public void addSkill(L2SkillInfo f)
	{
		_skills.put(f.getId(), f);
	}

	public boolean hasSpawn(L2SpawnLocInfo loc)
	{
		for (L2SpawnLocInfo l : _spawns)
		{
			if(l.equals(loc))
				return true;
		}

		return false;
	}

	public int getNpcId()
	{
		return _npcId;
	}

	public void addDialog(L2DialogInfo t)
	{
		if(!_dialogs.contains(t))
			_dialogs.add(t);
	}

	public List<L2DialogInfo> getDialogs()
	{
		return _dialogs;
	}
}
