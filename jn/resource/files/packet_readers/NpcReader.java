package packet_readers;

import javolution.util.FastList;
import com.jds.jn.Jn;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.parser.packetreader.PacketReader;
import com.jds.nio.buffer.NioBuffer;

import javax.swing.*;
import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 16.09.2009
 * Time: 14:39:34
 */
public class NpcReader implements PacketReader
{
	private String _text;

	@Override
	public void show()
	{
		JDialog dlg = new JDialog(Jn.getInstance(), "Npc Viewer for: Aion");
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dlg.setSize(350, 400);
		dlg.setLocationRelativeTo(Jn.getInstance());

		JEditorPane text = new JEditorPane();
		text.setEditable(false);
		text.setContentType("text/xml"); //is text/xml ???exists*??
		text.setText(_text);

		dlg.add(new JScrollPane(text));
		dlg.setVisible(true);
	}

	@Override
	public boolean read(DataPacket packet)
	{
		NioBuffer buf = NioBuffer.wrap(packet.getData());
		buf.order(ByteOrder.LITTLE_ENDIAN);
		float x = buf.getFloat();
		float y = buf.getFloat();
		float z = buf.getFloat();
		buf.getInt(); //objectId
		int npcId = buf.getInt();
		buf.getInt();
		buf.get();
		buf.get();
		buf.get();
		int h = buf.get() & 0xFF;
		int nameID = buf.getInt();
		int titleId = buf.getInt();
		buf.getInt();
		buf.getInt();
		buf.getInt();
		buf.getInt();
		buf.getShort();
		int level = buf.get() & 0xFF;
		int count = Integer.bitCount(buf.getShort() & 0xFFFF);
		FastList<Integer> itemsIds = new FastList<Integer>();

		for (int i = 0; i < count; i++)
		{
			itemsIds.add(buf.getInt());
			buf.getInt();
			buf.getInt();
		}

		buf.getFloat();
		float height = buf.getFloat();
		float speed = buf.getFloat();

		_text = "<?xml version='1.0' encoding='utf-8'?>\n" + "<list>\n" + " <npc id=\"" + npcId + "\" nameId=\"" + nameID + "\" titleId=\"" + titleId + "\" level=\"" + level + "\">\n" + "  <set name=\"height\" val=\"" + height + "\" /> \n" + "  <set name=\"baseSpeed\" val=\"" + speed + "\" /> \n" + "  <spawnlist>\n" + "   <spawn worldid=\"TODO\" respawnDelay=\"60\" periodOfDay=\"0\">\n" + "    <poslist>\n" + "     <pos x=\"" + x + "\" y=\"" + y + "\" z=\"" + z + "\" h=\"" + h + "\" />\n" + "    </poslist>\n" + "   </spawn>\n" + "  </spawnlist>\n";
		if (itemsIds.size() != 0)
		{
			_text += "   <items>\n";
			for (int itemId : itemsIds)
			{
				_text += "    <item itemId=\"" + itemId + "\" />\n";
			}
			_text += "   </items>\n";
		}

		_text += " </npc>\n" + "</list>\n";
		return true;
	}

}
