package com.jds.jn.network.packets;

import java.nio.BufferUnderflowException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.parser.datatree.DataForBlock;
import com.jds.jn.parser.datatree.DataForPart;
import com.jds.jn.parser.datatree.DataMacroPart;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.datatree.DataTreeNodeContainer;
import com.jds.jn.parser.datatree.RawValuePart;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.formattree.ForPart;
import com.jds.jn.parser.formattree.MacroPart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.formattree.PartContainer;
import com.jds.jn.parser.formattree.SwitchCaseBlock;
import com.jds.jn.parser.formattree.SwitchPart;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.MacroInfo;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.session.Session;
import com.jds.nio.buffer.NioBuffer;

/**
 * @author Gilles Duboscq   - first version
 * @author VISTALL - rewrite
 */
public class DecryptedPacket implements IPacket
{
	private static final Logger _log = Logger.getLogger(DecryptedPacket.class);

	private DataTreeNodeContainer _packetParts;

	private PacketInfo _packetFormat;

	protected String _error;

	protected String[] _colorForHex;

	private final byte[] _decryptedData;
	private final byte[] _cryptedData;
	private final long _time;
	private final PacketType _packetType;

	public DecryptedPacket(Session session, PacketType type, byte[] data, long time, Protocol protocol, boolean decode)
	{
		_packetType = type;
		_cryptedData = data;
		_time = time;

		byte[] decrypted = Arrays.copyOf(data, data.length);
		_decryptedData = decode ? session.getCrypt().decrypt(decrypted, type, session) : decrypted;
		_colorForHex = new String[_decryptedData.length];

		NioBuffer buf = NioBuffer.wrap(_decryptedData);
		buf.order(protocol.getOrder());

		_packetFormat = protocol.getPacketInfo(this, buf);
		if(_packetFormat != null)
		{
			try
			{
				parse(protocol, buf);
			}
			catch (BufferUnderflowException e)
			{
				_error = "Insuficient data for the specified format";
				MainForm.getInstance().info("Parsing packet (" + getName() + "), insuficient data for the specified format. Please verify the format.");
			}
			catch (Exception e)
			{
				_error = "Exception: " + e.getMessage();
				_log.info("Exception: " + e, e);
			}
		}
	}

	public String getName()
	{
		if (getPacketInfo() == null)
			return null;

		return getPacketInfo().getName();
	}

	public void setColor(int index, String color)
	{
		_colorForHex[index] = color;
	}

	public String getColor(int index)
	{
		return _colorForHex[index];
	}

	public void parse(Protocol protocol, NioBuffer buff)
	{
		_packetParts = new DataTreeNodeContainer();

		parse(protocol, buff, _packetFormat.getDataFormat().getMainBlock(), _packetParts);
	}

