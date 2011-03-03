package com.jds.jn.logs.readers;

import java.io.IOException;

import com.jds.jn.util.Util;

/**
 * @author VISTALL
 * @date 18:40/03.03.2011
 */
public class CapReader extends AbstractReader
{

	@Override
	public boolean parseHeader() throws IOException
	{
		readD(); // header
		readB(53);


		return true;
	}

	@Override
	public void parsePackets() throws IOException
	{
		for(int i = 0; i < 3; i++)
		{
			byte[] d = readB(23);
			int size = readD();
			int size2 = readD();

			byte[] data = readB(size);


			System.out.println("size " + size);
			System.out.println(Util.printData(d));
			System.out.println("-------------------------------------");
			//System.out.println(Util.printData(data));
		}
	}

	@Override
	public String getFileExtension()
	{
		return "cap";
	}

	@Override
	public String getReaderInfo()
	{
		return "MS Network Monitor log file";
	}
}
