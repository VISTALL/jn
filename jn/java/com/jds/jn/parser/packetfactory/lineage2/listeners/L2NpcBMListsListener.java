package com.jds.jn.parser.packetfactory.lineage2.listeners;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

import com.jds.jn.holders.ItemNameHolder;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.parser.packetfactory.lineage2.L2World;
import com.jds.jn.parser.packetfactory.lineage2.infos.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:33:17/31.07.2010
 */
public class L2NpcBMListsListener implements IPacketListener
{
	private static final String MULTI_SELL_LIST = "MultiSellList";
	private static final String EX_BUY_SELL_LIST = "ExBuySellListPacket";

	// values
	private static final String LIST_ID = "listId";
	private static final String BUY_SELL_REFUND = "buy/sell-refund";
	private static final String ENTRY_ID = "entryId";
	private static final String ENTRY = "entry";

	private Map<Integer, L2MultiSell> _multiSells = new TreeMap<Integer, L2MultiSell>();

	public L2NpcBMListsListener(L2World w)
	{
		//
	}

	@Override
	public void invoke(DecryptPacket p)
	{
		if (p.getName().equalsIgnoreCase(MULTI_SELL_LIST))
		{
			int listId = p.getInt(LIST_ID);
			L2MultiSell multisell = _multiSells.get(listId);
			if (multisell == null)
			{
				multisell = new L2MultiSell(listId);
				_multiSells.put(listId, multisell);
			}

			DataForPart entryList = (DataForPart) p.getRootNode().getPartByName(ENTRY);

			for (DataForBlock entryNode : entryList.getNodes())
			{
				int entryId = ((VisualValuePart) entryNode.getPartByName(ENTRY_ID)).getValueAsInt();
				L2MultiSellEntry entry = new L2MultiSellEntry(entryId);

				DataForPart productions = (DataForPart) entryNode.getPartByName("productions");
				DataForPart ingridients = (DataForPart) entryNode.getPartByName("ingridients");

				for (DataForBlock block : productions.getNodes())
				{
					int itemId = ((VisualValuePart) block.getPartByName("itemId")).getValueAsInt();
					long count = ((VisualValuePart) block.getPartByName("count")).getValueAsLong();

					entry.addProduction(new L2ItemComponent(itemId, count));
				}

				for (DataForBlock block : ingridients.getNodes())
				{
					int itemId = ((VisualValuePart) block.getPartByName("itemId")).getValueAsInt();
					long count = ((VisualValuePart) block.getPartByName("count")).getValueAsLong();

					entry.addIngridient(new L2ItemComponent(itemId, count));
				}

				multisell.addEntry(entry);
			}
		}
		else if (p.getName().equalsIgnoreCase(EX_BUY_SELL_LIST))
		{
			DataTreeNode switchBlock = p.getRootNode().getPartByName(BUY_SELL_REFUND);

			System.out.println(switchBlock.getClass().getName());
		}
	}

	@Override
	public void close()
	{
		for (L2MultiSell multisell : _multiSells.values())
		{
			try
			{
				FileWriter writer = new FileWriter("./saves/npc_multisell/" + multisell.getId() + ".xml");
				writer.write("<?xml version='1.0' encoding='utf-8'?>\n");
				writer.write("<list>\n");
				writer.write(String.format("\t<multisell id=\"%d\">\n", multisell.getId()));
				for(L2MultiSellEntry entry : multisell.getEntries())
				{
			   		writer.write(String.format("\t\t<item id=\"%d\">\n", entry.getId()));
					for(L2ItemComponent ingridient : entry.getIngridients())
					{
						writer.write(String.format("\t\t\t<ingredient id=\"%d\" count=\"%d\" /> <!--%s-->\n", ingridient.getItemId(), ingridient.getCount(), ItemNameHolder.getInstance().name(ingridient.getItemId())));
					}

					for(L2ItemComponent production : entry.getProductions())
					{
						writer.write(String.format("\t\t\t<production id=\"%d\" count=\"%d\" /> <!--%s-->\n", production.getItemId(), production.getCount(), ItemNameHolder.getInstance().name(production.getItemId())));
					}
					writer.write("\t\t</item>\n");
				}
				writer.write("</list>");
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
