package com.jds.jn.network.methods.jpcap;

import jpcap.packet.TCPPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 20:25 07/12/2009
 */
public class SeqHolder
{
	private final long _nextSeq;

	private final TCPPacket _packet;

	private boolean _acked;

	public SeqHolder(long nextSeq, TCPPacket packet)
	{
		_nextSeq = nextSeq;
		_packet = packet;
	}

	/**
	 * @return the nextSeq
	 */
	public long getNextSeq()
	{
		return _nextSeq;
	}

	/**
	 * @return the packet
	 */
	public TCPPacket getPacket()
	{
		return _packet;
	}

	public void ack()
	{
		_acked = true;
	}

	public boolean isAcked()
	{
		return _acked;
	}

}
