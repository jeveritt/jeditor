// SwingMyEditorZ00Handler.java
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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import my_proj.my_lib.lib.MyApplicationInterface;
import my_proj.my_lib.lib.MyMisc;
import my_proj.my_lib.lib.MyReadOption;
import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_encrypt.call_gpg.MyWriteOrReadGpgFile;
import my_proj.my_lib.lib_swing.SwingMyJDialogForPassword;


//------------------  CLASS: SwingMyEditorZ00Handler  ------------------
/**
 * This class
 *
 * @author James Everitt
 */
public final class SwingMyEditorZ03Handler {

//private static final boolean DO_TRACE = true;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private SwingMyEditorZ03Handler ( ) {}


//------------------------------------------------------------------------
//-----------------------  Static Methods:  ------------------------------
//------------------------------------------------------------------------
  
//------------------  Method ------------------
/**
 * This method prints out a help standard verbage.
 *
 * @param programPathName  ?
 * @param shortProgramDescription  ?
 */
  private static final void myAppGetHelpStandardVerbage ( String indent, String programPathName, String shortProgramDescription, StringBuffer sb )
  {
//
    sb.append( indent + shortProgramDescription + "\n" );
//
    sb.append( indent + "  Usage for stand alone editor:" + "\n" );
    sb.append( indent + "        [<java_path>/]java -cp <path_to_jar>/jeditor.jar " + programPathName + " [options]" + "\n" );
    sb.append( indent + "    Base:" + "\n" );
    sb.append( indent + "        [<java_path>/]java          : Path and name for the java virtual machine executable" + "\n" );
    sb.append( indent + "                                    : Path not needed if included in executable path variable PATH" + "\n" );
    sb.append( indent + "        [-cp <class_path>]          : Path to dir containing java classes needed for this application" + "\n" );
    sb.append( indent + "                                         or to java archive file (<name>.jar) of the java classes" + "\n" );
    sb.append( indent + "                                    : Not needed if set in java variable CLASS_PATH" + "\n" );
    sb.append( indent + "        " + programPathName  + "  : Program name" + "\n" );
  } //End: Method


//------------------  Method  ------------------
/**
 * This method prints instructions on starting the program.
 *
 * @param indent  ?
 * 
 * @return Returns help String
 */
  public static final String myGetHelp ( String indent )
  {
    String programPathName;
    if      ( SwingMyEditorConst.myType == SwingMyEditorConst.MY_TYPE.FRAME )     programPathName = SwingMyEditorJFrame.class.getName();
    else if ( SwingMyEditorConst.myType == SwingMyEditorConst.MY_TYPE.INT_FRAME ) programPathName = SwingMyEditorJInternalFrame.class.getName();
    else if ( SwingMyEditorConst.myType == SwingMyEditorConst.MY_TYPE.DIALOG )    programPathName = SwingMyEditorJDialog.class.getName();
    else programPathName = "SwingMyEditor";
//
    StringBuffer sb = new StringBuffer();
    if ( indent == null ) indent = "";
//
    String description =
          "The \"" + programPathName + "\" program provides a simple vi like text editor written in Java." +
          "\n  A sub-class can also be incorporated into other Java programs to provide a file editor function.";
//
    myAppGetHelpStandardVerbage( indent, programPathName, description, sb );
//
    sb.append( indent + "    Options:" + "\n" );
    sb.append( indent + "      -file <file_name>           : File to be opened at start of program" + "\n" );
    sb.append( indent + "      -dir <directory_name>       : Start editor pointing to specified directory" + "\n" );
    sb.append( indent + "      -ro                         : Set editor to read only" + "\n" );
    sb.append( indent + "      -rw                         : Set editor to read/write" + "\n" );
    sb.append( indent + "      -pw <password>              : Password for encrypted file" + "\n" );
    sb.append( indent + "      -isenc                      : Indicates file is encrypted" + "\n" );
    sb.append( indent + "      -enro                       : Set editor to encrypted read only" + "\n" );
    sb.append( indent + "      -enrw                       : Set editor to encrypted read/write" + "\n" );
    sb.append( indent + "      -nomenu                     : No menus implies only view starting file" + "\n" );
    sb.append( indent + "      -sizex <width>              : Width of GUI" + "\n" );
    sb.append( indent + "      -sizey <height>             : Height of GUI" + "\n" );
//
    sb.append( indent + "      --help|-help|--h|-h         : Prints this message and exits" + "\n" );
//
    sb.append( indent + "    Example for stand alone editor:" + "\n" );
    sb.append( indent + "       java \\" + "\n" );
    sb.append( indent + "         -cp <path_to_jar_dir>/jeditor.jar" + " \\" + "\n" );
    sb.append( indent + "           " + programPathName + " \\" + "\n" );
    sb.append( indent + "             " + "-rw" + " \\" + "\n" );
    sb.append( indent + "               " + "-file <file_name>" + "\n" );
//
    return sb.toString();
  } //End: Method


//------------------  Method  ------------------
/**
 * This method returns a short program description and operation cheat sheet.
 *
 * @return  Returns info String
 */
  static final String myGetAppHelpString ( )
  {
    String data =
        "This is a simple text editor for Java. It has a vi like mode and a\n" +
        "GUI mode. It is similar to a simplified VIM program.\n" +
        "The program is implemented using the Swing library. The GUI mode\n" +
        "is built into the library itself and behaves in a pretty standard way.\n" +
        "\n" +
        "Modes are as follows:\n" +
        "    GUI: works in parallel with Vi->Text mode\n" +
        "    Vi:\n" +
        "        Text mode: enter from command mode by typing \"i\" or \"a\"\n" +
        "        Command mode: enter from text mode with <esc> key\n" +
        "            Direct: command executed by typing in edit window\n"+
        "            CmdLn: type \":\" for cmd line then <cmd><return>\n"+
        "\n" +
        "Vi mode details:\n" +
        "\n" +
        "  Text mode:\n" +
        "     Edit window key strokes simply add text\n" +
        "     Esc key -> Shift to command mode\n" +
        "\n" +
        "  Command mode edit window key stroke commands:\n" +
        "     :     -> Shift to command line\n" +
        "     a     -> Return to text entry mode and move caret right 1\n" +
        "     <n>dd -> Delete one or n lines starting under caret\n" +
        "     h     -> Shift left 1 char\n" +
        "     i     -> Return to text entry mode\n" +
        "     j     -> Shift down 1 line\n" +
        "     k     -> Shift up 1 line\n" +
        "     l     -> Shift right 1 char\n" +
        "     n     -> Forward search for next match string\n" +
        "     N     -> Backward search for next match string\n" +
        "     p     -> Insert copied text under line with caret\n" +
        "     u     -> Undo last operation(s)\n" +
        "     x     -> Delete char\n" +
        "     <n>yy -> Copy one or n lines starting with caret to clip board\n" +
        "\n" +
        "  Command mode command line commands ->  :<cmd><return>\n" +
        "     /<match_string> -> search for match string\n" +
        "     w  -> write\n" +
        "     q  -> quit without writing\n" +
        "     wq -> write and then quit\n" +
        "     !q -> force quit without writing\n" +
        "     1  -> move to first line\n" +
        "     $  -> move to last line\n" +
        "\n";
    return data;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method checks input arguments.
 * 
 * @param inputArgs  ?
 *
 * @return  ?
 */
  static final String myAppCheckArgsOk ( String[] inputArgs )
  {
//    return SwingMyEditorZ01AppIfc.myAppCheckArgsOkSub(inputArgs,  SwingMyEditorJFrame.class.getName() );
    //    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": args= " + MyStringOps.myStringArrayToString(inputArgs, ',') );
//
    if ( inputArgs.length == 1 && !inputArgs[0].startsWith("-") ) return null;
//
    String[] allowableSingleArgs = new String[] {
        "-h","-help","--h","--help",
        MyApplicationInterface.ARG_NO_CONSOLE,
        MyApplicationInterface.ARG_NO_LIC,
        MyApplicationInterface.ARG_VERBOSE,
        MyApplicationInterface.ARG_QUIET,
        SwingMyEditorArgs.ARG_FILE_IS_ENCRYPTED,
        SwingMyEditorArgs.ARG_ALLOW_READ_ONLY,
        SwingMyEditorArgs.ARG_ALLOW_READ_WRITE,
        SwingMyEditorArgs.ARG_ALLOW_ENCRYPTED_READ_ONLY,
        SwingMyEditorArgs.ARG_ALLOW_ENCRYPTED_READ_WRITE,
        SwingMyEditorArgs.ARG_NO_MENU
      };
//
    String[] allowableDoubleArgs = new String[] {
        MyApplicationInterface.ARG_VERBOSE,
        SwingMyEditorArgs.ARG_TITLE,
        SwingMyEditorArgs.ARG_FILE,
//        SwingMyEditorCtrl.ARG_CONTROL,
        SwingMyEditorArgs.ARG_PASSWORD,
        SwingMyEditorArgs.ARG_SIZE_X,
        SwingMyEditorArgs.ARG_SIZE_Y,
        SwingMyEditorArgs.ARG_WORKING_DIR
      };
//
    String[] allowableMultArgs = null;
//
    String[] requiredArgs = null;
//
    String badArg = MyApplicationInterface.myAppCheckInputArgs ( inputArgs, allowableSingleArgs, allowableDoubleArgs, allowableMultArgs, requiredArgs, null );
//
    if ( badArg != null ) {
      System.out.println();
      System.out.println("---- Error: program = " + SwingMyEditorJFrame.class.getName() + " : " + badArg + " ----");
      System.out.println();
      System.out.println("Required syntax:");
      System.out.print( SwingMyEditorZ03Handler.myGetHelp( "  " ) );
    }
//
    return badArg;
  } //End: method


//------------------  Method  ------------------
/**
 * This static method takes input args and returns a editor configuration control int.
 *
 * @param args  Input arguments
 *
 * @return  Editor configuration control int
 */
  private static final int myGetCtrlIntFromInputArgs ( String[] args )
  {
    int ctrl = 0;
//
    boolean canReadUnencrypt    = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_READ_ONLY) ||
                                  MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_READ_WRITE);
    boolean canReadEncrypted    = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_ENCRYPTED_READ_ONLY) ||
                                  MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_ENCRYPTED_READ_WRITE);
    boolean canWriteUnencrypted = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_READ_WRITE);
    boolean canWriteEncrypted   = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_ENCRYPTED_READ_WRITE);
