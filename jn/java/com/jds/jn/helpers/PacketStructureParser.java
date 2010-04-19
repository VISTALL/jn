package com.jds.jn.helpers;

import com.jds.jn.Jn;
import com.jds.nio.buffer.NioBuffer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 22.08.2009
 * Time: 0:14:08
 */
public class PacketStructureParser
{
	/**
	 * C(val); - 1 byte
	 * H(val); - 2 byte
	 * D(val); - 4 byte
	 */
	private enum DataType
	{
		writeC,
		writeH,
		writeD,
		writeQ,
		writeF,
		writeS,
		writeB,
		writeIP
	}

	private NioBuffer _buff;
	private String _fullText;

	public PacketStructureParser(String fullText)
	{
		_fullText = fullText;
		_buff = NioBuffer.wrap(new byte[1]);
		_buff.setAutoExpand(true);
		_buff.order(ByteOrder.LITTLE_ENDIAN);
	}

	public void parse()
	{
		Pattern p = Pattern.compile("(\\S+)\\((\\S+)\\)\\;");
		Matcher m = p.matcher(_fullText);

		//_buff.putShort((short)0); // header

		while (m.find())
		{
			DataType data;

			try
			{
				data = DataType.valueOf(m.group(1));
			}
			catch (Exception e)
			{
				return;
			}

			switch (data)
			{
				case writeC:
					try
					{
						_buff.put(Byte.decode(m.group(2)));
						//MainForm.getInstance().info("Write C: " + m.group(2));
					}
					catch (NumberFormatException e)
					{
						Jn.getInstance().warn("Exception " + e.getLocalizedMessage(), e);
						//e.printStackTrace();
					}
					break;
				case writeH:
					try
					{
						_buff.putShort(Short.decode(m.group(2)));
						//MainForm.getInstance().info("Write H: " + m.group(2));
					}
					catch (NumberFormatException e)
					{
						Jn.getInstance().warn("Exception " + e.getLocalizedMessage(), e);
						//e.printStackTrace();
					}
					break;
				case writeD:
					try
					{
						_buff.putInt(Integer.decode(m.group(2)));
						//MainForm.getInstance().info("Write D: " + m.group(2));
					}
					catch (NumberFormatException e)
					{
						Jn.getInstance().warn("Exception " + e.getLocalizedMessage(), e);
						//e.printStackTrace();
					}
					break;
				case writeQ:
					try
					{
						_buff.putLong(Long.decode(m.group(2)));
						//MainForm.getInstance().info("Write Q: " + m.group(2));
					}
					catch (NumberFormatException e)
					{
						Jn.getInstance().warn("Exception " + e.getLocalizedMessage(), e);
						//e.printStackTrace();
					}
					break;
				case writeF:
					try
					{
						_buff.putFloat(Float.parseFloat(m.group(2)));
						//MainForm.getInstance().info("Write F: " + m.group(2));
					}
					catch (NumberFormatException e)
					{
						Jn.getInstance().warn("Exception " + e.getLocalizedMessage(), e);
						//e.printStackTrace();
					}
					break;
				case writeS:
					try
					{
						String text = m.group(2);
						if (text.equals("null"))
						{
							text = null;
						}

						if (text == null)
						{
							_buff.putChar('\000');
						}
						else
						{
							final int len = text.length();
							for (int i = 0; i < len; i++)
							{
								_buff.putChar(text.charAt(i));
							}
							_buff.putChar('\000');
						}

						//MainForm.getInstance().info("Write S: " + m.group(2));
					}
					catch (NumberFormatException e)
					{
						Jn.getInstance().warn("Exception " + e.getLocalizedMessage(), e);
						//e.printStackTrace();
					}
					break;
				case writeB:
					try
					{
						int size = Integer.parseInt(m.group(2));
						_buff.put(new byte[size]);
						//MainForm.getInstance().info("Write B: " + m.group(2));
					}
					catch (NumberFormatException e)
					{
						Jn.getInstance().warn("Exception " + e.getLocalizedMessage(), e);
						//e.printStackTrace();
					}
					break;
				case writeIP:
					try
					{
						InetAddress i4 = InetAddress.getByName(m.group(2));
						byte[] raw = i4.getAddress();
						_buff.put(raw[0]);  // ip1
						_buff.put(raw[1]);  // ip2
						_buff.put(raw[2]);  // ip 3
						_buff.put(raw[3]);  //ip 4
					}
					catch (UnknownHostException e)
					{
						Jn.getInstance().warn("Exception " + e.getMessage(), e);
					}
					break;
			}
		}

		//_buff.putShort(0, (short)_buff.limit());
	}

	public NioBuffer getBuffer()
	{
		return _buff;
	}
}
