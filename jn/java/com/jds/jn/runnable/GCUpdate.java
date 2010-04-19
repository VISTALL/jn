package com.jds.jn.runnable;

import com.jds.jn.Jn;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21.08.2009
 * Time: 3:48:30
 */
public class GCUpdate implements Runnable
{
	@Override
	public void run()
	{
		Jn.getInstance().updateMemoryBar();
	}
}