//
    if ( canReadUnencrypt ) ctrl = ctrl | SwingMyEditorCtrl.MY_ALLOW_UNENCRYP_READ;
//
    if ( canWriteUnencrypted ) ctrl = ctrl | SwingMyEditorCtrl.MY_ALLOW_UNENCRYP_WRITE;
//
    if ( canReadEncrypted ) ctrl = ctrl | SwingMyEditorCtrl.MY_ALLOW_ENCRYP_READ;
//
    if ( canWriteEncrypted ) ctrl = ctrl | SwingMyEditorCtrl.MY_ALLOW_ENCRYP_WRITE;
//
    if ( MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_NO_MENU) ) ctrl = ctrl | SwingMyEditorCtrl.MY_NO_MENU;
//
    if  ( MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_FILE_IS_ENCRYPTED) ||
          ( !canReadUnencrypt && canReadEncrypted ) )
        ctrl = ctrl | SwingMyEditorCtrl.MY_IS_ENCRYPT;
//
    return ctrl;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method 
 *
 * @param name  ?
 *
 * @return output String
 */
  private static final String myGetFileNameWithNameBeforePath ( String name )
  {
    if ( name == null || name.length() < 3 ) return name;
    int slIndx = name.lastIndexOf('/');
    if ( slIndx <= 0 ) slIndx = name.lastIndexOf('\\');
    if ( slIndx <= 0 || slIndx >= name.length()-2 ) return name;
    String name2 = name.substring(slIndx + 1) + " : " + name.substring(0, slIndx);
    return name2;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method returns info about program status.
 *
 * @param ind  Amount to indent
 * @param title  First line title or null
 * 
 * @return  Return info string
 */
  static final String myGetInfoString( String ind, int ctrl, SwingMyEditorZ04JPanel editPan )
  {
    StringBuffer sb = new StringBuffer();
//
    SwingMyEditorCtrl.myGetEditorCtrlInfo( ctrl, sb, ind);
    editPan.myGetTextArea().myGetInfoString(ind, sb);
//
    return sb.toString();
  } //End: Method


//------------------  Method  ------------------
/**
 * This method creates, initializes and returns a SwingMyEditorZ04JPanel using an input args array.
 * 
 * @param cont  ?
 * @param args  ?
 *
 * @return  Returns SwingMyEditorZ04JPanel
 */
  public static final SwingMyEditorZ04JPanel myInitializeFromArgs (
      Container  cont,
      String[]   args
      )
  {
// Possibly set trace
    int verboseLvl = MyReadOption.myIntValueAfterKey( -1, args, SwingMyEditorArgs.ARG_VERBOSE );
    if ( verboseLvl > 0 ) {
      String verboseFileNm = MyReadOption.myValueAfterKey(args, SwingMyEditorArgs.ARG_TRACE_OUTPUT);
      if ( verboseFileNm == null ) verboseFileNm = MyMisc.myGetTmpDirectory() + "/zz_lib_swing_editor_trace.txt";
      MyTrace.mySetVerboseLevel(verboseLvl, verboseFileNm);
    }
// Trace info
    if ( MyTrace.myDoPrint() ) {
      MyTrace.myPrintln(MyTrace.myInd() + MyTrace.myGetMethodName() + ": cont= " + (cont == null ? "null" : cont.getName() ) );
      if ( args.length == 0 ) MyTrace.myPrintln(MyTrace.myInd() + "  args: no args");
      else {
        MyTrace.myPrintln(MyTrace.myIndWithoutPrefix() + "  args:");
        for ( String str : args ) MyTrace.myPrintln(MyTrace.myIndWithoutPrefix() + "    " + str);
      }
    }
// Define font
    
// Starting file name or null
    String fileName = ( args.length == 1 ) ? args[0] : MyReadOption.myValueAfterKey(null, args, SwingMyEditorArgs.ARG_FILE);
    File startingDataFile = ( fileName == null ) ? null : new File(fileName);
// Title
    String title;
    if ( fileName == null ) title = "Empty File";
    else title = myGetFileNameWithNameBeforePath(fileName);
// Password
    String password = MyReadOption.myValueAfterKey(null, args, SwingMyEditorArgs.ARG_PASSWORD);
// Control int
    int ctrl = myGetCtrlIntFromInputArgs(args);
// Handle encryption
    boolean startingDataFileIsEncrypted = startingDataFile != null &&
        startingDataFile.isFile() &&
        (ctrl & SwingMyEditorCtrl.MY_ALLOW_ENCRYP_READ) != 0 &&
        ( startingDataFile.getName().endsWith(SwingMyEditorConst.MY_ENCRYPT_EXTENSION) ||
          MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_FILE_IS_ENCRYPTED ) )
        ;
// Define size
    int sizeX = MyReadOption.myIntValueAfterKey(600, args, SwingMyEditorArgs.ARG_SIZE_X);
    int sizeY = MyReadOption.myIntValueAfterKey(400, args, SwingMyEditorArgs.ARG_SIZE_Y);
// Get working directory
    String workingDirName = MyReadOption.myValueAfterKey(null, args, SwingMyEditorArgs.ARG_WORKING_DIR);
// Create editor
    SwingMyEditorZ04JPanel editorPanel = myInitializeFromFile(
        cont,                        // Container      cont,
        title,                       // String         title,
        startingDataFile,            // File           dataFile,
        startingDataFileIsEncrypted, // boolean        startingDataFileIsEncrypted
        ctrl,                        // int            ctrl,
        sizeX, sizeY,                // int sizeX, int sizeY,
        null,                        // JMenu[]        custom
        password,                    // String         password
        workingDirName               // String         workingDirName
        );
//
    return editorPanel;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method creates, initializes and returns a SwingMyEditorZ04JPanel using a File as the initial data.
 *
 * @param cont  ?
 * @param title  ?
 * @param startingDataFile  ?
 * @param startingDataFileIsEncrypted  ?
 * @param ctrl  ?
 * @param sizeX  ?
 * @param sizeY  ?
 * @param custom  ?
 * @param password  ?
 * @param workingDirName  ?
 * 
 * @return  Returns SwingMyEditorZ04JPanel
 */
  static final SwingMyEditorZ04JPanel myInitializeFromFile (
      Container      cont,
      String         title,
      File           startingDataFile,
      boolean        startingDataFileIsEncrypted,
      int            ctrl,
      int sizeX, int sizeY,
      JMenu[]        custom,
      String         password,
      String         workingDirName
      )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName()  + ": ctrl= 0x" + Integer.toHexString(ctrl) + ": cont= " + (cont == null ? null : cont.getName()) + MyTrace.myNlInd() + " dir= " + workingDirName  + MyTrace.myNlInd() + " file= " + (startingDataFile == null ? "null" : startingDataFile.getAbsolutePath()) );
