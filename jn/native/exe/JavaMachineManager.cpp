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

#include "JavaMachineManager.h"
#include "config.h"
#include <process.h>


JavaMachineManager::JavaMachineManager()
{
  DEBUG("Now searching the JVM installed on the system...");

    m_registryVms = JVMRegistryLookup::lookupJVM();
    m_javahomeVm = JVMEnvVarLookup::lookupJVM("JAVA_HOME");
    m_jrepathVm = JVMEnvVarLookup::lookupJVM("JRE_HOME");
    m_jdkpathVm = JVMEnvVarLookup::lookupJVM("JDK_HOME");
	_selfVarVm = JVMEnvVarLookup::lookupJVM(EXE_MAKER_VARIABLE_NAME);
    m_exitCode = 0;
    m_useConsole = true;

#ifdef EXE_MAKER_BUNDLED_JRE
	m_localVM.JavaHome = FileUtils::concFile(EXE_MAKER_CURRENT_DIR, EXE_MAKER_BUNDLED_JRE);
#endif
}

bool JavaMachineManager::run()
{
  string vmorder = EXE_MAKER_SEARCH_JRE_ORDER;
    
  DEBUG("JSmooth will now try to use the VM in the following order: " + vmorder);
    
  vector<string> jvmorder = StringUtils::split(vmorder, ";,", "");

  Version max = EXE_MAKER_JAVA_MAX_VERSION;
  Version min = EXE_MAKER_JAVA_MIN_VERSION;

  for (vector<string>::const_iterator i = jvmorder.begin(); i != jvmorder.end(); i++)
  {
      DEBUG("------------------------------");

	  if(*i == "selfvar" && _selfVarVm.size() > 0)
	  {
		  DEBUG("- Trying to use self variable");
		  if (internalRun(_selfVarVm[0], "selfvar"))
			  return true;
	  }
	  else if(*i == "bundled")
	  {
		  DEBUG("- Trying to use bundled jre");
		  if (internalRun(m_localVM, "bundled"))
			  return true;
	  }
	  else if (*i == "registry")
	  {
		DEBUG("Trying to use a JVM defined in the registry (" + StringUtils::toString(m_registryVms.size()) + " available)");
		string vms = "VM will be tried in the following order: ";
		for (int i=0; i<m_registryVms.size(); i++)
	    {
	      vms += m_registryVms[i].VmVersion.toString();
	      vms += ";";
	    }
		DEBUG(vms);

		for (int i=0; i<m_registryVms.size(); i++)
        {
	      DEBUG("- Trying registry: " + m_registryVms[i].toString());

	      if (internalRun(m_registryVms[i], "registry") == true)
			return true;

	      DEBUG("Couldn't use this VM, now trying something else");
        }
	  } 
      else if ((*i == "javahome") && (m_javahomeVm.size()>0))
	  {
		  DEBUG("- Trying to use JAVAHOME");
		  if (internalRun(m_javahomeVm[0], "jrehome"))
			  return true;
	  } 
	  else if ((*i == "jrepath") && (m_jrepathVm.size()>0))
	  {
		DEBUG("- Trying to use JRE_HOME");
		if (internalRun(m_jrepathVm[0], "jrehome"))
			return true;
	  } 
	  else if ((*i == "jdkpath") && (m_jdkpathVm.size()>0))
	  {
		DEBUG("- Trying to use JDK_HOME");
		if (internalRun(m_jdkpathVm[0], "jdkpath"))
		  return true;
	  } 
      else if (*i == "exepath")
	  {
		DEBUG("- Trying to use PATH");

		SunJVMLauncher launcher;
		if (launcher.runProc(m_useConsole, "path"))
	    {
	      m_exitCode = m_localVM.getExitCode();
	      return true;
	    }
	}
  }

  DEBUG("Couldn't run any suitable JVM!");
  return false;
}

bool JavaMachineManager::internalRun(SunJVMLauncher& launcher, const string& org)
{
	return launcher.run(org);
}


SunJVMLauncher* JavaMachineManager::runDLLFromRegistry(bool justInstanciate)
{
  string vms = "DLL VM will be tried in the following order: ";
  for (int i=0; i<m_registryVms.size(); i++)
    {
      vms += m_registryVms[i].VmVersion.toString();
      vms += ";";
    }
  DEBUG(vms);

  for (int i=0; i<m_registryVms.size(); i++)
    {
      DEBUG("- Trying registry: " + m_registryVms[i].toString());

      bool res = m_registryVms[i].run("registry", justInstanciate);
      
      if (res)
	return &m_registryVms[i];
    }

  return NULL;
}

void JavaMachineManager::setUseConsole(bool useConsole)
{
  m_useConsole = useConsole;
}


int JavaMachineManager::getExitCode()
{
  return m_exitCode;
}
