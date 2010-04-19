package com.jds.jn.remotefiles;

import com.jds.jn.Jn;
import javolution.util.FastList;

import java.util.HashMap;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 27/11/2009
 * Time: 7:05:13
 */
public class FileLoader
{
	private static FileLoader _instance;

	private HashMap<String, File> _classes = new HashMap<String, File>();
	private FastList<java.io.File> _fileNames = new FastList<java.io.File>();

	public static FileLoader getInstance()
	{
		if (_instance == null)
		{
			_instance = new FileLoader();
		}
		return _instance;
	}

	FileLoader()
	{
		load();
	}

	public int size()
	{
		return _classes.size();
	}

	public void load()
	{
		_classes.clear();
		_fileNames.clear();

		getFiles(new java.io.File("./files"), "");

		if (Compiler.getInstance().compile(_fileNames))
		{
			Compiler.MemoryClassLoader classLoader = Compiler.getInstance().getClassLoader();

			for (String name : classLoader.byteCodes.keySet())
			{
				if (name.contains("$"))
				{
					continue; // пропускаем вложенные классы
				}
				try
				{
					Class c = classLoader.loadClass(name);
					File s = new File(c);
					_classes.put(name, s);
				}
				catch (ClassNotFoundException e)
				{
					Jn.getInstance().warn("Can't load file " + e, e);
				}
			}

			Compiler.getInstance().setClassLoader(null);
		}
	}

	private void getFiles(java.io.File f, String dir)
	{
		for (java.io.File z : f.listFiles())
		{
			if (z.isDirectory())
			{
				if (z.isHidden() || z.getName().equals(".svn"))
				{
					continue;
				}
				String olddir = dir;
				dir = dir + z.getName() + "/";
				getFiles(z, dir);
				dir = olddir;
			}
			else
			{
				if (z.isHidden() || !z.getName().contains(".java"))
				{
					continue;
				}

				_fileNames.add(z);
			}
		}
	}

	public File getFile(String file) throws ClassNotFoundException
	{
		if (_classes.get(file) == null)
		{
			throw new ClassNotFoundException("Class not find: " + file);
		}

		return _classes.get(file);
	}
}

