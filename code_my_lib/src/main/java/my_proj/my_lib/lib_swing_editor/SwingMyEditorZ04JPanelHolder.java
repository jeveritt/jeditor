// SwingMyEditorZ01JPanel.java
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
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import my_proj.my_lib.lib.MyTrace;


//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
//------------------------  CLASS: SwingMyEditorZ01JPanel  ------------------------------
//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
/**
 * This class is the top JPanel used in JFrame, JDialog, and JInterFrame versions of the editor.
 *
 * @author James Everitt
 */
public final class SwingMyEditorZ04JPanelHolder implements WindowListener, InternalFrameListener
{
  
//private static final boolean DO_TRACE = true;
  
/** JScrollPane for panel */
  private              JScrollPane              myScrollPane = null;
  
/** Top JPanel */
  private              JPanel                   myTopPanel = null;
  
/** Holder of JTextArea placed in myTopPanel */
  private              SwingMyEditorZ07TextAreaHolder myTextAreaHolder = null;

/** Holder of JTextField placed in myTopPanel */
  private              SwingMyEditorZ10CmdLineHolder  myCmdLineHolder = null;
  
/** Extends JTextField and placed in myTopPanel */
  private              SwingMyEditorZ14MsgLine  myMsgLine = null;
  

//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param pan  ?
 * @param e  ?
 *
 */
  private static final void myHandleException ( JPanel pan, Exception e )
  {
    String msg = ( e == null ) ? "no msg" : e.getMessage();
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": msg= " + msg);
//
    if (SwingMyEditorConst.PRINT_STACK_TRACE) {
      System.out.println("Exception: SwingMyEditorZ01JPanel." + "SwingMyEditorZ01JPanel.myHandleException" + " : exc= " + msg + "\n");
      System.out.flush();
      if (e != null && SwingMyEditorConst.PRINT_STACK_TRACE ) e.printStackTrace();
    }
//
    String dialogMsg = "SwingMyEditorZ01JPanel.myHandleException" + "\n  " + msg;
    System.out.println(dialogMsg);
    while ( pan.getParent() != null ) pan.getParent();
    if ( pan != null && pan.isVisible() ) JOptionPane.showMessageDialog( pan, dialogMsg );
    else System.out.println(dialogMsg);
  } //End: Method


//------------------------------------------------------------------------
//-------------------  Setter/Getter Methods:  ---------------------------
//------------------------------------------------------------------------
  

//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  final JScrollPane myGetScrollPane () { return this.myScrollPane; }
  

//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  public final JPanel myGetTopJPanel () { return this.myTopPanel; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  final SwingMyEditorZ10CmdLineHolder myGetCmdLineHolder () { return this.myCmdLineHolder; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  public final SwingMyEditorZ07TextAreaHolder myGetTextAreaHolder () { return this.myTextAreaHolder; }


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  final SwingMyEditorZ14MsgLine myGetMsgLine () { return this.myMsgLine; }


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This constructor method ?
 *
 * @param textDataAsString  ?
 * @param lowerPanel  ?
 * @param password  ?
 * @param saveDataFile  ?
 * @param workingDirName  ?
 */
  SwingMyEditorZ04JPanelHolder (
      String     textDataAsString,
      JComponent lowerPanel,
      String     password,
      String     saveDataFileName,
      String     workingDirName
      )
  {
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl) + "\n dir= " + workingDirName + "\n file= " + (saveDataFile == null ? "null" : saveDataFile.getAbsolutePath()) );
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln(MyTrace.myInd() + MyTrace.myGetMethodName()
        + MyTrace.myNlInd() + " dir= " + workingDirName
        + MyTrace.myNlInd() + " file= " + saveDataFileName );
    try {
// Create and add layout manager
      this.myTopPanel = new JPanel();
      this.myTopPanel.setLayout( new myEditorLayoutManager() );
// Create command and message lines
      this.myCmdLineHolder = new SwingMyEditorZ10CmdLineHolder(this); // SwingMyEditorZ08CmdLine cmdLine = new SwingMyEditorZ08CmdLine();
      this.myCmdLineHolder.myGetJTextField().setVisible( !SwingMyEditorCtrl.noCmdLn ); //                    ctrl & SwingMyEditorCtrl.MY_NO_MENU) == 0 );
      this.myMsgLine = new SwingMyEditorZ14MsgLine(SwingMyEditorCtrl.doEncrypt);
      this.myMsgLine.setVisible( !SwingMyEditorCtrl.noCmdLn );
//      this.myMsgLine.mySetIsEncrypted(SwingMyEditorCtrl.doEncrypt.isSelected());
// Add text area and its scroll pane
      this.myTextAreaHolder = new SwingMyEditorZ07TextAreaHolder(
          textDataAsString,
          this,
          password,
          saveDataFileName,
          workingDirName
          );
      this.myScrollPane = new JScrollPane( this.myTextAreaHolder.myGetJTextArea() );
      this.myScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      this.myScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      this.myTopPanel.add(this.myScrollPane);
//
      this.myTopPanel.add(this.myCmdLineHolder.myGetJTextField());
      this.myTopPanel.add(this.myMsgLine);
// Possibly add lower panel
      if ( lowerPanel != null) {
        this.myTopPanel.add(lowerPanel);
      }
//
    } //End: try
    catch (Exception e) { SwingMyEditorZ04JPanelHolder.myHandleException(null, e); }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param custom  ?
 *
 * @return  ?
 */
  final JMenuBar myCreateMenuBar( JMenu[] custom ) {
    JMenuBar menuBar = SwingMyEditorZ06Menu.myCreateMenuBar ( custom, this.myGetTextAreaHolder().myGetActions() );
    return menuBar;
  } //End: method


