package packet_readers.lineage2.listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;
import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.infos.L2DialogInfo;
import packet_readers.lineage2.infos.L2Object;
import packet_readers.lineage2.infos.L2ServerObjectInfo;

/**
 * @author VISTALL
 * @date 2:04/01.10.2011
 */
public class L2ServerObjectInfoListener extends L2AbstractListener
{
	private IntObjectMap<L2ServerObjectInfo> _objects = new TreeIntObjectMap<L2ServerObjectInfo>();

	@Override
	public List<String> getPackets()
	{
		return Collections.singletonList("ServerObjectInfo");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		int objectId = p.getInt(L2World.OBJECT_ID);

		L2Object object = _world.getObject(objectId);
		if(object == null)
		{
			L2ServerObjectInfo objectInfo = new L2ServerObjectInfo(p);
			_world.addObject(objectId, objectInfo);
			_objects.put(objectId, objectInfo);
		}
	}

	@Override
	public void closeImpl() throws IOException
	{
		for(L2ServerObjectInfo npc : _objects.values())
			for(L2DialogInfo d : npc.getDialogs())
			{
				FileWriter writer = new FileWriter(getLogFile(d.getQuestId() == 0 ? ("server-object-dialogs/" + npc.getNpcId()) : ("server-object-dialogs/" + npc.getNpcId() + "-quest-" + d.getQuestId()), "htm"));
				writer.write(d.getDialog());
				writer.close();
			}

		FileWriter writer = new FileWriter(getLogFile("server-object-data/", "xml"));
		writer.write("<list>\n");
		for(L2ServerObjectInfo objectInfo : _objects.values())
			writer.write(String.format("\t<object npc_id=\"%d\" type=\"%d\" col_radius=\"%f\" col_height=\"%f\" max_hp=\"%d\" statistic_type=\"%d\" x=\"%d\" y=\"%d\" z=\"%d\" h=\"%d\"/>\n",
					objectInfo.getNpcId(), objectInfo.getType(), objectInfo.getColRadius(), objectInfo.getColHeight(), objectInfo.getMaxHp(), objectInfo.getStatisticType(), objectInfo.getSpawnLoc().getX(), objectInfo.getSpawnLoc().getY(), objectInfo.getSpawnLoc().getZ(), objectInfo.getSpawnLoc().getH()));
		writer.write("</list>");
		writer.close();
	}
}
