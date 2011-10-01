package packet_readers.aion.listeners;

import java.util.Arrays;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.aion.AionAbstractListener;
import packet_readers.aion.infos.AionNpc;

/**
 * @author VISTALL
 * @date 17:23/15.02.2011
 */
public class AionPlayerInfoListener extends AionAbstractListener
{
	private static final String SM_PLAYER_SPAWN = "SM_PLAYER_SPAWN";
	private static final String SM_TARGET_SELECTED = "SM_TARGET_SELECTED";

	public AionPlayerInfoListener()
	{
		//
	}

	@Override
	public List<String> getPackets()
	{
		return Arrays.asList(SM_PLAYER_SPAWN, SM_TARGET_SELECTED);
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		if(p.getName().equalsIgnoreCase(SM_PLAYER_SPAWN))
		{
			int worldId = p.getInt("worldId");
			_world.setWorldId(worldId);
		}
		else if(p.getName().equalsIgnoreCase(SM_TARGET_SELECTED))
		{
			AionNpc npc = _world.getNpc(p.getInt("targetObjId"));
			if(npc == null)
				return;

			if(_world.isOnSelectTarget())
				npc.setValid(true);
		}
	}
}
