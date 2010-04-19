package com.jds.jn.loaders;

import com.jds.jn.Jn;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.formattree.*;
import com.jds.jn.parser.packetreader.PacketReader;
import com.jds.jn.parser.parttypes.RawPartType;
import com.jds.jn.parser.valuereader.ValueReader;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.remotefiles.FileLoader;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 25.09.2009
 * Time: 14:30:14
 */
@SuppressWarnings("unchecked")
public class ProtocolLoader
{
	public static Protocol restore(File file)
	{
		Protocol protocol = null;
		Document doc = null;

		try
		{
			FileInputStream fis = new FileInputStream(file);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(false);
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			try
			{
				doc = docBuilder.parse(fis);
			}
			catch (SAXParseException e)
			{
				report("ERROR", e);
			}
			Node root = doc.getDocumentElement();

			if (!root.getNodeName().equals("protocol"))
			{
				Jn.getInstance().warn("Error malformed protocol : root node should be called 'protocol'.");
			}
			protocol = new Protocol(file.getAbsolutePath());

			NamedNodeMap attr = root.getAttributes();
			Node node = attr.getNamedItem("crypt");
			if (node != null)
			{
				protocol.setEncryption(node.getNodeValue());
			}
			else
			{
				protocol.setEncryption("Null");
			}

			node = attr.getNamedItem("checksumSize");
			if (node != null)
			{
				protocol.setChecksumSize(Integer.parseInt(node.getNodeValue()));
			}

			node = attr.getNamedItem("name");
			if (node != null)
			{
				protocol.setName(node.getNodeValue());
			}
			else
			{
				return null;
			}

			for (Node n = root.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("packetfamilly".equalsIgnoreCase(n.getNodeName()))
				{
					PacketFamilly familly = parseFamilly(protocol, n);

					if (familly != null)
					{
						protocol.setFamily(familly.getType(), familly);
					}
					else
					{
						Jn.getInstance().warn("Error packetfamilly returned is null there was an error");
					}
				}
			}
		}
		catch (Exception e)
		{
			Jn.getInstance().warn("Error while parsing protocol " + file.getName());
			e.printStackTrace();
		}

		return protocol;
	}

	private static PacketFamilly parseFamilly(Protocol protocol, Node n)
	{
		NamedNodeMap map = n.getAttributes();
		PacketType type = null;

		Node atr = map.getNamedItem("way");
		if (atr == null)
		{
			Jn.getInstance().warn("Error, Root packetfamilly don't have 'way'. skipping it");
			return null;
		}
		else
		{
			String way = atr.getNodeValue();

			type = PacketType.valueOf(way);
		}

		if(type == null)
		{
			return null;
		}

		PacketFamilly familly = new PacketFamilly(type);

		for (Node o = n.getFirstChild(); o != null; o = o.getNextSibling())
		{
			if ("packet".equalsIgnoreCase(o.getNodeName()))
			{
				map = o.getAttributes();

				String id = map.getNamedItem("id").getNodeValue();
				String name = map.getNamedItem("name") == null ? "Null Name" : map.getNamedItem("name").getNodeValue();
				boolean isKey = map.getNamedItem("key") != null && Boolean.parseBoolean(map.getNamedItem("key").getNodeValue());
				boolean server_list = map.getNamedItem("server_list") != null && Boolean.parseBoolean(map.getNamedItem("server_list").getNodeValue());
				String reader_c = map.getNamedItem("reader") == null ? null : map.getNamedItem("reader").getNodeValue();
				Class<PacketReader> reader = null;

				if(reader_c != null)
				{
					try
					{
						reader = FileLoader.getInstance().getFile("packet_readers." + reader_c + "Reader").getRawClass();
					}
					catch (ClassNotFoundException e)
					{
						e.printStackTrace();
					}
				}

				if(id.startsWith("0x"))
				{
					//TODO remake
					continue;
				}

				PacketInfo format = new PacketInfo(id, name, isKey, server_list, reader);

				boolean b = parseParts(o, format.getDataFormat().getMainBlock());
				if(!b)
				{
					System.out.println("Error after parsing");
				}

				familly.addPacket(format);
			}
		}

		return familly;
	}

