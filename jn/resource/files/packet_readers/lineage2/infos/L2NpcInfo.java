package packet_readers.lineage2.infos;

import java.lang.reflect.Field;
import java.util.*;

import packet_readers.lineage2.holders.NpcNameHolder;
import packet_readers.lineage2.holders.SkillNameHolder;
import com.jds.jn.network.packets.DecryptedPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:58:15/05.06.2010
 */
public class L2NpcInfo extends L2DialogObject
{
	private final String _name;
	private final int _npcId;

	private final int _pAttackSpeed;
	private final int _mAttackSpeed;
	private final int _runSpd;
	private final int _walkSpd;
	private final double _collisionRadius;
	private final double _collisionHeight;

	private final int _hp;
	private final int _mp;
	private int _level = 1;

	private final int _rhand;
	private final int _armor;
	private final int _lhand;

	private final Map<Integer, L2SkillInfo> _skills = new HashMap<Integer, L2SkillInfo>();
	private final Set<L2SpawnLocInfo> _spawnLocInfo;

	public L2NpcInfo(DecryptedPacket p)
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
		_hp = p.getInt("max_hp");
		_mp = p.getInt("max_mp");

		_spawnLocInfo = new HashSet<L2SpawnLocInfo>();
		_spawnLocInfo.add(new L2SpawnLocInfo(p));
	}

	public String toXML()
	{
		String xml =
				"\t<npc id=\"%npcId%\" name=\"%name%\">\n" +
				"\t\t<set name=\"collision_radius\" val=\"%collisionRadius%\" />\n" +
				"\t\t<set name=\"collision_height\" val=\"%collisionHeight%\" />\n" +
				"\t\t<set name=\"level\" val=\"%level%\" />\n" +
				"\t\t<set name=\"type\" val=\"Npc\" />\n" +
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
				"\t\t<set name=\"rewardExp\" val=\"0\" />\n" +
				"\t\t<set name=\"rewardSp\" val=\"0\" />\n" +
				"\t\t<set name=\"rewardRp\" val=\"0\" />\n" +
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
				"\t\t<set name=\"texture\" val=\"\" />\n" +
				"\t\t<set name=\"shots\" val=\"NONE\" />\n";

		if(_armor != 0 || _lhand != 0 || _rhand != 0)
		{
			xml += "\t\t<equip>\n";

			if(_armor != 0)
				xml += "\t\t\t<armor item_id=\"%armor%\" />\n";

			if(_lhand != 0)
				xml += "\t\t\t<lhand item_id=\"%lhand%\" />\n";

			if(_rhand != 0)
				xml += "\t\t\t<rhand item_id=\"%rhand%\" />\n";

			xml += "\t\t</equip>\n";
		}

		if(_skills.size() != 0)
		{
			xml += "\t\t<skills>\n";
			for(L2SkillInfo info : _skills.values())
				xml += String.format("\t\t\t<skill id=\"%d\" level=\"%d\" /> <!--%s-->\n", info.getId(), info.getLevel(), SkillNameHolder.getInstance().name(info.getId(), info.getLevel()));
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

	public int getNpcId()
	{
		return _npcId;
	}

	public Set<L2SpawnLocInfo> getSpawnLoc()
	{
		return _spawnLocInfo;
	}
}
