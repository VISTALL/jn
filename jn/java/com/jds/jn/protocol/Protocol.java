package com.jds.jn.protocol;

import org.apache.log4j.Logger;

import java.nio.ByteOrder;
import java.util.*;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.protocoltree.*;
import com.jds.jn.util.Util;
import com.jds.nio.buffer.NioBuffer;

/**
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class Protocol
{
	private static final Logger _log = Logger.getLogger(Protocol.class);
	private Map<PacketType, PacketFamilly> _familyes = new TreeMap<PacketType, PacketFamilly>();
	private Map<String, MacroInfo> _macros = new TreeMap<String, MacroInfo>();

	private String _encryption;
	private String _name;
	private String _filename;

	private ByteOrder _order = ByteOrder.LITTLE_ENDIAN;

	public Protocol(String pFile)
	{
		_filename = pFile;
	}

	public PacketInfo getPacketInfo(DecryptPacket packet)
	{
		PacketFamilly f = _familyes.get(packet.getPacketType());
		if (f == null)
		{
			return null;
		}

		PacketInfo info = null;

		for (PacketInfo format : f.getFormats().values())
		{
			int position = packet.getBuffer().position();
			final int start = position;

			boolean[] ok = new boolean[format.sizeId()];

			PartLoop:
			{
				// D0;0001
				for (int i = 0; i < format.sizeId(); i++)
				{
					String hexStep = format.getHexForIndex(i);

					// если у нас ктото накосячил?
					if (hexStep.trim().equals(""))
					{
						ok[i] = false;
						break PartLoop;
					}

					long val;

					int len = hexStep.length();
					int needValue = byteCount(len);
					// если у нас достаточно для чтения
					if ((packet.getBuffer().limit() - position) >= needValue)
					{
						val = read(len, packet.getBuffer(), position);
						position += needValue;
					}
					else
					{
						ok[i] = false;
						break PartLoop;
					}

					String hex = Long.toHexString(val).toUpperCase();

					// приводим в порядок если 0x0F - F то добавляем нулик
					if (hex.length() < hexStep.length())
					{
						String allZero = "";
						for (int $ = 0; $ < (hexStep.length() - hex.length()); $++)
						{
							allZero += "0";
						}

						hex = allZero + hex;
					}

					// не совпадает выходим
					if (!hex.equalsIgnoreCase(hexStep))
					{
						ok[i] = false;
						break PartLoop;
					}

					ok[i] = true;
				}

				boolean isAllOk = true;
				for (boolean s : ok)
				{
					if (!s)
					{
						isAllOk = false;
					}
				}

				if (isAllOk)
				{
					packet.getBuffer().position(position);

					for (int j = start; j < position; j++)
					{
						packet.setColor(j, "op");
					}

					info = format;
					break;
				}
			}
		}

		if(info == null)
		{
			int size = packet.getBuffer().limit() > 10 ? 10 : packet.getBuffer().limit();
			byte[] data = new byte[size];

			packet.getBuffer().get(data);
			packet.getBuffer().position(packet.getBuffer().position() - size);

			_log.info("Unknown packet: " + Util.hexDump(data) + "; PacketType: " + packet.getPacketType());
		}

		return info;
	}

	public int byteCount(int t)
	{
		if (t >= 1 && t <= 2)  //c 00
		{
			return 1;
		}
		if (t >= 3 && t <= 4) //h  0000
		{
			return 2;
		}
		if (t >= 5 && t <= 8) //d  0000.0000
		{
			return 4;
		}
		if (t >= 9 && t <= 12) //q 0000.0000.0000.0000
		{
			return 8;
		}
		return 0;
	}

	public long read(int t, NioBuffer b, int pos)
	{
		if (t >= 1 && t <= 2)  //c
		{
			return b.getUnsigned(pos);
		}
		if (t >= 3 && t <= 4) //h
		{
			return b.getUnsignedShort(pos);
		}
		if (t >= 5 && t <= 8) //d
		{
			return b.getInt(pos);
		}
		if (t >= 9 && t <= 12) //q
		{
			return b.getLong(pos);
		}
		return 0;
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
}