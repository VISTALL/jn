package com.jds.jn.helpers;

import com.jds.jn.rconfig.RConfig;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21.08.2009
 * Time: 3:01:37
 */
public class Shutdown extends Thread
{
	@Override
	public void run()
	{
		//RConfig.getInstance().shutdown();
	}
}
