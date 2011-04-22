package com.jds.jn.parser.formattree;

import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.protocol.protocoltree.PacketInfo;


/**
 * @author Gilles Duboscq
 */
public class Format
{
	private final PacketInfo _packetInfo;
	private final PartContainer _mainBlock;

	public Format(PacketInfo container)
	{
		_mainBlock = new PartContainer(PartType.block, true);
		_mainBlock.setContainingFormat(this);
		_packetInfo = container;
	}

	public PartContainer getMainBlock()
	{
		return _mainBlock;
	}

	public PacketInfo getPacketInfo()
	{
		return _packetInfo;
	}
}