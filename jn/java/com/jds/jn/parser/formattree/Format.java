package com.jds.jn.parser.formattree;

import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import javolution.util.FastList;

import com.jds.jn.parser.PartType;

import java.lang.ref.WeakReference;


/**
 * @author Gilles Duboscq
 */
public class Format
{
	private final PacketInfo _containingPacketFormat;
	private final PartContainer _mainBlock;
	private final FastList<WeakReference<DecryptPacket>> _formatChangeListeners;

	public Format(PacketInfo container)
	{
		_formatChangeListeners = new FastList<WeakReference<DecryptPacket>>();
		_mainBlock = new PartContainer(PartType.block, true);
		_mainBlock.setContainingFormat(this);
		_containingPacketFormat = container;
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

	public PacketInfo getContainingPacketFormat()
	{
		return _containingPacketFormat;
	}
}