package com.jds.jn_module.network.session;

import jpcap.packet.TCPPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:09:04/04.04.2010
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

