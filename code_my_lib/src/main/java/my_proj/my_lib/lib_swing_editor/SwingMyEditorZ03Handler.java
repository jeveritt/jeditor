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
import java.awt.Toolkit;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import my_proj.my_lib.lib.MyReadOption;
import my_proj.my_lib.lib_encrypt.call_gpg.MyWriteOrReadGpgFile;
import my_proj.my_lib.lib_swing.SwingMyJDialogForPassword;


//------------------  CLASS: SwingMyEditorZ00Handler  ------------------
/**
 * This class
 *
 * @author James Everitt
 */
public final class SwingMyEditorZ03Handler {

//  private static final boolean DO_TRACE = true;


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
  private static void myAppPrintHelpStandardVerbage ( String programPathName, String shortProgramDescription )
  {
    //
    System.out.println(shortProgramDescription);
//
    System.out.println("  Usage:");
    System.out.println("      [<java_path>/]java -cp <path_to_jar>/<jar_name>.jar " + programPathName + " [options]");
    System.out.println("     or if class files not in file_in_jar form:");
    System.out.println("      [<java_path>/]java [-cp <path_to_java_classes>] " + programPathName + " [options]");
    System.out.println("    Base:");
    System.out.println("      [<java_path>/]java          : Path and name for the java virtual machine executable");
    System.out.println("                                  : Path not needed if included in executable path variable PATH");
    System.out.println("      [-cp <class_path>]          : Path to dir containing java classes needed for this application");
    System.out.println("                                       or to java archive file (<name>.jar) of the java classes");
    System.out.println("                                  : Not needed if set in java variable CLASS_PATH");
    System.out.println("      " + programPathName );
    System.out.println("                                  : Program name");
  } //End: Method


//------------------  Method  ------------------
/**
 * This method prints help information.
 *
 */
  public static final void myPrintHelp ( String programPathName )
  {
//    String programPathName = SwingMyEditorZ00AppIfc.class.getName();
    String description =
        "  The \"" + programPathName + "\" program provices a simple vi like text editor written in Java.";
//
    myAppPrintHelpStandardVerbage( programPathName, description );
//
    System.out.println("    Options:");
    System.out.println("      -file <file_name>           : File to be opened");
    System.out.println("      -dir <directory_name>       : Start editor specified directory");
    System.out.println("      -ops                        : Allow various file operations");
    System.out.println("      -ro                         : Set file to read only");
    System.out.println("      -rw                         : Set file to read/write");
    System.out.println("      -pw <password>              : Password for encrypted file");
    System.out.println("      -isenc                      : Indicates file is encrypted");
    System.out.println("      -enro                       : Set file to encrypted read only");
    System.out.println("      -enrw                       : Set file to encrypted read/write");
    System.out.println("      -nomenu                     : Causes menu tool bar to not be shown");
    System.out.println("      -sizex <width>              : Width of GUI");
    System.out.println("      -sizey <height>             : Height of GUI");
//
    System.out.println("      --help|-help|--h|-h         : Prints this message");
//
    System.out.println("    Example:");
    System.out.println("       java \\");
    System.out.println("         -cp <path_to_jar_dir>/<jar_name>.jar \\");
    System.out.println("           " + programPathName + " \\");
    System.out.println("             -nolic -noconsole");
  } //End: Method


//------------------  Method  ------------------
/**
 * This method prints help information.
 *
 */
  static final String myGetAppHelpString ( )
  {
    String data =
        "This is a simple text viewer and vi like text editor for Java.\n" +
        "Vi has a command and a text edit mode. Enter command mode\n" +
        "with the 'esc' key, enter the edit mode with the 'i' or 'a' key.\n" +
        "When in command mode the ':' key shifts to the command line.\n" +
        "This program is implemented using the Swing library. It also\n" +
        "supports some GUI like stuff built into the swing component.\n" +
        "\n" +
        "  Edit window edit mode key stroke to enter command mode:\n" +
        "    Esc -> Shift to entering commands on command line\n" +
        "\n" +
        "  Edit window command mode key strokes:\n" +
        "    :     -> Shift to command line\n" +
        "    a     -> Return to text entry mode and move carot right 1\n" +
        "             (Not useful because editor differs from traditional vi)\n" +
        "    dd    -> Delete line under carot\n" +
        "    <n>dd -> Delete n lines starting under carot\n" +
        "    h     -> Shift left 1 char\n" +
        "    i     -> Return to text entry mode\n" +
        "    j     -> Shift down 1 line\n" +
        "    k     -> Shift up 1 line\n" +
        "    l     -> Shift right 1 char\n" +
        "    n     -> Search for next match string\n" +
        "    p     -> Puts back the yanked text\n" +
        "    u     -> Undo last operation\n" +
        "    x     -> Delete char\n" +
        "    yy    -> Delete and save line under carot\n" +
        "    <n>yy -> Delete and save n lines starting under carot\n" +
        "\n" +
        "  Command line commands:\n" +
        "    /<match_string> -> search for match string\n" +
        "    w  -> write\n" +
        "    q  -> quit without writing\n" +
        "    wq -> write and then quit\n" +
        "    !q -> force quit without writing\n" +
        "    1  -> move to first line\n" +
        "    $  -> move to last line\n" +
        "\n";
    return data;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method takes input args and returns a editor configuration control int.
 *
 * @param args  Input arguments
 *
 * @return  Editor configuration control int
 */
  static final int myGetCtrlIntFromInputArgs ( String[] args )
  {
    int ctrl = 0;
//
    boolean canReadUnencrypt    = MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_ALLOW_READ_ONLY) ||
                                  MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_ALLOW_READ_WRITE);
    boolean canReadEncrypted    = MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_ALLOW_ENCRYPTED_READ_ONLY) ||
                                  MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_ALLOW_ENCRYPTED_READ_WRITE);
    boolean canWriteUnencrypted = MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_ALLOW_READ_WRITE);
    boolean canWriteEncrypted   = MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_ALLOW_ENCRYPTED_READ_WRITE);
