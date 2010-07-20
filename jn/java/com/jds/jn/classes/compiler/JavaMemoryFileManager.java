package com.jds.jn.classes.compiler;

import javax.tools.*;

import java.io.IOException;

public class JavaMemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>
{
	private final MemoryClassLoader classLoader;

	public JavaMemoryFileManager(StandardJavaFileManager fileManager, MemoryClassLoader specClassLoader)
	{
		super(fileManager);
		classLoader = specClassLoader;
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String name, JavaFileObject.Kind kind, FileObject sibling) throws IOException
	{
		MemoryJavaByteCode byteCode = new MemoryJavaByteCode(name);
		classLoader.addClass(byteCode);
		return byteCode;
	}
}