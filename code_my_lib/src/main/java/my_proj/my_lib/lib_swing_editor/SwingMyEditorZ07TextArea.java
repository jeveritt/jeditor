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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_encrypt.call_gpg.MyWriteOrReadGpgFile;
import my_proj.my_lib.lib_swing.SwingMyJDialogForPassword;
import my_proj.my_lib.lib_swing.SwingMyStandardFonts;


//------------------  CLASS: SwingMyEditorZ05TextArea  ------------------
/**
 * This class extends JTextArea. It allows you to edit that text.
 * It also links the text to a file, allowing you to save the edited text.
 * It also allows you to set/get key/value pairs separated by "=" or " ".
 * It also supports a call back function which notifies when the text is saved.
 *
 * @author James Everitt
 */
public final class SwingMyEditorZ07TextArea extends JTextArea {
  
//private static final boolean         DO_TRACE = true;

/** Serial UID */
  private static final long serialVersionUID = -7941194344589361384L;

/** Enum list of edit modes */
          static enum MY_MODES { cmd, ins }

/** Edit mode */
  private MY_MODES                     myCurrentMode = MY_MODES.ins;
  
/** Setup control int */
  private int                          myCtrl = 0;
 
/** Saved data file */
  private File                         mySavedDataFile = null;
  
/** Boolean indicating if text has changed */
  private boolean                      myChanged = false;
  
/** Boolean indicating if a file save was done */
  private boolean                      myDidFileSave = false;
  
/** Boolean indicating if saving files is allowed */
  private boolean                      myAllowFileSave = false;
/** Boolean indicating if saving encrypted files is allowed */
  private boolean                      myAllowFileSaveAsEncrypted = false;
  
/** List of actions */
  private SwingMyEditorZ05Action       myActions = null;
  
/** Password String */
  private String                       myPassword;
  
/** Working directory name */
  private String                       myWorkingDirName = null;
  
/** Command line panel */
  private SwingMyEditorZ08CmdLine      myCmdLine = null;
  
/** Message line panel */
  private SwingMyEditorZ09MsgLine      myMsgLine = null;
  
/** Method handling keys that were typed */
  private SwingMyEditorZ10HandleKeyTyped myHandleKeyCmdTypedHandler = null;
  
/** Undo manager */
  private UndoManager                  myUndoManager = null;


//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This constructor method ?
 *
 * @param textDataAsString  ?
 * @param cmdLine  ?
 * @param msgLine  ?
 * @param ctrl  ?
 * @param password  ?
 * @param saveDataFile  ?
 * @param workingDirName  ?
 *
 * @throws Exception  Java standard exception
 */
  public SwingMyEditorZ07TextArea (
      String                  textDataAsString,
      SwingMyEditorZ08CmdLine cmdLine,
      SwingMyEditorZ09MsgLine msgLine,
      int                     ctrl,
      String                  password,
      File                    saveDataFile,
      String                  workingDirName
//,int dummy
  ) throws Exception
  {
    super(textDataAsString);
//
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl));
//
    this.myCmdLine = cmdLine;
    this.myMsgLine = msgLine;
    this.myCtrl = ctrl;
    this.myHandleKeyCmdTypedHandler = new SwingMyEditorZ10HandleKeyTyped(this, this.myMsgLine);
//
// Might want to prevent saving back encrypted files as plain text
    this.myAllowFileSave = (this.myCtrl & SwingMyEditorConst.MY_ALLOW_UNENCRYP_WRITE) != 0;
    this.myAllowFileSaveAsEncrypted = (this.myCtrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_WRITE) != 0;
//
    this.myChanged = false;
    this.myDidFileSave = false;
    this.myPassword = password;
    this.myWorkingDirName = workingDirName;
    this.myMsgLine.mySetWritable(this.myAllowWrite());
//
    this.mySavedDataFile = saveDataFile;
// If can't write then force into cmd mode
    boolean canWrite = (SwingMyEditorZ07TextArea.this.myCtrl & SwingMyEditorConst.MY_ALLOW_ANY_WRITE) != 0;
    if ( !canWrite ) this.mySetMode(MY_MODES.cmd);
    else this.mySetMode(MY_MODES.ins);
