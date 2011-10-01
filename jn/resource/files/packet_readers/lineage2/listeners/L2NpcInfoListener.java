package packet_readers.lineage2.listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.infos.L2DialogInfo;
import packet_readers.lineage2.infos.L2NpcInfo;
import packet_readers.lineage2.infos.L2SpawnLocInfo;

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

		L2NpcInfo list = _world.getNpcByNpcId(npc.getNpcId());
		if(list == null)
		{
			list = npc;
			_world.addNpcByNpcId(npc.getNpcId(), list);
		}
		else
			list.getSpawnLoc().add(new L2SpawnLocInfo(p));
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
