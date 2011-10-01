package packet_readers.aion;

import java.util.Collection;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CTreeIntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;
import packet_readers.aion.holders.ClientStringHolder;
import packet_readers.aion.infos.AionNpc;

/**
 * @author VISTALL
 * @date 13:56/15.02.2011
 */
public class AionWorld
{
	private IntObjectMap<AionNpc> _npcInfos = new TreeIntObjectMap<AionNpc>();
	private IntObjectMap<AionNpc> _npcInfosByNpcId = new CTreeIntObjectMap<AionNpc>();

	private boolean _onSelectTarget;
	private int _worldId;


	public AionWorld()
	{
		ClientStringHolder.getInstance();
	}

	public boolean isOnSelectTarget()
	{
		return _onSelectTarget;
	}

	public int getWorldId()
	{
		return _worldId;
	}

	public void setWorldId(int worldId)
	{
		_worldId = worldId;
	}

	public void setOnSelectTarget(boolean onSelectTarget)
	{
		_onSelectTarget = onSelectTarget;
	}

	//===========================================================================================
	// 				Npcs
	//===========================================================================================

	public void addNpc(int obj, AionNpc npc)
	{
		_npcInfos.put(obj, npc);
	}

	public void addNpcByNpcId(int npcId, AionNpc npc)
	{
		_npcInfosByNpcId.put(npcId, npc);
	}

	public AionNpc getNpc(int obj)
	{
		return _npcInfos.get(obj);
	}

	public AionNpc getNpcByNpcId(int npcId)
	{
		return _npcInfosByNpcId.get(npcId);
	}

	public Collection<AionNpc> valuesNpc()
	{
		return _npcInfosByNpcId.values();
	}

	public void clear()
	{
		_npcInfos.clear();
		_npcInfosByNpcId.clear();
	}

}