//
    super.setFont(SwingMyEditorConst.MY_FONT);
//
    super.setMargin( new Insets(2,6,2,6) );  // top, left, bottom, right
//
    super.setCaretColor(Color.red);
//
    super.addKeyListener(
        new KeyListener() {
          @Override public void keyTyped(KeyEvent e) {
            boolean canWrite = (SwingMyEditorZ07TextArea.this.myCtrl & SwingMyEditorConst.MY_ALLOW_ANY_WRITE) != 0;
            SwingMyEditorZ07TextArea.this.myHandleKeyTyped(e, canWrite);
          }
          @Override public void keyPressed(KeyEvent e) { }
          @Override public void keyReleased(KeyEvent e) {
            boolean canWrite = (SwingMyEditorZ07TextArea.this.myCtrl & SwingMyEditorConst.MY_ALLOW_ANY_WRITE) != 0;
            if( SwingMyEditorZ07TextArea.this.myCurrentMode == MY_MODES.cmd ) SwingMyEditorZ07TextArea.this.myHandleKeyReleasedInCommandMode(e, canWrite);
          }
        }
    );
//
    super.setFont(SwingMyStandardFonts.myGetStandardMonospacedFont());
//
    super.addMouseListener (
        new MouseListener() {
          @Override public void mouseClicked  (MouseEvent e) { if (e.isPopupTrigger()) SwingMyEditorZ07TextArea.this.myHandlePopup(e); }
          @Override public void mouseEntered  (MouseEvent e) { }
          @Override public void mouseExited   (MouseEvent e) { }
          @Override public void mousePressed  (MouseEvent e) { if (e.isPopupTrigger()) SwingMyEditorZ07TextArea.this.myHandlePopup(e); }
          @Override public void mouseReleased (MouseEvent e) { if (e.isPopupTrigger()) SwingMyEditorZ07TextArea.this.myHandlePopup(e); }
        }
    );
//
    this.myActions = new SwingMyEditorZ05Action( ctrl, this );
//
// Add undo manager
    this.myUndoManager = new UndoManager();
    this.getDocument().addUndoableEditListener(this.myUndoManager);
//
//    if ( DO_TRACE ) System.out.print(this.myGetInfoString(" ", "Program state at end of constructor", null));
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param e  ?
 */
  private final void myHandleKeyTyped (KeyEvent e, boolean canWrite)
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": keyEvent= " + e);
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": editable= " + this.isEditable() + ": mode= " + this.myCurrentMode + ": keyEvent= <" + e.getKeyChar() + ">" );
// non-editable mode seems to turn off caret by default
    this.getCaret().setVisible(true);
//
    this.myMsgLine.mySetMsg(null);
    char keyChar = e.getKeyChar();
// If escape key typed go to command mode
    if ( keyChar == KeyEvent.VK_ESCAPE ) this.mySetMode(MY_MODES.cmd);
// Else if already in command mode, handle command key
    else if ( this.myCurrentMode == MY_MODES.cmd ) this.myHandleKeyTypedInCommandMode ( keyChar, canWrite );
// Else in insert mode and need to acknowledge that key was typed
    else this.myChanged = true;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method handles key commands typed into the text area in the command mode.
 * Some key commands will be handled locally, most will be forwarded.
 *
 * @param keyChar  Key that was typed
 */
  private final void myHandleKeyTypedInCommandMode ( char keyChar, boolean canWrite )
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": keyChar= " + keyChar);
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": editable= " + this.isEditable() + ": mode= " + this.myCurrentMode + ": key= " + keyChar );
//
// If go to command line
    if ( this.myCmdLine != null && keyChar == ':' ) mySetCaretTo(this.myCmdLine);
// Else if handle undo
    else if ( keyChar == 'u' ) { if ( canWrite && this.myUndoManager.canUndo() ) this.myUndoManager.undo(); }
// Else if not going back to insert mode go to sub-routine
    else if ( keyChar != 'i' && keyChar != 'a' ) this.myHandleKeyCmdTypedHandler.myHandleCommandKey( keyChar, canWrite );
  } //End: Method

  
