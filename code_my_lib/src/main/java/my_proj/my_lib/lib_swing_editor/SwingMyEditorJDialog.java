// SwingMyEditorJDialog.java
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
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.WindowConstants;

import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_swing.SwingMyImages;


//------------------  CLASS: SwingMyEditorJDialog  ------------------
/**
 * This class creates a dialog text editor by extending JDialog, creating the editor, and then setting the editor as the content pane of the JDialog.
 *
 * @author James Everitt
 */
public final class SwingMyEditorJDialog extends JDialog
{
  
//private static final boolean DO_TRACE = true;

/** serial UID */
  private static final long serialVersionUID = 3005339266370888759L;

/** Main editor panel */
  private SwingMyEditorZ04JPanel myEditorPanel = null;
  

//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This constructor method constructs the dialog
 * 
 * @param owner     Owner of the dialog which is a subclass of Window.
 * @param title     Title for the JFrame tile bar. If null and a file is specified the file name is used.
 * @param dataFile  Starting file or null.
 * @param ctrl      Int specifying editor options.
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJDialog ( Window owner, String title, File dataFile, int ctrl ) throws Exception
  {
    super(owner);
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.DIALOG;
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromFile(
        this,            // Container      cont
        title,           // String         title
        dataFile,        // File           startingDataFile,
        false,           // boolean        startingDataFileIsEncrypted,
        ctrl,            // int            ctrl
        SwingMyEditorConst.MY_DEFAULT_SIZE_X, // int sizeX
        SwingMyEditorConst.MY_DEFAULT_SIZE_Y, // int sizeY
        null,           // JMenu[]        custom
        null,           // String         password
        null            // String         workingDirName
        );
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
  } //End: Method


//------------------  Method  ------------------
/**
 * This constructor method ?
 * 
 * @param owner     Owner of the dialog which is a subclass of Window.
 * @param title     Title for the JFrame tile bar. If null and a file is specified the file name is used.
 * @param dataFile  Starting file or null.
 * @param ctrl      Int specifying editor options.
 * @param sizeX     Width of the dialog in pixels.
 * @param sizeY     Height of the dialog in pixels.
 * @param modalityType Dialog modal type.
 * @param custom    JMenu array of custom menus to be added to the menu bar.
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJDialog (
      Window       owner,
      String       title,
      File         dataFile,
      int          ctrl,
      int          sizeX,
      int          sizeY,
      ModalityType modalityType,
      JMenu[]      custom
      ) throws Exception
  {
    super( owner );
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.DIALOG;
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromFile( this, title, dataFile, false, ctrl, sizeX, sizeY, custom, null, null );
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
    if ( modalityType != null ) super.setModalityType(modalityType);
  } //End: Method


//------------------  Method  ------------------
/**
 * This constructor method ?
 * 
 * @param owner     Owner of the dialog which is a subclass of Window.
 * @param title     Title for the JFrame tile bar. If null and a file is specified the file name is used.
 * @param ctrl      Int specifying editor options.
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJDialog ( Window owner, String title, int ctrl ) throws Exception
  {
    super(owner);
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.DIALOG;
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitialize( this, title, null, ctrl, SwingMyEditorConst.MY_DEFAULT_SIZE_X, SwingMyEditorConst.MY_DEFAULT_SIZE_Y, null, null, null, null );
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
  } //End: Method


//------------------ Method ------------------
/**
 * This constructor method ?
 *
 * @param owner     Owner of the dialog which is a subclass of Window.
 * @param title     Title for the JFrame tile bar. If null and a file is specified the file name is used.
 * @param list      ArrayList of starting data.
 * @param ctrl      Int specifying editor options.
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJDialog ( Window owner, String title, ArrayList<String> list, int ctrl ) throws Exception
  {
    super(owner);
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.DIALOG;
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromStringArray(this, title, list, ctrl, null);
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
  } //End: Method


//------------------ Method ------------------
/**
 * This constructor method ?
 *
 * @param owner     Owner of the dialog which is a subclass of Window.
 * @param title     Title for the JFrame tile bar. If null and a file is specified the file name is used.
 * @param list      ArrayList of starting data.
 * @param ctrl      Int specifying editor options.
 * @param modalityType Dialog modal type.
 * @param customMenu  JMenu array of custom menus to be added to the menu bar.
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJDialog (
      Window            owner,
      String            title,
      ArrayList<String> list,
      int               ctrl,
      ModalityType      modalityType,
      JMenu[]           customMenu
//      MyObjHolder<SwingMyEditorZ07TextArea> editorText
      ) throws Exception
  {
    super( owner );
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.DIALOG;
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromStringArray(this, title, list, ctrl, customMenu);
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
    if ( modalityType != null ) super.setModalityType(modalityType);
// Returns the text area even if the dialog locks
//    if ( editorText != null ) editorText.o = this.myEditorPanel.myGetTextArea();
  } //End: Method


//------------------  Method  ------------------
/**
 * This constructor method ?
 *
 * @param owner     Owner of the dialog which is a subclass of Window.
 * @param title     Title for the JFrame tile bar. If null and a file is specified the file name is used.
 * @param dataStr   String of starting data.
 * @param ctrl      Int specifying editor options.
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJDialog ( Window owner, String title, String dataStr, int ctrl ) throws Exception
  {
    super(owner);
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.DIALOG;
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromString(this, title, dataStr, ctrl, SwingMyEditorConst.MY_DEFAULT_SIZE_X, SwingMyEditorConst.MY_DEFAULT_SIZE_Y, null );
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
  } //End: Method


//------------------  Method  ------------------
/**
 * This constructor method ?
 *
 * @param owner     Owner of the dialog which is a subclass of Window.
 * @param title     Title for the JFrame tile bar. If null and a file is specified the file name is used.
 * @param dataStr   String of starting data.
 * @param ctrl      Int specifying editor options.
 * @param sizeX     Width of the dialog in pixels.
 * @param sizeY     Height of the dialog in pixels.
 * @param modalityType Dialog modal type.
 * @param custom   JMenu array of custom menus to be added to the menu bar.
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJDialog (
      Window       owner,
      String       title,
      String       dataStr,
      int          ctrl,
      int          sizeX,
      int          sizeY,
      ModalityType modalityType,
      JMenu[]      custom ) throws Exception
  {
    super(owner);
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.DIALOG;
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromString(this, title, dataStr, ctrl, sizeX, sizeY, custom );
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
    if ( modalityType != null ) super.setModalityType(modalityType);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method returns the SwingMyEditorZ04JPanel edit panel.
 *
 * @return  Returns the panel.
 */
  public final SwingMyEditorZ04JPanel myGetEditorPanel ( ) { return this.myEditorPanel; }
  

//------------------ Main Method ------------------
/**
* This method is the runnable main method.
* 
* @param args String[] of input arguments
* 
*/
 public static void main( String[] args )
 {
   try {
     JFrame mainFrame = new JFrame ( "Test Frame for SwingMyEditorJDialog" );
     mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//
     mainFrame.setSize(900,700);
     mainFrame.setLocationRelativeTo(null);  // MyCenterFrame.myCenterFrameOnScreen(mainFrame);
     Image img = SwingMyImages.myGetImage("my_proj/my_lib_resources/my_gifs/jedit_icon_26x26.gif");
     if ( img != null ) mainFrame.setIconImage(img);
//
     new SwingMyEditorJDialog (
         mainFrame,                   // Window container
         "Test SwingMyEditorJDialog", // String title,
         "ln 1\nln 2\nln 3\n",          //String dataStr,
         0 //int ctrl
         ); 
//
     mainFrame.setVisible(true);
   }
   catch (Exception e ) {
     e.printStackTrace();
   }
 } //End: Method


} //End: class SwingMyEditorJDialog

