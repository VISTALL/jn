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

	private ScheduledThreadPoolExecutor _shedulePool;
	private ThreadPoolExecutor _threadPool;

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
		_threadPool = new ThreadPoolExecutor(2, 4, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("Thread Pool", Thread.MIN_PRIORITY));
		_shedulePool = new ScheduledThreadPoolExecutor(3, new PriorityThreadFactory("Shedule", Thread.MIN_PRIORITY));
	}

	public void execute(Runnable r)
	{
		_threadPool.execute(r);
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
			return _shedulePool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS);
		}
		catch (RejectedExecutionException e)
		{
			return null; /* shutdown, ignore */
		}
	}

}