//------------------  Method  ------------------
/**
 * This method handles key commands typed into the text area in the command mode
 * that must be processed at key released
 *
 * @param keyChar  Key that was typed
 */
  private final void myHandleKeyReleasedInCommandMode ( KeyEvent e, boolean canWrite )
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": keyEvent= " + e);
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": editable= " + this.isEditable() + ": mode= " + this.myCurrentMode + ": keyEvent= <" + e.getKeyChar() + ">" );
//
    char keyChar = e.getKeyChar();
// Important to exit command mode here so that 'i' is not typed.
    if ( keyChar == 'i' || keyChar == 'a' ) if ( canWrite ) this.mySetMode(MY_MODES.ins);
    if ( keyChar == 'a' ) {
        if ( canWrite ) super.setCaretPosition(super.getCaretPosition()+1);
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param needToWrite  ?
 */
  final void mySetNeedToWrite( boolean needToWrite ) { this.myChanged = needToWrite; }


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param isEncrypted  ?
 *
 * @throws Exception  Java standard exception
 */
  private final void myOpenFileSub( boolean isEncrypted ) throws Exception
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": workingDir= " + this.myWorkingDirName);
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    Container topWind = this; // SwingMyEditorZ11Misc.myGetParentWindow(this);
    while ( topWind.getParent() != null ) topWind = topWind.getParent();
    JFileChooser fileChooser = new JFileChooser(this.myWorkingDirName);
    int chooseInt = fileChooser.showDialog(topWind, "select file to open");
    File newFile = (chooseInt == JFileChooser.APPROVE_OPTION) ? fileChooser.getSelectedFile() : null;
//
    if ( newFile != null && newFile.isFile() ) {
      this.myAskToSaveFileIfNecessary();
      this.mySavedDataFile = newFile;
// Get data as ArrayList<String>
      ArrayList<String> lst;
      if ( isEncrypted ) lst = MyWriteOrReadGpgFile.myReadDecryptedFile(newFile.getAbsolutePath(), this.myPassword, null);
      else lst = SwingMyEditorZ11Misc.myGetFileContentsAsArrayList(newFile, null);
// Convert to single String
      String dataAsStr = SwingMyEditorZ11Misc.myGetFileContentsAsString(lst, "");
// Update text area
      super.setText(dataAsStr);
    } //End: if ()
//
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @throws Exception  Java standard exception
 */
  final void myOpenEncryptedFile( ) throws Exception {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
    this.myOpenFileSub(true);
 }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @throws Exception  Java standard exception
 */
  final void myOpenFile( ) throws Exception {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
    this.myOpenFileSub(false);
  }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param file  ?
 */
  public final void mySetDataFile( File file ) {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    this.mySavedDataFile = file;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param mode  ?
 */
  private final void mySetMode ( MY_MODES mode ) {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": mode= " + mode);
//
    this.myCurrentMode = mode;
    this.myMsgLine.mySetMode(mode);
    if ( mode == MY_MODES.ins ) {
      super.setEditable( this.myAllowWrite() );
      mySetCaretTo(this);
    }
    else {
      super.setEditable( false );
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param comp  ?
 */
  static final void mySetCaretTo ( JTextComponent comp ) { comp.requestFocusInWindow(); comp.getCaret().setVisible(true); }


//------------------  Method  ------------------
/**
 * This method determines if a file can be written
 *
 * @return
 */
  private final boolean myAllowWrite( ) {
    boolean writable = (
                         (this.myPassword != null && (this.myCtrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_WRITE) != 0 ) ||
                         (this.myPassword == null && (this.myCtrl & SwingMyEditorConst.MY_ALLOW_UNENCRYP_WRITE) != 0 )
                       );
    return writable;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method sets the password
 *
 */
  public final void mySetPassword( )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    Container topWind = this; // SwingMyEditorZ11Misc.myGetParentWindow(this);
    while ( topWind.getParent() != null ) topWind = topWind.getParent();
    String password = SwingMyJDialogForPassword.myGetPassword( (Window)topWind );
    if ( password != null && password.length() > 0 ) this.myPassword = password;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param msg  ?
 */
  private final void myShowMessage( String msg )
  {
    Container fr = this; // SwingMyEditorZ11Misc.myGetParentWindow(this);
    while ( fr.getParent() != null ) fr = fr.getParent();
    if ( fr != null && fr.isVisible() ) JOptionPane.showMessageDialog(fr, msg);
    else System.out.println( MyTrace.myGetMethodName() + ": msg= ");
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @throws Exception  Java standard exception
 */
  public final void mySaveFile( ) throws Exception
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": no args");
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    this.mySaveFile(
        this.myAllowFileSaveAsEncrypted,
        (this.mySavedDataFile == null ? null : this.mySavedDataFile.getAbsolutePath()),
        SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED,
        false);
  }

//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param saveAsEncrypted  ?
 *
 * @throws Exception  Java standard exception
 */
  final void mySaveFile( boolean saveAsEncrypted ) throws Exception
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": saveEnc= " + saveAsEncrypted);
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    this.mySaveFile(
        saveAsEncrypted,
        (this.mySavedDataFile == null ? null : this.mySavedDataFile.getAbsolutePath()),
        SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED,
        false
        );
  }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @throws Exception  Java standard exception
 */
  final void mySaveFileAs( ) throws Exception
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName());
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    if ( !this.myAllowFileSave && !this.myAllowFileSaveAsEncrypted ) return;
//
    Container topWind = this;  // SwingMyEditorZ11Misc.myGetParentWindow(this);
    while ( topWind.getParent() != null ) topWind = topWind.getParent();
    JFileChooser fileChooser = new JFileChooser(this.myWorkingDirName);
    int chooseInt = fileChooser.showDialog(topWind, "select file to save data in");
    File newFile = (chooseInt == JFileChooser.APPROVE_OPTION) ? fileChooser.getSelectedFile() : null;
    String path = ( newFile == null ) ? null : newFile.getAbsolutePath();