//
    SwingMyEditorZ04JPanel editorPanel = null;
    try {
// Figure out permissions
      boolean readEncrypt = (ctrl & SwingMyEditorCtrl.MY_ALLOW_ENCRYP_READ) != 0;
      boolean readOrWriteEncryp = readEncrypt ||
                                  (ctrl & SwingMyEditorCtrl.MY_ALLOW_ENCRYP_WRITE) != 0;
// If necessary, get password
      if ( password == null && readOrWriteEncryp ) {
        password = MyWriteOrReadGpgFile.myGetStandardPas();
        if ( password == null ) {
          Window window = ( cont instanceof Window ) ? (Window)cont : null;
          if ( window != null ) password = SwingMyJDialogForPassword.myGetPassword(window);
        }
      }
// Read in data
      String textDataAsString = null;
        if ( startingDataFile != null && startingDataFile.isFile() ) {
        ArrayList<String> al;
        if ( readEncrypt && startingDataFileIsEncrypted ) al = MyWriteOrReadGpgFile.myReadDecryptedFile(startingDataFile.getCanonicalPath(), password, null);
        else al = SwingMyEditorZ11Misc.myGetFileContentsAsArrayList(startingDataFile, null);
        textDataAsString = SwingMyEditorZ11Misc.myGetFileContentsAsString(al, "");
      }
//
// Do initialization
      editorPanel = myInitialize(
          cont,               // Container        cont
          title,              // String           title,
          textDataAsString,   // String           textDataAsString,
          ctrl,               // int              ctrl,
          sizeX,              // int              sizeX,
          sizeY,              // int              sizeY,
          custom,             // JMenu[]          custom
          password,           // String           password
          startingDataFile,   // File             saveDataFile
          workingDirName      // String           workingDirName
          );
    }
