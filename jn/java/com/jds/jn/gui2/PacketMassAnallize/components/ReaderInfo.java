package com.jds.jn.gui2.PacketMassAnallize.components;

import com.jds.jn.logs.readers.AbstractReader;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:51:08/03.09.2010
 */
public class ReaderInfo
{
	private AbstractReader _reader;

	public ReaderInfo(AbstractReader reader)
	{
		_reader = reader;
	}

	public AbstractReader getReader()
	{
		return _reader;
	}

	@Override
	public String toString()
	{
		return _reader.getReaderInfo() + "(." + _reader.getFileExtension() + ")";
	}
}
