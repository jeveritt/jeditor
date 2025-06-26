// SwingMyEditorZ05TextArea.java
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.Caret;
import javax.swing.undo.UndoManager;

import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_encrypt.call_gpg.MyWriteOrReadGpgFile;
import my_proj.my_lib.lib_swing.SwingMyJDialogForPassword;
import my_proj.my_lib.lib_swing.SwingMyStandardFonts;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//---------------------  CLASS: SwingMyEditorZ05TextArea  ---------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class extends JTextArea. It allows you to edit that text.
 * It also links the text to a file, allowing you to save the edited text.
 * It also allows you to set/get key/value pairs separated by "=" or " ".
 * It also supports a call back function which notifies when the text is saved.
 *
 * @author James Everitt
 */
public final class SwingMyEditorZ07TextAreaHolder
{
  
//private static final boolean         DO_TRACE = true;

/** Enum list of edit modes: MY_MODES = cmd | ins */
          static enum MY_MODES {
/** cmd => in command mode */
            cmd,
/** ins => in text insert mode */
            ins 
            }
          
/** Text area */
  private JTextArea                     myTextArea = null;
  
/** Edit mode */
  private MY_MODES                      myCurrentMode = MY_MODES.ins;
  
/** Saved data file */
  private String                        mySavedDataFileName = null;
  
/** Boolean indicating if text has changed: Note: always use methods to change this */
  private boolean                       myHasChanged = false;
  
/** Boolean indicating if a file save was done */
  private boolean                       myDidFileSave = false;
  
/** List of actions */
          SwingMyEditorZ05Action        myActions = null;
  
/** Password String */
  private String                        myPassword = null;
  
/** Working directory name */
  private String                        myWorkingDirName = null;
  
/** Top panel */
  private SwingMyEditorZ04JPanelHolder  myTopPanelHolder = null;
  
/** Method handling keys that were typed */
  private SwingMyEditorZ08KeyTypedInJTextArea myHandleKeyCmdTypedHandler = null;
  
/** Undo manager */
  private UndoManager                   myUndoManager = null;
  
/** ArrayList&lt;int&gt; of line start and \n positions */
  private ArrayList<int[]>              myLineNumAndStrtPosAndNlPos = null;


//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------------------------------------------------------------
//--------------------  Setter/Getter Methods:  --------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  Returns allowed actions
 */
  public final SwingMyEditorZ05Action myGetActions () { return this.myActions; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  public final JTextArea myGetJTextArea () { return this.myTextArea; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  public final String myGetDataFileName( ) { return this.mySavedDataFileName; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 *
 */
  public final SwingMyEditorZ08KeyTypedInJTextArea myGetHandleKeyTyped() { return this.myHandleKeyCmdTypedHandler; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 *
 */
  public final boolean myGetTextHasChanged () { return this.myHasChanged; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 *
 */
  public final UndoManager myGetUndoManager () { return this.myUndoManager; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param file  ?
 */
  public final void mySetDataFileName( String fileName ) { this.mySavedDataFileName = fileName; }


//------------------  Method  ------------------
/**
 * This method sets/clears the myHasChanged boolean.
 *
 * @param hasChanged  Set to true/false to change variable
 */
  final void mySetTextHasChanged( boolean hasChanged )
  {
    this.myHasChanged = hasChanged;
    if ( hasChanged ) this.myLineNumAndStrtPosAndNlPos = null;
  }


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This constructor method ?
 *
 * @param textDataAsString  ?
 * @param topPanelHolder  ?
 * @param ctrl  ?
 * @param password  ?
 * @param saveDataFile  ?
 * @param workingDirName  ?
 *
 * @throws Exception  Java standard exception
 */
  public SwingMyEditorZ07TextAreaHolder (
      String                  textDataAsString,
      SwingMyEditorZ04JPanelHolder topPanelHolder,
      String                  password,
      String                  saveDataFileName,
      String                  workingDirName
//,int dummy
  ) throws Exception
  {
    this.myTextArea = new JTextArea(textDataAsString);
    this.myTextArea.setFocusable(true);
//
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl));
//
    this.myTopPanelHolder = topPanelHolder;
//
    SwingMyEditorZ14MsgLine msgLine = this.myTopPanelHolder.myGetMsgLine();
//  
    this.myHandleKeyCmdTypedHandler = new SwingMyEditorZ08KeyTypedInJTextArea(this.myTopPanelHolder);
//
// Might want to prevent saving back encrypted files as plain text
//    this.myAllowFileSave = (this.myCtrl & SwingMyEditorCtrl.MY_ALLOW_UNENCRYP_WRITE) != 0;
// Be liberal in allowing saving as encrypted
//    this.myAllowFileSaveAsEncrypted = this.myAllowFileSave || (this.myCtrl & SwingMyEditorCtrl.MY_ALLOW_ENCRYP_WRITE) != 0;
//
    this.mySetTextHasChanged(false);
    this.myDidFileSave = false;
    this.myPassword = password;
    this.myWorkingDirName = workingDirName;
    msgLine.mySetWritable( SwingMyEditorCtrl.canWriteFile );
//
    this.mySavedDataFileName = saveDataFileName;
// If can't write then force into cmd mode
    boolean canWrite = SwingMyEditorCtrl.canWriteFile; // (SwingMyEditorZ07TextAreaHolder.this.myCtrl & SwingMyEditorCtrl.MY_ALLOW_ANY_WRITE) != 0;
    if ( !canWrite ) this.mySetMode(MY_MODES.cmd);
    else this.mySetMode(MY_MODES.ins);
//
    this.myTextArea.setFont(SwingMyEditorConst.MY_FONT);
//
    this.myTextArea.setMargin( new Insets(2,6,2,6) );  // top, left, bottom, right
//
    this.myTextArea.setCaretColor(Color.red);
    this.myTextArea.setCaretPosition(0);
    this.myTextArea.getCaret().setBlinkRate(0);
//
    this.myTextArea.addKeyListener(
        new KeyListener() {
          @Override public void keyTyped(KeyEvent e) { SwingMyEditorZ07TextAreaHolder.this.myHandleKeyTyped(e); }
          @Override public void keyPressed(KeyEvent e) { }
          @Override public void keyReleased(KeyEvent e) { SwingMyEditorZ07TextAreaHolder.this.myHandleKeyReleasedInCommandMode(e); }
        }
    );
//
    this.myTextArea.setFont(SwingMyStandardFonts.myGetStandardMonospacedFont());
//
    this.myTextArea.addMouseListener (
        new MouseListener() {
          @Override public void mouseClicked  (MouseEvent e) {
            JTextArea txtArea = SwingMyEditorZ07TextAreaHolder.this.myTextArea;
            txtArea.grabFocus();  // txtArea.requestFocusInWindow();
            if (e.isPopupTrigger()) SwingMyEditorZ07TextAreaHolder.this.myHandlePopup(e);
          }
          @Override public void mouseEntered  (MouseEvent e) { }
          @Override public void mouseExited   (MouseEvent e) { }
          @Override public void mousePressed  (MouseEvent e) { if (e.isPopupTrigger()) SwingMyEditorZ07TextAreaHolder.this.myHandlePopup(e); }
          @Override public void mouseReleased (MouseEvent e) { if (e.isPopupTrigger()) SwingMyEditorZ07TextAreaHolder.this.myHandlePopup(e); }
        }
    );
//
    this.myTextArea.addFocusListener (
        new FocusListener() {
          @Override public void focusGained(FocusEvent arg0) { Caret car = SwingMyEditorZ07TextAreaHolder.this.myTextArea.getCaret(); car.setVisible(true); }
          @Override public void focusLost(FocusEvent arg0)   { Caret car = SwingMyEditorZ07TextAreaHolder.this.myTextArea.getCaret(); car.setVisible(true); }
        }
    );
//
    this.myActions = new SwingMyEditorZ05Action( topPanelHolder );
//
// Add undo manager
    this.myUndoManager = new UndoManager();
    this.myTextArea.getDocument().addUndoableEditListener(this.myUndoManager);
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param e  ?
 */
  final void myHandleKeyTyped (KeyEvent e)
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": keyEvent= " + e);
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": editable= " + this.myTextArea.isEditable() + ": mode= " + this.myCurrentMode + ": keyEvent= <" + e.getKeyChar() + ">" );
//
    try {
//
      boolean canWrite = SwingMyEditorCtrl.canWriteFile;
//
      this.myTopPanelHolder.myGetMsgLine().mySetMsg(null);
      char keyChar = e.getKeyChar();
// If escape key typed go to command mode
      if ( keyChar == KeyEvent.VK_ESCAPE ) { this.mySetMode(MY_MODES.cmd); this.myHandleKeyCmdTypedHandler.myReset(); }
// Else if already in command mode, handle command key
      else if ( this.myCurrentMode == MY_MODES.cmd ) {
        SwingMyEditorZ10CmdLineHolder cmdLine = this.myTopPanelHolder.myGetCmdLineHolder();
// If go to command line
        if ( cmdLine != null && keyChar == ':' ) { cmdLine.myGetJTextField().grabFocus(); this.myHandleKeyCmdTypedHandler.myReset(); }
// Handle multiple key commands
        else this.myHandleKeyCmdTypedHandler.myHandle02CommandKey( keyChar, canWrite );
      }
// Else in insert mode and need to acknowledge that key was typed
      else this.mySetTextHasChanged( true );
    }
    catch (Exception e2) {
      e2.printStackTrace();
    }
  } //End: method

  
//------------------  Method  ------------------
/**
 * This method handles key commands typed into the text area in the command mode
 * that must be processed at key released
 *
 * @param keyChar  Key that was typed
 */
  private final void myHandleKeyReleasedInCommandMode ( KeyEvent e )
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": keyEvent= " + e);
    if( SwingMyEditorZ07TextAreaHolder.this.myCurrentMode == MY_MODES.cmd ) {
      if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": editable= " + this.myTextArea.isEditable() + ": mode= " + this.myCurrentMode + ": keyEvent= <" + e.getKeyChar() + ">" );
//
      boolean canWrite = SwingMyEditorCtrl.canWriteFile;
      char keyChar = e.getKeyChar();
// Important to exit command mode here so that 'i' is not typed.
      StringBuffer cmdSB = this.myHandleKeyCmdTypedHandler.myCmdSB;
      if ( cmdSB != null && cmdSB.length() == 1 ) {
        if ( keyChar == 'i' || keyChar == 'a' ) { if ( canWrite ) this.mySetMode(MY_MODES.ins); this.myHandleKeyCmdTypedHandler.myReset(); }
        if ( keyChar == 'a' ) { if ( canWrite ) this.myTextArea.setCaretPosition(this.myTextArea.getCaretPosition()+1); }
      }
    } //ENd: if ()
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param isEncrypted  ?
 *
 * @throws Exception  Java standard exception
 */
  final void myOpenFile( ) throws Exception
  {
//if(DO_TRACE) System.out.println( "\n" + MyTrace.myGetMethodName() + ": workingDir= " + this.myWorkingDirName );
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    Container topWind = this.myTextArea; // SwingMyEditorZ11Misc.myGetParentWindow(this);
    while ( topWind.getParent() != null ) topWind = topWind.getParent();
    JFileChooser fileChooser = new JFileChooser(this.myWorkingDirName);
    int chooseInt = fileChooser.showDialog(topWind, "select file to open");
    File newFile = (chooseInt == JFileChooser.APPROVE_OPTION) ? fileChooser.getSelectedFile() : null;
//
    if ( newFile != null && newFile.isFile() ) {
      this.myAskToSaveFileIfNecessary();
      this.mySavedDataFileName = newFile.getAbsolutePath();
      boolean useEncrypt = SwingMyEditorCtrl.doEncrypt.isSelected() || newFile.getName().endsWith(SwingMyEditorConst.MY_ENCRYPT_EXTENSION);
// Check on password
      if ( this.myPassword == null && useEncrypt ) {
        this.myPassword = MyWriteOrReadGpgFile.myGetStandardPas();
        if ( this.myPassword == null ) {
          Container fr = this.myTextArea; // SwingMyEditorZ11Misc.myGetParentWindow(this);
          while ( fr.getParent() != null ) fr = fr.getParent();
          this.myPassword = SwingMyJDialogForPassword.myGetPassword((Window)fr);
        }
      }
// Get data as ArrayList<String>
      ArrayList<String> lst;
      if ( useEncrypt && this.myPassword != null ) lst = MyWriteOrReadGpgFile.myReadDecryptedFile(newFile.getAbsolutePath(), this.myPassword, null);
      else lst = SwingMyEditorZ16Misc.myGetFileContentsAsArrayList(newFile, null);
// Convert to single String
      String dataAsStr = SwingMyEditorZ16Misc.myGetFileContentsAsString(lst, "");
// Update text area
      this.myTextArea.setText(dataAsStr);
// Reset undo manager
      this.myUndoManager.discardAllEdits();
// Set caret at top
      this.myTextArea.setCaretPosition(0);
    } //End: if ()
//
  } //End: method


//------------------  Method  ------------------
/**
 * This method returns a ArrayList&lt;int&gt; with each element consisting of:
 * <pre>
 *    0 -> line number from 0
 *    1 -> line start position within the text
 *    2 -> line NL char position within the text or line length within the text.
 *  </pre>
 * It gets cleared every time a change to the text is made and must then be re-created.
 *
 * @Return  Returns the ArrayList
 */
  final ArrayList<int[]> myGetTextArrayList()
  {
    if ( this.myLineNumAndStrtPosAndNlPos == null ) {
      String txt = this.myTextArea.getText();
      int len = txt == null ? 0 : txt.length();
//
      if ( len > 0 ) {
        this.myLineNumAndStrtPosAndNlPos = new ArrayList<int[]>();
        int lineIndx = 0;
        int[] next = new int[] {lineIndx, 0, 0} ;
        for ( int i1=0 ; i1<len ; i1++ ) {
          char ch = txt.charAt(i1);
          if ( ch == '\n' ) {
            next[2] = i1;
            this.myLineNumAndStrtPosAndNlPos.add( next );
            next = new int[] { ++lineIndx, i1+1, 0 };
          }
          else if ( i1 == len - 1 )  {
            next[2] = i1 + 1;
            this.myLineNumAndStrtPosAndNlPos.add( next );
          }
        }
      } //End: else
    } //End: if ()
    return this.myLineNumAndStrtPosAndNlPos;
  } //End: method


//------------------  Method  ------------------
/**
 * This method returns a &lt;int&gt; representing the line number, start index, and new line index that contains a position within the line
 * 
 * @param positionWithinJTextArea Position within full JTextArea text of desired line
 *
 * @Return  Returns &lt;int&gt; of line
 */
  final int[] myGetLineNumStrtNlAry ( int positionWithinJTextArea )
  {
    int[] retVal = null;
    ArrayList<int[]> lst = this.myGetTextArrayList();
//if(DO_TRACE)System.out.print( "\n" + MyTrace.myGetMethodName() + ": position= " + positionWithinJTextArea + " : lines\n" + SwingMyEditorZ16Misc.myLineInfoToString(this.myTextArea.getText(), lst) );
    if ( lst != null ) {
      if ( positionWithinJTextArea < 0 ) retVal = lst.get(0);
      else {
        for ( int[] tmp : lst ) {
          if ( tmp[1] <= positionWithinJTextArea && tmp[2] >= positionWithinJTextArea ) { retVal = tmp; break; }
        }
        if ( retVal == null ) retVal = lst.get(lst.size()-1);
      }
    }
//if(DO_TRACE)System.out.println( MyTrace.myGetMethodName() + ": retVal Ln= " + retVal[0] );
    return retVal;
  } //End: method


//------------------  Method  ------------------
/**
 * This method returns line
 *
 * @Return  Returns line
 */
  final String myGetLine ( int lineNumbFrm0 )
  {
    ArrayList<int[]> aryLst = this.myGetTextArrayList();
    int[] ln = aryLst.get(lineNumbFrm0);
    String fullTxt = this.myGetJTextArea().getText();
    String retVal = fullTxt.substring(ln[1],ln[2]);
    return retVal;
  } //End: method

  
//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param mode  ?
 */
  final void mySetMode ( MY_MODES mode ) {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": mode= " + mode);
//
    this.myCurrentMode = mode;
    this.myTopPanelHolder.myGetMsgLine().mySetMode(mode);
    if ( mode == MY_MODES.ins ) {
      this.myTextArea.setEditable( SwingMyEditorCtrl.canWriteFile );
      this.myTextArea.grabFocus();   // this.myTextArea.requestFocusInWindow();
    }
    else {
      this.myTextArea.setEditable( false );
    }
  } //End: method


