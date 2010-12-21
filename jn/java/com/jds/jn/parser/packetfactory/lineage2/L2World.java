package com.jds.jn.parser.packetfactory.lineage2;

import java.util.*;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.parser.packetfactory.lineage2.infos.L2NpcInfo;
import com.jds.jn.parser.packetfactory.lineage2.listeners.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:29:53/31.07.2010
 */
public class L2World implements IPacketListener
{
	private static L2World _instance;

	public static L2World getInstance()
	{
		if(_instance == null)
		{
			_instance = new L2World();
		}

		return _instance;
	}

	public static final String OBJECT_ID = "obj_id";
	//listeners
	private List<IPacketListener> _listeners = new ArrayList<IPacketListener>(5);

	// npcs
	private Map<Integer, L2NpcInfo> _npcInfos = new HashMap<Integer, L2NpcInfo>();
	private Map<Integer, L2NpcInfo> _npcInfosByNpcId = new TreeMap<Integer, L2NpcInfo>();

	public L2World()
	{
		_listeners.add(new L2NpcSpawnListener(this));
		_listeners.add(new L2NpcDialogListener(this));
		_listeners.add(new L2NpcInfoListener(this));
		_listeners.add(new L2NpcBMListsListener(this));
		_listeners.add(new L2AirShipTeleportListListener());
	}

	@Override
	public void invoke(DecryptedPacket p)
	{
		if (p == null || p.getPacketInfo() == null || p.getName() == null || p.hasError())
		{
			return;
		}

		for (IPacketListener l : _listeners)
		{
			l.invoke(p);
		}
	}

	@Override
	public void close()
	{
		for (IPacketListener l : _listeners)
		{
			l.close();
		}
	}

	//===========================================================================================
	// 				Npcs
	//===========================================================================================
	public void addNpc(int obj, L2NpcInfo npc)
	{
		_npcInfos.put(obj, npc);
	}

	public void addNpcByNpcId(int npcId, L2NpcInfo npc)
	{
		_npcInfosByNpcId.put(npcId, npc);
	}

	public L2NpcInfo getNpc(int obj)
	{
		return _npcInfos.get(obj);
	}

	public L2NpcInfo getNpcByNpcId(int obj)
	{
		return _npcInfosByNpcId.get(obj);
	}

	public Collection<L2NpcInfo> valuesNpc()
	{
		return _npcInfosByNpcId.values();
	}
}
