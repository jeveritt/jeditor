// MyApplicationInterface.java
/*
 * Copyright (C) 2009 James Everitt
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

//------------------  Package statement  ------------------
package my_proj.my_lib.lib;

//------------------  Import statements  ------------------

import my_proj.my_lib.lib_ops.MyStringOps;

//------------------  INTERFACE: MyApplicationInterface  ------------------
/**
 * This interface is the base for an application.
 *
 * @author James Everitt
 */
public interface MyApplicationInterface {
  
//static final boolean DO_TRACE = true;
  
/** ? */
  public static final String ARG_HELP            = "-help";

/**Prevents application from printing license info.<p>Ex: -nolic*/
  public static final String ARG_NO_LIC          = "-nolic";

/**Prevents application from opening its own console.<p>Ex: -noconsole*/
  public static final String ARG_NO_CONSOLE      = "-noconsole";
  
/**Set temporary directory */
  public static final String ARG_TMP_DIRECTORY   = "-tmp_directory";

/**Sets verbose level.<p>Ex: -verbose 5*/
  public static final String ARG_VERBOSE         = "-verbose";

/**Suppresses most output.<p>Ex: -quiet*/
  public static final String ARG_QUIET           = "-quiet";
  
/** ? */
  public static final String ARG_TEST            = "-test";


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This method 
 * 
 * @param inputArgs  ?
 * @param singleArgs  ?
 * @param doubleArgs  ?
 * @param multipleArgs  ?
 * @param requiredArgs  ?
 * @param unallowedArgPairs  ?
 *
 * @return  ?
 */
  public static String myAppCheckInputArgs (
        String[] inputArgs,
        String[] singleArgs,
        String[] doubleArgs,
        String[] multipleArgs,
        String[] requiredArgs,
        String[][] unallowedArgPairs)
  {
/*
if(DO_TRACE)
System.out.println("\n" + MyTrace.myGetMethodName()
    + "\n inputArgs:  " + MyArrayOps.myArrayToString(inputArgs, ',')
    + "\n singleArgs: " + MyArrayOps.myArrayToString(singleArgs, ',')
    + "\n doubleArgs: " + MyArrayOps.myArrayToString(doubleArgs, ',')
    + "\n multipArgs: " + MyArrayOps.myArrayToString(multipleArgs, ',')
    + "\n requirArgs: " + MyArrayOps.myArrayToString(required, ',')
   );
*/
//
    String badArg = null;
//
// Check that input args are valid
//
    if ( inputArgs != null && inputArgs.length > 0 ) {
      String arg, argNext;
      for ( int i1=0 ; i1<inputArgs.length ; i1++ ) {
        arg = inputArgs[i1];
        argNext = ( i1+1 < inputArgs.length ) ? inputArgs[i1+1] : null;
        boolean argNextIsFlag = argNext != null && argNext.startsWith("-") && !MyStringOps.myIsIntegerOrLong(argNext) && !MyStringOps.myIsRealOrScientificNotationNumber(argNext);
        boolean allowsSingle = MyStringOps.myStringArrayContainsString(singleArgs, arg, false );
        boolean allowsDouble = MyStringOps.myStringArrayContainsString(doubleArgs, arg, false );
        boolean allowsMultiple = MyStringOps.myStringArrayContainsString(multipleArgs, arg, false );
//
// Nothing allowed
        if ( !allowsSingle && !allowsDouble && !allowsMultiple ) badArg = "unrecognized arg: " + arg;
// Single param args
        if ( badArg == null && allowsSingle && !allowsDouble && !allowsMultiple && argNext != null && !argNextIsFlag ) badArg = "arg can not have value: " + arg;
// 2 or more param args
        if ( badArg == null && ( allowsDouble || allowsMultiple ) ) {
          if ( !allowsSingle && argNextIsFlag ) badArg = "arg needs value: " + arg;
          else if ( !allowsMultiple && argNext != null && !argNextIsFlag ) i1++;
          else while ( i1+1 < inputArgs.length && !inputArgs[i1+1].startsWith("-") ) i1++;
        }
// break on found bad arg
        if ( badArg != null ) break;
      } //End: for ()
    } //End: if ()
//
// Check for missing required arguments
//
    if ( badArg == null && requiredArgs != null && requiredArgs.length > 0 ) {
      if ( inputArgs == null || inputArgs.length == 0 ) badArg = "missed required arg: " + requiredArgs[0];
      else {
        for ( int i1=0 ; i1<requiredArgs.length ; i1++ ) {
          if ( !MyStringOps.myStringArrayContainsString(inputArgs, requiredArgs[i1], true) ) {
            badArg = "missed required arg: " + requiredArgs[i1];
            break;
          }
        } //End: for ()
      } //End: else
    } //End: if ()
//
// Check for unallowed pairs
//
    if ( badArg == null && unallowedArgPairs != null && unallowedArgPairs.length > 0 ) {
      for ( String[] pair : unallowedArgPairs ) {
        if ( pair.length > 1 &&
             MyStringOps.myStringArrayContainsString(inputArgs, pair[0],true ) &&
             MyStringOps.myStringArrayContainsString(inputArgs, pair[1],true ) ) {
          badArg = "can not have \"" + pair[0] + "\" and \"" + pair[1] + "\"";
          break;
        }
      }
    }
//
    return badArg;
  } //End: Method


//------------------  Method ------------------
/**
 * This static method prints out the test type, input arguments, and the program name.
 * 
 * @param testType  ?
 * @param args  ?
 * @param programName  ?
 *
 */
  static void myAppPrintTestArgs ( int testType, String[] args, String programName )
  {
//
// Print out info
//
    if ( args == null ) System.out.println(programName + ": bad test = " + testType);
    else {
      System.out.println("-----------------------------------------------------------------------------------");
      System.out.println(programName + ": doing test " + testType);
//
      if ( args.length > 0 ) {
        System.out.println("  args:");
        for (int i1=0 ; i1<args.length ; i1++) {
          if ( args[i1] == null || args[i1].length() < 1 ) continue;
          else if ( i1== 0 ) System.out.print("   " + args[i1]);
          else if ( args[i1].startsWith("-") ) System.out.print("\n   " + args[i1]);
          else System.out.print("  " + args[i1]);
        } //End: for ()
        System.out.println();
      } //End: if ()
      System.out.println("-----------------------------------------------------------------------------------");
    }
  } //End: Method


} // End: public class MyApplicationInterface