//------------------  Method  ------------------
/**
 * This method determines if a file can be written
 *
 * @return
 */
/*
  private final boolean myAllowWrite( ) {
    boolean writable = (
                         (this.myPassword != null && (this.myCtrl & SwingMyEditorCtrl.MY_ALLOW_ENCRYP_WRITE) != 0 ) ||
                         (this.myPassword == null && (this.myCtrl & SwingMyEditorCtrl.MY_ALLOW_UNENCRYP_WRITE) != 0 )
                       );
    return writable;
  } //End: method
*/

//------------------  Method  ------------------
/**
 * This method sets the password
 *
 */
  public final void mySetPassword( )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    Container topWind = this.myTextArea; // SwingMyEditorZ11Misc.myGetParentWindow(this);
    while ( topWind.getParent() != null ) topWind = topWind.getParent();
    String password = SwingMyJDialogForPassword.myGetPassword( (Window)topWind );
    if ( password != null && password.length() > 0 ) this.myPassword = password;
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  final String mySaveFileAskForName( )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName());
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
//    if ( !this.myAllowFileSave && !this.myAllowFileSaveAsEncrypted ) return;
//
    Container topWind = this.myTextArea;  // SwingMyEditorZ11Misc.myGetParentWindow(this);
    while ( topWind.getParent() != null ) topWind = topWind.getParent();
    JFileChooser fileChooser = new JFileChooser(this.myWorkingDirName);
    int chooseInt = fileChooser.showDialog(topWind, "select file to save data in");
    File newFile = (chooseInt == JFileChooser.APPROVE_OPTION) ? fileChooser.getSelectedFile() : null;
    String path = ( newFile == null ) ? null : newFile.getAbsolutePath();
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": newFile= " + newFile + ": path= " + path);
    return path;
