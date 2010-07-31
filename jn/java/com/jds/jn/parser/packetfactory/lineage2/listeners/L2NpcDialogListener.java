package com.jds.jn.parser.packetfactory.lineage2.listeners;

import java.io.*;

import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.parser.packetfactory.lineage2.L2World;
import com.jds.jn.parser.packetfactory.lineage2.infos.L2DialogInfo;
import com.jds.jn.parser.packetfactory.lineage2.infos.L2NpcInfo;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:40:44/31.07.2010
 * <packet id="19" name="NpcHtmlMessage">
 * <part name="obj_id" type="d" />
 * <part name="html" type="S">
 * <reader type="HTML" />
 * </part>
 * <part name="item_id" type="d" />
 * </packet>
 * <p/>
 * <packet id="FE;008D" name="ExNpcQuestHtmlMessage">
 * <part name="obj_id" type="d" />
 * <part name="html" type="S">
 * <reader type="HTML" />
 * </part>
 * <part name="quest_id" type="d" />
 * </packet>
 */
public class L2NpcDialogListener implements IPacketListener
{
	private static final String NPC_HTML_MESSAGE = "NpcHtmlMessage";
	private static final String NPC_QUEST_HTML_MESSAGE = "ExNpcQuestHtmlMessage";
	//values
	private static final String HTML = "html";
	private static final String QUEST_ID = "quest_id";

	private L2World _world;

	public L2NpcDialogListener(L2World w)
	{
		_world = w;
	}

	@Override
	public void invoke(DecryptPacket p)
	{
		if (p.getName().equalsIgnoreCase(NPC_HTML_MESSAGE))
		{
			int objectId = p.getInt(L2World.OBJECT_ID);
			String html = p.getString(HTML);
			L2NpcInfo npc = _world.getNpc(objectId);
			if (npc != null)
			{
				npc.addDialog(new L2DialogInfo(0, html));
			}
		}
		else if (p.getName().equalsIgnoreCase(NPC_QUEST_HTML_MESSAGE))
		{
			int objectId = p.getInt(L2World.OBJECT_ID);
			int questId = p.getInt(QUEST_ID);
			String html = p.getString(HTML);
			L2NpcInfo npc = _world.getNpc(objectId);
			if (npc != null)
			{
				npc.addDialog(new L2DialogInfo(questId, html));
			}
		}
	}

	@Override
	public void close()
	{
		for (L2NpcInfo npc : _world.valuesNpc())
		{

			for (L2DialogInfo d : npc.getDialogs())
			{
				String fileName = "";
				if (d.getQuestId() == 0)
				{
					int index = 0;
					fileName = "./saves/npc_dialogs/%d-%d.htm";
					String t;
					while (new File(t = String.format(fileName, npc.getNpcId(), index)).exists())
					{
						index++;
					}

					try
					{
						FileWriter writer = new FileWriter(t);
						writer.write(d.getDialog());
						writer.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					int index = 0;
					fileName = "./saves/npc_dialogs/%d_quest-%d-%d.htm";
					String t;
					while (new File(t = String.format(fileName, npc.getNpcId(), d.getQuestId(), index)).exists())
					{
						index++;
					}

					try
					{
						FileWriter writer = new FileWriter(t);
						writer.write(d.getDialog());
						writer.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}
