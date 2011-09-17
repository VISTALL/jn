package com.jds.jn.logs;

import java.io.File;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.jds.jn.config.RValues;
import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.logs.listeners.DefaultReaderListener;
import com.jds.jn.logs.listeners.ReaderListener;
import com.jds.jn.logs.readers.AbstractReader;
import com.jds.jn.logs.readers.CapReader;
import com.jds.jn.logs.readers.JNL2Reader;
import com.jds.jn.logs.readers.JNLReader;
import com.jds.jn.logs.readers.LogReader;
import com.jds.jn.logs.readers.PCapReader;
import com.jds.jn.logs.readers.PLogReader;
import com.jds.jn.logs.readers.PSLReader;
import com.jds.jn.util.FileUtils;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:40:56
 */
public class Reader
{
	private static Reader _instance;
	public static final ReaderListener DEFAULT_READER_LISTENER = new DefaultReaderListener();
	private Map<String, Map.Entry<AbstractReader, FileFilter>> _readers = new HashMap<String, Map.Entry<AbstractReader, FileFilter>>();

	public static Reader getInstance()
	{
		if (_instance == null)
		{
			_instance = new Reader();
		}

		return _instance;
	}

	private Reader()
	{
		addReader(new PSLReader());
		addReader(new JNLReader());
		addReader(new JNL2Reader());
		addReader(new PLogReader());
		addReader(new LogReader());
		addReader(new CapReader());
		addReader(new PCapReader());
	}

	public void addReader(AbstractReader r)
	{
		_readers.put(r.getFileExtension(), new AbstractMap.SimpleEntry<AbstractReader, FileFilter>(r, new ReaderFileFilter(r)));
	}

	public JFileChooser getFileChooser()
	{
		final JFileChooser chooser = new JFileChooser(RValues.LAST_FOLDER.asString());
		for (Map.Entry<AbstractReader, FileFilter> v : _readers.values())
		{
			chooser.addChoosableFileFilter(v.getValue());
		}

		return chooser;
	}

	public void showChooseDialog()
	{
		final JFileChooser chooser = getFileChooser();

		final int returnVal = chooser.showOpenDialog(MainForm.getInstance());

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			JActionListener.handle(JActionEvent.OPEN_SELECT_FILE, this, chooser.getSelectedFile());
		}
	}

	public void read(File f, ReaderListener listener) throws Exception
	{
		String ex = FileUtils.getFileExtension(f);

		Map.Entry<AbstractReader, FileFilter> entry = _readers.get(ex);
		if (entry != null)
		{
			entry.getKey().read(f, listener);
		}
		else
		{
			listener.onFinish(null, null);
		}
	}

	private class ReaderFileFilter extends FileFilter
	{
		private final AbstractReader _reader;

		public ReaderFileFilter(AbstractReader reader)
		{
			_reader = reader;
		}

		@Override
		public boolean accept(File f)
		{
			return f.isDirectory() || f.isFile() && f.getName().endsWith("." + _reader.getFileExtension());
		}

		@Override
		public String getDescription()
		{
			return String.format("%s (.%s)", _reader.getReaderInfo(), _reader.getFileExtension());
		}

		@Override
		public String toString()
		{
			return getDescription();
		}
	}

	public Collection<Map.Entry<AbstractReader, FileFilter>> getReaders()
	{
		return _readers.values();
	}
}
