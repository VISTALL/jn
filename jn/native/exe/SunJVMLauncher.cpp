/*
  JSmooth: a VM wrapper toolkit for Windows
  Copyright (C) 2003-2007 Rodrigo Reyes <reyes@charabia.net>

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

#include "SunJVMLauncher.h"
#include "Process.h"

#include "SunJVMDLL.h"
#include "JArgs.h"
#include "JClassProxy.h"
#include "config.h"
#include "JVMOptions.h"

extern "C" {
  static jint JNICALL  myvprintf(FILE *fp, const char *format, va_list args)
  {
        DEBUG("MYPRINTF");
  }
  void JNICALL myexit(jint code)
  {
       DEBUG("EXIT CALLED FROM JVM DLL");        
       exit(code); 
  }
}

std::string SunJVMLauncher::toString() const
{
    return "<" + JavaHome + "><" + RuntimeLibPath + "><" + VmVersion.toString() + ">";
}

static std::string boolString(bool val)
{
	return val ? "true" : "false";
}

bool SunJVMLauncher::run(const string& origin, bool justInstanciate)
{
  DEBUG("Running now " + this->toString() + ", instanciate=" + (justInstanciate?"yes":"no"));

  Version max = EXE_MAKER_JAVA_MAX_VERSION;
  Version min =  EXE_MAKER_JAVA_MIN_VERSION;

  // patch proposed by zregvart: if you're using bundeled JVM, you
  // apriori know the version bundled and we can trust. The version
  // check is therefore unrequired.
      
    if (VmVersion.isValid() == false)
      {
	DEBUG("No version identified for " + toString());
	SunJVMExe exe(this->JavaHome);
	VmVersion = exe.guessVersion();
	DEBUG("Version found: " + VmVersion.toString());
      }

    if (VmVersion.isValid() == false)
      {
	DEBUG("No version found, can't instanciate DLL without it");
	return false;
      }
  
    if (min.isValid() && (VmVersion < min))
      return false;
      
    if (max.isValid() && (max < VmVersion))
      return false;

  DEBUG("Launching " + toString());
    
  //
  // search for the dll if it's not set in the registry, or if the
  // file doesn't exist
  //
  if (this->JavaHome.size() > 0 && this->RuntimeLibPath.size() == 0 || !FileUtils::fileExists(this->RuntimeLibPath))
  {
	  JVMOptions::getInstance().readFromFile();
	  bool serverVm = JVMOptions::getInstance().removeOption("-server");
	  bool clientVm = JVMOptions::getInstance().removeOption("-client");

	  DEBUG("serverVm " + boolString(serverVm));
	  DEBUG("clientVm " + boolString(clientVm));

	  std::string clientDll = FileUtils::concFile(this->JavaHome, EXE_MAKER_CLIENT_JVM);
	  std::string serverDll = FileUtils::concFile(this->JavaHome, EXE_MAKER_SERVER_JVM);

	  if(serverVm)
	  {
		if(!FileUtils::fileExists(serverDll))
		{
			MessageBox(NULL, EXE_MAKER_INCORRECT_SERVER_OPTION, EXE_MAKER_ERROR_CAPTION, MB_OK|MB_APPLMODAL);
			ExitProcess(-1);
			return false;
		}

		this->RuntimeLibPath = serverDll;
	  }
	  else if(clientVm)
	  {
		if(!FileUtils::fileExists(clientDll))
		{
			MessageBox(NULL, EXE_MAKER_INCORRECT_CLIENT_OPTION, EXE_MAKER_ERROR_CAPTION, MB_OK|MB_APPLMODAL);
			ExitProcess(-1);
			return false;
		}

		this->RuntimeLibPath = clientDll;
	  }
	  else
	  {
		  if (FileUtils::fileExists(EXE_MAKER_SERVER_DEFAULT_JVM ? serverDll : clientDll))
			  this->RuntimeLibPath = EXE_MAKER_SERVER_DEFAULT_JVM ? serverDll : clientDll;
		  else if (FileUtils::fileExists(EXE_MAKER_SERVER_DEFAULT_JVM ? clientDll : serverDll))
			  this->RuntimeLibPath = EXE_MAKER_SERVER_DEFAULT_JVM ? clientDll : serverDll;
		  else
			  return false;
	  }

  }

  if (FileUtils::fileExists(this->RuntimeLibPath)) //not need?
  {
	  DEBUG("RuntimeLibPath used: " + this->RuntimeLibPath);      
	  Version v = this->VmVersion;
	  if (!v.isValid())
	  {
		v = min;
		if (!v.isValid())
		v = Version("1.2.0");
		DEBUG("No version, trying with " + v.toString());
	  }

	  m_dllrunner = new SunJVMDLL(this->RuntimeLibPath, v);

	  setupVM(m_dllrunner);
	  
	  JVMOptions::getInstance().initClasspath(*this, this->JavaHome);
	  
	  return m_dllrunner->run(EXE_MAKER_MAIN_CLASS, true);
  }

  return false;
}

bool SunJVMLauncher::runProc(bool useConsole, const string& origin)
{
  std::string classname = EXE_MAKER_MAIN_CLASS;  

  if (VmVersion.isValid() == false)
    {
      DEBUG("No version identified for " + toString());
      SunJVMExe exe(this->JavaHome);
      VmVersion = exe.guessVersion();
      DEBUG("Version found: " + VmVersion.toString());
    }

  if (VmVersion.isValid() == false)
    return false;

      Version max = EXE_MAKER_JAVA_MAX_VERSION;
      Version min = EXE_MAKER_JAVA_MIN_VERSION;
  
      if (min.isValid() && (VmVersion < min))
        return false;
      
      if (max.isValid() && (max < VmVersion))
	return false;
  SunJVMExe exe(this->JavaHome, VmVersion);   
  setupVM(&exe);
  if (exe.run(classname, useConsole))
    {
      m_exitCode = exe.getExitCode();
      return true;
    }
  return false;
}

bool SunJVMLauncher::setupVM(JVMBase* vm)
{
  return true;
}

SunJVMDLL* SunJVMLauncher::getDLL()
{
  return m_dllrunner;
}

bool operator < (const SunJVMLauncher& v1, const SunJVMLauncher& v2)
{
  return v1.VmVersion < v2.VmVersion;
}

int SunJVMLauncher::getExitCode()
{
  return m_exitCode;
}