	private static boolean parseParts(Node n, PartContainer pc)
	{
		NamedNodeMap attrs;
		Node atr;
		for (Node o = n.getFirstChild(); o != null; o = o.getNextSibling())
		{
			if ("part".equalsIgnoreCase(o.getNodeName()))
			{
				Part pp = parsePart(o, pc);
				if (pp == null)
				{
					return false;
				}
				pc.addPart(pp);
			}
			else if ("for".equalsIgnoreCase(o.getNodeName()))
			{
				attrs = o.getAttributes();
				int forId;
				String name = "";

				atr = attrs.getNamedItem("id");
				if (atr == null)
				{
					Jn.getInstance().warn("Error, for doesnt have 'id'. skipping packet");
					return false;
				}
				forId = Integer.parseInt(atr.getNodeValue());

				if (attrs.getNamedItem("name") != null)
				{
					name = attrs.getNamedItem("name").getNodeValue();
				}

				ForPart newForPart = new ForPart(forId);
				newForPart.setName(name);
				newForPart.setParentContainer(pc);
				newForPart.setContainingFormat(pc.getContainingFormat());
				if (parseParts(o, newForPart.getModelBlock()))
				{
					pc.addPart(newForPart);
				}
				else
				{
					return false;
				}
			}
			else if ("switch".equalsIgnoreCase(o.getNodeName()))
			{
				attrs = o.getAttributes();
				int switchId;
				atr = attrs.getNamedItem("id");


				if (atr == null)
				{
					Jn.getInstance().warn("Error, switch doesnt have 'id'. skipping packet");
					return false;
				}
				switchId = Integer.parseInt(atr.getNodeValue());

				SwitchPart newSwitchBlock = new SwitchPart(switchId);
				newSwitchBlock.setParentContainer(pc);
				newSwitchBlock.setContainingFormat(pc.getContainingFormat());
				for (Node caseNode = o.getFirstChild(); caseNode != null; caseNode = caseNode.getNextSibling())
				{
					if ("case".equalsIgnoreCase(caseNode.getNodeName()))
					{
						attrs = caseNode.getAttributes();

						atr = attrs.getNamedItem("id");
						if (atr == null)
						{
							Jn.getInstance().warn("Error, case doesnt have 'id'. skipping packet");
							return false;
						}

						String caseName = "";

						if (attrs.getNamedItem("name") != null)
						{
							caseName = attrs.getNamedItem("name").getNodeValue();
						}

						SwitchCaseBlock newSwitchCase;

						if (atr.getNodeValue().equalsIgnoreCase("default"))
						{
							newSwitchCase = new SwitchCaseBlock(newSwitchBlock);
						}
						else
						{
							int caseId;
							try
							{
								caseId = Integer.decode(atr.getNodeValue());
								newSwitchCase = new SwitchCaseBlock(newSwitchBlock, caseId);
							}
							catch (NumberFormatException e)
							{
								Jn.getInstance().warn("Warning, case doesnt have a valid 'id'. making it default");
								newSwitchCase = new SwitchCaseBlock(newSwitchBlock);
							}
						}
						newSwitchCase.setName(caseName);
						newSwitchCase.setParentContainer(pc);
						newSwitchCase.setContainingFormat(pc.getContainingFormat());

						if (parseParts(caseNode, newSwitchCase))
						{
							newSwitchBlock.addCase(newSwitchCase);
						}
						else
						{
							return false;
						}
					}
				}
				pc.addPart(newSwitchBlock);
			}
		}
		return true;
	}

