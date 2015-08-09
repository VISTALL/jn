package com.jds.jn.data.xml.parser;

import java.io.File;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import com.jds.jn.classes.CLoader;
import com.jds.jn.config.RValues;
import com.jds.jn.data.xml.holder.ProtocolHolder;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.parser.PartTypeManager;
import com.jds.jn.parser.formattree.ChangeOrderPart;
import com.jds.jn.parser.formattree.ForPart;
import com.jds.jn.parser.formattree.MacroPart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.parser.formattree.SwitchPart;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.parser.packetreader.PacketReader;
import com.jds.jn.parser.valuereader.ValueReader;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.MacroInfo;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.util.ClassUtil;
import com.jds.jn.util.xml.AbstractDirParser;

/**
 * @author VISTALL
 * @date 23:48/26.09.2011
 */
public class ProtocolParser extends AbstractDirParser<ProtocolHolder>
{
	private static ProtocolParser _instance = new ProtocolParser();

	public static ProtocolParser getInstance()
	{
		return _instance;
	}

	private ProtocolParser()
	{
		super(ProtocolHolder.getInstance());
	}

	@Override
	public File getXMLDir()
	{
		return new File(RValues.PROTOCOL_DIR.asString());
	}

	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}

	@Override
	public String getDTDFileName()
	{
		return "protocol.dtd";
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void readData(Element rootElement) throws Exception
	{
		Protocol protocol = new Protocol(_currentFile);
		protocol.setEncryption(rootElement.attributeValue("crypt", "Null"));
		protocol.setOrder((ByteOrder) ByteOrder.class.getField(rootElement.attributeValue("order", "LITTLE_ENDIAN")).get(null));
		protocol.setExtends(rootElement.attributeValue("extends"));
		protocol.setName(rootElement.attributeValue("name"));

		getHolder().addProtocol(protocol);

		for(Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); )
		{
			Element element = iterator.next();
			if(element.getName().equals("packetfamilly"))
			{
				PacketFamilly familly = new PacketFamilly(PacketType.valueOf(element.attributeValue("way")));
				protocol.setFamily(familly);

				for(Iterator<Element> packetIterator = element.elementIterator(); packetIterator.hasNext(); )
				{
					Element packetElement = packetIterator.next();

					String id = packetElement.attributeValue("id");
					String name = packetElement.attributeValue("name", "Empty Name");
					boolean isKey = Boolean.parseBoolean(packetElement.attributeValue("key", "false"));
					boolean isServerList = Boolean.parseBoolean(packetElement.attributeValue("server_list", "false"));
					String readerValue = packetElement.attributeValue("reader");
					Class<PacketReader> reader = null;

					if(readerValue != null)
					{
						try
						{
							reader = (Class<PacketReader>) CLoader.getInstance().forName("packet_readers." + readerValue + "Reader");
						}
						catch(ClassNotFoundException e)
						{
							warn("Can't find reader: " + readerValue, e);
						}
					}

					if(id.startsWith("0x"))
					{
						//ignore
						continue;
					}

					PacketInfo format = new PacketInfo(id, name, isKey, isServerList, reader);
					familly.addPacket(format, protocol);

					parseParts(packetElement, format.getDataFormat().getMainBlock());
				}
			}
			else if(element.getName().equals("macro"))
			{
				String id = element.attributeValue("id");
				MacroInfo part = new MacroInfo(id);
				parseParts(element, part.getModelBlock());
				protocol.addMacro(part);
			}
			else if(element.getName().equals("global_listeners"))
			{
				protocol.setGlobalListeners(ClassUtil.newInstancesFrom(listClasses(element)));
			}
			else if(element.getName().equals("session_listeners"))
			{
				protocol.setSessionListeners(listClasses(element));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Class<IPacketListener>> listClasses(Element rootElement)
	{
		List<Class<IPacketListener>> classes = new ArrayList<Class<IPacketListener>>(2);
		for(Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); )
		{
			Element element = iterator.next();
			if(element.getName().equalsIgnoreCase("listener"))
			{
				String value = element.attributeValue("name");
				if(value != null)
				{
					try
					{
						Class<?> clazz = CLoader.getInstance().forName("packet_readers." + value);
						if(IPacketListener.class.isAssignableFrom(clazz))
						{
							classes.add((Class<IPacketListener>) clazz);
						}
						else
						{
							info("Class: " + value + " is not instanceof IPacketListener");
						}
					}
					catch(Exception e)
					{
						info("Not find listener: " + value);
					}
				}

			}
		}
		return classes.isEmpty() ? Collections.<Class<IPacketListener>>emptyList() : classes;
	}

	@SuppressWarnings("unchecked")
	private void parseParts(Element rootElement, PartContainer container)
	{
		for(Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); )
		{
			Element element = iterator.next();
			if(element.getName().equals("part"))
			{
				String name = element.attributeValue("name");
				String type = element.attributeValue("type");
				int id = Integer.parseInt(element.attributeValue("id", "-1"));
				boolean invert = Boolean.parseBoolean(element.attributeValue("invert", "false"));

				int size = 0;
				int sizeid = -1;
				boolean dynBSize = false;
				String atr = element.attributeValue("size");
				if(atr != null)
				{
					size = Integer.decode(atr);
				}
				else
				{
					atr = element.attributeValue("sizeid");
					if(atr != null)
					{
						sizeid = Integer.parseInt(atr);
						dynBSize = true;
					}
				}

				ValueReader r = null;
				Element readerElement = element.element("reader");
				if(readerElement != null)
				{
					String readerType = readerElement.attributeValue("type");
					try
					{
						Class<?> clazz = CLoader.getInstance().forName("part_readers." + readerType + "Reader");
						r = (ValueReader) clazz.newInstance();
					}
					catch(Exception e)
					{
						warn("Can't ini reader: " + readerType, e);
					}
				}

				Part part = new Part(PartTypeManager.getInstance().getType(type), id, name, invert);
				part.setDynamicBSize(dynBSize);
				if(dynBSize)
				{
					part.setBSizeId(sizeid);
				}
				else
				{
					part.setBSize(size);
				}

				part.setReader(r);
				container.addPart(part);
			}
			else if(element.getName().equals("for"))
			{
				int id = Integer.parseInt(element.attributeValue("id", "-1"));
				int fixed = Integer.parseInt(element.attributeValue("fixed", "0"));
				String name = element.attributeValue("name");

				ForPart newForPart = new ForPart(id, fixed);
				newForPart.setName(name);
				newForPart.setParentContainer(container);
				newForPart.setContainingFormat(container.getContainingFormat());

				parseParts(element, newForPart.getModelBlock());

				container.addPart(newForPart);
			}
			else if(element.getName().equals("changeOrder"))
			{
				try
				{
					container.addPart(new ChangeOrderPart((ByteOrder) ByteOrder.class.getField(element.attributeValue("order",
							"LITTLE_ENDIAN")).get(null)));
				}
				catch(Exception e)
				{
					throw new Error(e);
				}
			}
			else if(element.getName().equals("switch"))
			{
				int id = Integer.parseInt(element.attributeValue("id"));
				String name = element.attributeValue("name");

				SwitchPart newSwitchBlock = new SwitchPart(id);
				newSwitchBlock.setParentContainer(container);
				newSwitchBlock.setContainingFormat(container.getContainingFormat());
				newSwitchBlock.setName(name);
				container.addPart(newSwitchBlock);

				for(Iterator<Element> caseDefaultIterator = element.elementIterator(); caseDefaultIterator.hasNext(); )
				{
					Element caseDefaultElement = caseDefaultIterator.next();
					if(caseDefaultElement.getName().equals("case"))
					{
						String caseIdString = caseDefaultElement.attributeValue("id");
						String caseName = caseDefaultElement.attributeValue("name", "");

						SwitchCaseBlock newSwitchCase;

						if(caseIdString.equalsIgnoreCase("default"))
						{
							newSwitchCase = new SwitchCaseBlock(newSwitchBlock);
						}
						else
						{
							int caseId = Integer.decode(caseDefaultElement.attributeValue("id"));

							newSwitchCase = new SwitchCaseBlock(newSwitchBlock, caseId);
						}

						newSwitchCase.setName(caseName);
						newSwitchCase.setParentContainer(container);
						newSwitchCase.setContainingFormat(container.getContainingFormat());

						parseParts(caseDefaultElement, newSwitchCase);

						newSwitchBlock.addCase(newSwitchCase);
					}
				}
			}
			else if(element.getName().equals("macro"))
			{
				String id = element.attributeValue("id");
				String name = element.attributeValue("name", id);

				MacroPart part = new MacroPart(id, name);
				part.setParentContainer(container);
				part.setContainingFormat(container.getContainingFormat());

				container.addPart(part);
			}
		}
	}
}