//
//    if ( path != null ) {
//      File tmp = this.mySaveFile( this.myAllowFileSaveAsEncrypted, path, SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED, true); 
//      if ( tmp != null && this.mySavedDataFile == null ) this.mySavedDataFile = tmp;
//    }
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param saveAsEncrypted  ?
 * @param fileName  ?
 * @param numbOldToSave  ?
 * @param cameFromSaveFileAs  ?
 *
 * @return  ?
 *
 * @throws Exception  Java standard exception
 */
  final File mySaveFile( /*boolean saveAsEncrypted,*/ String fileName,  int numbOldToSave )
  {
    boolean saveAsEncrypted = SwingMyEditorCtrl.doEncrypt.isSelected();
//if(DO_TRACE)System.out.println("\n" +  MyTrace.myGetMethodName() + ": encr= " + saveAsEncrypted + ": savedOld= " + numbOldToSave + ": file= " + fileName);
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    try {
// If file name not specified, ask
      if ( fileName == null ) {
        fileName = this.mySaveFileAskForName( );
        this.mySavedDataFileName = fileName;
      }
//if(DO_TRACE)System.out.println( MyTrace.myGetMethodName() + ": must have fileName" + ": encr= " + saveAsEncrypted + ": savedOld= " + numbOldToSave + ": file= " + fileName);
// Return if not enough info or save not allowed
      if ( fileName == null || !SwingMyEditorCtrl.canWriteFile ) return null;
//
      if ( saveAsEncrypted && !fileName.endsWith(SwingMyEditorConst.MY_ENCRYPT_EXTENSION) ) fileName += SwingMyEditorConst.MY_ENCRYPT_EXTENSION;
// Handle old file
      File oldFile = new File(fileName);
      if ( oldFile.exists() ) {
        if ( numbOldToSave > 0 ) SwingMyEditorZ16Misc.myMoveFileToOldSimpler(oldFile, numbOldToSave);
        else oldFile.delete();
      }
// New file
      File newFile = new File(fileName);
// Save as un-encrypted
      if ( !saveAsEncrypted ) {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write(this.myTextArea.getText());
        out.close();
      }
// Or save as encrypted
      else {
//  Handle password
        if ( this.myPassword == null ) this.myPassword = MyWriteOrReadGpgFile.myGetStandardPas();
        if ( this.myPassword == null ) {
          Container fr = this.myTextArea; // SwingMyEditorZ11Misc.myGetParentWindow(this);
          while ( fr.getParent() != null ) fr = fr.getParent();
          this.myPassword = SwingMyJDialogForPassword.myGetPassword((Window)fr);
        }
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": doing enc" + ": pw= " + this.myPassword + ": file= " + fileName);
//  Do write
        MyWriteOrReadGpgFile.myWriteEncryptedFile(
           fileName,       // String outputFileName,
           this.myTextArea.getText(), // String data,
           this.myPassword // String passwordOrNull
            );
      }
// Note new status
      this.mySetTextHasChanged(false);
      this.myDidFileSave = true;
// Return result
      return newFile;
    } //End: try
    catch (Exception e) {
      SwingMyEditorZ16Misc.myShowDialog (
          this.myTextArea,                  // Component comp,
          "WARNING: File write error", // String title,
          "Could not write to " + fileName // String msg 
          );
      return null;
    }
    
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @throws Exception  Java standard exception
 */
  final int myAskToSaveFileIfNecessary ( ) throws Exception
  {
//if(true) new Exception(MyTrace.myGetMethodName()).printStackTrace();
//if(true)throw new Exception(MyTrace.myGetMethodName() + ": force error");
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": hasChanged= " +  this.myGetTextHasChanged() );
//
    int closingArg;
    if ( !this.myGetTextHasChanged() ) closingArg = JOptionPane.YES_OPTION;
    else {
      Container fr = this.myTextArea;
      while ( fr.getParent() != null ) fr = fr.getParent();
      if ( fr != null && fr.isVisible() ) closingArg =  JOptionPane.showConfirmDialog(fr, "Save File", "Save ?", JOptionPane.YES_NO_CANCEL_OPTION);
      else closingArg = JOptionPane.YES_OPTION;
      if ( closingArg == JOptionPane.YES_OPTION )
          this.mySaveFile( 
              this.mySavedDataFileName, // String fileName,
              SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED                // int numbOldToSave
              );
      this.mySetTextHasChanged(false);
    }