//
    if ( path != null ) {
      File tmp = this.mySaveFile( this.myAllowFileSaveAsEncrypted, path, SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED, true); 
      if ( tmp != null && this.mySavedDataFile == null ) this.mySavedDataFile = tmp;
    }
  } //End: Method


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
  private final File mySaveFile( boolean saveAsEncrypted, String fileName,  int numbOldToSave, boolean cameFromSaveFileAs ) throws Exception
  {
//if(DO_TRACE)MyTrace.myPrintln( MyTrace.myGetMethodName() + ": encr= " + saveAsEncrypted + ": savedOld= " + numbOldToSave + "\n file= " + fileName);
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
// If file name not specified, ask
    if ( fileName == null && !cameFromSaveFileAs ) mySaveFileAs( );
// Return if not enough info or save not allowed
    if ( fileName == null ||
          ( saveAsEncrypted && !this.myAllowFileSaveAsEncrypted ) ||
          ( !saveAsEncrypted && !this.myAllowFileSave ) ) return null;
//
    if ( saveAsEncrypted && !fileName.endsWith(SwingMyEditorConst.MY_ENCRYPT_EXTENSION) ) fileName += SwingMyEditorConst.MY_ENCRYPT_EXTENSION;
// Handle old file
    File oldFile = new File(fileName);
    if ( oldFile.exists() ) {
      if ( numbOldToSave > 0 ) SwingMyEditorZ11Misc.myMoveFileToOldSimpler(oldFile, numbOldToSave);
      else oldFile.delete();
    }
// New file
    File newFile = new File(fileName);
// Save as un-encrypted
    if ( !saveAsEncrypted ) {
      BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
      out.write(this.getText());
      out.close();
    }
// Or save as encrypted
    else {
//  Handle password
      if ( this.myPassword == null ) this.myPassword = MyWriteOrReadGpgFile.myGetStandardPas();
      if ( this.myPassword == null ) {
        Container fr = this; // SwingMyEditorZ11Misc.myGetParentWindow(this);
        while ( fr.getParent() != null ) fr = fr.getParent();
        this.myPassword = SwingMyJDialogForPassword.myGetPassword((Window)fr);
      }
//  Do write
      MyWriteOrReadGpgFile.myWriteEncryptedFile(
         fileName,       // String outputFileName,
         this.getText(), // String data,
         this.myPassword // String passwordOrNull
          );
    }
// Note new status
    this.myChanged = false;
    this.myDidFileSave = true;
// Return result
    return newFile;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @throws Exception  Java standard exception
 */
  private final void myAskToSaveFileIfNecessary ( ) throws Exception
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": hasChanged= " + this.myChanged );
    if ( this.myChanged ) {
      Container fr = this;
      while ( fr.getParent() != null ) fr = fr.getParent();
      int closingArg;
      if ( fr != null && fr.isVisible() ) closingArg = JOptionPane.showConfirmDialog(fr, "Save modified file?");
      else closingArg = JOptionPane.YES_OPTION;
      if ( closingArg == JOptionPane.YES_OPTION ) this.mySaveFile();
    }
  } //End: Method


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
     String text = this.getText();
     int strt = text.indexOf(oldTxt);
     int stop = text.indexOf("\n", strt+1);