//
    catch (Exception e) {
      System.out.println( "SwingMyEditorZ00Handler.myInitializeFromFile: exc= " + e.getMessage());
//      if ( MyC.PRINT_STACK_TRACE ) e.printStackTrace();
    }
//
    return editorPanel;
  } //End: Method

//------------------  Method  ------------------
/**
 * This method creates, initializes and returns a SwingMyEditorZ04JPanel using a ArrayList<String> as the initial data.
 *
 * @param cont  ?
 * @param title  ?
 * @param list  ?
 * @param ctrl  ?
 * @param custom  ?
 *
 * @return  Returns SwingMyEditorZ04JPanel
 */
  static final SwingMyEditorZ04JPanel myInitializeFromStringArray (
      Container         cont,
      String            title,
      ArrayList<String> list,
      int               ctrl,
      JMenu[]           custom )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName()  + ": ctrl= 0x" + Integer.toHexString(ctrl) + MyTrace.myNlInd() + MyTrace.myNlInd() );
//
    SwingMyEditorZ04JPanel editorPanel = null;
    try {
      String textDataAsString = SwingMyEditorZ11Misc.myGetFileContentsAsString(list, "");
      editorPanel = SwingMyEditorZ03Handler.myInitialize (
          cont,                 // Container      cont
          title,                // String         title
          textDataAsString,     // String         textDataAsString
          ctrl,                 // int            ctrl
          SwingMyEditorConst.MY_DEFAULT_SIZE_X, // int sizeX
          SwingMyEditorConst.MY_DEFAULT_SIZE_Y, // int sizeY
          custom,               // JMenu[]        custom
          null,                 // String         password
          null,                 // File           saveDataFile
          null                  // String         workingDirName
         );
    }
    catch (Exception e) {
      System.out.println( "SwingMyEditorZ00Handler.myInitializeFromStringArray: exc= " + e.getMessage());
      if ( SwingMyEditorConst.PRINT_STACK_TRACE ) e.printStackTrace();
    }
