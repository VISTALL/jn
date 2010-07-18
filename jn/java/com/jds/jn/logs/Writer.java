package com.jds.jn.logs;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.jds.jn.Jn;
import com.jds.jn.config.RValues;
import com.jds.jn.logs.writers.AbstractWriter;
import com.jds.jn.logs.writers.JNL2Writer;
import com.jds.jn.session.Session;
import com.jds.jn.util.ThreadPoolManager;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:41:42
 */
public class Writer
{
	private static Writer _instance;
	private HashMap<String, AbstractWriter> _writers = new HashMap<String, AbstractWriter>();

	public static Writer getInstance()
	{
		if (_instance == null)
		{
			_instance = new Writer();
		}

		return _instance;
	}

	private Writer()
	{
		addWriter(new JNL2Writer());
	}

	public void addWriter(AbstractWriter r)
	{
		_writers.put(r.getFileExtension(), r);
	}

	public void chooseDialog()
	{
		final JFileChooser c = new JFileChooser(RValues.LAST_FOLDER.asString());
		for(AbstractWriter w : _writers.values())
		{
			c.addChoosableFileFilter(new WriterFileFilter(w));
		}

		final int r = c.showSaveDialog(Jn.getForm());

		if (r == JFileChooser.APPROVE_OPTION)
		{
			ThreadPoolManager.getInstance().execute(new Runnable()
			{
				@Override
				public void run()
				{
				 	Session session = Jn.getForm().getViewTabbedPane().getCurrentViewPane().getSession();

					if (session == null)
					{
						return;
					}

					AbstractWriter w = ((WriterFileFilter)c.getFileFilter()).getWriter();

					try
					{
						w.write(c.getSelectedFile(), session);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	}

	private class WriterFileFilter extends FileFilter
	{
		private final AbstractWriter _writer;

		public WriterFileFilter(AbstractWriter writer)
		{
			_writer = writer;
		}

		@Override
		public boolean accept(File f)
		{
			return f.isDirectory() || f.isFile() && f.getName().endsWith("." +  _writer.getFileExtension());
		}

		@Override
		public String getDescription()
		{
			return String.format("%s (.%s)", _writer.getWriterInfo(), _writer.getFileExtension());
		}

		public AbstractWriter getWriter()
		{
			return _writer;
		}
	}
}
