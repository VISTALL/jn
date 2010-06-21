package com.jds.jn.network.packets;

import java.nio.BufferUnderflowException;

import com.jds.jn.Jn;
import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.formattree.*;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.nio.buffer.NioBuffer;

/**
 * @author Gilles Duboscq
 * @rewrite VISTALL - full rewrite
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
	protected DataPacketMode _mode;

	protected String[] _colorForHex;

	public enum DataPacketMode
	{
		PARSING,
		FORGING
	}

	public DecryptPacket(byte[] data, PacketType type, Protocol protocol)
	{
		this(data, type, protocol, true);
	}

	public DecryptPacket(byte[] data, PacketType type, Protocol protocol, boolean parse)
	{
		this(new NotDecryptPacket(type, data, System.currentTimeMillis()), protocol, parse);
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

		_packetFormat = getProtocol().getFormat(this);
		if (_packetFormat == null)
		{
			_buf.position(0);
		}
		else
		{
			_dataFormat = _packetFormat.getDataFormat();
		}

		_mode = DataPacketMode.PARSING;

		if (parse)
		{
			try
			{
				parse();
			}
			catch (BufferUnderflowException e)
			{
				_error = "Insuficient data for the specified format";
				Jn.getInstance().warn("Parsing packet (" + getName() + "), insuficient data for the specified format. Please verify the format.");
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
		if (getMode() != DataPacketMode.PARSING)
		{
			throw new IllegalStateException("Can not parse a non-parsing mode DataPacket");
		}
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
					_error = "Error: could not find valuepart to loop on for (For " + part.getName() + " - id:" + ((ForPart) part).getForId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
					return false;
				}
				if (!(vp instanceof NumberValuePart))
				{
					_error = "Error: for id didnt refer to an IntValePart in (For " + part.getName() + " - id:" + ((ForPart) part).getForId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
					return false;
				}
				int size = ((NumberValuePart) vp).getValueAsInt();

				//check size here
				if (((ForPart) part).getModelBlock().hasConstantLength())
				{
					int forBlockSize = ((ForPart) part).getModelBlock().getLength();
					if (size * forBlockSize > getBuffer().remaining())
					{
						_error = "Error size is too big (" + size + ") for For (Part Name: " + part.getName() + " - Id: " + ((ForPart) part).getForId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
						return false;
					}
				}
				else if (size > getBuffer().remaining())
				{
					_error = "Error size is too big (" + size + ") for For (Part Name: " + part.getName() + " - Id: " + ((ForPart) part).getForId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
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
			else if (part instanceof SwitchPart)
			{
				//find the actual type
				ValuePart vp = dataNode.getPacketValuePartById(((SwitchPart) part).getSwitchId());
				if (vp == null)
				{
					_error = "Error: could not find valuepart to switch on for Switch (Part: " + part.getName() + " - id:" + ((SwitchPart) part).getSwitchId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
					return false;
				}
				if (!(vp instanceof NumberValuePart))
				{
					_error = "Error: swicth id didnt refer to an IntValePart in Switch (Part: " + part.getName() + " - id:" + ((SwitchPart) part).getSwitchId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
					return false;
				}
				SwitchCaseBlock caseBlockFormat = ((SwitchPart) part).getCase((int) ((NumberValuePart) vp).getValueAsInt());
				if (caseBlockFormat == null)
				{
					_error = "Error: no such case: " + ((NumberValuePart) vp).getValueAsInt() + " for (Switch " + part.getName() + " - id:" + ((SwitchPart) part).getSwitchId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
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
		if (getMode() == DataPacketMode.PARSING)
		{
			parse();
		}
		return _packetParts;
	}

	public DataPacketMode getMode()
	{
		return _mode;
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
		if (this.getMode() != DataPacketMode.PARSING)
		{
			throw new IllegalStateException("Can not invalidate parsing on a non-parsing mode DataPacket");
		}
		synchronized (this)
		{
			_mustUpdate = true;
			_packetParts = null;
		}

	}

	public void invalidateForging()
	{
		if (this.getMode() != DataPacketMode.FORGING)
		{
			throw new IllegalStateException("Can not invalidate forging on a non-forging mode DataPacket");
		}
		synchronized (this)
		{
			_mustUpdate = true;
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
		return ((NumberValuePart)getRootNode().getPartByName(s)).getValueAsDouble();
	}

	public int getInt(String s)
	{
		return ((NumberValuePart)getRootNode().getPartByName(s)).getValueAsInt();
	}

	public Protocol getProtocol()
	{
		return _protocol;
	}
}