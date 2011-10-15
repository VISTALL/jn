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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocumentType;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.datatree.DataForBlock;
import com.jds.jn.parser.datatree.DataForPart;
import com.jds.jn.parser.datatree.VisualValuePart;
import packet_readers.lineage2.L2AbstractListener;
import packet_readers.lineage2.infos.L2Loc;
import packet_readers.lineage2.infos.jump.JumpChooseType;
import packet_readers.lineage2.infos.jump.JumpNode;
import packet_readers.lineage2.infos.jump.JumpPoint;
import packet_readers.lineage2.infos.jump.JumpTrack;

/**
 * @author VISTALL
 * @date 21:00/13.10.2011
 * <list>
 * <jump_track id="">
 * <node id="1" />
 * <node id="2" />
 * </jump_track>
 * </list>
 */
public class L2JumpListener extends L2AbstractListener
{
	private static final Logger _log = Logger.getLogger(L2JumpListener.class);
	private List<JumpTrack> _jumpTracks = new ArrayList<JumpTrack>();

	private JumpNode _jumpNode;
	private boolean _canCreate;

	@Override
	public List<String> getPackets()
	{
		return Arrays.asList("ExFlyMove", "RequestFlyMove", "RequestFlyMoveStart");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		if(p.getName().equals("RequestFlyMoveStart"))
		{
			_jumpNode = null;
			_canCreate = true;
		}
		else if(p.getName().equals("RequestFlyMove"))
		{
			int node = p.getInt("node");
			if(node == -1)
				return;

			if(_jumpNode == null)
			{
				_log.warn("L2JumpListener: _jumpNode == null:" + node);
			}
			else
				_jumpNode = _jumpNode.getNode(node);
		}
		else
		{
			int id = p.getInt("id");
			JumpChooseType type = JumpChooseType.VALUES[p.getInt("type")];

			JumpTrack jumpTrack = null;
			if(_jumpNode == null)
			{
				if(!_canCreate)
				{
					_log.warn("L2JumpListener: _canCreate is not true. Incorrect packet?.");
					return;
				}

				_canCreate = false;

				jumpTrack = new JumpTrack(id, type);
				_jumpTracks.add(jumpTrack);
				_jumpNode = jumpTrack;
			}
			else
				jumpTrack = _jumpNode.getParent();

			DataForPart node = p.getRootNode().getFirstForById(0);
			for(DataForBlock n : node.getNodes())
			{
				int pointId = ((VisualValuePart) n.getPartByName("point-id")).getValueAsInt();
				int x = ((VisualValuePart) n.getPartByName("x")).getValueAsInt();
				int y = ((VisualValuePart) n.getPartByName("y")).getValueAsInt();
				int z = ((VisualValuePart) n.getPartByName("z")).getValueAsInt();

				JumpPoint point = new JumpPoint(pointId, type, new L2Loc(x, y, z, 0), jumpTrack);

				_jumpNode.addNode(point);
			}
		}
	}

	@Override
	public void closeImpl() throws IOException
	{
		if(_jumpTracks.isEmpty())
			return;

		Document document = DocumentHelper.createDocument();
		document.setDocType(new DefaultDocumentType("list", "jump_nodes.dtd"));
		Element listElement = document.addElement("list");

		for(JumpTrack track : _jumpTracks)
		{
			Element element = listElement.addElement("jump_track");
			element.addAttribute("id", String.valueOf(track.getId()));
			element.addAttribute("type", track.getChooseType().name());

			write(element, track);
		}

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent("\t");
		XMLWriter writer = new XMLWriter(new FileWriter(getLogFile("jump_tracks ", "xml")), format);
		writer.write(document);
		writer.close();
	}

	private void write(Element element, JumpNode node)
	{
		for(JumpPoint point : node.getPoints())
		{
			Element pointElement = element.addElement("point");
			pointElement.addAttribute("id", String.valueOf(point.getId()));
			pointElement.addAttribute("x", String.valueOf(point.getLoc().getX()));
			pointElement.addAttribute("y", String.valueOf(point.getLoc().getY()));
			pointElement.addAttribute("z", String.valueOf(point.getLoc().getZ()));
			if(node.getChooseType() != JumpChooseType.NO_SELECT)
				pointElement.addAttribute("type", point.getChooseType().name());

			write(pointElement, point);
		}
	}
}
