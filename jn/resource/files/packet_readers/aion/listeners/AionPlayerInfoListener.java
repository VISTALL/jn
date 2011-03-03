package packet_readers.aion.listeners;

import java.util.Collections;
import java.util.List;

import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import packet_readers.aion.AionWorld;

/**
 * @author VISTALL
 * @date 17:23/15.02.2011
 */
public class AionPlayerInfoListener implements IPacketListener
{
	private static final String SM_PLAYER_SPAWN = "SM_PLAYER_SPAWN";
	private static final String SM_TARGET_SELECTED = "SM_TARGET_SELECTED";
	private AionWorld _world;

	public AionPlayerInfoListener(AionWorld world)
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
		if(p.getName().equalsIgnoreCase(SM_PLAYER_SPAWN))
		{
			int worldId = p.getInt("worldId");
			_world.setWorldId(worldId);
		}
		else if(p.getName().equalsIgnoreCase(SM_TARGET_SELECTED))
		{
			_world.setSelectTargetObjectId(p.getInt("targetObjId"));
		}
	}

	@Override
	public void close()
	{

	}
}