//------------------  Method  ------------------
/**
 * This method sets editor text from an ArrayList&lt;String&gt;
 * 
 * @param txtAl  Text for editor
 */
  final void mySetText ( ArrayList<String> txtAl )
  {
    StringBuffer txt = new StringBuffer();
    for ( String str : txtAl ) txt.append(str + "\n");
    this.myTextAreaHolder.myGetJTextArea().setText(txt.toString());
  } //End: method


//------------------  Static Method  ------------------
/**
 * This method 
 * 
 * @param doEncrypt  True for doing encryption
 * 
 */
/*
  final void myHandleEncryptionChangedTo( boolean newDoEncrypt )
  {
    boolean oldDoEncrypt = SwingMyEditorCtrl.doEncrypt.isSelected();
    if ( oldDoEncrypt != newDoEncrypt ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": do encrypt = " + oldDoEncrypt);
      SwingMyEditorCtrl.doEncrypt.setSelected( newDoEncrypt );
      this.myTextAreaHolder.myActions.MY_FILE_SET_PASSWORD.setEnabled(newDoEncrypt);
      this.myMsgLine.mySetIsEncrypted(newDoEncrypt);
    }
  } //End: method
*/

//------------------------------------------------------------------------
//------------------  Stuff to implement WindowListener  -----------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This method handles window closing.
 * setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE) should have been set.
 * This means that windowClosed() will never be set and this method must close things down.
 * 
 * @param arg0  Argument is not used
 */
  private final void myDoWindowClosing() {
//if (DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": fileToSave= " + this.mySavedDataFile );
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
// Get JFrame or JInternalFrame or JDialog
    Container cont = this.myTopPanel;
    while ( cont.getParent() != null ) {
//      if ( cont instanceof SwingMyEditorZ04JPanel ) topPanel = (SwingMyEditorZ04JPanel)cont;
      cont = cont.getParent();
      if ( (cont instanceof JFrame) || (cont instanceof JInternalFrame) || (cont instanceof JDialog) ) break;
    }
// Decide on how to close
    try {
      boolean closeWindow = true;
// Decide what to do if text has changed
      if ( this.myTextAreaHolder.myGetTextHasChanged() ) {
// If can not write
        if ( !SwingMyEditorCtrl.canWriteFile ) {
          SwingMyEditorZ16Misc.myShowDialog(
                  cont,             // Component  parentComponent
                  "Warning",              // Object message,
                  "Warning: can not write to file"            // String title,
             );
        }
// Continue after possible warning
        int reply = this.myTextAreaHolder.myAskToSaveFileIfNecessary();
        if ( reply == JOptionPane.CANCEL_OPTION ) closeWindow = false;
/*
        int reply = JOptionPane.showConfirmDialog(cont, "Save File", "Save ?", JOptionPane.YES_NO_CANCEL_OPTION);
        if ( reply == JOptionPane.YES_OPTION ) {
          this.myTextAreaHolder.mySaveFile(
              this.myTextAreaHolder.myGetAllowFileSaveAsEncrypted(),  // boolean saveAsEncrypted,
              this.myTextAreaHolder.myGetDataFileName(),              // String fileName,
              SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED      // int numbOldToSave
              );
        }
        else if ( reply == JOptionPane.CANCEL_OPTION ) closeWindow = false;
*/
      } //End: if ()
// If decide to close
      if ( closeWindow ) {
//if (DO_TRACE) System.out.println(MyTrace.myGetMethodName() + ": closing window" );
//        this.myTextArea.myCloseFile();
        MyTrace.mySetVerboseLevel(0);
        if      ( cont instanceof JFrame ) { ((JFrame)cont).dispose(); System.exit(0); }
        else if ( cont instanceof JInternalFrame ) { ((JInternalFrame)cont).dispose(); }
        else if ( cont instanceof JDialog ) { ((JDialog)cont).dispose(); }
      }
//
    } //End: try
    catch (Exception e) {
      System.out.println(MyTrace.myGetMethodName()  + ": exc= " + e.getMessage());
      e.printStackTrace();
    }
  } //End: method


  @Override public void windowClosing(WindowEvent arg0) { myDoWindowClosing(); }

  @Override public void windowOpened(WindowEvent arg0) {}

  @Override public void windowClosed(WindowEvent arg0) {}

  @Override public void windowActivated(WindowEvent arg0) {}

  @Override public void windowDeactivated(WindowEvent arg0) {}

  @Override public void windowDeiconified(WindowEvent arg0) {}

  @Override public void windowIconified(WindowEvent arg0) {}

