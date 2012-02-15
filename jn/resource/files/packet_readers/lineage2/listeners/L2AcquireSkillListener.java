/*
 * Jn (Java sNiffer)
 * Copyright (C) 2012 napile.org
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
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.datatree.DataForBlock;
import com.jds.jn.parser.datatree.DataForPart;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.holders.SkillNameHolder;
import packet_readers.lineage2.infos.L2AcquireType;
import packet_readers.lineage2.infos.L2ItemWithCount;
import packet_readers.lineage2.infos.L2SkillInfo;
import packet_readers.lineage2.infos.L2SkillLearn;

/**
 * @author VISTALL
 * @date 10:41/12.02.2012
 */
public class L2AcquireSkillListener extends L2AbstractListener
{
	private final static String EX_ACQUIRE = "ExAcquirableSkillListByClass";
	private final static String ACQUIRE = "AcquireSkillInfo";

	private IntObjectMap<Map<L2AcquireType, Set<L2SkillLearn>>> _skillTree = new TreeIntObjectMap<Map<L2AcquireType, Set<L2SkillLearn>>>();

	@Override
	public List<String> getPackets()
	{
		return Arrays.asList(EX_ACQUIRE, ACQUIRE);
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		boolean ex = p.getName().equals(EX_ACQUIRE);

		Map<L2AcquireType, Set<L2SkillLearn>> map = _skillTree.get(_world.getUserClassId());
		if(map == null)
			_skillTree.put(_world.getUserClassId(), map = new HashMap<L2AcquireType, Set<L2SkillLearn>>());

		L2AcquireType type = ex ? L2AcquireType.NORMAL : L2AcquireType.VALUES[p.getInt("type")];
		Set<L2SkillLearn> set = map.get(type);
		if(set == null)
			map.put(type, set = new TreeSet<L2SkillLearn>());

		if(ex)
		{
			DataForPart list = (DataForPart) p.getRootNode().getPartByName("list");
			for (DataForBlock block : list.getNodes())
			{
				int id = block.getInt("id");
				int level = block.getInt("level");
				int cost = block.getInt("cost");
				int minLevel = block.getInt("min-level");

				L2SkillLearn skillLearn = new L2SkillLearn(id, level, minLevel, cost);
				set.add(skillLearn);

				DataForPart needItems = (DataForPart) block.getPartByName("need-items");
				for (DataForBlock block2 : needItems.getNodes())
				{
					int itemId = block2.getInt("item-id");
					long itemCount = block2.getLong("item-count");

					skillLearn.getNeedItems().add(new L2ItemWithCount(itemId, itemCount));
				}

				DataForPart needSkills = (DataForPart) block.getPartByName("need-skills");
				for (DataForBlock block2 : needSkills.getNodes())
				{
					int skillId = block2.getInt("skill-id");
					int skillLevel = block2.getInt("skill-level");

					skillLearn.getNeedSkills().add(new L2SkillInfo(skillId, skillLevel, 0, 0, 0));
				}
			}
		}
		else
		{
			int id = p.getInt("id");
			int level = p.getInt("level");
			int cost = p.getInt("cost");
			int minLevel = _world.getUserLevel();

			L2SkillLearn skillLearn = new L2SkillLearn(id, level, minLevel, cost);
			set.add(skillLearn);

			DataForPart needItems = (DataForPart) p.getRootNode().getPartByName("need-items");
			for (DataForBlock block : needItems.getNodes())
			{
				int itemId = block.getInt("item-id");
				long itemCount = block.getLong("item-count");

				skillLearn.getNeedItems().add(new L2ItemWithCount(itemId, itemCount));
			}
		}
	}

	@Override
	public void closeImpl() throws IOException
	{
		if(_skillTree.isEmpty())
			return;

		Document document = DocumentHelper.createDocument();
		Element listElement = document.addElement("list");

		for(IntObjectMap.Entry<Map<L2AcquireType, Set<L2SkillLearn>>> entry : _skillTree.entrySet())
		{
			Element classIdElement = listElement.addElement("class_id");
			classIdElement.addAttribute("id", String.valueOf(entry.getKey()));

			for(Map.Entry<L2AcquireType, Set<L2SkillLearn>> sEntry : entry.getValue().entrySet())
			{
				Element acquireTypeElement = classIdElement.addElement("type");
				acquireTypeElement.addAttribute("id", sEntry.getKey().name());

				for(L2SkillLearn val : sEntry.getValue())
				{
					Element skillElement = acquireTypeElement.addElement("skill");
					skillElement.addAttribute("id", String.valueOf(val.getId()));
					skillElement.addAttribute("level", String.valueOf(val.getLevel()));
					skillElement.addAttribute("min-level", String.valueOf(val.getMinLevel()));
					skillElement.addAttribute("cost", String.valueOf(val.getCost()));
					skillElement.addComment(SkillNameHolder.getInstance().name(val.getId(), val.getLevel()));

					for(L2ItemWithCount item : val.getNeedItems())
					{
						Element needItemElement = skillElement.addElement("need_item");
						needItemElement.addAttribute("id", String.valueOf(item.getItemId()));
						needItemElement.addAttribute("count", String.valueOf(item.getCount()));
						//needItemElement.addComment(ItemNameHolder.getInstance().name(item.getItemId()));
					}

					for(L2SkillInfo skill : val.getNeedSkills())
					{
						Element needSkillElement = skillElement.addElement("need_skill");
						needSkillElement.addAttribute("id", String.valueOf(skill.getId()));
						needSkillElement.addAttribute("level", String.valueOf(skill.getLevel()));
						//needSkillElement.addComment(SkillNameHolder.getInstance().name(skill.getId(), skill.getLevel()));
					}
				}
			}
		}

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent("\t");
		format.setExpandEmptyElements(false);
		XMLWriter writer = new XMLWriter(new FileWriter(getLogFile("./skilltree/ ", "xml")), format);
		writer.write(document);
		writer.close();
	}

}
