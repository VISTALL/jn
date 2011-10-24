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

package packet_readers.lineage2.infos;

import java.util.ArrayList;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.datatree.DataForBlock;
import com.jds.jn.parser.datatree.DataForPart;
import com.jds.jn.parser.datatree.DataMacroPart;
import com.jds.jn.parser.datatree.DataTreeNode;
import com.jds.jn.parser.datatree.VisualValuePart;

/**
 * @author VISTALL
 * @date 19:18/24.10.2011
 */
public class L2NpcSayInfo
{
	private int _id;
	private L2ChatType _chatType;
	private List<String> _params = new ArrayList<String>(5);

	public L2NpcSayInfo(DecryptedPacket packet)
	{
		_chatType = L2ChatType.VALUES[packet.getInt("type")];
		DataMacroPart macroPart = (DataMacroPart)packet.getRootNode().getPartByName("npc_string_container");

		_id = ((VisualValuePart)macroPart.getPartByName("npc_string_id")) .getValueAsInt();
		for(DataTreeNode node : macroPart.getNodes())
		{
			if(node instanceof DataForPart)
			{
				DataForPart forPart = (DataForPart)node;
				for(DataForBlock b : forPart.getNodes())
					_params.add(((VisualValuePart)b.getPartByName("arg")).getValueAsString());
				break;
			}
		}
	}

	@Override
	public boolean equals(Object o)
	{
		return hashCode() == o.hashCode();
	}

	@Override
	public int hashCode()
	{
		return _id;
	}

	public int getId()
	{
		return _id;
	}

	public L2ChatType getChatType()
	{
		return _chatType;
	}

	public List<String> getParams()
	{
		return _params;
	}
}
