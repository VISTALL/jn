package com.jds.jn.classes.compiler;

import org.apache.log4j.Logger;

import javax.tools.*;

import java.io.File;
import java.util.List;

public class Compiler
{
	private static Compiler _instance;

	public static Compiler getInstance()
	{
		if (_instance == null)
		{
			_instance = new Compiler();
		}
		return _instance;
	}

	private final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
	private final DiagnosticCollector<JavaFileObject> diacol = new DiagnosticCollector<JavaFileObject>();
	private final StandardJavaFileManager standartFileManager = javac.getStandardFileManager(diacol, null, null);
	private static final Logger _log = Logger.getLogger(Compiler.class);

	private MemoryClassLoader classLoader = null;

	public boolean compile(java.io.File[] files)
	{
		setClassLoader(new MemoryClassLoader());
		JavaMemoryFileManager fileManager = new JavaMemoryFileManager(standartFileManager, getClassLoader());

		if (javac.getTask(null, fileManager, diacol, null, null, standartFileManager.getJavaFileObjects(files)).call())
		{
			return true;
		}

		for (Diagnostic<? extends JavaFileObject> dia : diacol.getDiagnostics())
		{
			_log.info("Compiler " + dia);
		}
		return false;
	}

	public boolean compile(List<File> files)
	{
		return compile(files.toArray(new File[files.size()]));
	}

	public boolean compile(File file)
	{
		return compile(new File[]{file});
	}

	public MemoryClassLoader getClassLoader()
	{
		return classLoader;
	}

	public void setClassLoader(MemoryClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}
}