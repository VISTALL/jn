package com.jds.jn.logs.readers;

import java.io.File;
import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  10:32:04/20.06.2010
 */
public class JNL2Reader extends AbstractReader
{
	protected JNL2Reader(File file) throws IOException
	{
		super(file);
	}

	@Override
	public boolean parseHeader() throws IOException
	{
		return false;
	}

	@Override
	public boolean parsePackets() throws IOException
	{
		return false;
	}
}
