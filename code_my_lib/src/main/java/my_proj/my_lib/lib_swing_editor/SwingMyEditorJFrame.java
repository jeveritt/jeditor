// SwingMyEditorJFrame.java
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

// Package statement
package my_proj.my_lib.lib_swing_editor;

//------------------  Import statements  ------------------

import java.awt.Image;
import java.io.File;
import javax.swing.JFrame;

import my_proj.my_lib.lib.MyApplicationInterface;
import my_proj.my_lib.lib.MyMisc;
import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_encrypt.call_gpg.MyWriteOrReadGpgFile;
import my_proj.my_lib.lib_swing.SwingMyImages;


//------------------  CLASS: SwingMyEditorJFrame  ------------------
/**
 * This class creates a stand alone text editor by extending JFrame, creating the editor, and then setting the editor as the content pane of the JFrame.
 *
 * @author James Everitt
 */
public final class SwingMyEditorJFrame extends JFrame
{

//private static final boolean DO_TRACE = true;

/** Serial UID */
  private static final long serialVersionUID = -4154327986415311727L;

/** Main editor panel */
  private SwingMyEditorZ04JPanel myEditorPanel = null;

  
//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This constructor method creates the JFrame and then constructs the editor using an array of arguments.
 *
 * @param args  String[] of arguments for the program.
 */
  public SwingMyEditorJFrame ( String[] args )
  {
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.FRAME; 
    try {
// If this is a test, redefine args for appropriate test.
      if ( args.length > 1 && args[0].equals(MyApplicationInterface.ARG_TEST) ) args = myReturnTestArgs ( Integer.valueOf(args[1]), args );
// If doing help, then print help message
      if  ( args.length > 0 && args[0].equals(MyApplicationInterface.ARG_HELP) ) System.out.print( "\n" + SwingMyEditorZ03Handler.myGetHelp("  ") );
// Else run program
      else {
// Set image for icon
        Image img = SwingMyImages.myGetImage("my_proj/my_lib_resources/my_gifs/jedit_icon_26x26.gif");
        if ( img != null ) super.setIconImage(img);
// Check for errors in input arguments
        String error = SwingMyEditorZ03Handler.myAppCheckArgsOk(args);
        if ( error != null ) {
          System.out.print( SwingMyEditorZ03Handler.myGetHelp( "  ") );
          throw new Exception( MyTrace.myGetMethodName() + ": error= " + error);
        }
// Run the program
        this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromArgs ( this, args );
      }
    } //End: try
    catch (Exception e) {
      System.out.println("SwingMyEditorJFrame.myAppRunSub" + ": exc= " + e.getMessage());
    }
  } //End: method


//------------------  Method  ------------------
/**
 * This constructor method can be called directly from within a Java program.
 * 
 * @param title     Title for the JFrame tile bar. If null and a file is specified the file name is used.
 * @param dataFile  Starting file or null.
 * @param ctrl      Int specifying editor options.
 * @param sizeX     Width of the JFrame in pixels.
 * @param sizeY     Height of the JFrame in pixels.
 *
 * @throws Exception  Java standard exception 
 */
  public SwingMyEditorJFrame (
      String     title,
      File       dataFile,
      int        ctrl,
      int        sizeX,
      int        sizeY ) throws Exception
  {
    super();
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.FRAME;
    Image img = SwingMyImages.myGetImage("my_proj/my_lib_resources/my_gifs/jedit_icon_26x26.gif");
    if ( img != null ) super.setIconImage(img);
//
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromFile (
        this,         // Container      cont,
        title,        // String         title,
        dataFile,     // File           startingDataFile,
        false,        // boolean        startingDataFileIsEncrypted,
        ctrl,         // int            ctrl,
        sizeX, sizeY, // int sizeX, int sizeY,
        null,         // JMenu[]        custom,
        null,         // String         password,
        null          // String         workingDirName
    );
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
  } //End: Method


//------------------  Method  ------------------
/**
 * This method provides test arguments for various test runs.
 *
 * @param testType  Int representing type of test to be done.
 * 
 * @return  Returns String[] of args for the selected test
 * 
 * @throws Exception  Java standard exception 
 */
  private static final String[] myReturnTestArgs ( int testType, String[] inputArgs ) throws Exception
  {
    String[] args = null;
//
// Create unencrypted test file
    String tmpFileName = MyMisc.myGetTmpDirectory() + File.separator + "swingMyEditor_tmpFile.txt";
    if ( !new File(tmpFileName).exists() ) {
      System.out.println( MyTrace.myGetMethodName() + ": creating " + tmpFileName );
      SwingMyEditorZ11Misc.myCreateFile(new File(tmpFileName), "0123\n5678\nSee spot run.\nNext is empty\n\n0123\n5678\n", true);
    }
//
// Create encrypted test file

//
// Set test based on test type
//
// Do help
    if ( testType == 0 ) args = new String[] { "-help", "-nolic", "-noconsole" };
//
// Just view data
     else if ( testType == 1 ) {
       System.out.println( MyTrace.myGetMethodName() + ": Open empty editor" );
       args = new String[] {
//           SwingMyEditorArgs.ARG_FILE, tmpFileName,
           SwingMyEditorArgs.ARG_NO_MENU,
           SwingMyEditorArgs.ARG_SIZE_X, "800",
           SwingMyEditorArgs.ARG_SIZE_Y, "600",
           SwingMyEditorArgs.ARG_WORKING_DIR, MyMisc.myGetTmpDirectory(),
           "-nolic",
           "-noconsole"
       };
     }
//
// Test un-encrypted file
    else if ( testType == 2 ) {
      System.out.println( MyTrace.myGetMethodName() + ": Open and possibly edit and save "  + tmpFileName );
      args = new String[] {
          SwingMyEditorArgs.ARG_FILE, tmpFileName,
          SwingMyEditorArgs.ARG_ALLOW_READ_WRITE,
          SwingMyEditorArgs.ARG_SIZE_X, "800",
          SwingMyEditorArgs.ARG_SIZE_Y, "600",
          SwingMyEditorArgs.ARG_WORKING_DIR, MyMisc.myGetTmpDirectory(),
          "-nolic",
          "-noconsole"
      };
    }
//
// View un-encrypted file
    else if ( testType == 3 ) {
      System.out.println( MyTrace.myGetMethodName() + ": View "  + tmpFileName );
      args = new String[] {
          SwingMyEditorArgs.ARG_FILE, tmpFileName,
          SwingMyEditorArgs.ARG_SIZE_X, "800",
          SwingMyEditorArgs.ARG_SIZE_Y, "600",
          SwingMyEditorArgs.ARG_WORKING_DIR, MyMisc.myGetTmpDirectory(),
          "-nolic",
          "-noconsole"
      };
    }
//
// Test encrypted file
    else if ( testType == 4 ) {
      String tmpEncryptFileName = tmpFileName + SwingMyEditorConst.MY_ENCRYPT_EXTENSION;
      String password = "password";
//
      if ( !new File(tmpEncryptFileName).exists() ) {
        System.out.println( MyTrace.myGetMethodName() + ": creating " + tmpEncryptFileName );
        String data = "Enc Line 1\nEnc Line 2\nSee spot run.\nEnc Line 3\n";
        MyWriteOrReadGpgFile.myWriteEncryptedFile(
          tmpEncryptFileName,  // String outputFileName,
          data,         // String data,
          password      // String passwordOrNull
        );
      }
//
      System.out.println( MyTrace.myGetMethodName() + ": Encrypted file: Open and possibly edit and save "  + tmpEncryptFileName );
      args = new String[] {
          SwingMyEditorArgs.ARG_FILE, tmpEncryptFileName,
          SwingMyEditorArgs.ARG_PASSWORD, password,
          SwingMyEditorArgs.ARG_ALLOW_ENCRYPTED_READ_WRITE,
          SwingMyEditorArgs.ARG_SIZE_X, "800",
          SwingMyEditorArgs.ARG_SIZE_Y, "600",
          SwingMyEditorArgs.ARG_WORKING_DIR, MyMisc.myGetTmpDirectory(),
          "-nolic",
          "-noconsole"
      };
    }
//
// View encrypted directory
    else if ( testType == 5 ) {
      System.out.println( MyTrace.myGetMethodName() + ": Set up to open encrypted files in " + MyMisc.myGetTmpDirectory() );
      args = new String[] {
         SwingMyEditorArgs.ARG_WORKING_DIR, MyMisc.myGetTmpDirectory(),
         SwingMyEditorArgs.ARG_FILE_IS_ENCRYPTED,
         SwingMyEditorArgs.ARG_ALLOW_ENCRYPTED_READ_WRITE,
         "-nolic",
         "-noconsole"
      };
    }
//
// Print out info
//
    MyApplicationInterface.myAppPrintTestArgs ( testType, args, MyTrace.myGetMethodName() );
//
    return args;
  } //End: method


//------------------  Method  ------------------
/**
 * This method prints help information.
 *
 */
//  @Override public final void myAppPrintHelp ( )
//  { System.out.print( SwingMyEditorZ03Handler.myGetHelp( "  ") ); }


// ------------------ Main Method ------------------
/**
 * This method is the runnable main method.
 * <pre>
 *  Arguments are:
 *    -file &lt;file_name&gt;           : File to be opened at start of program
 *    -dir &lt;directory_name&gt;       : Start editor pointing to specified directory
 *    -ro                         : Set editor to read only
 *    -rw                         : Set editor to read/write
 *    -pw &lt;password&gt;              : Password for encrypted file
 *    -isenc                      : Indicates file is encrypted
 *    -enro                       : Set editor to encrypted read only
 *    -enrw                       : Set editor to encrypted read/write
 *    -nomenu                     : No menus implies only view starting file
 *    -sizex &lt;width&gt;              : Width of GUI
 *    -sizey &lt;height&gt;             : Height of GUI
 *    -help                       : Prints this message and exits
 * </pre>
 * 
 * @param args String[] of input arguments
 */
  public static void main( String[] args )
  {
    new SwingMyEditorJFrame ( args );
  } //End: Method


} //End: class SwingMyEditorJFrame

