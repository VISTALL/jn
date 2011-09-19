package com.jds.jn;

import java.text.SimpleDateFormat;

/**
 * Author: VISTALL
 * Date:  10:55/21.12.2010
 */
public class JnTest
{
	public static void main(String... arg) throws Exception
	{
		System.out.println(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(1316364017L * 1000L));
	}
}
