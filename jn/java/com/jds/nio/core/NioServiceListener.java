package com.jds.nio.core;

import java.util.EventListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 02.09.2009
 * Time: 18:11:46
 */
public interface NioServiceListener extends EventListener
{
	/**
	 * вызывается когда сервис стартовал, забинден хост либо приконектилось к хосту
	 *
	 * @param service
	 */
	public void serviceActivated(NioService service);

	/**
	 * аналогично выше ток наоборот
	 *
	 * @param service
	 */
	public void serviceDeactivated(NioService service);
}
