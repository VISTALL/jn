package packet_readers.aion.listeners;

import java.util.Collections;
import java.util.List;

import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import packet_readers.aion.AionWorld;
import packet_readers.aion.infos.AionLoc;
import packet_readers.aion.infos.AionNpc;

/**
 * @author VISTALL
 * @date 14:16/15.02.2011
 */
public class AionNpcInfoListener  implements IPacketListener
{
	public static final String SM_NPC_INFO = "SM_NPC_INFO";
	private AionWorld _world;

	public AionNpcInfoListener(AionWorld world)
	{
		_world = world;
	}

	@Override
	public List<JRibbonBand> getRibbonBands()
	{
		return Collections.emptyList();
	}

	@Override
	public void invoke(DecryptedPacket p)
	{
		if(p.getName().equalsIgnoreCase(SM_NPC_INFO))
		{
			int objId = p.getInt("objId");
			AionNpc npc = _world.getNpc(objId);
			if(npc == null)
			{
				npc = new AionNpc(p);
				_world.addNpc(objId, npc);
				if(_world.isOnSelectTarget())
				{
					if(_world.getSelectTargetObjectId() == objId)
						_world.addNpcByNpcId(npc.getNpcId(), npc);
				}
				else
				{
					_world.addNpcByNpcId(npc.getNpcId(), npc);
				}
			}

			float x = p.getFloat("x");
			float y = p.getFloat("y");
			float z = p.getFloat("z");
			int npcHeading = p.getInt("npcHeading");
			int spawnStaticId = p.getInt("spawnStaticId");

			AionLoc loc = new AionLoc(_world.getWorldId(), x, y, z, npcHeading, spawnStaticId);
			npc.addLoc(objId, loc);
		}
	}

	@Override
	public void close()
	{

	}
}
