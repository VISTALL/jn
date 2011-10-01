package packet_readers.lineage2.listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.datatree.DataForBlock;
import com.jds.jn.parser.datatree.DataForPart;
import com.jds.jn.parser.datatree.VisualValuePart;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.infos.L2BoatPoint;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:12:10/27.08.2010
 */
public class L2AirShipTeleportListListener extends L2AbstractListener
{
	private IntObjectMap<IntObjectMap<L2BoatPoint>> _list = new TreeIntObjectMap<IntObjectMap<L2BoatPoint>>();

	@Override
	public List<String> getPackets()
	{
		return Collections.singletonList("ExAirShipTeleportList");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		int dockId = p.getInt("dockId");
		if(_list.containsKey(dockId))
			return;

		IntObjectMap<L2BoatPoint> l = new TreeIntObjectMap<L2BoatPoint>();

		DataForPart list = (DataForPart) p.getRootNode().getPartByName("list");

		for(DataForBlock block : list.getNodes())
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


	@Override
	public void closeImpl() throws IOException
	{
		FileWriter writer = new FileWriter(getLogFile("air_docks ", "xml"));
		writer.write("<?xml version='1.0' encoding='utf-8'?>\n");
		writer.write("<!--Create by VISTALL-->\n");
		writer.write("<list>\n");
		for(IntObjectMap.Entry<IntObjectMap<L2BoatPoint>> e : _list.entrySet())
		{
			writer.write(String.format("\t<dock id=\"%d\">\n", e.getKey()));
			for(IntObjectMap.Entry<L2BoatPoint> point : e.getValue().entrySet())
				writer.write(String.format("\t\t<point x=\"%d\" y=\"%d\" z=\"%d\" fuel=\"%d\" /> <!--%d-->\n", point.getValue().getX(), point.getValue().getY(), point.getValue().getZ(), point.getValue().getFuel(), point.getKey()));
			writer.write("\t</dock>\n");
		}
		writer.write("</list>");
		writer.close();
	}
}
