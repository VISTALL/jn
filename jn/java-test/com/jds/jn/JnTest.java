package com.jds.jn;

import java.text.SimpleDateFormat;

/**
 * Author: VISTALL
 * Date:  10:55/21.12.2010
 */
public class JnTest
{
	public static void main(String... arg)
	{
		long l = 1341171684 * 1000L;

		System.out.println(new SimpleDateFormat("HH.mm.SS dd.MM.yyyy").format(l));
	}
}
