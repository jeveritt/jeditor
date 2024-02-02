// SwingMyEditorZ11Misc.java
/*
 * Copyright (C) 2022 James Everitt
 *
 * This file is part of a Swing based vi like text editor.
 * 
 * This is free software: you can redistribute it
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
package my_proj.my_lib.lib_swing_editor;

//------------------  Import statements  ------------------

import java.awt.Container;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JInternalFrame;


//--------------------------------------------------------------------
//------------------  CLASS: SwingMyEditorZ11Misc  ------------------
//--------------------------------------------------------------------
/**
 * This class
 *
 * @author James Everitt
 */
final class SwingMyEditorZ11Misc {


//------------------------------------------------------------------------
//------------------------  Static Methods:  -----------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param cont  ?
 *
 * @return  ?
 */
  static final Window myGetParentWindow( Container cont )
  {
    while ( (cont = cont.getParent()) != null && !(cont instanceof Window) ) {}
    return (Window)cont;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param cont  ?
 *
 * @return  ?
 */
  static final Container myGetParentWindowOrInternalFrame( Container cont )
  {
    while ( (cont = cont.getParent()) != null && !(cont instanceof Window) && !(cont instanceof JInternalFrame) ) {}
    return cont;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param file  File to read
 * @param indent  ?
 *
 * @return  ?
 *
 */
  public static final ArrayList<String> myGetFileContentsAsArrayList( File file, ArrayList<String> lst ) throws Exception
  {
    if ( file == null ) throw new Exception("SwingMyEditorZ11Misc.myGetFileContentsAsArrayList" + ": no file");
    if ( !file.isFile() ) throw new Exception("SwingMyEditorZ11Misc.myGetFileContentsAsArrayList" + ": not a file: " + file.getAbsolutePath());
    BufferedReader reader = new BufferedReader( new FileReader(file) );
    if ( lst == null ) lst = new ArrayList<> ();
//
    String line;
    while ( ( line = reader.readLine() ) != null ) lst.add(line);
    reader.close();
//
    return lst;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method gets file content as String.
 *
 * @param list ArrayList of file contents
 * @param indent  Initial indent of returned String
 *
 * @return  Returns String
 *
 * @throws Exception  Java standard exception 
 */
  public static final String myGetFileContentsAsString( ArrayList<String> lst, String indent ) throws Exception
  {
    if ( lst.size() == 0 ) return null;
    else {
      StringBuffer sb = new StringBuffer();
      for ( String str : lst ) {
        if ( indent != null ) sb.append(indent);
        sb.append(str + "\n");
      }
      return sb.toString();
    }
  } //End: Method


//------------------  Method  ------------------
// Moves <dir>/<file>.<ext> to <dir>/<file>.<ext>_old_<indx>
/**
 * This static method moves a file named <some_file> to <some_file>_old_<index>
 *
 * @param fileToMove  ?
 * @param numbOfFilesLimit  ?
 *
 * @throws Exception  Java standard exception
 */
  public static final void myMoveFileToOldSimpler (
      File fileToMove,
      int numbOfFilesLimit
      ) throws Exception
  {
// Check file and dir exist
    if ( fileToMove == null ) throw new Exception("MyFileOps.myMoveFileToOld: file to move is null");
    if ( !fileToMove.isFile() ) throw new Exception("MyFileOps.myMoveFileToOld: not file: " + fileToMove.getAbsolutePath());
// Break up file
    String fileFullName = fileToMove.getAbsolutePath();
    String fileName = fileToMove.getName();
    String dirName = fileFullName.substring(0,fileFullName.length()-fileName.length());
    File parentDir = new File(dirName);
//
    String oldId = "_old_";
// Get oldest and youngest files
    File[] files = parentDir.listFiles();
    String compName = fileFullName + oldId;
    File oldestFile = null;
    int oldestFileIndx = Integer.MAX_VALUE;
    int newestFileIndx = 0;
    for ( File file : files ) {
      if ( file.isFile() && file.getAbsolutePath().startsWith(compName) ) {
        String fileNm = file.getName();
        int indx = fileNm.indexOf(oldId);
        String intStr = fileNm.substring(indx + oldId.length());
        int numb = Integer.parseInt(intStr);
        if ( numb < oldestFileIndx ) { oldestFileIndx = numb; oldestFile = file; }
        if ( numb > newestFileIndx ) { newestFileIndx = numb; }
      }
    }
// Possibly delete youngest
    if ( newestFileIndx - oldestFileIndx >= numbOfFilesLimit-1 ) oldestFile.delete();
// Rename file
    newestFileIndx++;
    String newIndxStr;
    if       ( newestFileIndx >= 1000 ) newIndxStr = String.valueOf(newestFileIndx);
    else if  ( newestFileIndx >= 100 )  newIndxStr = "0" + String.valueOf(newestFileIndx);
    else if  ( newestFileIndx >= 10 )   newIndxStr = "00" + String.valueOf(newestFileIndx);
    else                                newIndxStr = "000" + String.valueOf(newestFileIndx);
    String newName = fileFullName + oldId + newIndxStr;
// Do move
    fileToMove.renameTo(new File(newName));
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param outFile  ?
 * @param inData  ?
 * @param deleteOld  ?
 * 
 * @return  ?
 *
 * @throws Exception  Java standard exception
 */
  public static final boolean myCreateFile ( File outFile, String inData, boolean deleteOld ) throws Exception
  {
    boolean retVal = false;
//
    if ( outFile != null && inData != null ) {
//
      if ( outFile.exists() ) {
        if ( deleteOld ) outFile.delete();
        else throw new Exception ("SwingMyEditorZ0Misc.myCreateFile" + ": output file exists: " + outFile);
      }
//
      try {
        FileWriter os = new FileWriter(outFile);
        os.write(inData);
        os.close();
        retVal = true;
      } //End: try
      catch (Exception e) { retVal = false; System.out.println("*" + "SwingMyEditorZ0Misc.myCreateFile" + ": exc= " + e.getMessage()); }
//
    } //End: if ()
//
    return retVal;
//
  } //End: Method


//------------------  Method  ------------------
/**
 * This method is a simple String parser.
 *
 * @param inStr  Input String to be parsed
 * @param parseCharStr String of parse characters
 * 
 * @return  Returns ArrayList<String> of parsed String including the parse chars
 */
  static final ArrayList<String> myStringParser ( String inStr, String parseCharStr, boolean splitOutIntegers )
  {
    ArrayList<String> parsed = new ArrayList<>();
//
// If the string exists but can't be parsed
    if ( ( inStr != null && inStr.length() <= 2 ) || parseCharStr == null || parseCharStr.length() == 0 )
          parsed.add(inStr);
// If I have enough info to do parsing
    else if ( inStr != null && inStr.length() > 2 && parseCharStr != null && parseCharStr.length() > 0 ) {
// Scan thru input string
      while ( inStr != null && inStr.length() > 0 ) {
// Find next index of parser char
        int indx = -1;
        for ( int i1=0 ; i1<parseCharStr.length() ; i1++ ) {
          indx = inStr.indexOf(parseCharStr.charAt(i1));
          if ( indx >= 0 ) break;
        }
// If no parser found, then add only/last string segment and I'm done
        if ( indx < 0 && inStr != null ) {
          parsed.add(inStr);
          inStr = null;
        }
// Else add new segment and parser and then shorten string
        else {
          parsed.add(inStr.substring(0,indx));
          parsed.add(inStr.substring(indx,indx+1));
          inStr = inStr.substring(indx+1);
        }
      } //End: while
    } //End: if ()
//
// If also split out integers
    if ( splitOutIntegers && parsed.size() > 0 ) {
      ArrayList<String> parsedOld = parsed;
      parsed = new ArrayList<>();
      for ( String str : parsedOld ) {
        ArrayList<String> frags = myStringParseNumbers(str);
        for ( String str2 : frags ) parsed.add(str2);
      }
    } //End: if ()
//
    return parsed;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method is a simple String parser.
 *
 * @param inStr  Input String to be parsed
 * 
 * @return  Returns ArrayList<String> of parsed String including the parse chars
 */
  static final ArrayList<String> myStringParseNumbers ( String inStr )
  {
    ArrayList<String> parsed = new ArrayList<>();
// If only 1 character in string
    if ( inStr != null && inStr.length() == 1 ) parsed.add(inStr);
// Else if more than 1 charactr in string
    else if ( inStr != null && inStr.length() >= 2 ) {
      boolean lastIsInt = inStr.charAt(0) >= '0' && inStr.charAt(0) <= '9';
      int lastTrans = 0;
      for ( int i1=1 ; i1<inStr.length() ; i1++ ) {
        boolean isInt = inStr.charAt(i1) >= '0' && inStr.charAt(i1) <= '9';
        if ( lastIsInt != isInt ) {
          parsed.add(inStr.substring(lastTrans, i1));
          lastTrans = i1;
        }
        else if (i1 == inStr.length()-1 ) parsed.add(inStr.substring(lastTrans));
        lastIsInt = isInt;
      }
    } //End: else if
//
    return parsed;
  } //End: Method


} //End: Class SwingMyEditorZ11Misc