//
     if (strt >= 0 ) this.replaceRange(newTxt,strt,stop);
     else this.append(newTxt);
//
     this.myChanged = true;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  final void myCloseFile( )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
    try {
      this.myAskToSaveFileIfNecessary();
      this.setText("");
    }
    catch ( Exception e ) {
      this.myShowMessage( "SwingMyEditorZ05TextArea.myCloseFile" + ": exc= " + e.getMessage() );
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  final void myCloseWindow( )
  {
//    if (DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": fileToSave= " + this.mySavedDataFile );
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    Container topContainer = this;  // SwingMyEditorZ11Misc.myGetParentWindowOrInternalFrame(this);
    while ( topContainer.getParent() != null ) topContainer = topContainer.getParent();
//
    if ( topContainer instanceof Window ) {
      this.myCloseFile();
      Window wind = (Window)topContainer;
//      wind.setVisible(false);
      wind.dispose();
    }
    else if ( topContainer instanceof JInternalFrame ) {
      this.myCloseFile();
      JInternalFrame intFr = (JInternalFrame)topContainer;
//      intFr.setVisible(false);
      intFr.dispose();
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return    Returns allowed actions
 */
  public final SwingMyEditorZ05Action myGetActions () { return this.myActions; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  public final File myGetDataFile( ) { return this.mySavedDataFile; }


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
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 *
 */
  public final SwingMyEditorZ10HandleKeyTyped myGetHandleKeyTyped() { return this.myHandleKeyCmdTypedHandler; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 *
 */
  public final boolean myGetHasChanged () { return this.myChanged; }


//------------------  Method  ------------------
/**
 * This method returns info about program status.
 *
 * @param ind  Amount to indent
 * @param title  First line title or null
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
    sb.append( ind + "file             = " + ( this.mySavedDataFile == null ? "null" : this.mySavedDataFile.getAbsolutePath() ) + "\n" );
    sb.append( ind + "file changed     = " + this.myChanged + "\n" );
    sb.append( ind + "did save         = " + this.myDidFileSave + "\n" );
    sb.append( ind + "allow unenc save = " + this.myAllowFileSave + "\n" );
    sb.append( ind + "allow enc save   = " + this.myAllowFileSaveAsEncrypted+ "\n" );
    sb.append( ind + "working dir      = " + this.myWorkingDirName + "\n" );
    sb.append( ind + "text is editable = " + this.isEditable() + "\n" );
//    SwingMyEditorConst.myGetEditorConstInfo( this.myCtrl, sb, ind);
//
    return sb.toString();
  } //End: Method


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
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  final void myHandleCopy ()
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    String val = this.getSelectedText();
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//
    if ( val == null || val.length() == 0 ) this.myMsgLine.mySetMsg("nothing selected");
    else if ( clipboard == null ) this.myMsgLine.mySetMsg("system clipboard not available");
    else {
      StringSelection strSel = new StringSelection(val);
      clipboard.setContents(strSel, strSel);
      this.myMsgLine.mySetMsg("copied selected to system clipboard");
    } //End: if ()
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
//  final void myHandleSetVisible ()
//  {
////if(DO_TRACE)System.out.println( "\n" + MyTrace.myGetMethodName() + ": isVisible= " + this.isVisible());
//
//    this.mySetMode(MY_MODES.ins);
//  } //End: Method


} //End: class SwingMyEditorZ05TextArea