	private boolean parse(Protocol protocol, NioBuffer buff, PartContainer protocolNode, DataTreeNodeContainer dataNode)
	{
		for (Part part : protocolNode.getParts())
		{
			if (part instanceof ForPart)
			{
				ForPart forPart = (ForPart)part;
				int size = 0;
				if(forPart.getFixedSize() > 0)
					size = forPart.getFixedSize();
				else
				{
					// find the size of this for in the scope
					ValuePart vp = dataNode.getPacketValuePartById(forPart.getForId());
					if (vp == null)
					{
						_error = "Error: could not find valuepart to loop on for (For " + part.getName() + " - id:" + forPart.getForId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
						return false;
					}
					if (!(vp instanceof VisualValuePart))
					{
						_error = "Error: for id didnt refer to an IntValePart in (For " + part.getName() + " - id:" + forPart.getForId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
						return false;
					}
					size = ((VisualValuePart) vp).getValueAsInt();
				}

				//check size here
				if (forPart.getModelBlock().hasConstantLength())
				{
					int forBlockSize = forPart.getModelBlock().getLength();
					if (size * forBlockSize > buff.remaining())
					{
						_error = "Error size is too big (" + size + ") for For (Part Name: " + part.getName() + " - Id: " + forPart.getForId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
						return false;
					}
				}
				else if (size > buff.remaining())
				{
					_error = "Error size is too big (" + size + ") for For (Part Name: " + part.getName() + " - Id: " + forPart.getForId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
					return false;
				}
				DataForPart dataForPart = new DataForPart(dataNode, forPart);
				for (int i = 0; i < size; i++)
				{
					DataForBlock forBlock = new DataForBlock(dataForPart, forPart.getModelBlock(), i, size);
					if (!parse(protocol, buff, forPart.getModelBlock(), forBlock))
						return false;
				}
			}
			else if (part instanceof MacroPart)
			{
				MacroInfo macro = protocol.getMacroInfo(((MacroPart)part).getMacroId());
				if(macro == null)
				{
					_error = "Not find macro by id: " + ((MacroPart)part).getMacroId();
					return false;
				}

				if (macro.getModelBlock().hasConstantLength())
				{
					int macroSize = macro.getModelBlock().getLength();
					if (macroSize > buff.remaining())
					{
						_error =  "Incorrect buffer to read macro " + part.getName();
						return false;
					}
				}

				DataMacroPart macroPart = new DataMacroPart(dataNode, (MacroPart)part);
				if (!parse(protocol, buff, macro.getModelBlock(), macroPart))
					return false;
			}
			else if (part instanceof SwitchPart)
			{
				//find the actual type
				ValuePart vp = dataNode.getPacketValuePartById(((SwitchPart) part).getSwitchId());
				if (vp == null)
				{
					_error = "Error: could not find valuepart to switch on for Switch (Part: " + part.getName() + " - id:" + ((SwitchPart) part).getSwitchId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
					return false;
				}
				if (!(vp instanceof VisualValuePart))
				{
					_error = "Error: swicth id didnt refer to an IntValePart in Switch (Part: " + part.getName() + " - id:" + ((SwitchPart) part).getSwitchId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
					return false;
				}
				SwitchCaseBlock caseBlockFormat = ((SwitchPart) part).getCase(((VisualValuePart) vp).getValueAsInt());
				if (caseBlockFormat == null)
				{
					_error = "Error: no such case: " + ((VisualValuePart) vp).getValueAsInt() + " for (Switch " + part.getName() + " - id:" + ((SwitchPart) part).getSwitchId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
					return false;
				}

				DataSwitchBlock caseBlock = new DataSwitchBlock(dataNode, caseBlockFormat, vp);
				if (!parse(protocol, buff, caseBlockFormat, caseBlock))
					return false;
			}
			else if (part instanceof PartContainer)
			{
				_error = "Error: Unparsed new type of PartContainer (" + this.getClass().getSimpleName() + ")";
				return false;
			}
			else if (part.getType() != null && part.getType().isReadableType())
			{
				ValuePart vp = part.getType().getValuePart(dataNode, part);
				vp.parse(buff, this);
			}
		}
		return true;
	}

	public DataTreeNodeContainer getRootNode()
	{
		return _packetParts;
	}

	public PacketInfo getPacketInfo()
	{
		return _packetFormat;
	}

	public boolean hasError()
	{
		return _error != null;
	}

	public String getErrorMessage()
	{
		return _error != null ? _error : null;
	}

	@Override
	public PacketType getPacketType()
	{
		return _packetType;
	}

	@Override
	public long getTime()
	{
		return _time;
	}

	@Override
	public byte[] getAllData()
	{
		return _decryptedData;
	}

	public byte[] getCryptedData()
	{
		return _cryptedData;
	}

	public boolean isKey()
	{
		return _packetFormat != null && _packetFormat.isKey();
	}

	public float getFloat(String s)
	{
		return ((VisualValuePart)getRootNode().getPartByName(s)).getValueAsFloat();
	}

	public double getDouble(String s)
	{
		return ((VisualValuePart)getRootNode().getPartByName(s)).getValueAsDouble();
	}

	public int getInt(String s)
	{
		return ((VisualValuePart)getRootNode().getPartByName(s)).getValueAsInt();
	}

	public String getString(String s)
	{
		return ((VisualValuePart)getRootNode().getPartByName(s)).getValueAsString();
	}

	public byte[] getBytes(String s)
	{
		return ((RawValuePart)getRootNode().getPartByName(s)).getBytes();
	}
}