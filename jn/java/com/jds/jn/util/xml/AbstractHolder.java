package com.jds.jn.util.xml;

import org.apache.log4j.Logger;

/**
 * Author: VISTALL
 * Date:  18:34/30.11.2010
 */
public abstract class AbstractHolder
{
	protected final Logger _log = Logger.getLogger(getClass());

	public void log()
	{
		info(String.format("loaded %d%s(s) count.", size(), formatOut(getClass().getSimpleName().replace("Holder", "")).toLowerCase()));
	}

	protected void process()
	{

	}

	public abstract int size();

	public abstract void clear();

	private static String formatOut(String st)
	{
		char[] chars = st.toCharArray();
		StringBuffer buf = new StringBuffer(chars.length);

		for (char ch : chars)
		{
			if (Character.isUpperCase(ch))
				buf.append(" ");

			buf.append(Character.toLowerCase(ch));
		}

		return buf.toString();
	}

	public void error(String st, Exception e)
	{
		_log.error(st, e);
	}

	public void error(String st)
	{
		_log.error(st);
	}

	public void warn(String st, Exception e)
	{
		_log.warn(st, e);
	}

	public void warn(String st)
	{
		_log.warn(st);
	}

	public void info(String st, Exception e)
	{
		_log.info(st, e);
	}

	public void info(String st)
	{
		_log.info(st);
	}
}
