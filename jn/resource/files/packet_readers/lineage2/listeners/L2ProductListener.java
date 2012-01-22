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
import java.util.Arrays;
import java.util.List;

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
import com.jds.jn.parser.datatree.VisualValuePart;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.infos.L2Product;
import packet_readers.lineage2.infos.L2ProductComponent;

/**
 * @author VISTALL
 * @date 19:00/22.01.2012
 */
public class L2ProductListener extends L2AbstractListener
{
	private IntObjectMap<L2Product> _products = new TreeIntObjectMap<L2Product>();

	@Override
	public List<String> getPackets()
	{
		return Arrays.asList("ExBR_ProductList", "ExBR_ProductInfo");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		if(p.getName().equals("ExBR_ProductList"))
		{
			DataForPart entryList = (DataForPart) p.getRootNode().getPartByName("list");

			for (DataForBlock entryNode : entryList.getNodes())
			{
				int id = ((VisualValuePart) entryNode.getPartByName("product-id")).getValueAsInt();
				if(_products.containsKey(id))
					continue;

				L2Product product = new L2Product(entryNode);

				_products.put(product.getId(), product);
			}

		}
		else
		{
			int productId = p.getInt("product-id");
			L2Product product = _products.get(productId);
			if(product == null)
				return;

			DataForPart entryList = (DataForPart) p.getRootNode().getPartByName("list");

			for (DataForBlock entryNode : entryList.getNodes())
			{
				int itemId = entryNode.getInt("item-id");
				int count = entryNode.getInt("count");

				product.getItems().add(new L2ProductComponent(itemId, count));
			}
		}
	}

	@Override
	public void closeImpl() throws IOException
	{
		Document document = DocumentHelper.createDocument();
		Element listElement = document.addElement("list");

		for(L2Product product : _products.values())
		{
			Element productElement = listElement.addElement("product");
			productElement.addAttribute("id", String.valueOf(product.getId()));
			productElement.addAttribute("price", String.valueOf(product.getPrice()));
			productElement.addAttribute("category", String.valueOf(product.getCategory()));
			productElement.addAttribute("tab-id", String.valueOf(product.getTabId()));
			productElement.addAttribute("start-sell-time", TIME_FORMAT.format(product.getStartSellDate()));
			productElement.addAttribute("stop-sell-time", TIME_FORMAT.format(product.getStopSellDate()));
			productElement.addAttribute("start-sell-hour", String.valueOf(product.getStartSellHour()));
			productElement.addAttribute("start-sell-minute", String.valueOf(product.getStartSellMinute()));
			productElement.addAttribute("stop-sell-hour", String.valueOf(product.getStopSellHour()));
			productElement.addAttribute("stop-sell-minute", String.valueOf(product.getStopSellMinute()));
			productElement.addAttribute("week-of-day", String.valueOf(product.getWeekOfDay()));
			productElement.addAttribute("max-stock", String.valueOf(product.getStock()));

			for(L2ProductComponent itemInfo : product.getItems())
			{
				Element element = productElement.addElement("item");
				element.addAttribute("id", String.valueOf(itemInfo.getItemId()));
				element.addAttribute("count", String.valueOf(itemInfo.getCount()));
			}
		}

		if(listElement.elements().isEmpty())
			return;

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent("\t");
		XMLWriter writer = new XMLWriter(new FileWriter(getLogFile("./X-productlist ", "xml")), format);
		writer.write(document);
		writer.close();
	}
}
