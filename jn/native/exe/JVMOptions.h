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
#pragma once

#include "config.h"
#include "common.h"
#include <string>
#include <vector>
#include "jni.h"
#include "ClasspathEntry.h"
#include "SunJVMLauncher.h"

class JVMOptions
{
protected:
	std::vector<std::string> _options;
	std::vector<ClasspathEntry*> _classpath;
private:
	JVMOptions();
	JVMOptions(const JVMOptions& a); 
	//~JVMOptions();
public:
	void readFromFile();

	void initClasspath(SunJVMLauncher launcher, std::string javaHome);

	void addOption(std::string str);

	bool removeOption(char* option);

	int getSize();

	JavaVMOption* toJavaVMOptions();

	static JVMOptions& getInstance(); 
};