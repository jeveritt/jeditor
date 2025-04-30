// MyMisc2.java
/*
 * Copyright (C) 2010 James Everitt
 *
 * This file is part of Open Schematic Capture.
 * 
 * Open Schematic Capture is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

// Package statement
package my_proj.my_lib.lib;

//------------------  Import statements  ------------

import java.util.ArrayList;
import java.io.File;


//  ----------------  CLASS:  ----------------
/**
 * This class has miscellaneous useful static methods.
 *
 * @author James Everitt
 */
public class MyMisc2 {
  
//private static final boolean DO_TRACE = true;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private MyMisc2 ( ) {}


//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------ Method ------------------
/**
 * This method was copied from MyMisc2.
 * 
 * @param indent  Output are prepended with indent String.
 *
 * @return  Returns Java and system info
 */
  public static final String myGetJavaAndSystemInfo ( String indent )
  {
    StringBuffer sb = new StringBuffer();
//
    sb.append(indent + "os name/version/arch    = " + System.getProperty("os.name") + " / " + System.getProperty("os.version") + " / " + System.getProperty("os.arch") + "\n");
    sb.append(indent + "java virtual machine    = " + System.getProperty("java.home") + "\n" );
    sb.append(indent + "jvm vendor/version      = " + System.getProperty("java.vm.vendor") + " / " + System.getProperty("java.vm.version") + "\n" );
    try {
      ArrayList<File> jarFiles = MyMisc2.myGetClassPathsWithoutJavaLibJarsArrayList();
      sb.append(indent + "java class path(s)      = " + jarFiles.get(0).getCanonicalPath() + "\n" );
      for ( int i1=1 ; i1<jarFiles.size() ; i1++) sb.append(indent + "                          " + jarFiles.get(i1).getCanonicalPath() + "\n");
    }
    catch (Exception e) {}
    sb.append(indent + "java default temp dir   = " + MyMisc.myGetTmpDirectory() + "\n" );
    sb.append(indent + "user                    = " + System.getProperty("user.name") + "\n" );
    sb.append(indent + "working dir             = " + System.getProperty("user.dir") + "\n" );
    sb.append(indent + "home dir                = " + System.getProperty("user.home") + "\n" );
//
    Runtime rt = Runtime.getRuntime();
    sb.append(indent + "max memory              = " + ( rt.maxMemory() / 1000000) + " meg\n" );
///
    return sb.toString();
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method prints a formated word string.
 *
 * @param indent  ?
 * @param programName  ?
 * @param programArgs  ?
 *
 * @return output String
 *
 */
/*
  public static String myGetProgramRunStatement ( String indent, String programName, String[] programArgs ) {
//
    StringBuffer sb = new StringBuffer();
//
      String indent2 = indent;
      MyMisc.MY_SYSTEM_TYPE sysType = MyMisc.myGetSystemType();
//
      sb.append(indent2 + "Running:\n");
      sb.append(indent2 + "  " + MyStringOps.myQuoteIfHasSpaces(System.getProperty("java.home")) + File.separator + "bin" + File.separator + "java");
      indent2 = sysType == MY_SYSTEM_TYPE.WINDOWS ?  (" ^\n" + indent2) : (" \\\n" + indent2);
      sb.append(indent2 + "    -cp " + MyStringOps.myQuoteIfHasSpaces(MyMisc2.myGetClassPathsWithoutJavaLibJars()));
      sb.append(indent2 + "      " + MyStringOps.myQuoteIfHasSpaces(programName));
//
      if (programArgs != null && programArgs.length > 0 ) {
        indent2 = indent2 +  "        ";
        for (int i1=0 ; i1<programArgs.length ; i1++) {
          if ( programArgs[i1] == null || programArgs[i1].length() < 1 ) continue;
          else if ( i1==0 || programArgs[i1].startsWith("-") ) {
            sb.append(indent2);
            indent2 = indent2 + "  ";
          }
          else sb.append(" ");
//
          sb.append(programArgs[i1]);
        } //End: for ()
      } //End: if ()
//
    return sb.toString();
  } //End: Method
*/


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @return  ?
 *
 */
  public static final ArrayList<File> myGetClassPathsWithoutJavaLibJarsArrayList()
  {
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName());
    ArrayList<File> retVal = new ArrayList<>();
    String classPaths = System.getProperty("java.class.path");
    String workingDir = System.getProperty("user.dir");
//
    boolean isWindows = classPaths.contains(";");
    String sep = isWindows ? ";" : ":";
    String[] frags = classPaths.split(sep);    
//    ArrayList<String> frags = MyStrTok.myTokenizeToArrayList(classPaths, sep, null, null, null, MyStrTok.MY_SEPARATE_NO_OPTIONS);
    String jvmLib = isWindows ? "\\lib\\jvm\\" : "/lib/jvm/";
//
    for ( String frag : frags ) {
      if ( !frag.contains(jvmLib) ) {
        File tmp = new File(frag);
        if ( !tmp.exists() ) tmp = new File(workingDir + File.separator + frag);
        if ( tmp.exists() ) retVal.add(tmp);
//if(DO_TRACE)System.out.println(" " + MyTrace.myGetMethodName() + ": adding frag= " + frag);
//        {
//          if ( sb.length() > 0 ) sb.append(sep);
//          sb.append(tmp.getAbsolutePath());
//        }
      } //End: if ()
    } //End: for ()
//
    return retVal;
  } //End: Method


} //End: class MyMisc2

