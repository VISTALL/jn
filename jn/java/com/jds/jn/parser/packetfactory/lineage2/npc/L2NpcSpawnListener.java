package com.jds.jn.parser.packetfactory.lineage2.npc;

import gnu.trove.TIntObjectHashMap;

import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:56:57/05.06.2010
 */
public class L2NpcSpawnListener implements IPacketListener
{
	private static final String NPC_INFO = "NpcInfo";

	//values
	private static final String OBJECT_ID = "obj_id";

	private TIntObjectHashMap<NpcInfo> _npcInfos = new TIntObjectHashMap<NpcInfo>();

	@Override
	public void invoke(DataPacket p)
	{
		if (p == null || p.getPacketFormat() == null || p.getName() == null)
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
	}
}
