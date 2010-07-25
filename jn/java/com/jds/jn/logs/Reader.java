package com.jds.jn.logs;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jds.jn.config.RValues;
import com.jds.jn.gui.JActionEvent;
import com.jds.jn.gui.JActionListener;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.logs.readers.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:40:56
 */
public class Reader
{
	private static Reader _instance;
	private HashMap<String, AbstractReader> _readers = new HashMap<String, AbstractReader>();

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
	}

	public void addReader(AbstractReader r)
	{
		_readers.put(r.getFileExtension(), r);
	}

	public void showChooseDialog()
	{
		final JFileChooser chooser = new JFileChooser(RValues.LAST_FOLDER.asString());
		for(AbstractReader reader : _readers.values())
		{
			chooser.addChoosableFileFilter(new ReaderFileFilter(reader));
		}

		final int returnVal = chooser.showOpenDialog(MainForm.getInstance());

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			JActionListener.handle(JActionEvent.OPEN_SELECT_FILE, this, chooser.getSelectedFile());
		}
	}

	public void read(File f) throws Exception
	{
		Pattern p = Pattern.compile("(\\S+)\\.(\\S+)");
		Matcher m = p.matcher(f.getName());

		if (m.find())
		{
			String st = m.group(2);
			AbstractReader reader = _readers.get(st);
			if(reader != null)
			{
				reader.read(f);
			}
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
			return f.isDirectory() || f.isFile() && f.getName().endsWith("." +  _reader.getFileExtension());
		}

		@Override
		public String getDescription()
		{
			return String.format("%s (.%s)", _reader.getReaderInfo(), _reader.getFileExtension());
		}
	}
}
