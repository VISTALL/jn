package com.jds.jn.classes.compiler;

import java.util.HashMap;
import java.util.Map;

public class MemoryClassLoader extends ClassLoader
{
	public final Map<String, MemoryJavaByteCode> byteCodes = new HashMap<String, MemoryJavaByteCode>();

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		MemoryJavaByteCode byteCode = byteCodes.get(name);
		if (byteCode == null)
		{
			throw new ClassNotFoundException(name);
		}
		byte[] bytecode = byteCode.getBytes();
		return defineClass(name, bytecode, 0, bytecode.length);
	}

	public void addClass(MemoryJavaByteCode code)
	{
		byteCodes.put(code.getName(), code);
	}
}