//
    return editorPanel;
  } //End: Method

//------------------  Method  ------------------
/**
 * This method creates, initializes and returns a SwingMyEditorZ04JPanel using a String as the initial data.
 *
 * @param cont  ?
 * @param title  ?
 * @param dataStr  ?
 * @param ctrl  ?
 * @param sizeX  ?
 * @param sizeY  ?
 * @param custom  ?
 *
 * @return  Returns SwingMyEditorZ04JPanel
 */
  public static final SwingMyEditorZ04JPanel myInitializeFromString (
      Container    cont,
      String       title,
      String       dataStr,
      int          ctrl,
      int          sizeX,
      int          sizeY,
      JMenu[]      custom )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName()  + ": ctrl= 0x" + Integer.toHexString(ctrl) + MyTrace.myNlInd() + MyTrace.myNlInd() );
//
    SwingMyEditorZ04JPanel editPanel = null;
    try {
      editPanel = SwingMyEditorZ03Handler.myInitialize(
          cont,               // Container        cont
          title,              // String           title
          dataStr,            // String           textDataAsString
          ctrl,               // int              ctrl
          sizeX,              // int              sizeX
          sizeY,              // int              sizeY
          custom,             // JMenu[]          custom
          null,               // String           password
          null,               // File             saveDataFile
          null                // String           workingDirName
          );
    }
    catch (Exception e) {
      System.out.println( "SwingMyEditorZ00Handler.myInitializeFromString: exc= " + e.getMessage());
      if ( SwingMyEditorConst.PRINT_STACK_TRACE ) e.printStackTrace();
    }
