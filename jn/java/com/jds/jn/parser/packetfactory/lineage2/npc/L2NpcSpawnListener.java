package com.jds.jn.parser.packetfactory.lineage2.npc;

import gnu.trove.TIntObjectHashMap;

import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.packetfactory.IPacketListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:56:57/05.06.2010
 */
public class L2NpcSpawnListener implements IPacketListener
{
	// status update flags
	public final static int MAX_HP = 0x0a;
	public final static int MAX_MP = 0x0c;

	private static final String NPC_INFO = "NpcInfo";
	private static final String STATUS_UPDATE = "StatusUpdate";
	private static final String USER_INFO = "UserInfo";
	private static final String MY_TARGET_SELECTED = "MyTargetSelected";
	private static final String MAGIC_SKILL_USE = "MagicSkillUse";
	
	//values
	private static final String OBJECT_ID = "obj_id";

	private int _level;

	private TIntObjectHashMap<NpcInfo> _npcInfos = new TIntObjectHashMap<NpcInfo>();

	@Override
	public void invoke(DecryptPacket p)
	{
		if (p == null || p.getPacketFormat() == null || p.getName() == null || p.hasError())
		{
			return;
		}

		if(p.getName().equalsIgnoreCase(NPC_INFO))
		{
			int objectId = p.getInt(OBJECT_ID);

			NpcInfo npc = _npcInfos.get(objectId);
			if(npc == null)
			{
				npc = new NpcInfo(p);
				_npcInfos.put(objectId, npc);
			}
		}
		else if (p.getName().equalsIgnoreCase(USER_INFO))
		{
			_level = p.getInt("level");	
		}
		else if (p.getName().equalsIgnoreCase(MAGIC_SKILL_USE))
		{
			int obj_id = p.getInt(OBJECT_ID);

			NpcInfo npc = _npcInfos.get(obj_id);
			if(npc == null)
			{
			 	return;
			}

			int id = p.getInt("skillId");
			int level = p.getInt("skillLevel");
			int reuse = p.getInt("reuse");
			int hitTime = p.getInt("hitTime");

			if(!npc.hasSkill(id))
			{
				npc.addSkill(new SkillInfo(id, level, reuse, hitTime));
			}
		}
		else if (p.getName().equalsIgnoreCase(MY_TARGET_SELECTED))
		{
			int obj_id = p.getInt(OBJECT_ID);

			NpcInfo npc = _npcInfos.get(obj_id);
			if(npc == null)
			{
			 	return;
			}

			int diff = p.getInt("diff");
			int level = _level - diff;

			npc.setLevel(level);
		}
		else if (p.getName().equalsIgnoreCase(STATUS_UPDATE))
		{
			int obj_id = p.getInt(OBJECT_ID);

			NpcInfo npc = _npcInfos.get(obj_id);
			if(npc == null)
			{
			 	return;
			}
			DataForPart att = (DataForPart)p.getRootNode().getPartByName("attibutes");

			for(DataForBlock block : att.getNodes())
			{
				int id = ((NumberValuePart)block.getPartByName("id")).getValueAsInt();
				int value = ((NumberValuePart)block.getPartByName("value")).getValueAsInt();

				switch (id)
				{
					case MAX_HP:
						npc.setHp(value);
						break;
					case MAX_MP:
						npc.setMp(value);
						break;
				}
			}
		}
	}
}
