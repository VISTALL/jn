package com.jds.jn.util;

import java.util.concurrent.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 9:34:38
 */
public class ThreadPoolManager
{
	private static ThreadPoolManager _instance;

	private ScheduledThreadPoolExecutor _pool;

	public static ThreadPoolManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new ThreadPoolManager();
		}
		return _instance;
	}

	ThreadPoolManager()
	{
		_pool = new ScheduledThreadPoolExecutor(4, new PriorityThreadFactory("ThreadPoolManager", Thread.NORM_PRIORITY));
	}

	public void execute(Runnable r)
	{
		//new Thread(r).start(); //TODO ЧЕ ЗА БРЕД
		_pool.execute(r);
	}

	public ScheduledFuture scheduleAtFixedRate(Runnable r, long initial, long delay)
	{
		try
		{
			if (delay < 0)
			{
				delay = 0;
			}
			if (initial < 0)
			{
				initial = 0;
			}
			return _pool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS);
		}
		catch (RejectedExecutionException e)
		{
			return null; /* shutdown, ignore */
		}
	}

	public void shutdown()
	{
		_pool.shutdown();
	}
}
