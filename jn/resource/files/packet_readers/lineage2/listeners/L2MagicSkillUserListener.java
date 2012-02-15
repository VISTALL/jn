package packet_readers.lineage2.listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.infos.L2NpcInfo;
import packet_readers.lineage2.infos.L2Object;
import packet_readers.lineage2.infos.L2SkillInfo;

/**
 * @author VISTALL
 * @date 23:26/30.09.2011
 */
public class L2MagicSkillUserListener extends L2AbstractListener
{
	public L2MagicSkillUserListener()
	{
		//
	}

	@Override
	public List<String> getPackets()
	{
		return Collections.singletonList("MagicSkillUse");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		int obj_id = p.getInt(L2World.OBJECT_ID);

		int id = p.getInt("skill-id");
		int level = p.getInt("skill-level");
		int reuse = p.getInt("reuse");
		int hitTime = p.getInt("hit-time");
		int reuseGroup = p.getInt("reuse-group");

		L2SkillInfo skill = new L2SkillInfo(id, level, reuse, hitTime, reuseGroup);

		_world.getSkills().put(getSkillHashCode(id, level), skill);

		L2Object object = _world.getObject(obj_id);
		if(!(object instanceof L2NpcInfo))
			return;

		L2NpcInfo npc = (L2NpcInfo)object;
		if(!npc.hasSkill(id))
			npc.addSkill(new L2SkillInfo(id, level, reuse, hitTime, reuseGroup));
	}

	@Override
	public void closeImpl() throws IOException
	{
		FileWriter writer = new FileWriter(getLogFile("skill-data/", "xml"));
		writer.write("<list>\n");
		for(L2SkillInfo skill : _world.getSkills().values())
			writer.write(String.format("\t<skill id=\"%d\" level=\"%d\" reuse=\"%d\" hit_time=\"%d\" reuse_group=\"%d\" />\n", skill.getId(), skill.getLevel(), skill.getReuse(), skill.getHitTime(), skill.getReuseGroup()));
		writer.write("</list>");
		writer.close();
	}

	public static int getSkillHashCode(int skillId, int skillLevel)
	{
		return skillId * 1000 + skillLevel;
	}
}