	private static Part parsePart(Node n, PartContainer pc)
	{
		String partName;
		NamedNodeMap attrs = n.getAttributes();
		Node atr = attrs.getNamedItem("name");
		if (atr == null)
		{
			Jn.getInstance().warn("Warning, part doesnt have 'name'");
			partName = "";
		}
		else
		{
			partName = atr.getNodeValue();
		}

		int partId;
		atr = attrs.getNamedItem("id");
		if (atr == null)
		{
			partId = -1;
		}
		else
		{
			try
			{
				partId = Integer.parseInt(atr.getNodeValue());
			}
			catch (NumberFormatException nfe)
			{
				Jn.getInstance().warn("Warning: parts id must be an integer");
				partId = -1;
			}
		}

		atr = attrs.getNamedItem("type");
		if (atr == null)
		{
			Jn.getInstance().warn("Error, part doesnt have 'type'. skipping packet");
			return null;
		}
		String type = atr.getNodeValue();

		int size = 0;
		int sizeid = -1;
		boolean dynBSize = false;
		atr = attrs.getNamedItem("size");
		if (atr != null)
		{
			size = Integer.decode(atr.getNodeValue());
		}
		else
		{
			atr = attrs.getNamedItem("sizeid");
			if (atr != null)
			{
				sizeid = Integer.parseInt(atr.getNodeValue());
				dynBSize = true;
			}
		}
		ValueReader r = null;
		for (Node subNode = n.getFirstChild(); subNode != null; subNode = subNode.getNextSibling())
		{

			if ("reader".equals(subNode.getNodeName()))
			{
				if (r != null)
				{
					Jn.getInstance().warn("Warning, part '" + (partName) + "' has mutiple readers");
				}
				NamedNodeMap attrs2 = subNode.getAttributes();
				atr = attrs2.getNamedItem("type");
				if (atr == null)
				{
					Jn.getInstance().warn("Warning, part '" + (partName) + "' has a reader with no type");
					continue;
				}

				//try default package
				Class<?> clazz = null;
				try
				{
					clazz = FileLoader.getInstance().getFile("part_readers." + atr.getNodeValue() + "Reader").getRawClass();
				}
				catch (DOMException e)
				{
					e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{
				}

				if (clazz == null)
				{
					Jn.getInstance().warn("Warning, part '" + (partName) + "' reader's could not be found in either parser or custom packages");
					continue;
				}
				try
				{
					r = (ValueReader) clazz.newInstance();
					if (!r.loadReaderFromXML(subNode)) //drop reader if loading went wrong
					{
						r = null;
					}

				}
				catch (InstantiationException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}


		boolean invert;
		atr = attrs.getNamedItem("invert");
		invert = atr != null && Boolean.parseBoolean(atr.getNodeValue());

		Part pp = new Part(PartTypeManager.getInstance().getType(type), partId, partName, invert);

		if (dynBSize)
		{
			pp.setBSizeId(sizeid);
			pp.setDynamicBSize(true);
		}
		else
		{
			pp.setBSize(size);
			pp.setDynamicBSize(false);
		}

		pp.setReader(r);
		return pp;
	}

	private static void processParts(List<Part> parts, Element newNode, Document document)
	{
		for (Part part : parts)
		{
			Element partNode;
			if (part instanceof ForPart)
			{
				ForPart block = (ForPart) part;
				partNode = document.createElement("for");
				partNode.setAttribute("id", String.valueOf(block.getForId()));

				if (!block.getName().equals(""))
				{
					partNode.setAttribute("name", block.getName());
				}
				processParts(block.getModelBlock().getParts(), partNode, document);
			}
			else if (part instanceof SwitchPart)
			{
				SwitchPart block = (SwitchPart) part;
				partNode = document.createElement("switch");
				partNode.setAttribute("id", String.valueOf(block.getSwitchId()));
				for (SwitchCaseBlock sCase : block.getCases(true))
				{
					Element caseNode = document.createElement("case");

					if (sCase.isDefault())
					{
						caseNode.setAttribute("id", "default");
					}
					else
					{
						caseNode.setAttribute("id", Integer.toString(sCase.getSwitchCase()));
					}

					if (!sCase.getName().equals(""))
					{
						caseNode.setAttribute("name", sCase.getName());
					}

					processParts(sCase.getParts(), caseNode, document);
					partNode.appendChild(caseNode);
				}
			}
			else
			{
				partNode = document.createElement("part");
				partNode.setAttribute("type", part.getType().getName());
				if (part.getId() != -1)
				{
					partNode.setAttribute("id", String.valueOf(part.getId()));
				}
				if (part.getName() != null && part.getName().length() > 0)
				{
					partNode.setAttribute("name", part.getName());
				}

				if (part.getType() instanceof RawPartType)
				{
					if (part.isDynamicBSize())
					{
						partNode.setAttribute("sizeid", String.valueOf(part.getBSizeId()));
					}
					else if (part.getBSize() != 0)
					{
						partNode.setAttribute("size", String.valueOf(part.getBSize()));
					}
				}
				if (part.isInvert())
				{
					partNode.setAttribute("invert", String.valueOf(true));
				}

				ValueReader reader = part.getReader();
				if (reader != null)
				{
					Element readerNode = document.createElement("reader");
					readerNode.setAttribute("type", reader.getClass().getSimpleName().replace("Reader", ""));
					reader.saveReaderToXML(readerNode, document);
					partNode.appendChild(readerNode);
				}
			}
			newNode.appendChild(partNode);
		}
	}

	//private static void buildDOM(ProtocolNode node, Element packetFamilly, Document document)
	{
		/*if (node instanceof PacketFormat)
		{
			PacketFormat format = (PacketFormat) node;
			Element newNode = document.createElement("packet");
			newNode.setAttribute("id", "0x" + Integer.toHexString(format.getID()));
			newNode.setAttribute("name", format.getName());
			if (format.isKey())
			{
				newNode.setAttribute("key", String.valueOf(true));
			}

			if (format.getPacketReader() != null)
			{
				newNode.setAttribute("reader", format.getPacketReader().getClass().getSimpleName().replace("Reader", ""));
			}

			if (format.isServerList())
			{
				newNode.setAttribute("server_list", String.valueOf(true));
			}

			processParts(format.getDataFormat().getMainBlock().getParts(), newNode, document);
			packetFamilly.appendChild(newNode);
		}
		else if (node instanceof PacketFamilly)
		{
			PacketFamilly familly = (PacketFamilly) node;
			Element newNode = document.createElement("packetfamilly");
			//newNode.setAttribute("switchtype", familly.getSwitchType());
			newNode.setAttribute("id", "0x" + Integer.toHexString(familly.getID()));
			// sort elements in family
			FastList<ProtocolNode> nodes = new FastList<ProtocolNode>(familly.getNodes().values());
			Collections.sort(nodes, PROTOCOL_NODE_COMPARATOR);
			for (ProtocolNode pnode : nodes)
			{
				buildDOM(pnode, newNode, document);
			}
			packetFamilly.appendChild(newNode);
		}*/
	}

	public static void report(String severity, SAXParseException e)
	{
		Jn.getInstance().warn(severity + ": " + e.getMessage() + " (Line " + e.getLineNumber() + ", Column: " + e.getColumnNumber() + ")", e);
	}
}
