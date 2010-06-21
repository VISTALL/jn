package com.jds.jn.protocol;

import javolution.util.FastMap;

import java.util.Collection;

import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;

/**
 * @author Gilles Duboscq  && VISTALL
 */
public class Protocol
{
	private FastMap<PacketType, PacketFamilly> _familyes = new FastMap<PacketType, PacketFamilly>();
	private int _checksumSize = 0;

	private String _encryption;
	private String _name;
	private String _filename;

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
				String hex = format.getHexForIndex(i);

				if (hex.trim().equals(""))
				{
					ok[i] = false;
					continue;
				}
				int val = -1;
				boolean sho = false;

				if(hex.length() > 2 && ((packet.getBuffer().limit() - position) >= 2))
				{
					val =  packet.getBuffer().getUnsignedShort(position);
					position += 2;
					sho = true;
				}
				else if((packet.getBuffer().limit() - position) >= 1)
				{
					try
					{
						val = packet.getBuffer().getUnsigned(position++);
					}
					catch(Exception e)
					{
						position--;
					}
				}
				else
				{
					ok[i] = false;
					continue;
				}

				String hex2 = Integer.toHexString(val).toUpperCase();

				if (val <= 0x0F)
				{
					hex2 = "0" + hex2;
				}

				if(val <= 0xFF && sho)
				{
					hex2 = hex2 + "00";
				}


				if (!hex2.equalsIgnoreCase(hex))
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

	public int getChecksumSize()
	{
		return _checksumSize;
	}

	public void setChecksumSize(int it)
	{
		_checksumSize = it;
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

	@Override
	public String toString()
	{
		return getName();
	}

	public Collection<PacketFamilly> getFamilies()
	{
		return _familyes.values();
	}
}