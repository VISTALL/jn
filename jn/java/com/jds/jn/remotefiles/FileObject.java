package com.jds.jn.remotefiles;

import com.jds.jn.Jn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 27/11/2009
 * Time: 7:04:50
 */
public class FileObject
{
	private Class _class;
	private Object _instance;

	public FileObject(Class c, Object o)
	{
		_class = c;
		_instance = o;
	}

	@SuppressWarnings("unchecked")
	public void setProperty(String s, Object o)
	{
		try
		{
			if (_class != null && _instance != null)
			{
				if (_class.getField(s) != null && Modifier.isPublic(_class.getField(s).getModifiers()))
				{
					_class.getField(s).set(_instance, o);
				}
				else
				{
					Jn.getInstance().warn("Class " + getName() + " field " + s + " not found or private!");
				}
			}
		}
		catch (NoSuchFieldException e)
		{
			Jn.getInstance().warn("Class " + getName() + " field " + s + " not found or private!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Object getProperty(String s)
	{
		Object o = null;
		try
		{
			if (_class != null && _instance != null)
			{
				if (_class.getField(s) != null && Modifier.isPublic(_class.getField(s).getModifiers()))
				{
					o = _class.getField(s).get(_instance);
				}
				else
				{
					Jn.getInstance().warn("Class " + getName() + " field " + s + " not found or private!");
				}
			}
		}
		catch (NoSuchFieldException e)
		{
			Jn.getInstance().warn("Class " + getName() + " field " + s + " not found or private!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	public Object invokeMethod(String s)
	{
		Object o = null;
		try
		{
			if (_class != null && _instance != null)
			{
				if (Modifier.isStatic(_class.getMethod(s).getModifiers()))
				{
					o = _class.getMethod(s).invoke(null);
				}
				else
				{
					o = _class.getMethod(s).invoke(_instance);
				}
			}
		}
		catch (NoSuchMethodException e)
		{
		}
		catch (InvocationTargetException f)
		{
			Jn.getInstance().warn("Class " + getName() + " method " + s + " return a error!");
			f.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return o;
	}

	@SuppressWarnings({
			"unchecked",
			"null"
	})
	public Object invokeMethod(String s, Object[] args)
	{
		Object o = null;
		Class[] types = new Class[args.length];
		try
		{
			if (_class != null && _instance != null)
			{
				int n = 0;
				boolean onlynull = false;
				for (Object element : args)
				{
					if (element == null)
					{
						n++;
					}
				}

				if (n == args.length)
				{
					onlynull = true;
				}

				for (int i = 0; i < args.length; i++)
				{
					types[i] = args[i] != null ? args[i].getClass() : null;
				}
				Method meth = null;
				if (!onlynull && getMethod(s, types) == null)
				{
					boolean[] accept = new boolean[args.length];
					for (Method m : _class.getMethods())
					{
						if (m.getName().equals(s) && m.getParameterTypes().length == args.length)
						{
							for (int i = 0; i < m.getParameterTypes().length; i++)
							{
								Class p = m.getParameterTypes()[i];
								if (args[i] != null && args[i].getClass() == p)
								{
									accept[i] = true;
								}
								else if (args[i] != null)
								{
									Class argc = args[i].getClass();
									while (true)
									{
										if (argc != null && argc.getSuperclass() == p)
										{
											accept[i] = true;
											break;
										}
										else if (argc == null)
										{
											break;
										}
										else
										{
											argc = argc.getSuperclass();
										}
									}
								}
								if (!accept[i])
								{
									accept[i] = args[i] == null;
								}
							}
						}

						boolean result = true;
						for (boolean a : accept)
						{
							if (!a)
							{
								result = false;
								break;
							}
						}

						if (result)
						{
							meth = m;
							break;
						}
					}
				}
				else
				{
					for (int i = 0; i < args.length; i++)
					{
						types[i] = args[i] != null ? args[i].getClass() : Object.class;
					}
					meth = getMethod(s, types);
				}

				if (meth == null && onlynull)
				{
					for (Method m : _class.getMethods())
					{
						if (m.getName().equals(s) && m.getParameterTypes().length == args.length)
						{
							meth = m;
							break;
						}
					}
				}
				if (meth == null && onlynull)
				{
					return invokeMethod(s);
				}

				if (meth == null)
				{
					Jn.getInstance().warn("Class " + getName() + " method " + s + " not found!");
					return null;
				}

				if (Modifier.isStatic(meth.getModifiers()))
				{
					o = meth.invoke(null, args);
				}
				else
				{
					o = meth.invoke(_instance, args);
				}
			}
		}
		catch (InvocationTargetException f)
		{
			Jn.getInstance().warn("Class " + getName() + " method " + s + " return a error!");
			f.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	private Method getMethod(String s, Class[] types)
	{
		try
		{
			return _class.getMethod(s, types);
		}
		catch (NoSuchMethodException e)
		{
			return null;
		}
	}

	public String getName()
	{
		return _class.getName();
	}

	public Class getRawClass()
	{
		return _class;
	}
}
