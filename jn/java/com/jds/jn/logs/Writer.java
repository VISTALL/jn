package com.jds.jn.logs;

import com.jds.jn.logs.writers.JNLWriter;
import com.jds.jn.session.Session;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.09.2009
 * Time: 21:41:42
 */
public class Writer
{
	public static void write(File f, FileFilter filter, Session sss) throws Exception
	{
		if (f.getAbsolutePath().endsWith(".jnl"))
		{
			JNLWriter writer = new JNLWriter(f.getAbsolutePath(), sss);
			writer.write();
		}
		else
		{
			JNLWriter writer = new JNLWriter(f.getAbsolutePath() + ".jnl", sss);
			writer.write();
		}
	}
}
