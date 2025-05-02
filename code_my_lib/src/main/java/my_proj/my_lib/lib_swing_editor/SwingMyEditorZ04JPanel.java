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
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
public final class SwingMyEditorZ04JPanel extends JPanel implements WindowListener {
  
//  private static final boolean DO_TRACE = true;

/** Serial UID */
  private static final long serialVersionUID = -1055780583253290755L;

/** JScrollPane for panel */
  private              JScrollPane              myScrollPane = null;

/** SwingMyEditorZ07TextArea for panel */
  private              SwingMyEditorZ07TextArea myTextArea = null;
  
/** Panel control int */
  private              int                      myCtrl = 0;
  
/** True will close the program on window close */
  private              boolean                  myForceSystemCloseOnWindowClose = false;


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
 * @param ctrl  ?
 * @param lowerPanel  ?
 * @param password  ?
 * @param saveDataFile  ?
 * @param workingDirName  ?
 */
  public SwingMyEditorZ04JPanel (
      String     textDataAsString,
      int        ctrl,
      JComponent lowerPanel,
      String     password,
      File       saveDataFile,
      String     workingDirName
//,int dummy
      )
  {
    super();
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl) + "\n dir= " + workingDirName + "\n file= " + (saveDataFile == null ? "null" : saveDataFile.getAbsolutePath()) );
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln(MyTrace.myInd() + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl)
        + MyTrace.myNlInd() + " dir= " + workingDirName
        + MyTrace.myNlInd() + " file= " + (saveDataFile == null ? "null" : saveDataFile.getAbsolutePath()) );
    try {
      this.myCtrl = ctrl;
// Create and add layout manager
      super.setLayout( new myEditorLayoutManager() );
// Create command and message lines
      SwingMyEditorZ08CmdLine cmdLine = new SwingMyEditorZ08CmdLine();
      cmdLine.setVisible( (ctrl & SwingMyEditorCtrl.MY_NO_MENU) == 0 );
      SwingMyEditorZ09MsgLine msgLine = new SwingMyEditorZ09MsgLine();
      msgLine.setVisible( (ctrl & SwingMyEditorCtrl.MY_NO_MENU) == 0 );
// Add text area and its scroll pane
      this.myTextArea = new SwingMyEditorZ07TextArea(
          textDataAsString,
          cmdLine,
          msgLine,
          ctrl,
          password,
          saveDataFile,
          workingDirName
          );
      this.myScrollPane = new JScrollPane( this.myTextArea );
      this.myScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      this.myScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      super.add(this.myScrollPane);
//
      cmdLine.mySetTextAreaAndMsgLines(this.myTextArea, msgLine);
//
      super.add(cmdLine);
      super.add(msgLine);
// Possibly add lower panel
      if ( lowerPanel != null) {
        this.add(lowerPanel);
      }
//
    } //End: try
    catch (Exception e) { SwingMyEditorZ04JPanel.myHandleException(null, e); }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  public final SwingMyEditorZ07TextArea myGetTextArea () { return this.myTextArea; }


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
 * @param custom  ?
 *
 * @return  ?
 */
  final JMenuBar myCreateMenuBar( JMenu[] custom ) {
    JMenuBar menuBar = SwingMyEditorZ06Menu.myCreateMenuBar ( custom, this.myGetTextArea().myGetActions() );
    return menuBar;
  }


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
//------------------  Stuff to implement WindowListener  -----------------
//------------------------------------------------------------------------

  @Override public void windowClosing(WindowEvent arg0) {
    try {
      if ( ( (this.myCtrl & SwingMyEditorCtrl.MY_ALLOW_UNENCRYP_WRITE) != 0 ||
             (this.myCtrl & SwingMyEditorCtrl.MY_ALLOW_ENCRYP_WRITE) != 0 )
          && this.myTextArea.myGetHasChanged() ) {
        Container cont = this;
        while ( cont.getParent() != null ) cont = cont.getParent();
        if ( cont != null && cont.isVisible() ) {
          int reply = JOptionPane.showConfirmDialog(cont, "Save File", "Save ?", JOptionPane.YES_OPTION);
          if ( reply == JOptionPane.YES_OPTION ) this.myTextArea.mySaveFile();
        }
        else System.out.println( MyTrace.myGetMethodName() + ": failed to save" );
      } //End: if ()
      if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": exiting");
    }
    catch (Exception e) { SwingMyEditorZ04JPanel.myHandleException(this, e); }
  } //End: Method

  @Override public void windowActivated(WindowEvent arg0) {
  }

  @Override public void windowClosed(WindowEvent arg0) {
//if(DO_TRACE) System.out.println(MyTrace.myGetMethodName());
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": window closed" );
    MyTrace.mySetVerboseLevel(0);
    if ( this.myForceSystemCloseOnWindowClose ) System.exit(0);
  }

  @Override public void windowDeactivated(WindowEvent arg0) {}

  @Override public void windowDeiconified(WindowEvent arg0) {}

  @Override public void windowIconified(WindowEvent arg0) {}

  @Override public void windowOpened(WindowEvent arg0) {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": window opened" +  ": ctrl= 0x" + Integer.toHexString(this.myCtrl) );
  }

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
    SwingMyEditorZ08CmdLine cmdLine = comps.length < 2 ? null : (SwingMyEditorZ08CmdLine)comps[1];
    SwingMyEditorZ09MsgLine msgLine = comps.length < 3 ? null : (SwingMyEditorZ09MsgLine)comps[2];
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

