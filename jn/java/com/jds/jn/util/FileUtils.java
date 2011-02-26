package com.jds.jn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21 лист 2009
 * Time: 10:37:01
 */
public class FileUtils
{
	public static String getFileExtension(File f)
	{
		String name = f.getName();
		int dot = name.lastIndexOf('.');
		return name.substring(dot + 1);
	}

	public static void copy(String fileNameSource, String fileNameDecs) throws IOException
	{
		File fileDecs = new File(fileNameDecs);
		File fileSource = new File(fileNameSource);
		if (!fileSource.exists())
		{
			throw new IllegalArgumentException("Source file is not exists. File " + fileNameSource);
		}
		if (fileDecs.exists())
		{
			fileDecs.delete();
		}
		fileDecs.createNewFile();

		InputStream outReal = new FileInputStream(fileSource);
		byte[] data = new byte[outReal.available()];
		outReal.read(data);
		outReal.close();


		OutputStream in = new FileOutputStream(fileDecs);
		in.write(data);
		in.close();
	}

}
