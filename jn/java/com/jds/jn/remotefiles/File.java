package com.jds.jn.remotefiles;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 27/11/2009
 * Time: 7:04:35
 */
public class File
{
	private Class _class;

	public File(Class c)
	{
		_class = c;
	}

	public FileObject newInstance()
	{
		FileObject o = null;
		Object instance = null;
		try
		{
			instance = _class.newInstance();
		}
		catch (InstantiationException e)
		{
			//_log.warn("Class " + getName() + " hasn't default constructor.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		o = new FileObject(_class, instance);

		return o;
	}

	@SuppressWarnings("unchecked")
	public FileObject newInstance(Object[] args)
	{
		FileObject o = null;
		Object instance = null;
		try
		{
			Class[] types = new Class[args.length];
			boolean arg = false;
			for (int i = 0; i < args.length; i++)
			{
				if (args[i] != null)
				{
					types[i] = args[i].getClass();
					arg = true;
				}
			}
			if (!arg)
			{
				return newInstance();
			}
			instance = _class.getConstructor(types).newInstance(args);
		}
		catch (InstantiationException e)
		{
			//_log.warn("Class " + getName() + " hasn't constructor with such arguments.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		o = new FileObject(_class, instance);

		return o;
	}

	public Class getRawClass()
	{
		return _class;
	}

	public String getName()
	{
		return _class.getName();
	}
}

