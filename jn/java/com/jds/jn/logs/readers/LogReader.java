package com.jds.jn.logs.readers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.logs.listeners.ReaderListener;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.session.Session;
import com.jds.jn.util.StringHexBuffer;
import com.jds.jn.util.Util;
import com.jds.jn.util.version_control.Program;
import com.jds.jn.util.version_control.Version;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  14:41:18/03.09.2010
 */
public class LogReader extends AbstractReader
{
	private static final Logger _log = Logger.getLogger(LogReader.class);
	private LineNumberReader _reader;

	@Override
	public void read(File file, ReaderListener listener) throws IOException
	{
		if (!file.exists())
		{
			_log.info("File not exists: " + file);
			listener.onFinish(null, null);
			return;
		}

		if (_currentFile != null)
		{
			_log.info("Reader is busy: " + _currentFile.getName());
			listener.onFinish(null, null);
			return;
		}

		_listener = listener;
		_currentFile = file;
		_reader = new LineNumberReader(new FileReader(file));

		read();
	}

	@Override
	protected void close() throws IOException
	{
		_reader.close();
		_currentFile = null;
	}

	@Override
	public boolean parseHeader() throws IOException
	{
		_session = new Session(ListenerType.Game_Server, Util.positiveRandom());
		_session.setVersion(new Version(Program.L2EX_DUMPER, 1, 0, Version.STABLE, 0));
		return true;
	}

	@Override
	public void parsePackets() throws IOException
	{
		List<String> lines = new ArrayList<String>();
		String readLine = null;
		while ((readLine = _reader.readLine()) != null)
			lines.add(readLine);

		MainForm.getInstance().getProgressBar().setMaximum(lines.size());
		for(int i = 0; i < lines.size(); i++)
		{
			StringTokenizer token = new StringTokenizer(lines.get(i));
			if(token.countTokens() < 4)
				continue;
			token.nextToken();
			String type = token.nextToken();
			int size = Integer.parseInt(token.nextToken());
			StringHexBuffer buffer = new StringHexBuffer(token.nextToken());

			PacketType packetType = type.charAt(0) == 'I' ? PacketType.SERVER : PacketType.CLIENT;

			byte[] data = new byte[size];
			for(int $ = 0; $ < data.length; $++)
				data[$] = buffer.nextByte();

			DecryptedPacket dp = new DecryptedPacket(null, packetType, data, System.currentTimeMillis(), _session.getProtocol(), false);

			_session.receiveQuitPacket(dp, true, true);

			MainForm.getInstance().getProgressBar().setValue(i);
		}
	}

	@Override
	public String getFileExtension()
	{
		return "log";
	}

	@Override
	public String getReaderInfo()
	{
		return "l2ex.dumper";
	}
}
