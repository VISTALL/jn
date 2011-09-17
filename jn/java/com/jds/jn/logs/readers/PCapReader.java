package com.jds.jn.logs.readers;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.jds.jn.logs.listeners.ReaderListener;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.session.Session;
import com.jds.jn.util.Util;
import com.jds.jn.util.version_control.Program;
import com.jds.jn.util.version_control.Version;
import jpcap.JpcapCaptor;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

/**
 * @author VISTALL
 * @date 17:52/07.09.2011
 */
public class PCapReader extends AbstractReader
{
	private JpcapCaptor _jpcap;

	public PCapReader()
	{

	}

	@Override
	public void read(File file, ReaderListener listener) throws IOException
	{
		if(!file.exists())
		{
			_log.info("File not exists: " + file);
			listener.onFinish(null, null);
			return;
		}

		if(_currentFile != null)
		{
			_log.info("Reader is busy: " + _currentFile.getName());
			listener.onFinish(null, null);
			return;
		}

		_listener = listener;

		_jpcap = JpcapCaptor.openFile(file.getAbsolutePath());

		read();
	}

	@Override
	public boolean parseHeader() throws IOException
	{
		_session = new Session(ListenerType.Game_Server, Util.positiveRandom());
		_session.setVersion(new Version(Program.UNKNOWN, 1, 0, Version.STABLE, 0));
		return true;
	}

	@Override
	public void parsePackets() throws IOException
	{
		while(true)
		{
			Packet p = _jpcap.getPacket();
			if(p == null || p == Packet.EOF)
				break;

			if(!(p instanceof TCPPacket))
				continue;

			TCPPacket tcpPacket = (TCPPacket)p;

			InetAddress addr = tcpPacket.src_ip;

			System.out.println(addr + " " + tcpPacket.dst_port);
		}
	}

	@Override
	protected void close() throws IOException
	{
		_jpcap.close();
	}

	@Override
	public String getFileExtension()
	{
		return "pcap";
	}

	@Override
	public String getReaderInfo()
	{
		return "WinPcap log";
	}
}