//
    if ( MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_ALLOW_FILE_OPS) ||
           canWriteUnencrypted || canWriteEncrypted )
      ctrl = ctrl | SwingMyEditorConst.MY_ALLOW_FILE_OPS;
//
    if ( canReadUnencrypt ) ctrl = ctrl | SwingMyEditorConst.MY_ALLOW_UNENCRYP_READ;
//
    if ( canWriteUnencrypted ) ctrl = ctrl | SwingMyEditorConst.MY_ALLOW_UNENCRYP_WRITE;
//
    if ( canReadEncrypted ) ctrl = ctrl | SwingMyEditorConst.MY_ALLOW_ENCRYP_READ;
//
    if ( canWriteEncrypted ) ctrl = ctrl | SwingMyEditorConst.MY_ALLOW_ENCRYP_WRITE;
//
    if ( MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_NO_MENU) ) ctrl = ctrl | SwingMyEditorConst.MY_NO_MENU;
//
    if  ( MyReadOption.myKeyExists(args, SwingMyEditorConst.ARG_FILE_IS_ENCRYPTED) ||
          ( !canReadUnencrypt && canReadEncrypted ) )
        ctrl = ctrl | SwingMyEditorConst.MY_IS_ENCRYPT;
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
  public static String myGetFileNameWithNameBeforePath ( String name )
  {
    if ( name == null || name.length() < 3 ) return name;
    int slIndx = name.lastIndexOf('/');
    if ( slIndx <= 0 ) slIndx = name.lastIndexOf('\\');
    if ( slIndx <= 0 || slIndx >= name.length()-2 ) return name;
    String name2 = name.substring(slIndx + 1) + " : " + name.substring(0, slIndx);
    return name2;
  } //End: Method