//------------------------------------------------------------------------
//--------------  Stuff to implement InternalFrameListener  --------------
//------------------------------------------------------------------------


  @Override public void internalFrameClosing(InternalFrameEvent e) { myDoWindowClosing(); }

  @Override public void internalFrameOpened(InternalFrameEvent e) {}

  @Override public void internalFrameClosed(InternalFrameEvent e) {}

  @Override public void internalFrameIconified(InternalFrameEvent e) {}

  @Override public void internalFrameDeiconified(InternalFrameEvent e) {}

  @Override public void internalFrameActivated(InternalFrameEvent e) {}

  @Override public void internalFrameDeactivated(InternalFrameEvent e) {}

} //End: class SwingMyEditorZ01JPanel


//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
//------------------------  CLASS: myEditorLayoutManager  -------------------------------
//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
/**
* This class
*
* @author James Everitt
*/
final class myEditorLayoutManager implements LayoutManager {

  @Override public void addLayoutComponent(String name, Component comp) { }

  @Override public void removeLayoutComponent(Component comp) { }

  @Override public Dimension preferredLayoutSize(Container parent)
  {
    Component[] comps = parent.getComponents();
// Text area
    Dimension prefSize = comps[0].getPreferredSize();
// Cmd line
    if ( comps.length > 1 && comps[1].isVisible() ) prefSize.height += comps[1].getPreferredSize().height;
// Message line
    if ( comps.length > 2 && comps[2].isVisible() ) prefSize.height += comps[2].getPreferredSize().height;
//
    return prefSize;
  } //End: Method

  @Override public Dimension minimumLayoutSize(Container parent) { return new Dimension(300,200); }

  @Override public void layoutContainer(Container parent) {
    Component[] comps = parent.getComponents();
    Component scrollPane = comps[0];
    JTextField cmdLine = comps.length < 2 ? null : (JTextField)comps[1];
    SwingMyEditorZ14MsgLine msgLine = comps.length < 3 ? null : (SwingMyEditorZ14MsgLine)comps[2];
    Dimension size = parent.getSize();
//
    int sep = 3;
    int cmdLineHeight = cmdLine.getPreferredSize().height;
    int msgLineHeight = msgLine.getPreferredSize().height;
    int scrollPaneHeight = size.height;
    if ( cmdLine != null && cmdLine.isVisible() ) scrollPaneHeight -= cmdLineHeight + sep;
    if ( msgLine != null && msgLine.isVisible() ) scrollPaneHeight -= msgLineHeight + sep;
    scrollPane.setBounds(0, 0, size.width, scrollPaneHeight);
    int y = scrollPaneHeight + sep;
    if ( cmdLine != null && cmdLine.isVisible() ) {
      cmdLine.setBounds(0, y, size.width, cmdLineHeight);
      y += cmdLineHeight + sep;
    }
    if ( msgLine != null && msgLine.isVisible() )
      msgLine.setBounds(0, y, size.width, msgLineHeight);
  } //End: Method

} //End: class myEditorLayoutManager

