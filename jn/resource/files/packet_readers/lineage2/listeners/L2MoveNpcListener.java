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
import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.holders.NpcNameHolder;
import packet_readers.lineage2.infos.L2Loc;
import packet_readers.lineage2.infos.L2NpcInfo;
import packet_readers.lineage2.infos.L2Object;

/**
 * @author VISTALL
 * @date 19:54/24.10.2011
 */
public class L2MoveNpcListener extends L2AbstractListener
{
	@Override
	public List<String> getPackets()
	{
		return Collections.singletonList("CharMoveToLocation");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		L2Object o = _world.getObject(p.getInt(L2World.OBJECT_ID));
		if(!(o instanceof L2NpcInfo))
			return;

		L2NpcInfo npcInfo = (L2NpcInfo)o;

		L2Loc loc = new L2Loc(p.getInt("desc_x"), p.getInt("desc_y"), p.getInt("desc_z"), 0);

		npcInfo.getMoveLocs().add(loc);
	}

	@Override
	public void closeImpl() throws IOException
	{
		Document document = DocumentHelper.createDocument();
		Element listElement = document.addElement("list");

		for(L2NpcInfo info : _world.valuesNpc())
		{
			if(info.getMoveLocs().isEmpty())
				continue;

			Element npcElement = listElement.addElement("npc");
			npcElement.addAttribute("id", String.valueOf(info.getNpcId()));
			npcElement.addComment(NpcNameHolder.getInstance().name(info.getNpcId()));

			for(L2Loc loc : info.getMoveLocs())
			{
				Element element = npcElement.addElement("loc");
				element.addAttribute("x", String.valueOf(loc.getX()));
				element.addAttribute("y", String.valueOf(loc.getY()));
				element.addAttribute("z", String.valueOf(loc.getZ()));
			}
		}

		if(listElement.elements().isEmpty())
			return;

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent("\t");
		XMLWriter writer = new XMLWriter(new FileWriter(getLogFile("./move_locs ", "xml")), format);
		writer.write(document);
		writer.close();
	}
}
