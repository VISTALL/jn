package com.jds.jn.protocol;

import java.nio.ByteOrder;
import java.util.*;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.protocoltree.*;
import com.jds.nio.buffer.NioBuffer;

/**
 * @author Gilles Duboscq  && VISTALL
 */
public class Protocol
{
	private Map<PacketType, PacketFamilly> _familyes = new HashMap<PacketType, PacketFamilly>();
	private Map<String, MacroInfo> _macros = new HashMap<String, MacroInfo>();

	private String _encryption;
	private String _name;
	private String _filename;

	private ByteOrder _order = ByteOrder.LITTLE_ENDIAN;

	public Protocol(String pFile)
	{
		_filename = pFile;
	}

	public PacketInfo getFormat(DecryptPacket packet)
	{
		PacketFamilly f = _familyes.get(packet.getPacketType());
		if (f == null)
		{
			return null;
		}

		for (PacketInfo format : f.getFormats().values())
		{
			int position = packet.getBuffer().position();
			final int start = position;

			boolean[] ok = new boolean[format.sizeId()];

			for (int i = 0; i < format.sizeId(); i++)
			{
				String hexStep = format.getHexForIndex(i);

				if (hexStep.trim().equals(""))
				{
					ok[i] = false;
					continue;
				}


				int val = -1;

				int len = hexStep.length();
				int needValue = byteCount(len);
				if((packet.getBuffer().limit() - position) >= needValue)
				{
					val = read(len, packet.getBuffer(), position);
					position += needValue;
				}
				else
				{
					ok[i] = false;
					continue;
				}

				String hex = Integer.toHexString(val).toUpperCase();

				if(hex.length() < hexStep.length())
				{
					String allZero = "";
					for(int $ = 0; $ < (hexStep.length() - hex.length()); $++)
					{
						allZero += "0";
					}

					hex = allZero + hex;
				}

				if (!hex.equalsIgnoreCase(hexStep))
				{
					ok[i] = false;
					continue;
				}

				ok[i] = true;
			}

			boolean isAllOk = true;
			for(boolean s : ok)
			{
				if(!s)
					isAllOk = false;
			}

			if(isAllOk)
			{
				packet.getBuffer().position(position);

				for (int j = start; j < position; j++)
				{
					packet.setColor(j, "op");
				}

				return format;
			}
		}

		return null;
	}

	public int byteCount(int t)
	{
		if(t >= 1 && t <= 2)  //c
			return 1;
		if(t >= 3 && t <= 4) //h
			return 2;
		if(t >= 5 && t <= 8) //d
			return 4;

		return 0;
	}

	public int read(int t, NioBuffer b, int pos)
	{
		if(t >= 1 && t <= 2)  //c
			return b.getUnsigned(pos);
		if(t >= 3 && t <= 4) //h
			return b.getUnsignedShort(pos);
		if(t >= 5 && t <= 8) //d
			return b.getInt(pos);

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

	@Override
	public String toString()
	{
		return getName();
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
}