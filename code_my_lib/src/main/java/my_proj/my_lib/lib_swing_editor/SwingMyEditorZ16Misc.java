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

import java.awt.Component;

//------------------  Import statements  ------------------

import java.awt.Container;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//---------------------  CLASS: SwingMyEditorZ11Misc  -------------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class
 *
 * @author James Everitt
 */
final class SwingMyEditorZ16Misc {

//private static final boolean DO_TRACE = true;
  

//------------------------------------------------------------------------
//------------------------  Static Methods:  -----------------------------
//------------------------------------------------------------------------

  
//------------------  Method  ------------------
/**
 * This method returns an array list of text positions where lines start.
 *
 * @param text  Single String of text in JTextArea
 *
 * @return  Returns ArrayList&lt;Integer&gt; of text positions
 */
  static final ArrayList<Integer> myGetLineStartIndexes( String text )
  {
    ArrayList<Integer> retVal = new ArrayList<>();
    retVal.add(Integer.valueOf(0));
    for ( int i1=0 ; i1<text.length()-1 ; i1++ ) {
      if ( text.charAt(i1) == '\n' ) retVal.add(Integer.valueOf(i1+1));
    }
    return retVal;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param file  File to read
 * @param lst  ?
 *
 * @return  ?
 * 
 * @throws Exception  Throws Java standard exception
 */
  static final ArrayList<String> myGetFileContentsAsArrayList( File file, ArrayList<String> lst ) throws Exception
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
 * @param lst ArrayList of file contents
 * @param indent  Initial indent of returned String
 *
 * @return  Returns String
 *
 * @throws Exception  Throws Java standard exception 
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
/**
 * This static method ?
 *
 * @param imageFileName  ?
 *
 * @return  ?
 */
  public static final Image myGetImage ( String imageFileName )
  {
    Image img = null;
//
    if ( imageFileName != null ) {
// If in file_in_jar
      ClassLoader cldr = SwingMyEditorZ16Misc.class.getClassLoader();
// Note: The name of a resource is a '/'-separated path name that identifies the resource.
      URL imageURL = cldr.getResource( imageFileName );
      if (imageURL != null ) img = Toolkit.getDefaultToolkit().getImage(imageURL);
    }
//
    return img;
  } //End: Method


//------------------  Method  ------------------
// Moves <dir>/<file>.<ext> to <dir>/<file>.<ext>_old_<indx>
/**
 * This static method moves a file named <some_file> to <some_file>_old_<index>
 *
 * @param fileToMove  ?
 * @param numbOfFilesLimit  ?
 *
 * @throws Exception  Throws Java standard exception
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
  static final boolean myCreateFile ( File outFile, String inData, boolean deleteOld ) throws Exception
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
 * This method ?
 * 
 * @param cont  ?
 * @param title  ?
 * @param msg  ?
 */
  static final void myShowDialog( Container cont, String title, String msg )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": cont= " + cont);
    while ( cont.getParent() != null ) cont = cont.getParent();
    JOptionPane.showMessageDialog(
      cont,             // Component  parentComponent
      msg,              // Object message,
      title,            // String title,
      JOptionPane.ERROR_MESSAGE // int messageType
      );
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param txt  ?
 * @param info  ?
 * 
 * @return  ?
 */
  static final String myLineInfoToString( String txt, ArrayList<int[]> info )
  {
    StringBuffer sb = new StringBuffer();
    String indent = "  ";
//
    for ( int[] lnI : info ) {
      sb.append(indent + "ln[" + lnI[0] + "] = " );
      sb.append( "<" + txt.substring(lnI[1], lnI[2]) + ">" );
      sb.append( "   : idxs= " + lnI[0] + " , " + lnI[1] + " , " + lnI[2] );
      sb.append("\n");
    }
//  
    return sb.toString();
  } //End: method


//------------------  Method  ------------------
/**
 * This method finds the focus owner
 * 
 * @return  Returns focus owner
 */
  static final Component myGetFocusOwner() { return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner(); }


} //End: Class SwingMyEditorZ11Misc
