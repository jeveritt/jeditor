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
  
//  private static final boolean         DO_TRACE = true;
  
          static enum MY_MODES { COMMAND, INSERT }

  private static final long            serialVersionUID = 0;
  
  private MY_MODES                     myCurrentMode = MY_MODES.INSERT;
  
  private File                         mySavedDataFile = null;
  
  private boolean                      myChanged = false;
  
  private boolean                      myDidFileSave = false;
  
  private boolean                      myAllowFileSave = false;
  private boolean                      myAllowFileSaveAsEncrypted = false;
  
  private SwingMyEditorZ05Action       myActions = null;
  
  private String                       myPassword;
  
  private String                       myWorkingDirName = null;
  
  private SwingMyEditorZ08CmdLine      myCmdLine = null;
  
  private SwingMyEditorZ09MsgLine      myMsgLine = null;
  
  private SwingMyEditorZ10HandleKeyTyped myHandleKeyCmdTypedHandler = null;
  
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
 * @param dataAsBufLine  ?
 * @param ctrl  ?
 * @param password  ?
 * @param saveDataFile  ?
 * @param workingDirName  ?
 *
 * @throws Exception  Java standard exception
 */
  public SwingMyEditorZ07TextArea (
      String    textDataAsString,
      int       ctrl,
      String    password,
      File      saveDataFile,
      String    workingDirName
//,int dummy
  ) throws Exception
  {
    super(textDataAsString);
//
// Might want to prevent saving back encrypted files as plain text
    this.myAllowFileSave = (ctrl & SwingMyEditorConst.MY_ALLOW_UNENCRYP_WRITE) != 0;
    this.myAllowFileSaveAsEncrypted = (ctrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_WRITE) != 0;
//
    this.myChanged = false;
    this.myDidFileSave = false;
    this.myPassword = password;
    this.myWorkingDirName = workingDirName;
//
    this.mySavedDataFile = saveDataFile;
//
    super.setFont(SwingMyEditorConst.MY_FONT);
//
    super.setMargin( new Insets(2,6,2,6) );  // top, left, bottom, right
//
    super.setEditable( (ctrl & SwingMyEditorConst.MY_ALLOW_FILE_OPS) != 0 );
    super.addKeyListener(
        new KeyListener() {
          @Override public void keyTyped(KeyEvent e) { SwingMyEditorZ07TextArea.this.myHandleKeyTyped(e); }
          @Override public void keyPressed(KeyEvent e) { }
          @Override public void keyReleased(KeyEvent e)
          { if( SwingMyEditorZ07TextArea.this.myCurrentMode == MY_MODES.COMMAND ) SwingMyEditorZ07TextArea.this.myHandleKeyReleasedInCommandMode(e); }
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
// Maybe set carot invisible
    if ( (ctrl & SwingMyEditorConst.MY_ALLOW_FILE_OPS) == 0 ) super.getCaret().setVisible(false);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 *
 */
  private final void myHandleKeyTyped (KeyEvent e)
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": keyEvent= " + e);
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": editable= " + this.isEditable() + ": mode= " + this.myCurrentMode + ": keyEvent= " + e.getKeyChar() );
//
    this.myMsgLine.mySetMsg(null);
    char keyChar = e.getKeyChar();
// If escape key typed go to command mode
    if ( keyChar == KeyEvent.VK_ESCAPE ) {
      this.myCurrentMode = MY_MODES.COMMAND;
      this.myMsgLine.mySetMode(MY_MODES.COMMAND);
      super.setEditable(false);
    }
// Else if already in command mode, handle command key
    else if ( this.myCurrentMode == MY_MODES.COMMAND ) this.myHandleKeyTypedInCommandMode ( keyChar );
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
  private final void myHandleKeyTypedInCommandMode ( char keyChar )
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": keyChar= " + keyChar);
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": editable= " + this.isEditable() + ": mode= " + this.myCurrentMode + ": key= " + keyChar );
//
// If go to command line
    if ( this.myCmdLine != null && keyChar == ':' ) SwingMyEditorZ04JPanel.mySetCaretTo(this.myCmdLine);
// Else if handle undo
    else if ( keyChar == 'u' ) { if ( this.myUndoManager.canUndo() ) this.myUndoManager.undo(); }
