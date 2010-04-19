package com.jds.jn.parser;

import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.formattree.*;
import com.jds.nio.buffer.NioBuffer;
import javolution.util.FastList;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gilles Duboscq
 */
public class DataStructure
{
	protected ByteOrder _order;
	protected NioBuffer _buf;
	protected NioBuffer _fullBuffer;
	protected byte[] _unparsed;
	protected Format _format;
	protected DataTreeNodeContainer _packetParts;
	protected boolean _mustUpdate = true;
	protected boolean _isValid;
	protected String _error;
	protected DataPacketMode _mode;

	protected String[] _colorForHex;

	public enum DataPacketMode
	{
		PARSING,
		FORGING
	}

	public DataStructure(byte[] raw, Format format)
	{
		this(NioBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN), format);
	}

	public DataStructure(NioBuffer buf, Format format)
	{
		setData(buf);
		_format = format;
	}

	public DataStructure(DataTreeNodeContainer root)
	{
		this(root, null, ByteOrder.LITTLE_ENDIAN);
	}

	public DataStructure(DataTreeNodeContainer root, ByteOrder order)
	{
		this(root, null, order);
	}

	public DataStructure(DataTreeNodeContainer root, Format format)
	{
		this(root, format, ByteOrder.LITTLE_ENDIAN);
	}

	public void setData(NioBuffer buf)
	{
		_colorForHex = new String[buf.array().length ];

		setBuffer(buf);

		byte[] fdata = Arrays.copyOf(buf.array(), buf.array().length);
		setFullBuffer(NioBuffer.wrap(fdata).order(ByteOrder.LITTLE_ENDIAN));

		_mode = DataPacketMode.PARSING;
		_order = buf.order();
	}

	public void setColor(int index, String color)
	{
		_colorForHex[index] = color;
	}

	public String getColor(int index)
	{
		return _colorForHex[index];
	}

	public DataStructure(DataTreeNodeContainer root, Format format, ByteOrder order)
	{
		if (!root.isRoot())
		{
			throw new IllegalArgumentException("The root of a Forging mode packet must be... root :p");
		}
		_packetParts = root;
		_format = format;
		_mode = DataPacketMode.FORGING;
		_order = order;
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

		//getBuffer().rewind();

		if (getFormat() != null)
		{
			getFormat().registerFormatChangeListener(this);
			boolean ret = parse(getFormat().getMainBlock(), _packetParts);
			setValid(ret);
		}
		else
		{
			this.setValid(false);
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
		if (this.getMode() == DataPacketMode.PARSING)
		{
			this.parse();
		}
		return _packetParts;
	}

	public DataPacketMode getMode()
	{
		return _mode;
	}

	public Format getFormat()
	{
		return _format;
	}

	public void setFormat(Format f)
	{
		_format = f;
	}

	public byte get(int idx)
	{
		return getBuffer().get(idx);
	}

	public byte[] getUnparsedData()
	{
		if (_unparsed == null)
		{
			int size = getBuffer().remaining();
			_unparsed = new byte[size];
			getBuffer().get(_unparsed);
		}
		return _unparsed;
	}

	public byte[] getData()
	{
		return getBuffer().array();
	}

	public int getSize()
	{
		return getBuffer().limit();
	}

	/**
	 * to be used by UI
	 *
	 * @return
	 */
	public List<ValuePart> getValuePartList()
	{
		return getValuePartList(getRootNode());
	}

	private List<ValuePart> getValuePartList(DataTreeNodeContainer node)
	{
		FastList<ValuePart> parts = new FastList<ValuePart>();
		for (DataTreeNode n : node.getNodes())
		{
			if (n instanceof ValuePart)
			{
				parts.add((ValuePart) n);
			}
			else if (n instanceof DataTreeNodeContainer)
			{
				parts.addAll(getValuePartList((DataTreeNodeContainer) n));
			}
		}
		return parts;
	}

	protected void setValid(boolean val)
	{
		_isValid = val;
	}

	public boolean isValid()
	{
		return _isValid || _mustUpdate;
	}

	public void invalidateParsing()
	{
		if (this.getMode() != DataPacketMode.PARSING)
		{
			throw new IllegalStateException("Can not invalidate parsing on a non-parsing mode DataPacket");
		}
		synchronized (this)
		{
			this.setValid(false);
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
			this.setValid(false);
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

	public boolean isTreeValid()
	{
		boolean ret;
		if (this.getFormat() != null)
		{
			// validate against Format
			ret = validateTree(this.getRootNode(), this.getFormat().getMainBlock());
		}
		else
		{
			// just validate the structure
			ret = validateTree(this.getRootNode());
		}
		return ret;
	}

	public NioBuffer getFullBuffer()
	{
		return _fullBuffer;
	}

	public NioBuffer getBuffer()
	{
		return _buf;
	}

	public void setFullBuffer(NioBuffer buf)
	{
		_fullBuffer = buf;
	}

	protected void setBuffer(NioBuffer buf)
	{
		_buf = buf;
	}

	private boolean validateTree(DataTreeNodeContainer node)
	{
		boolean insideFor = node instanceof DataForPart;

		DataForBlock model = null;
		for (DataTreeNode n : node.getNodes())
		{
			if (n instanceof DataForPart)
			{
				if (insideFor)
				{
					return false;
				}
				if (!validateTree((DataForPart) n))
				{
					return false;
				}
			}

			else if (n instanceof DataSwitchBlock)
			{
				if (insideFor)
				{
					return false;
				}
				if (!validateTree((DataSwitchBlock) n))
				{
					return false;
				}
			}
			else if (n instanceof DataForBlock)
			{
				if (!insideFor)
				{
					return false;
				}
				if (model == null)
				{
					model = (DataForBlock) n;
				}
				else
				{
					if (!branchesEqual(model, (DataForBlock) n))
					{
						return false;
					}
				}
				if (!validateTree((DataForBlock) n))
				{
					return false;
				}
			}
			else if (insideFor)
			{
				return false;
			}
		}
		return true;
	}

	private boolean branchesEqual(DataTreeNodeContainer branch1, DataTreeNodeContainer branch2)
	{
		Iterator<? extends DataTreeNode> it1 = branch1.getNodes().iterator();
		Iterator<? extends DataTreeNode> it2 = branch2.getNodes().iterator();
		while (it1.hasNext())
		{
			if (!it2.hasNext())
			{
				return false;
			}
			DataTreeNode node1 = it1.next();
			DataTreeNode node2 = it2.next();
			if (node1.getClass() != node2.getClass())
			{
				return false;
			}
			if (node1 instanceof NumberValuePart && ((NumberValuePart) node1).getType() != ((NumberValuePart) node2).getType())
			{
				return false;
			}
			//if(node1 instanceof D)
		}
		if (it2.hasNext())
		{
			return false;
		}
		return true;
	}

	private boolean validateTree(DataTreeNodeContainer dataTreeNode, PartContainer protocolNode)
	{
		return true;
	}
}