package com.jds.jn.classes;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

import com.jds.jn.classes.compiler.Compiler;
import com.jds.jn.classes.compiler.MemoryClassLoader;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 27/11/2009
 * Time: 7:05:13
 */
public class CLoader
{
	private static CLoader _instance;

	private Map<String, Class<?>> _classes = new HashMap<String, Class<?>>();
	private static final Logger _log = Logger.getLogger(CLoader.class);

	public static CLoader getInstance()
	{
		if (_instance == null)
		{
			_instance = new CLoader();
		}
		return _instance;
	}

	CLoader()
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

		List<File> fileNames = new ArrayList<File>();

		getFiles(fileNames, new java.io.File("./files"), "");

		if (com.jds.jn.classes.compiler.Compiler.getInstance().compile(fileNames))
		{
			MemoryClassLoader classLoader = Compiler.getInstance().getClassLoader();

			for (String name : classLoader.byteCodes.keySet())
			{
				if (name.contains("$"))
				{
					continue; // пропускаем вложенные классы
				}
				try
				{
					Class c = classLoader.loadClass(name);
					_classes.put(name, c);
				}
				catch (ClassNotFoundException e)
				{
					_log.info("Can't load file " + e, e);
				}
			}

			Compiler.getInstance().setClassLoader(null);
		}
	}

	private void getFiles(List<File> list, File f, String dir)
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
				getFiles(list, z, dir);
				dir = olddir;
			}
			else
			{
				if (z.isHidden() || !z.getName().contains(".java"))
				{
					continue;
				}

				list.add(z);
			}
		}
	}

	public Class<?> forName(String file) throws ClassNotFoundException
	{
		if (_classes.get(file) == null)
		{
			throw new ClassNotFoundException("Class not find: " + file);
		}

		return _classes.get(file);
	}
}

