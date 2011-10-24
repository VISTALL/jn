/*
 * Jn (Java sNiffer)
 * Copyright (C) 2011 napile.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package packet_readers.lineage2.listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocumentType;
import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.holders.NpcNameHolder;
import packet_readers.lineage2.infos.L2NpcInfo;
import packet_readers.lineage2.infos.L2NpcSayInfo;
import packet_readers.lineage2.infos.jump.JumpTrack;

/**
 * @author VISTALL
 * @date 19:15/24.10.2011
 */
public class L2NpcSayListener extends L2AbstractListener
{
	@Override
	public List<String> getPackets()
	{
		return Collections.singletonList("NpcSay");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		int npcId = p.getInt("npc_id") - 1000000;

		L2NpcInfo info = _world.getNpcByNpcId(npcId);
		if(info == null)
			return;

		info.getSays().add(new L2NpcSayInfo(p));
	}

	@Override
	public void closeImpl() throws IOException
	{
		Document document = DocumentHelper.createDocument();
		Element listElement = document.addElement("list");

		for(L2NpcInfo info : _world.valuesNpc())
		{
			if(info.getSays().isEmpty())
				continue;

			Element npcElement = listElement.addElement("npc");
			npcElement.addAttribute("id", String.valueOf(info.getNpcId()));
			npcElement.addComment(NpcNameHolder.getInstance().name(info.getNpcId()));

			for(L2NpcSayInfo npcSay : info.getSays())
			{
				Element element = npcElement.addElement("npc_say");
				element.addAttribute("string_id", String.valueOf(npcSay.getId()));
				element.addAttribute("type", String.valueOf(npcSay.getChatType()));

				for(String val : npcSay.getParams())
				{
					if(val.isEmpty())
						continue;

					Element valElement = element.addElement("arg");
					valElement.setText(val);
				}
			}
		}

		if(listElement.elements().isEmpty())
			return;

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent("\t");
		XMLWriter writer = new XMLWriter(new FileWriter(getLogFile("./npc_say ", "xml")), format);
		writer.write(document);
		writer.close();
	}
}
