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

//#define EXE_MAKER_DEBUG

#define EXE_MAKER_CURRENT_DIR "."

#define EXE_MAKER_MAIN_CLASS "com.jds.jn.Jn"

#define EXE_MAKER_SEARCH_JRE_ORDER "bundled;selfvar;registry;jdkpath;jrepath;javahome;exepath"

#define EXE_MAKER_VARIABLE_NAME "JN_JRE"

#define EXE_MAKER_JAVA_MIN_VERSION "1.6"

#define EXE_MAKER_JAVA_MAX_VERSION "1.7"

#define EXE_MAKER_ARGUMENTS ""

#define EXE_MAKER_SINGLE_INSTANCE true

//#define EXE_MAKER_INCLUDE_TOOLS_JAR //TODO [VISTALL] remake it

#define EXE_MAKER_ERROR_TEXT "Not find valid JDK. Please set JN_JRE variable, or download it.(Ok - go to site, Cancel - close)"

#define EXE_MAKER_DOWNLOAD_URL "http://java.com"

#define EXE_MAKER_ERROR_CAPTION "JN Startup Error"

#ifdef WIN32
#define EXE_MAKER_VM_PROPERTIES "jn.exe.vmoptions"
#elif WIN64
#define EXE_MAKER_VM_PROPERTIES "jn64.exe.vmoptions"
#endif

//#define EXE_MAKER_BUNDLED_JRE "../jre/"

// jvm type
#define EXE_MAKER_SERVER_JVM "jre\\bin\\server\\jvm.dll"

#define EXE_MAKER_CLIENT_JVM "jre\\bin\\client\\jvm.dll"

#define EXE_MAKER_SERVER_DEFAULT_JVM true

#define EXE_MAKER_INCORRECT_SERVER_OPTION "Incorrect '-server' option"

#define EXE_MAKER_INCORRECT_CLIENT_OPTION "Incorrect '-client' option"