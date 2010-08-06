package com.jds.jn.network.packets;

import java.nio.BufferUnderflowException;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.formattree.*;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.MacroInfo;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.nio.buffer.NioBuffer;

/**
 * @author Gilles Duboscq   - first version
 * @author VISTALL - full rewrite
 */
public class DecryptPacket implements IPacketData
{
	private final NotDecryptPacket _notDecryptPacket;
	private Protocol _protocol;

	private DataTreeNodeContainer _packetParts;
	private Format _dataFormat;
	private PacketInfo _packetFormat;

	protected NioBuffer _buf;

	protected boolean _mustUpdate = true;
	protected String _error;

	protected String[] _colorForHex;

	public DecryptPacket(byte[] data, PacketType type, Protocol protocol)
	{
		this(data, type, protocol, true);
	}

	public DecryptPacket(byte[] data, PacketType type, Protocol protocol, boolean parse)
	{
		this(new NotDecryptPacket(type, data, System.currentTimeMillis(), protocol.getOrder()), protocol, parse);
	}

	public DecryptPacket(NotDecryptPacket packet, Protocol protocol)
	{
		this(packet, protocol, true);
	}

	public DecryptPacket(NotDecryptPacket packet, Protocol protocol, boolean parse)
	{
		_notDecryptPacket = packet;
		_protocol = protocol;

		_buf = packet.getBuffer().clone();
		_colorForHex = new String[packet.length()];

		_packetFormat = getProtocol().getPacketInfo(this);
		if (_packetFormat == null)
		{
			_buf.position(0);
		}
		else
		{
			_dataFormat = _packetFormat.getDataFormat();
		}

		if (parse)
		{
			try
			{
				parse();
			}
			catch (BufferUnderflowException e)
			{
				_error = "Insuficient data for the specified format";
				MainForm.getInstance().info("Parsing packet (" + getName() + "), insuficient data for the specified format. Please verify the format.");
			}
			catch (Exception e)
			{
				_error = "Exception: " + e.getMessage();
				MainForm.getInstance().info("Exception: " + e);
			}
		}
	}

	public String getName()
	{
		if (getPacketFormat() == null)
		{
			return null;
		}
		return getPacketFormat().getName();
	}

	public void setColor(int index, String color)
	{
		_colorForHex[index] = color;
	}

	public String getColor(int index)
	{
		return _colorForHex[index];
	}

	public synchronized void parse()
	{
		if (!_mustUpdate) // could also be used to invalidate parsing results after protocol change
		{
			return;
		}
		_mustUpdate = false;
		_packetParts = new DataTreeNodeContainer();

		if (getDataFormat() != null)
		{
			getDataFormat().registerFormatChangeListener(this);

			parse(getDataFormat().getMainBlock(), _packetParts);
		}
	}

	private boolean parse(PartContainer protocolNode, DataTreeNodeContainer dataNode)
	{
		for (Part part : protocolNode.getParts())
		{
			if (part instanceof ForPart)
			{
				// find the size of this for in the scope
				ValuePart vp = dataNode.getPacketValuePartById(((ForPart) part).getForId());
				if (vp == null)
				{
					_error = "Error: could not find valuepart to loop on for (For " + part.getName() + " - id:" + ((ForPart) part).getForId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
					return false;
				}
				if (!(vp instanceof VisualValuePart))
				{
					_error = "Error: for id didnt refer to an IntValePart in (For " + part.getName() + " - id:" + ((ForPart) part).getForId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
					return false;
				}
				int size = ((VisualValuePart) vp).getValueAsInt();

				//check size here
				if (((ForPart) part).getModelBlock().hasConstantLength())
				{
					int forBlockSize = ((ForPart) part).getModelBlock().getLength();
					if (size * forBlockSize > getBuffer().remaining())
					{
						_error = "Error size is too big (" + size + ") for For (Part Name: " + part.getName() + " - Id: " + ((ForPart) part).getForId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
						return false;
					}
				}
				else if (size > getBuffer().remaining())
				{
					_error = "Error size is too big (" + size + ") for For (Part Name: " + part.getName() + " - Id: " + ((ForPart) part).getForId() + ") in [" + part.getContainingFormat().getPacketInfo() + "]";
					return false;
				}
				DataForPart forPart = new DataForPart(dataNode, (ForPart) part);
				for (int i = 0; i < size; i++)
				{
					DataForBlock forBlock = new DataForBlock(forPart, ((ForPart) part).getModelBlock(), i, size);
					if (!parse(((ForPart) part).getModelBlock(), forBlock))
					{
						return false;
					}
				}
			}
			else if (part instanceof MacroPart)
			{
				MacroInfo macro = _protocol.getMacroInfo(((MacroPart)part).getMacroId());
				if(macro == null)
				{
					_error = "Not find macro by id: " + ((MacroPart)part).getMacroId();
					return false;
				}

				if (macro.getModelBlock().hasConstantLength())
				{
					int macroSize = macro.getModelBlock().getLength();
					if (macroSize > getBuffer().remaining())
					{
						_error =  "Incorrect buffer to read macro " + part.getName();
						return false;
					}
				}

				DataMacroPart macroPart = new DataMacroPart(dataNode, (MacroPart)part);
				if (!parse(macro.getModelBlock(), macroPart))
				{
					return false;
				}
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
				if (!parse(caseBlockFormat, caseBlock))
				{
					return false;
				}
			}
			else if (part instanceof PartContainer)
			{
				_error = "Error: Unparsed new type of PartContainer (" + this.getClass().getSimpleName() + ")";
				return false;
			}
			else if (part.getType().isReadableType())
			{
				ValuePart vp = part.getType().getValuePart(dataNode, part);
				vp.parse(getBuffer(), this);
			}
		}
		return true;
	}

	public DataTreeNodeContainer getRootNode()
	{
		return _packetParts;
	}

	public Format getDataFormat()
	{
		return _dataFormat;
	}

	public PacketInfo getPacketFormat()
	{
		return _packetFormat;
	}

	public int getSize()
	{
		return getBuffer().limit();
	}

	public void invalidateParsing()
	{
		synchronized (this)
		{
			_mustUpdate = true;
			_packetParts = null;
		}
	}

	public boolean hasError()
	{
		return _error != null;
	}

	public String getErrorMessage()
	{
		return _error != null ? _error : null;
	}

	public NioBuffer getBuffer()
	{
		return _buf;
	}

	@Override
	public PacketType getPacketType()
	{
		return _notDecryptPacket.getPacketType();
	}

	@Override
	public long getTime()
	{
		return _notDecryptPacket.getTime();
	}

	@Override
	public byte[] getAllData()
	{
		return _notDecryptPacket.getAllData();
	}

	public byte[] getNotDecryptData()
	{
		return _notDecryptPacket.getAllData();
	}

	public boolean isKey()
	{
		return _packetFormat != null && _packetFormat.isKey();
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

	public Protocol getProtocol()
	{
		return _protocol;
	}
}