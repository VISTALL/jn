package com.jds.jn.parser.formattree;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.protocol.protocoltree.PacketInfo;


/**
 * @author Gilles Duboscq
 */
public class Format
{
	private final PacketInfo _packetInfo;
	private final PartContainer _mainBlock;
	private final List<WeakReference<DecryptedPacket>> _formatChangeListeners;

	public Format(PacketInfo container)
	{
		_formatChangeListeners = new ArrayList<WeakReference<DecryptedPacket>>();
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
		for (WeakReference<DecryptedPacket> ref : _formatChangeListeners)
		{
			DecryptedPacket dp = ref.get();
			if (dp != null)
			{
				dp.invalidateParsing();
			}
		}
	}

	public void registerFormatChangeListener(DecryptedPacket packet)
	{
		_formatChangeListeners.add(new WeakReference<DecryptedPacket>(packet));
	}

	public PacketInfo getPacketInfo()
	{
		return _packetInfo;
	}
}