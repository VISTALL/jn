package com.jds.jn.parser.packetfactory.lineage2.npc;

import java.io.*;
import java.util.*;

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

	private Map<Integer, NpcInfo> _npcInfos = new HashMap<Integer, NpcInfo>();
   	private Map<Integer, NpcInfo> _npcInfosByNpcId = new TreeMap<Integer, NpcInfo>();

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

			NpcInfo  list = _npcInfosByNpcId.get(npc.getNpcId());
			if(list == null)
			{
				list = npc;
				_npcInfosByNpcId.put(npc.getNpcId(), list);
			}
			else
			{
				list.addSpawnLoc(p);	
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
				int id = ((VisualValuePart)block.getPartByName("id")).getValueAsInt();
				int value = ((VisualValuePart)block.getPartByName("value")).getValueAsInt();

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

	@Override
	public void close()
	{
	 	int id = 0;
		String fileName = "./npc_sniff_%d.xml";
		while (new File(String.format(fileName, id)).exists())
		{
			id ++;
		}

		try
		{
			FileWriter writer = new FileWriter(String.format(fileName, id));
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<list>\n");
			for(NpcInfo npc : _npcInfosByNpcId.values())
			{
				writer.write(npc.toXML());
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
