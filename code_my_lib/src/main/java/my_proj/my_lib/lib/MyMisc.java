// MyMisc.java
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

import java.io.File;


//  ----------------  CLASS:  ----------------
/**
 * This class has miscellaneous useful static methods.
 *
 * @author James Everitt
 */
public class MyMisc {
  
  private static File myTmpDirExternallySet = null;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private MyMisc ( ) {}


//------------------  Method  ------------------
/**
 * This static method sets the temporary directory
 *
 * @param args String[] of input args
 */
  public static final void mySetTmpDirectory ( String[] args )
  {
    String dirName = MyReadOption.myValueAfterKey(args, MyApplicationInterface.ARG_TMP_DIRECTORY);
    if ( dirName != null ) mySetTmpDirectory ( dirName );
  } //End: method


//------------------  Method  ------------------
/**
 * This static method sets the temporary directory
 *
 * @param dirName Name of temporary directory
 */
  public static final void mySetTmpDirectory ( String dirName )
  {
    if ( dirName != null ) {
      myTmpDirExternallySet = new File(dirName);
      if ( !myTmpDirExternallySet.exists() ) myTmpDirExternallySet.mkdirs();
      if ( !myTmpDirExternallySet.isDirectory() ) myTmpDirExternallySet = null;
    }
  } //End: method


//------------------  Method  ------------------
/**
 * This method returns a reasonable OS dependent temporary directory.
 * 
 * @return  Return String naming the temporary directory.
 */
  public static final String myGetTmpDirectory ()
  {
    if ( myTmpDirExternallySet != null && myTmpDirExternallySet.isDirectory() ) return myTmpDirExternallySet.getAbsolutePath();
//    else if ( MyC.MY_HARD_WIRED_TMP_DIR_FILE.isDirectory() ) return MyC.MY_HARD_WIRED_TMP_DIR_FILE.getAbsolutePath();
    else return System.getProperty("java.io.tmpdir");
  } //End: method


} //End: class MyMisc