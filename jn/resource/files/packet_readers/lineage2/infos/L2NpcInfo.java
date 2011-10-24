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
	private final String _title;
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

	private final boolean _showName;

	private final Map<Integer, L2SkillInfo> _skills = new HashMap<Integer, L2SkillInfo>();
	private final Set<L2Loc> _spawnLocInfo = new HashSet<L2Loc>();
	private final Set<L2NpcSayInfo> _says = new HashSet<L2NpcSayInfo>();
	private final Set<L2Loc> _moveLocs = new HashSet<L2Loc>();

	public L2NpcInfo(DecryptedPacket p)
	{
		_npcId = p.getInt("npcId") - 1000000;
		_name = NpcNameHolder.getInstance().name(_npcId);
		_title = NpcNameHolder.getInstance().title(_npcId);
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
		_showName = p.getInt("show_name") == 1;

		_spawnLocInfo.add(new L2Loc(p));
	}

	public String toXML()
	{
		String xml =
				"\t<npc id=\"%npcId%\" name=\"%name%\" title=\"%title%\">\n" +
				"\t\t<set name=\"collision_radius\" value=\"%collisionRadius%\" />\n" +
				"\t\t<set name=\"collision_height\" value=\"%collisionHeight%\" />\n" +
				"\t\t<set name=\"level\" value=\"%level%\" />\n" +
				"\t\t<set name=\"type\" value=\"Npc\" />\n" +
				"\t\t<set name=\"ai_type\" value=\"CharacterAI\" />\n" +
				"\t\t<set name=\"baseAtkRange\" value=\"40\" />\n" +
				"\t\t<set name=\"baseAtkType\" value=\"SWORD\" />\n" +
				"\t\t<set name=\"baseHpMax\" value=\"%hp%\" />\n" +
				"\t\t<set name=\"baseMpMax\" value=\"%mp%\" />\n" +
				"\t\t<set name=\"baseHpReg\" value=\"7.5\" />\n" +
				"\t\t<set name=\"baseHpRate\" value=\"1\" />\n" +
				"\t\t<set name=\"baseMpReg\" value=\"2.7\" />\n" +
				"\t\t<set name=\"baseSTR\" value=\"40\" />\n" +
				"\t\t<set name=\"baseCON\" value=\"43\" />\n" +
				"\t\t<set name=\"baseDEX\" value=\"30\" />\n" +
				"\t\t<set name=\"baseINT\" value=\"21\" />\n" +
				"\t\t<set name=\"baseWIT\" value=\"20\" />\n" +
				"\t\t<set name=\"baseMEN\" value=\"10\" />\n" +
				"\t\t<set name=\"rewardExp\" value=\"0\" />\n" +
				"\t\t<set name=\"rewardSp\" value=\"0\" />\n" +
				"\t\t<set name=\"rewardRp\" value=\"0\" />\n" +
				"\t\t<set name=\"basePAtk\" value=\"500\" />\n" +
				"\t\t<set name=\"basePDef\" value=\"500\" />\n" +
				"\t\t<set name=\"baseMAtk\" value=\"500\" />\n" +
				"\t\t<set name=\"baseMDef\" value=\"500\" />\n" +
				"\t\t<set name=\"basePAtkSpd\" value=\"%pAttackSpeed%\" />\n" +
				"\t\t<set name=\"baseMAtkSpd\" value=\"%mAttackSpeed%\" />\n" +
				"\t\t<set name=\"aggroRange\" value=\"0\" />\n" +
				"\t\t<set name=\"baseWalkSpd\" value=\"%walkSpd%\" />\n" + 
				"\t\t<set name=\"baseRunSpd\" value=\"%runSpd%\" />\n" +
				"\t\t<set name=\"baseShldDef\" value=\"0\" />\n" +
				"\t\t<set name=\"baseShldRate\" value=\"0\" />\n" +
				"\t\t<set name=\"baseCritRate\" value=\"40\" />\n" +
				"\t\t<set name=\"texture\" value=\"\" />\n";

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

		if(!_showName)
		{
			xml += "\t\t<ai_params>\n";
			xml += "\t\t\t<set name=\"showName\" value=\"false\" />\n";
			xml += "\t\t</ai_params>\n";
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

	public Set<L2Loc> getSpawnLoc()
	{
		return _spawnLocInfo;
	}

	public Set<L2NpcSayInfo> getSays()
	{
		return _says;
	}

	public Set<L2Loc> getMoveLocs()
	{
		return _moveLocs;
	}
}
