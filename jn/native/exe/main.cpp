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

#include <windows.h>
#include <iostream>
#include <vector>

#include "common.h"
#include "config.h"
#include "JVMRegistryLookup.h"
#include "JavaMachineManager.h"
#include "SingleInstanceManager.h"
#include "JVMOptions.h"
#include "ClasspathEntry.h"
#include "ClasspathSingle.h"

DebugConsole *DEBUGCONSOLE = 0;

void lastExit()
{
  delete DEBUGCONSOLE;
  DEBUGCONSOLE = 0;
}

void _debugOutput(const std::string& text)
{
  if (DEBUGCONSOLE != NULL)
    DEBUGCONSOLE->writeline(text);
}

void _debugWaitKey()
{
  if (DEBUGCONSOLE != NULL)
    DEBUGCONSOLE->waitKey();
}

DWORD WINAPI ThreadProc(LPVOID a)
{
	atexit(lastExit);
	SingleInstanceManager instanceman;

#ifdef EXE_MAKER_DEBUG
	bool dodebug = true;
#else
	bool dodebug = false;
#endif

	if (dodebug)
		DEBUGCONSOLE = new DebugConsole("JSmooth Debug");

	//JVMOptions *options = new JVMOptions();

	bool singleinstance = EXE_MAKER_SINGLE_INSTANCE;
	if (singleinstance)
	{
		if (instanceman.alreadyExists())
		{
			instanceman.sendMessageInstanceShow();
			exit(0);
		}
		else
			instanceman.startMasterInstanceServer();
	}

	DEBUG(string("Main class: ") + EXE_MAKER_MAIN_CLASS);

	char curdir[256];
	GetCurrentDirectory(256, curdir);
	DEBUG(string("Currentdir: ") + curdir);

	SetCurrentDirectory(EXE_MAKER_CURRENT_DIR);

	JavaMachineManager man;

	man.setUseConsole(dodebug);

	int retvalue = 0;

	if (man.run() == false)
	{
		DEBUG("Displaying error message to user...");
		if (MessageBox(NULL, EXE_MAKER_ERROR_TEXT, EXE_MAKER_ERROR_CAPTION, MB_OKCANCEL|MB_ICONERROR|MB_APPLMODAL) == IDOK)
			ShellExecute(NULL, "open", EXE_MAKER_DOWNLOAD_URL, NULL, "", 0);
	}
	else
		retvalue = man.getExitCode();

	DEBUG("NORMAL EXIT");
	DEBUGWAITKEY();

	return retvalue;
}

                          
int WINAPI WinMain (HINSTANCE hThisInstance, HINSTANCE hPrevInstance, LPSTR lpszArgument, int nFunsterStil)
{ 
	HANDLE h = CreateThread(0, NULL, ThreadProc, 0, NULL, 0);

	WaitForSingleObject(h, INFINITE);
    return 0;
}