//
    return closingArg;
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param oldTxt  ?
 * @param newTxt  ?
 *
 */
  final void myReplaceLineStartingWithOrAdd( String oldTxt, String newTxt )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
     String text = this.myTextArea.getText();
     int strt = text.indexOf(oldTxt);
     int stop = text.indexOf("\n", strt+1);
//
     if (strt >= 0 ) this.myTextArea.replaceRange(newTxt,strt,stop);
     else this.myTextArea.append(newTxt);
//
     this.mySetTextHasChanged(true);
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  final void myCloseFile()
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
    try {
      this.myAskToSaveFileIfNecessary();
      this.myTextArea.setText("");
    }
    catch ( Exception e ) {
      SwingMyEditorZ16Misc.myShowDialog(
          this.myTextArea, // Component comp,
          "WARNING: Failed file close", //String title,
          "Failed to close file:\n   exc= " + e.getMessage() // String msg"
       );
    }
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param reset  ?
 *
 * @return  ?
 */
  public final boolean myGetDidFileSave ( boolean reset )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
    boolean retVal = this.myDidFileSave;
    if ( reset ) this.myDidFileSave = false;
    return retVal;
  } //End: method


//------------------  Method  ------------------
/**
 * This method does a specified number of undo's.
 * 
 * @param undoCnt  Number of undo's to implement
 */
  public final void myUndo ( int undoCnt )
  {
    while ( undoCnt-- > 0 && this.myUndoManager.canUndo() ) this.myUndoManager.undo();
    this.mySetTextHasChanged(true);
  } //End: method


