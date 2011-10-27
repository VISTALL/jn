package packet_readers.lineage2.listeners;

import java.util.Arrays;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.infos.L2DialogInfo;
import packet_readers.lineage2.infos.L2DialogObject;

/**
 * @author VISTALL
 * @date  20:40:44/31.07.2010
 */
public class L2NpcDialogListener extends L2AbstractListener
{
	private static final String NPC_HTML_MESSAGE = "NpcHtmlMessage";
	private static final String NPC_QUEST_HTML_MESSAGE = "ExNpcQuestHtmlMessage";
	// values
	private static final String HTML = "html";
	private static final String QUEST_ID = "quest_id";

	public L2NpcDialogListener()
	{

	}

	@Override
	public List<String> getPackets()
	{
		return Arrays.asList(NPC_HTML_MESSAGE, NPC_QUEST_HTML_MESSAGE);
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		if (p.getName().equalsIgnoreCase(NPC_HTML_MESSAGE))
		{
			int objectId = p.getInt(L2World.OBJECT_ID);
			String html = p.getString(HTML);
			L2DialogObject npc = (L2DialogObject)_world.getObject(objectId);
			if (npc != null)
				npc.addDialog(new L2DialogInfo(0, html));
		}
		else if (p.getName().equalsIgnoreCase(NPC_QUEST_HTML_MESSAGE))
		{
			int objectId = p.getInt(L2World.OBJECT_ID);
			int questId = p.getInt(QUEST_ID);
			String html = p.getString(HTML);
			L2DialogObject npc = (L2DialogObject)_world.getObject(objectId);
			if (npc != null)
				npc.addDialog(new L2DialogInfo(questId, html));
		}
	}

}