// Else if not going back to insert mode go to sub-routine
    else if ( keyChar != 'i' && keyChar != 'a' ) this.myHandleKeyCmdTypedHandler.myHandleCommandKey( keyChar );
  } //End: Method

  
//------------------  Method  ------------------
/**
 * This method handles key commands typed into the text area in the command mode
 * that must be processed at key released
 *
 * @param keyChar  Key that was typed
 */
  private final void myHandleKeyReleasedInCommandMode (KeyEvent e)
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": keyEvent= " + e);
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": editable= " + this.isEditable() + ": mode= " + this.myCurrentMode + ": keyEvent= " + e.getKeyChar() );
//
    char keyChar = e.getKeyChar();
// Important to exit command mode here so that 'i' is not typed.
    if ( keyChar == 'i' || keyChar == 'a' ) {
      this.myCurrentMode = MY_MODES.INSERT;
      this.myMsgLine.mySetMode(MY_MODES.INSERT);
      super.setEditable(true);
    }
    if ( keyChar == 'a' ) {
      super.setCaretPosition(super.getCaretPosition()+1);
    }
  } //End: Method

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
 * @param needToWrite  ?
 *
 */
  final void mySetNeedToWrite( boolean needToWrite ) { this.myChanged = needToWrite; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @throws Exception  Java standard exception
 */
  private final void myOpenFileSub( boolean isEncrypted ) throws Exception
  {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": workingDir= " + this.myWorkingDirName);
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    Window topWind = SwingMyEditorZ11Misc.myGetParentWindow(this);
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
 *
 */
  public final void mySetDataFile( File file ) {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    this.mySavedDataFile = file;
  }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param file  ?
 *
 */
  public final void mySetPassword( )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    Window topWind = SwingMyEditorZ11Misc.myGetParentWindow(this);
    String password = SwingMyJDialogForPassword.myGetPassword(topWind);
    if ( password != null && password.length() > 0 ) this.myPassword = password;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param file  ?
 *
 */
  private final void myShowMessage( String msg )
  {
    Window fr = SwingMyEditorZ11Misc.myGetParentWindow(this);
    JOptionPane.showMessageDialog(fr, msg);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 *
 */
  public final File myGetDataFile( ) { return this.mySavedDataFile; }


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
 * @param saveOld  ?
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
    Window topWind = SwingMyEditorZ11Misc.myGetParentWindow(this);
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
 * @param fileName  ?
 * @param saveOld  ?
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
        Window fr = SwingMyEditorZ11Misc.myGetParentWindow(this);
        this.myPassword = SwingMyJDialogForPassword.myGetPassword(fr);
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
 * @param text  ?
 *
 */
  private final void myAskToSaveFileIfNecessary ( ) throws Exception
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
    if ( this.myChanged ) {
      Window fr = SwingMyEditorZ11Misc.myGetParentWindow(this);
      int closingArg = JOptionPane.showConfirmDialog(fr, "Save modified file?");
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
    Container topContainer = SwingMyEditorZ11Misc.myGetParentWindowOrInternalFrame(this);
//
    if ( topContainer instanceof Window ) {
      this.myCloseFile();
      Window wind = (Window)topContainer;
      wind.setVisible(false);
      wind.dispose();
    }
    else if ( topContainer instanceof JInternalFrame ) {
      this.myCloseFile();
      JInternalFrame intFr = (JInternalFrame)topContainer;
      intFr.setVisible(false);
      intFr.dispose();
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param cmdLine  ?
 *
 */
  final void mySetCmdAndMsgLines( SwingMyEditorZ08CmdLine cmdLine, SwingMyEditorZ09MsgLine msgLine )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
    this.myCmdLine = cmdLine;
    this.myMsgLine = msgLine;
//
    this.myHandleKeyCmdTypedHandler = new SwingMyEditorZ10HandleKeyTyped(this, this.myMsgLine);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param key  ?
 *
 * @return  ?
 *
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
 * @param e  ?
 *
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
    if ( clipboard != null ) {
      StringSelection strSel = new StringSelection(val);
      clipboard.setContents(strSel, strSel);
    } //End: if ()
//
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  public final SwingMyEditorZ05Action myGetActions () { return this.myActions; }


} //End: class SwingMyEditorZ05TextArea