//------------------  Method  ------------------
/**
 * This method returns info about program status.
 *
 * @param ind  Amount to indent
 * @param sb  StringBuffer to which info is added
 * 
 * @return  Return info string
 */
  final String myGetInfoString( String ind, StringBuffer sb )
  {
    if ( sb == null ) sb = new StringBuffer();
//
    sb.append(ind + this.getClass().getSimpleName() + " info:" + "\n");
    ind += "  ";
//
    sb.append( ind + "mode             = " + this.myCurrentMode + "\n" );
    sb.append( ind + "file             = " + this.mySavedDataFileName + "\n" );
    sb.append( ind + "file changed     = " + this.myGetTextHasChanged() + "\n" );
    sb.append( ind + "did save         = " + this.myDidFileSave + "\n" );
    sb.append( ind + "is encrypted     = " + SwingMyEditorCtrl.doEncrypt + "\n" );
    sb.append( ind + "allow save       = " + SwingMyEditorCtrl.canWriteFile + "\n" );
    sb.append( ind + "working dir      = " + this.myWorkingDirName + "\n" );
    sb.append( ind + "text is editable = " + this.myTextArea.isEditable() + "\n" );
//    SwingMyEditorConst.myGetEditorConstInfo( this.myCtrl, sb, ind);
//
    return sb.toString();
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param e  ?
 */
  final void myHandlePopup ( MouseEvent e )
  {
//    if (DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": fileToSave= " + this.mySavedDataFile );
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    JPopupMenu menu = SwingMyEditorZ06Menu.myCreatePopupMenu(null, this.myActions);
//
    Point lastPoint = e.getPoint();
    Component parent = e.getComponent();
    menu.show(parent, lastPoint.x, lastPoint.y);
//
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  final void myHandleCopy ()
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    String val = this.myTextArea.getSelectedText();
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//
    if ( val == null || val.length() == 0 ) this.myTopPanelHolder.myGetMsgLine().mySetMsg("nothing selected");
    else if ( clipboard == null ) this.myTopPanelHolder.myGetMsgLine().mySetMsg("system clipboard not available");
    else {
      StringSelection strSel = new StringSelection(val);
      clipboard.setContents(strSel, strSel);
      this.myTopPanelHolder.myGetMsgLine().mySetMsg("copied selected to system clipboard");
    } //End: if ()
  } //End: method


} //End: class SwingMyEditorZ05TextArea