//
    return editPanel;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method creates and returns a SwingMyEditorZ04JPanel
 *
 * @param callingProgramName  ?
 * @param cont  ?
 * @param title  ?
 * @param textDataAsString  ?
 * @param ctrl  ?
 * @param sizeX  ?
 * @param sizeY  ?
 * @param custom  ?
 * @param password  ?
 * @param saveDataFile  ?
 * @param workingDirName  ?
 *
 * @return  Returns SwingMyEditorZ04JPanel
 */
  static final SwingMyEditorZ04JPanel myInitialize (
      Container        cont,
      String           title,
      String           textDataAsString,
      int              ctrl,
      int sizeX, int sizeY,
      JMenu[]          custom,
      String           password,
      File             saveDataFile,
      String           workingDirName
      ) {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl) + ": cont= " + (cont == null ? null : cont.getName()) + ": file= " + saveDataFile);
// If ctrl == 0 which means you can't do anything then set for read/write
    if ( ctrl == 0 ) ctrl =  SwingMyEditorCtrl.MY_ALLOW_UNENCRYP_READ | SwingMyEditorCtrl.MY_ALLOW_UNENCRYP_WRITE;
// Get password if necessary
    boolean readOrWriteEncryp = (ctrl & SwingMyEditorCtrl.MY_ALLOW_ENCRYP_READ) != 0 ||
                                (ctrl & SwingMyEditorCtrl.MY_ALLOW_ENCRYP_WRITE) != 0 ;
    if ( password == null && readOrWriteEncryp ) password = MyWriteOrReadGpgFile.myGetStandardPas();
