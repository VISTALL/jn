/*
  ExeMaker: .exe wrapper for Java
  Copyright (C) 2010-2012 napile.org

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Library General Public
  License as published by the Free Software Foundation; either
  version 2 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Library General Public License for more details.
  
  You should have received a copy of the GNU Library General Public
  License along with this library; if not, write to the Free
  Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
#include "JVMOptions.h"
#include "FileUtils.h"
#include "StringUtils.h"
#include "ClasspathSingle.h"
#include "ClasspathDirectory.h"
#include "ClasspathEntry.h"
#include "SunJVMLauncher.h"

JVMOptions::JVMOptions()
{
	//	
}

void JVMOptions::initClasspath(SunJVMLauncher launcher, std::string javaHome)
{
	_classpath.push_back(new ClasspathSingle(std::string("../libs/bridj-0.5-SNAPSHOT-shaded.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/commons-io-2.0.1.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/dom4j-2.0.0-alpha-2.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/forms_rt.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/javolution-5.5.1.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/jn.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/jn-resources.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/jpcap.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/jribbon.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/log4j-1.2.16.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/org.napile.primitive.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/swingx.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/tools.jar")));
	_classpath.push_back(new ClasspathSingle(std::string("../libs/trident.jar")));

#ifdef EXE_MAKER_INCLUDE_TOOLS_JAR
	std::vector<string> jars = FileUtils::recursiveSearch(javaHome, "tools.jar", true);
	if(jars.size() > 0)
		_classpath.push_back(new ClasspathSingle(jars[0]));
	else
	{
		DEBUG("Displaying error message to user... Type: EXE_MAKER_INCLUDE_TOOLS_JAR");
		if (MessageBox(NULL, EXE_MAKER_ERROR_TEXT, EXE_MAKER_ERROR_CAPTION, MB_OKCANCEL|MB_ICONERROR|MB_APPLMODAL) == IDOK)
		{
			ShellExecute(NULL, "open", EXE_MAKER_DOWNLOAD_URL, NULL, "", 0);
			ExitProcess(-1);
		}
	}
#endif

	//addOption(std::string("-Xbootclasspath/a:../lib/boot.jar"));
	//addOption(std::string("-Didea.paths.selector=IdeaIC"));


	DEBUG("------------------------------------------------------------------");
	DEBUG("Calling initClasspath " + StringUtils::toString(_classpath.size()));

	std::vector<std::string> *list = new std::vector<std::string>;

	for(int i = 0; i < _classpath.size(); i++)
	{
		ClasspathEntry *entry = _classpath[i];

		entry->addToClassPath(list);
	}

	DEBUG("Path size " + StringUtils::toString(list->size()));
	DEBUG("------------------------------------------------------------------");

	if(list->size() > 0)
		addOption("-Djava.class.path=" + StringUtils::join(*list, ";"));
}

void JVMOptions::readFromFile()
{
	std::string fileName = std::string(EXE_MAKER_VM_PROPERTIES);
	if(FileUtils::fileExists(std::string(fileName)))
	{
		DEBUG("File of VM options find");
		std::string fileData = FileUtils::readFile(fileName);

		vector<std::string> params = StringUtils::split(fileData, " \t\n\r", "\"\'");
		for(int i = 0; i < params.size(); i++)
			addOption(params[i]);
	}
	else
		DEBUG("File of VM options not find");
}

void JVMOptions::addOption(std::string val)
{
	DEBUG("JVMOptions::addOption(" + val + ")");

	_options.push_back(val);	

	DEBUG("Options size: " + StringUtils::toString(_options.size()));
}

JavaVMOption* JVMOptions::toJavaVMOptions()
{
	DEBUG("Return options: " + StringUtils::toString(_options.size()));
	JavaVMOption* options = new JavaVMOption[_options.size()];
	for(int i = 0; i < _options.size(); i++)
	{
		JavaVMOption option;
		option.optionString = (char*)_options[i].c_str();

		DEBUG("> " + _options[i]);
		options[i] = option;
	}

	return options;
}

bool JVMOptions::removeOption(char* val)
{
	 for (vector<string>::const_iterator i = _options.begin(); i != _options.end(); i++)
	{
		DEBUG("removeOption " + *i + " == " + std::string(val));
		if(*i == val)
		{
			
			_options.erase(i);
			return true;
		}
	}
	return false;
}

int JVMOptions::getSize()
{
	return _options.size();
}

JVMOptions& JVMOptions::getInstance() 
{ 
	static JVMOptions options;
	return options; 
} 