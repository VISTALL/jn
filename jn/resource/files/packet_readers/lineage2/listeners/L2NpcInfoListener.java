package packet_readers.lineage2.listeners;

import java.util.Collections;
import java.util.List;

import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.infos.L2NpcInfo;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:05:17/31.07.2010
 */
public class L2NpcInfoListener   implements IPacketListener
{
	private static final String NPC_INFO = "NpcInfo";

	private L2World _world;

	public L2NpcInfoListener(L2World w)
	{
		_world = w;
	}

	@Override
	public List<JRibbonBand> getRibbonBands()
	{
		return Collections.emptyList();
	}

	@Override
	public void invoke(DecryptedPacket p)
	{
		if(p.getName().equalsIgnoreCase(NPC_INFO))
		{
			int objectId = p.getInt(L2World.OBJECT_ID);

			L2NpcInfo npc = _world.getNpc(objectId);
			if(npc == null)
			{
				npc = new L2NpcInfo(p);
				_world.addNpc(objectId, npc);
			}

			L2NpcInfo list = _world.getNpcByNpcId(npc.getNpcId());
			if(list == null)
			{
				list = npc;
				_world.addNpcByNpcId(npc.getNpcId(), list);
			}
		}
	}

	@Override
	public void close()
	{

	}
}
