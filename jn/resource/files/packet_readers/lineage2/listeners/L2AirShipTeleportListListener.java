package packet_readers.lineage2.listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.datatree.DataForBlock;
import com.jds.jn.parser.datatree.DataForPart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.packetfactory.IPacketListener;
import packet_readers.lineage2.infos.L2BoatPoint;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:12:10/27.08.2010
 */
public class L2AirShipTeleportListListener implements IPacketListener
{
	private static final String EX_AIR_SHIP_TELEPORT_LIST = "ExAirShipTeleportList";

	private Map<Integer, Map<Integer, L2BoatPoint>> _list = new TreeMap<Integer, Map<Integer, L2BoatPoint>>();

	@Override
	public void invoke(DecryptedPacket p)
	{
		if (p.getName().equalsIgnoreCase(EX_AIR_SHIP_TELEPORT_LIST))
		{
			int dockId = p.getInt("dockId");
			if (_list.containsKey(dockId))
			{
				return;
			}
			Map<Integer, L2BoatPoint> l = new TreeMap<Integer, L2BoatPoint>();

			DataForPart list = (DataForPart) p.getRootNode().getPartByName("list");

			for (DataForBlock block : list.getNodes())
			{
				int index = ((VisualValuePart) block.getPartByName("index")).getValueAsInt();
				int x = ((VisualValuePart) block.getPartByName("x")).getValueAsInt();
				int y = ((VisualValuePart) block.getPartByName("y")).getValueAsInt();
				int z = ((VisualValuePart) block.getPartByName("z")).getValueAsInt();
				int fuel = ((VisualValuePart) block.getPartByName("fuel")).getValueAsInt();
				L2BoatPoint point = new L2BoatPoint(x, y, z, fuel);

				l.put(index, point);
			}

			_list.put(dockId, l);
		}
	}

	@Override
	public void close()
	{
		try
		{
			FileWriter writer = new FileWriter("./saves/air_docks.xml");
			writer.write("<?xml version='1.0' encoding='utf-8'?>\n");
			writer.write("<!--Create by VISTALL-->\n");
			writer.write("<list>\n");
			for(Entry<Integer, Map<Integer, L2BoatPoint>> e : _list.entrySet())
			{
				writer.write(String.format("\t<dock id=\"%d\">\n", e.getKey()));
				for (Entry<Integer, L2BoatPoint> point : e.getValue().entrySet())
				{
					writer.write(String.format("\t\t<point x=\"%d\" y=\"%d\" z=\"%d\" fuel=\"%d\" /> <!--%d-->\n", point.getValue().getX(), point.getValue().getY(), point.getValue().getZ(), point.getValue().getFuel(), point.getKey()));
				}
				writer.write("\t</dock>\n");
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
