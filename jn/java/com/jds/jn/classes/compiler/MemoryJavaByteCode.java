package com.jds.jn.classes.compiler;

import javax.tools.SimpleJavaFileObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

public class MemoryJavaByteCode extends SimpleJavaFileObject
{
	private ByteArrayOutputStream oStream;
	private final String className;

	public MemoryJavaByteCode(String name)
	{
		super(URI.create("byte:///" + name), Kind.CLASS);
		className = name;
	}

	@Override
	public OutputStream openOutputStream()
	{
		oStream = new ByteArrayOutputStream();
		return oStream;
	}

	public byte[] getBytes()
	{
		return oStream.toByteArray();
	}

	@Override
	public String getName()
	{
		return className;
	}
}
