package com.jds.jn.protocol;

import java.nio.ByteOrder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import com.jds.jn.config.RValues;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.parser.Type;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.parser.packetfactory.PacketListenerFactory;
import com.jds.jn.parser.parservalue.ParserValue;
import com.jds.jn.protocol.protocoltree.MacroInfo;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.util.Util;

/**
 * @author Gilles Duboscq
 * @author VISTALL
 */
@SuppressWarnings("unchecked")
public class Protocol
{
	private static final Logger _log = Logger.getLogger(Protocol.class);

	private Map<PacketType, PacketFamilly> _familyes = new TreeMap<PacketType, PacketFamilly>();
	private Map<String, MacroInfo> _macros = new TreeMap<String, MacroInfo>();

	private List<Class<IPacketListener>> _sessionListeners = Collections.emptyList();
	private List<IPacketListener> _globalListeners = Collections.emptyList();

	private String _encryption;
	private String _name;
	private String _filename;

	private String _extends;
	private Protocol _superProtocol;

	private ByteOrder _order = ByteOrder.LITTLE_ENDIAN;

	public Protocol(String pFile)
	{
		_filename = pFile;
	}

	public PacketInfo getPacketInfo(DecryptedPacket packet)
	{
		PacketFamilly f = _familyes.get(packet.getPacketType());
		if (f == null)
			return null;

		PacketInfo info = null;

		for (PacketInfo format : f.getFormats().values())
		{
			final int position = packet.getBuffer().position();

			PartLoop:
			{
				Map.Entry<Type, Long>[] opcode = format.getOpcode();
				// D0;0001
				for(Map.Entry<Type, Long> op : opcode)
				{
					ParserValue<Number> t = (ParserValue<Number>)op.getKey().getInstance();

					Number val;
					// если у нас достаточно для чтения
					if((packet.getBuffer().limit() - packet.getBuffer().position()) >= t.length())
						val = t.getValue(packet.getBuffer(), null);
					else
					{
						packet.getBuffer().position(position);
						break PartLoop;
					}

					// не совпадает выходим
					if(op.getValue().longValue() != val.longValue())
					{
						packet.getBuffer().position(position);
						break PartLoop;
					}
				}

				for (int j = position; j < packet.getBuffer().position(); j++)
					packet.setColor(j, "op");

				info = format;
				break;
			}
		}

		if(info == null && RValues.PRINT_UNKNOWN_PACKET.asBoolean())
		{
			int size = packet.getBuffer().limit() > 10 ? 10 : packet.getBuffer().limit();
			byte[] data = new byte[size];

			packet.getBuffer().get(data);
			packet.getBuffer().position(packet.getBuffer().position() - size);

			_log.info("Unknown packet: " + Util.hexDump(data) + "; PacketType: " + packet.getPacketType());
		}

		return info;
	}


	public void setEncryption(String enc)
	{
		_encryption = enc;
	}

	public String getEncryption()
	{
		return _encryption;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public String getFileName()
	{
		return _filename;
	}

	public void setFamily(PacketType type, PacketFamilly fa)
	{
		_familyes.put(type, fa);
	}

	public PacketFamilly getFamilly(PacketType t)
	{
		return _familyes.get(t);
	}

	public void addMacro(MacroInfo part)
	{
		if ((part = _macros.put(part.getId(), part)) != null)
		{
			MainForm.getInstance().info("More than 1 packet register for 1 macro name: " + part.getId());
		}
	}

	public MacroInfo getMacroInfo(String name)
	{
		return _macros.get(name);
	}

	public Collection<PacketFamilly> getFamilies()
	{
		return _familyes.values();
	}

	public ByteOrder getOrder()
	{
		return _order;
	}

	public void setOrder(ByteOrder order)
	{
		_order = order;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public List<IPacketListener> getSessionListeners()
	{
		return PacketListenerFactory.listListeners(_sessionListeners);
	}

	public List<IPacketListener> getGlobalListeners()
	{
		return _globalListeners;
	}

	public void setGlobalListeners(List<IPacketListener> globalListeners)
	{
		_globalListeners = globalListeners;
	}

	public void setSessionListeners(List<Class<IPacketListener>> sessionListeners)
	{
		_sessionListeners = sessionListeners;
	}

	public String getExtends()
	{
		return _extends;
	}

	public void setExtends(String anExtends)
	{
		_extends = anExtends;
	}

	public Protocol getSuperProtocol()
	{
		return _superProtocol;
	}

	public void setSuperProtocol(Protocol superProtocol)
	{
		_superProtocol = superProtocol;
	}

	public Map<String, MacroInfo> getMacros()
	{
		return _macros;
	}
}