// Create edit panel
    SwingMyEditorZ04JPanel editorPanel = new SwingMyEditorZ04JPanel(
        textDataAsString,   // String     textDataAsString
        ctrl,               // int        ctrl,
        null,               // JComponent lowerPanel
        password,           // String     password
        saveDataFile,       // File       saveDataFile
        workingDirName      // String     workingDirName
        );
// Figure out type of container
    JFrame jfr = ( cont instanceof JFrame ) ? (JFrame)cont : null;
    JInternalFrame jifr = ( cont instanceof JInternalFrame ) ? (JInternalFrame)cont : null;
    JDialog jdlg = ( cont instanceof JDialog ) ? (JDialog)cont : null;
// If necessary, pick default title
    if ( title == null ) title = "SwingMyEditorZ00Handler";
// Handle container size
    if ( sizeX < 50 ) sizeX = 50;
    if ( sizeY < 50 ) sizeY = 50;
    editorPanel.setPreferredSize( new Dimension(sizeX, sizeY) );
// Get Icon image
    Image img = SwingMyEditorZ11Misc.myGetImage("my_proj/my_lib_resources/my_gifs/jedit_icon_26x26.gif");
//
    if ( MyTrace.myDoPrint() ) {
      MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": about to start GUI and event queue: setup info as follows:");
      MyTrace.myPrint( myGetInfoString(MyTrace.myIndWithoutPrefix() + "  ", ctrl, editorPanel) );
    }
// Do GUI setup for various types of containers and then set visible
    if ( jfr != null ) {
      jfr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      jfr.setTitle(title);
      if ( img != null ) jfr.setIconImage(img);
      jfr.addWindowListener(editorPanel);
      if ( (ctrl & SwingMyEditorCtrl.MY_NO_MENU) == 0 ) jfr.setJMenuBar( editorPanel.myCreateMenuBar(custom) );
      jfr.setContentPane(editorPanel);
      jfr.pack();
      jfr.setLocationRelativeTo(null);
      jfr.setVisible(true);
    }
    else if ( jifr != null ) {
      jifr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      jifr.setTitle(title);
      if ( (ctrl & SwingMyEditorCtrl.MY_NO_MENU) == 0 ) jifr.setJMenuBar( editorPanel.myCreateMenuBar(custom) );
      jifr.setContentPane(editorPanel);
      jifr.pack();
      jifr.setVisible(true);
    }
    else if ( jdlg != null ) {
      jdlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      jdlg.setTitle(title);
      if ( img != null ) jdlg.setIconImage(img);
      jdlg.addWindowListener(editorPanel);
      if ( (ctrl & SwingMyEditorCtrl.MY_NO_MENU) == 0 ) jdlg.setJMenuBar( editorPanel.myCreateMenuBar(custom) );
      jdlg.setContentPane(editorPanel);
      jdlg.pack();
      jdlg.setLocationRelativeTo(null);
      jdlg.setVisible(true);
    }
// Set initial focus to the text entry area
    editorPanel.myGetTextArea().requestFocus();
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": exiting");
// Return SwingMyEditorZ04JPanel editorPanel
    return editorPanel;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method creates and returns a default JMenuBar
 *
 * @param pan  ?
 * @param additionalItemsOrNull ?
 *
 * @return  Returns a default JMenuBar
 */
  public static final JMenuBar myGetDefaultMenu (
      SwingMyEditorZ04JPanel   pan,
      JMenu[]                  additionalItemsOrNull
      )
  {
    SwingMyEditorZ05Action actions = pan.myGetTextArea().myGetActions();
//
    JMenuBar menu = SwingMyEditorZ06Menu.myCreateMenuBar(
        additionalItemsOrNull,
        actions
        );
//
    return menu;
  } //End: Method


} //End: class SwingMyEditorZ00Handler

