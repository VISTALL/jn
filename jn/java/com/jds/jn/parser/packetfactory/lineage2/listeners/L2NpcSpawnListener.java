package com.jds.jn.parser.packetfactory.lineage2.listeners;

import java.io.*;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.parser.packetfactory.lineage2.L2World;
import com.jds.jn.parser.packetfactory.lineage2.infos.L2NpcInfo;
import com.jds.jn.parser.packetfactory.lineage2.infos.L2SkillInfo;

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

	private static final String STATUS_UPDATE = "StatusUpdate";
	private static final String USER_INFO = "UserInfo";
	private static final String MY_TARGET_SELECTED = "MyTargetSelected";
	private static final String MAGIC_SKILL_USE = "MagicSkillUse";
	
	//values
   	private int _userLevel;

	private L2World _world;

	public L2NpcSpawnListener(L2World w)
	{
		_world = w;
	}

	@Override
	public void invoke(DecryptedPacket p)
	{
		if (p.getName().equalsIgnoreCase(USER_INFO))
		{
			_userLevel = p.getInt("level");
		}
		else if (p.getName().equalsIgnoreCase(MAGIC_SKILL_USE))
		{
			int obj_id = p.getInt(L2World.OBJECT_ID);

			L2NpcInfo npc = _world.getNpc(obj_id);
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
				npc.addSkill(new L2SkillInfo(id, level, reuse, hitTime));
			}
		}
		else if (p.getName().equalsIgnoreCase(MY_TARGET_SELECTED))
		{
			int obj_id = p.getInt(L2World.OBJECT_ID);

			L2NpcInfo npc = _world.getNpc(obj_id);
			if(npc == null)
			{
			 	return;
			}

			int diff = p.getInt("diff");
			int level = _userLevel - diff;

			npc.setLevel(level);
		}
		else if (p.getName().equalsIgnoreCase(STATUS_UPDATE))
		{
			int obj_id = p.getInt(L2World.OBJECT_ID);

			L2NpcInfo npc = _world.getNpc(obj_id);
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
		String fileName = "./saves/npc_data/npc_sniff_%d.xml";
		while (new File(String.format(fileName, id)).exists())
		{
			id ++;
		}

		try
		{
			FileWriter writer = new FileWriter(String.format(fileName, id));
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<list>\n");
			for(L2NpcInfo npc : _world.valuesNpc())
			{
				writer.write(npc.toXML());
			}
			writer.write("</list>");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
