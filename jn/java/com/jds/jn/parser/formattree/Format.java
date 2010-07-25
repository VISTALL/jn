package com.jds.jn.parser.formattree;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.protocol.protocoltree.PacketInfo;


/**
 * @author Gilles Duboscq
 */
public class Format
{
	private final PacketInfo _packetInfo;
	private final PartContainer _mainBlock;
	private final List<WeakReference<DecryptPacket>> _formatChangeListeners;

	public Format(PacketInfo container)
	{
		_formatChangeListeners = new ArrayList<WeakReference<DecryptPacket>>();
		_mainBlock = new PartContainer(PartType.block, true);
		_mainBlock.setContainingFormat(this);
		_packetInfo = container;
	}

	public PartContainer getMainBlock()
	{
		return _mainBlock;
	}

	public void triggerFormatChanged()
	{
		for (WeakReference<DecryptPacket> ref : _formatChangeListeners)
		{
			DecryptPacket dp = ref.get();
			if (dp != null)
			{
				dp.invalidateParsing();
			}
		}
	}

	public void registerFormatChangeListener(DecryptPacket packet)
	{
		_formatChangeListeners.add(new WeakReference<DecryptPacket>(packet));
	}

	public PacketInfo getPacketInfo()
	{
		return _packetInfo;
	}
}