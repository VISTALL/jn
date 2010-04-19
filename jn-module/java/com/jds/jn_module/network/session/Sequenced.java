package com.jds.jn_module.network.session;

import jpcap.packet.TCPPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  3:08:26/04.04.2010
 */
public class Sequenced
{
	private static final long MODULO = 4294967296L;
	private HashMap<Long, SeqHolder> _waitingPrevious = new HashMap<Long, SeqHolder>();
	private ArrayList<TCPPacket> _sequenced = new ArrayList<TCPPacket>();
	private long _lastAck;

	public Sequenced()
	{
	}

	public void add(TCPPacket p)
	{
		for (SeqHolder sh : _waitingPrevious.values())
		{
			TCPPacket old = sh.getPacket();
			if (sh.getPacket().sequence == p.sequence)
			{
				if (old.data.length < p.data.length)
				{
					int diff = p.data.length - old.data.length;
					//System.err.printf("DIFF %d = %d - %d \n",diff, p.data.length, old.data.length);
					long seq = (old.sequence + old.data.length) % MODULO;
					//System.err.println("ADJUSTED TO SEQ: "+seq+"\nPACKET: "+p);
					byte[] data = new byte[diff];
					System.arraycopy(p.data, p.data.length - diff, data, 0, data.length);
					p.data = data;
					p.sequence = seq;
					//System.err.println(Util.hexDump(data));
				}
				else if (old.data.length == p.data.length)
				{
					// packet retransmitted
					// dont add, else the data will be duped (acked again)
					return;
				}
			}
		}

		long nextSeq = (p.sequence + p.data.length) % MODULO;


		_waitingPrevious.put(nextSeq, new SeqHolder(nextSeq, p));
		this.processAck(_lastAck);
	}

	public void ack(TCPPacket p)
	{
		_lastAck = p.ack_num;

		processAck(p.ack_num);

		byte[] options = p.option;
		if (options != null)
		{
			long start = -1;
			long end = -1;

			for (int i = 0; i < options.length;)
			{
				// 0 end of opts
				// 1 nop
				if (options[i] == 0 || options[i] == 1)
				{
					i++;
					continue;
				}

				int type = options[i++] & 0xff;
				int size = options[i++] & 0xff;
				switch (type)
				{
					case 5: //SACK
						if (size - 2 == 8)
						{
							//System.err.println("SACK no pacote: "+p);
							//System.exit(0);
							start = getUInt(options, i);
							i += 4;
							end = getUInt(options, i);
						}
						else
						{
							throw new RuntimeException("No support for multiple S-ACK");
						}
						break;
					default: //unsupported operation
						break;
				}

				i += size - 2;
			}

			while (end != -1 && start != -1)
			{
				SeqHolder sh = null;
				for (SeqHolder seqHolder : _waitingPrevious.values())
				{
					if (seqHolder.getPacket().sequence == start)
					{
						long nextSeq = (seqHolder.getPacket().sequence + seqHolder.getPacket().data.length) % MODULO;

						// ignore if SACK covers whole packet
						if (end != nextSeq)
						{
							sh = seqHolder;
							break;
						}
					}
				}

				// if not already ack'ed (duplicate ack recvd)
				if (sh != null)
				{
					//System.err.println("NULL ACK: "+_lastAck+" -> "+p.toString());
					//System.err.println("END: "+end+" START: "+start);
					int diff = (int) ((end - start) % MODULO);
					TCPPacket packet = sh.getPacket();

					// check if sack doesnt overlap to next packet
					if (diff > packet.data.length)
					{
						// this one covers whole packet so just ignore
						start = (start + packet.data.length) % MODULO;
						continue;
					}
					start = -1;
					end = -1;
					byte[] data = new byte[diff];

					//System.err.println("sh SEQ: "+sh.getPacket().sequence+" LEN: "+packet.data.length+" DIFF: "+diff);
					byte[] data2 = new byte[packet.data.length - diff];

					System.arraycopy(sh.getPacket().data, 0, data, 0, data.length);
					System.arraycopy(sh.getPacket().data, diff, data2, 0, data2.length);
					//System.err.println("### SACK ("+data2.length+")\nCAUSED BY: "+p+"\nON: "+sh.getPacket());

					TCPPacket sackPacket = new TCPPacket(packet.src_port, packet.dst_port, packet.sequence, packet.ack_num, packet.urg, packet.ack, packet.psh, packet.rst, packet.syn, packet.fin, packet.rsv1, packet.rsv2, packet.window, packet.urgent_pointer);
					sackPacket.data = data;
					sh.getPacket().data = data2;

					this.addSequenced(sackPacket);
				}
				else
				{
					end = -1;
					start = -1;
				}
			}
		}
		else
		{
			//processAck(p.ack_num);
		}
	}

	private static long getUInt(byte[] array, int offset)
	{
		if (array.length < offset + 4)
		{
			throw new IllegalArgumentException("Invalid offset for size");
		}

		long ret = ((array[offset] & 0xffl) << 24);
		ret |= ((array[offset + 1] & 0xffl) << 16);
		ret |= ((array[offset + 2] & 0xffl) << 8);
		ret |= (array[offset + 3] & 0xffl);

		return ret;
	}

	public void processAck(long ack)
	{
		SeqHolder sh = _waitingPrevious.get(ack);
		if (sh != null && !sh.isAcked())
		{
			//long previousSeq = (ack - sh.getPacket().data.length)%MODULO;
			long previousSeq = sh.getPacket().sequence;
			processAck(previousSeq);
			this.addSequenced(ack);
		}
	}

	private void addSequenced(long ack)
	{
		SeqHolder seqHolder = _waitingPrevious.get(ack);
		seqHolder.ack();
		TCPPacket packet = seqHolder.getPacket();

		this.addSequenced(packet);
	}

	private void addSequenced(TCPPacket packet)
	{
		_sequenced.add(packet);
	}

	public List<TCPPacket> getSequencedPackets()
	{
		return _sequenced;
	}

	public int getPendingSequencePackets()
	{
		return _waitingPrevious.size();
	}

	public void flush()
	{
		_sequenced.clear();
	}

	public boolean hasSequencedPacket()
	{
		return (!_sequenced.isEmpty());
	}

}

