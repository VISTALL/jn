package com.jds.jn.network.methods.jpcap;

import com.jds.jn.network.packets.JPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.session.CaptorSession;
import com.jds.jn.session.SessionTable;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 20:27 07/12/2009
 */
public class Receiver implements PacketReceiver
{
	private final Jpcap _pcap;

	public Receiver(Jpcap p)
	{
		_pcap = p;
	}

	@Override
	public void receivePacket(Packet p)
	{
		if (p instanceof TCPPacket)
		{
			TCPPacket pi = (TCPPacket) p;

			CaptorSession session = null;

			if ((session = (CaptorSession) SessionTable.getInstance().getSession(pi.src_port * pi.dst_port)) == null)
			{
				session = SessionTable.getInstance().newGameSession(_pcap, pi.src_port * pi.dst_port);
			}

			int size = 0;

			if (pi.dst_port != _pcap.getPort()) // client
			{
				if (p.data.length > 0)
				{
					session.getServerSequenced().add(pi);
				}

				session.getClientSequenced().ack(pi);

				for (TCPPacket packet : session.getClientSequenced().getSequencedPackets())
				{
					session.getClientbuf().putData(packet.data);
					while ((size = session.getClientbuf().nextAvaliablePacket()) > 0)
					{
						byte[] header = new byte[2];
						byte[] packetData = new byte[size];
						session.getClientbuf().getNextPacket(header, packetData);
						session.receivePacket(new JPacket(PacketType.CLIENT, packetData));
					}
				}
				session.getClientSequenced().flush();
			}
			else
			{
				if (p.data.length > 0)
				{
					session.getClientSequenced().add(pi);
				}

				session.getServerSequenced().ack(pi);

				for (TCPPacket packet : session.getServerSequenced().getSequencedPackets())
				{
					session.getServerbuf().putData(packet.data);
					while ((size = session.getServerbuf().nextAvaliablePacket()) > 0)
					{
						byte[] header = new byte[2];
						byte[] packetData = new byte[size];
						session.getServerbuf().getNextPacket(header, packetData);
						session.receivePacket(new JPacket(PacketType.SERVER, packetData));
					}
				}
				session.getServerSequenced().flush();
			}
		}
	}
}

