package packet_readers.lineage2.listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.holders.NpcNameHolder;
import packet_readers.lineage2.infos.L2NpcInfo;
import packet_readers.lineage2.infos.L2Object;
import packet_readers.lineage2.infos.L2SpawnLocInfo;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  17:56:57/05.06.2010
 */
public class L2NpcSpawnListener extends L2AbstractListener
{
	private static final String USER_INFO = "UserInfo";
	private static final String MY_TARGET_SELECTED = "MyTargetSelected";

	//values
   	private int _userLevel;

	public L2NpcSpawnListener()
	{

	}

	@Override
	public List<String> getPackets()
	{
		return Arrays.asList(USER_INFO, MY_TARGET_SELECTED);
	}
 
	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		if (p.getName().equalsIgnoreCase(USER_INFO))
			_userLevel = p.getInt("level");
		else if (p.getName().equalsIgnoreCase(MY_TARGET_SELECTED))
		{
			int obj_id = p.getInt(L2World.OBJECT_ID);

			L2Object object = _world.getObject(obj_id);
			if(!(object instanceof L2NpcInfo))
			 	return;

			int diff = p.getInt("diff");
			int level = _userLevel - diff;

			((L2NpcInfo) object).setLevel(level);
		}
	}

	@Override
	public void closeImpl() throws IOException
	{
		Collection<L2NpcInfo> npcs = _world.valuesNpc();
		FileWriter writer = new FileWriter(getLogFile("npc_data/", "xml"));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<list>\n");
		for(L2NpcInfo npc : npcs)
			writer.write(npc.toXML());
		writer.write("</list>");
		writer.close();

		writer = new FileWriter(getLogFile("spawn_data/", "xml"));
		writer.write(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<list>\n");
		for(L2NpcInfo npc : npcs)
		{
			for(L2SpawnLocInfo spawnLocInfo : npc.getSpawnLoc())
			{
				String text =
						"\t<spawn count=\"1\" respawn=\"60\" respawn_random=\"0\" period_of_day=\"none\">\n" +
						"\t\t<point x=\"%d\" y=\"%d\" z=\"%d\" h=\"%d\" />\n" +
						"\t\t<npc id=\"%d\" /><!--%s-->\n" +
						"\t</spawn>\n";
				writer.write(String.format(text, spawnLocInfo.getX(), spawnLocInfo.getY(), spawnLocInfo.getZ(), spawnLocInfo.getH(), npc.getNpcId(), NpcNameHolder.getInstance().name(npc.getNpcId())));
			}
		}
		writer.write("</list>");
		writer.close();
	}
}