//------------------ Method ------------------
/**
 * This method 
 * 
 * @param args String[] of input arguments that are ignored
 */
  public static final void myInitialize( String[] args, boolean putInDialog )
  {
    String fileName = MyReadOption.myValueAfterKey(args, SwingMyEditorConst.ARG_FILE);
    if ( fileName == null && args != null && args.length == 1 ) fileName = args[0];
    String title = MyReadOption.myValueAfterKey(args, SwingMyEditorConst.ARG_TITLE);
    if ( title == null ) title = fileName;
    if ( title == null ) title = "Test";
    int sizex = MyReadOption.myIntValueAfterKey(600, args, SwingMyEditorConst.ARG_SIZE_X);
    int sizey = MyReadOption.myIntValueAfterKey(700, args, SwingMyEditorConst.ARG_SIZE_Y);
    int ctrl = SwingMyEditorZ03Handler.myGetCtrlIntFromInputArgs(args);
//
    JFrame fr = new JFrame(title);
    fr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//
    JDialog dlg = !putInDialog ? null : new JDialog(fr, title);
//
    String password = MyReadOption.myValueAfterKey(args, SwingMyEditorConst.ARG_PASSWORD);
//
    String workingDirName = MyReadOption.myValueAfterKey(args, SwingMyEditorConst.ARG_WORKING_DIR);
    if ( workingDirName == null ) workingDirName = System.getProperty("user.home");
//
//Create edit panel
    SwingMyEditorZ04JPanel editPanel = SwingMyEditorZ03Handler.myInitializeFromFile(
         putInDialog ? dlg : fr,           // Container   cont
         title,                            // String      title
         fileName == null ? null : new File(fileName),  // File  dataFile
         ctrl,                             // int         ctrl
         sizex,                            // int         sizeX
         sizey,                            // int         sizeY
         null,                             // JMenu[]     custom
         password,                         // String      password
         workingDirName                    // String      workingDirName
         );
//Set application image
    ClassLoader cl = SwingMyEditorZ03Handler.class.getClassLoader();
    URL imageURL = cl.getResource( "my_proj/my_lib_resources/my_gifs/jedit_icon_26x26.gif" );
    Image img = ( imageURL == null ) ? null : Toolkit.getDefaultToolkit().getImage(imageURL);
    if ( img != null ) fr.setIconImage(img);
//
//If just JFrame
    if ( !putInDialog ) {
//Set edit panel as content pane
      fr.setContentPane(editPanel);
//Create and set menu
      fr.setJMenuBar( editPanel.myCreateMenuBar(null) );
      fr.pack();
    }
//Else put in dialog
    else {
      fr.setSize(400, 300);
    }
//Final setup
    fr.setLocationRelativeTo(null);
    fr.setVisible(true);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param cont  ?
 * @param args  ?
 *
 */
  static final SwingMyEditorZ04JPanel myInitializeFromFile ( Container cont, String[] args )
  {
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName());
//
    String fileName = ( args.length == 1 ) ? args[0] : MyReadOption.myValueAfterKey(null, args, SwingMyEditorConst.ARG_FILE);
//
    File file = ( fileName == null ) ? null : new File(fileName);
//
    String title;
    if ( fileName == null ) title = "Empty File";
    else title = myGetFileNameWithNameBeforePath(fileName);
//
    String password = MyReadOption.myValueAfterKey(null, args, SwingMyEditorConst.ARG_PASSWORD);
//
    int ctrl = myGetCtrlIntFromInputArgs(args);
//
    int sizeX = MyReadOption.myIntValueAfterKey(600, args, SwingMyEditorConst.ARG_SIZE_X);
    int sizeY = MyReadOption.myIntValueAfterKey(400, args, SwingMyEditorConst.ARG_SIZE_Y);
//
    String workingDirName = MyReadOption.myValueAfterKey(null, args, SwingMyEditorConst.ARG_WORKING_DIR);
//
    SwingMyEditorZ04JPanel editorPanel = myInitializeFromFile(
        cont,           // Container      cont,
        title,          // String         title,
        file,           // File           dataFile,
        ctrl,           // int            ctrl,
        sizeX, sizeY,   // int sizeX, int sizeY,
        null,           // JMenu[]        custom
        password,       // String         password
        workingDirName  // String         workingDirName
        );
    return editorPanel;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param cont  ?
 * @param title  ?
 * @param dataFile  ?
 * @param ctrl  ?
 * @param sizeX  ?
 * @param sizeY  ?
 * @param supplementalActions  ?
 * 
 */
  static final SwingMyEditorZ04JPanel myInitializeFromFile (
      Container      cont,
      String         title,
      File           dataFile,
      int            ctrl,
      int sizeX, int sizeY,
      JMenu[]        custom,
      String         password,
      String         workingDirName
      )
  {
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl) + "\n dir= " + workingDirName + "\n file= " + (dataFile == null ? "null" : dataFile.getAbsolutePath()) + "\n cont= " + cont);
//
    SwingMyEditorZ04JPanel editorPanel = null;
    try {
// Figure out permissions
      boolean readEncrypt = (ctrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_READ) != 0;
      boolean readOrWriteEncryp = readEncrypt ||
                                  (ctrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_WRITE) != 0;
// If necessary, get password
      if ( password == null && readOrWriteEncryp ) {
        password = MyWriteOrReadGpgFile.myGetStandardPas();
        if ( password == null ) {
          JFrame jfr = ( cont instanceof JFrame ) ? (JFrame)cont : null;
          if ( jfr != null ) password = SwingMyJDialogForPassword.myGetPassword(jfr);
        }
      }
// Read in data
      String textDataAsString = null;
        if ( dataFile != null && dataFile.isFile() ) {
        ArrayList<String> al;
        if ( readEncrypt ) al = MyWriteOrReadGpgFile.myReadDecryptedFile(dataFile.getCanonicalPath(), password, null);
        else al = SwingMyEditorZ11Misc.myGetFileContentsAsArrayList(dataFile, null);
        textDataAsString = SwingMyEditorZ11Misc.myGetFileContentsAsString(al, "");
      }
//
// Do initialization
      editorPanel = myInitialize(
          cont,             // Container        cont
          title,            // String           title,
          textDataAsString, // String           textDataAsString,
          ctrl,             // int              ctrl,
          sizeX,            // int              sizeX,
          sizeY,            // int              sizeY,
          custom,           // JMenu[]          custom
          password,         // String           password
          dataFile,         // File             saveDataFile
          workingDirName    // String           workingDirName
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
 * This method ?
 *
 * @param cont  ?
 * @param title  ?
 * @param list  ?
 * @param ctrl  ?
 * @param modalityType  ?
 * @param custom  ?
 *
 */
  static final SwingMyEditorZ04JPanel myInitializeFromStringArray (
      Container         cont,
      String            title,
      ArrayList<String> list,
      int               ctrl,
      JMenu[]           custom )
{
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
 * This method ?
 *
 * @param cont  ?
 * @param title  ?
 * @param dataStr  ?
 * @param ctrl  ?
 * @param sizeX  ?
 * @param sizeY  ?
 * @param modalityType ?
 * @param custom  ?
 *
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
    SwingMyEditorZ04JPanel editPanel = null;
    try {
      editPanel = SwingMyEditorZ03Handler.myInitialize( cont, title, dataStr, ctrl, sizeX, sizeY, custom, null, null, null );
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
 * This method ?
 *
 * @param cont  ?
 * @param title  ?
 * @param dataFile  ?
 * @param ctrl  ?
 * @param sizeX  ?
 * @param sizeY  ?
 *
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
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl) + "\n dir= " + workingDirName + "\n file= " + (saveDataFile == null ? "null" : saveDataFile.getAbsolutePath()) + "\n cont= " + cont);
//
    boolean readOrWriteEncryp = (ctrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_READ) != 0 ||
                                (ctrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_WRITE) != 0 ;
    if ( password == null && readOrWriteEncryp ) password = MyWriteOrReadGpgFile.myGetStandardPas();
//
    SwingMyEditorZ04JPanel editorPanel = new SwingMyEditorZ04JPanel(
        textDataAsString, // String     textDataAsString
        ctrl,             // int        ctrl,
        null,             // JComponent lowerPanel
        password,         // String     password
        saveDataFile,     // File       saveDataFile
        workingDirName    // String     workingDirName
        );
//
    JFrame jfr = ( cont instanceof JFrame ) ? (JFrame)cont : null;
    JInternalFrame jifr = ( cont instanceof JInternalFrame ) ? (JInternalFrame)cont : null;
    JDialog jdlg = ( cont instanceof JDialog ) ? (JDialog)cont : null;
//
    if ( title == null ) title = "SwingMyEditorZ00Handler";
//
    if ( sizeX < 50 ) sizeX = 50;
    if ( sizeY < 50 ) sizeY = 50;
    editorPanel.setPreferredSize( new Dimension(sizeX, sizeY) );
//
    if ( jfr != null ) {
      jfr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      jfr.setTitle(title);
      jfr.addWindowListener(editorPanel);
      if ( (ctrl & SwingMyEditorConst.MY_NO_MENU) == 0 ) jfr.setJMenuBar( editorPanel.myCreateMenuBar(custom) );
      jfr.setContentPane(editorPanel);
      jfr.pack();
      jfr.setLocationRelativeTo(null);
      jfr.setVisible(true);
    }
    else if ( jifr != null ) {
      jifr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      jifr.setTitle(title);
      if ( (ctrl & SwingMyEditorConst.MY_NO_MENU) == 0 ) jifr.setJMenuBar( editorPanel.myCreateMenuBar(custom) );
      jifr.setContentPane(editorPanel);
      jifr.pack();
      jifr.setVisible(true);
    }
    else if ( jdlg != null ) {
      jdlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      jdlg.setTitle(title);
      jdlg.addWindowListener(editorPanel);
      if ( (ctrl & SwingMyEditorConst.MY_NO_MENU) == 0 ) jdlg.setJMenuBar( editorPanel.myCreateMenuBar(custom) );
      jdlg.setContentPane(editorPanel);
      jdlg.pack();
      jdlg.setLocationRelativeTo(null);
      jdlg.setVisible(true);
    }
//
//    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln(MyTrace.myInd() + MyTrace.myGetMethodName() + ": just set visible" + ": ctrl= 0x" + Integer.toHexString(ctrl)
//      + MyTrace.myNlInd() + " dir= " + workingDirName
//      + MyTrace.myNlInd() + " file= " + (saveDataFile == null ? "null" : saveDataFile.getAbsolutePath()));
//
    editorPanel.mySetStartingFocusToTextArea();
//
    return editorPanel;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param ctrl  ?
 *
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


//------------------ Main Method ------------------
/**
  * This main method runs some tests.
  * 
  * @param args String[] of input arguments
  * 
  */
  public static void main( String[] args )
  {
    if ( args.length == 0 ) args = new String[] {
       SwingMyEditorConst.ARG_ALLOW_FILE_OPS,
       SwingMyEditorConst.ARG_ALLOW_READ_WRITE,
       SwingMyEditorConst.ARG_WORKING_DIR, "/tmp",
       SwingMyEditorConst.ARG_TITLE, "Editor Test"
       };
    SwingMyEditorZ03Handler.myInitialize(args, false);
//    SwingMyEditorZ03Handler.myInitialize(args, true);
  } //End: Method


} //End: class SwingMyEditorZ00Handler

