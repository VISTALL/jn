package packet_readers.lineage2.listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.infos.L2DialogInfo;
import packet_readers.lineage2.infos.L2Loc;
import packet_readers.lineage2.infos.L2NpcInfo;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:05:17/31.07.2010
 */
public class L2NpcInfoListener extends L2AbstractListener
{
	public L2NpcInfoListener()
	{

	}

	@Override
	public List<String> getPackets()
	{
		return Collections.singletonList("NpcInfo");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		int objectId = p.getInt(L2World.OBJECT_ID);

		L2NpcInfo npc = (L2NpcInfo)_world.getObject(objectId);
		if(npc == null)
		{
			npc = new L2NpcInfo(p);
			_world.addObject(objectId, npc);
		}

		L2NpcInfo npcInfo = _world.getNpcByNpcId(npc.getNpcId());
		if(npcInfo == null)
		{
			npcInfo = npc;
			_world.addNpcByNpcId(npc.getNpcId(), npcInfo);
		}
		else
		{
			L2Loc loc = new L2Loc(p);
			if(!npcInfo.getMoveLocs().contains(loc))
				npcInfo.getSpawnLoc().add(loc);
		}
	}

	@Override
	public void closeImpl() throws IOException
	{
		for (L2NpcInfo npc : _world.valuesNpc())
			for (L2DialogInfo d : npc.getDialogs())
			{
				FileWriter writer = new FileWriter(getLogFile(d.getQuestId() == 0 ? ("npc_dialogs/" + npc.getNpcId() + "-") : ("npc_dialogs/" + npc.getNpcId() + "-quest-" + d.getQuestId()), "htm"));
				writer.write(d.getDialog());
				writer.close();
			}
	}
}
