// SwingMyEditorZ02Action.java
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

//------------------  Import statements  ------------------

import java.awt.Container;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import my_proj.my_lib.lib.MyMisc2;
import my_proj.my_lib.lib.MyTrace;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//---------------------  CLASS: SwingMyEditorZ02Action  -----------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class
 *
 * @author James Everitt
 */
final class SwingMyEditorZ05Action {

//private static final boolean DO_TRACE = true;

/** ? */
  AbstractAction MY_EDIT_COPY = null;
/** ? */
  AbstractAction MY_FILE_OPEN = null;
/** ? */
  AbstractAction MY_FILE_SAVE = null;
/** ? */
  AbstractAction MY_FILE_SAVE_AS = null;
/** ? */
  AbstractAction MY_FILE_CLOSE = null;
/** ? */
  AbstractAction MY_FILE_SET_PASSWORD = null;
/** ? */
  AbstractAction MY_HELP_ABOUT_OPEN = null;
/** ? */
  AbstractAction MY_HELP_ABOUT_RUN = null;
/** ? */
  AbstractAction MY_HELP_INFO = null;
/** ? */
  AbstractAction MY_HELP_COPYRIGHT = null;
/** ? */
  AbstractAction MY_VIEW_SET_TO_TOP = null;

/** ? */
  private SwingMyEditorZ04JPanelHolder myTopPanel = null;
  

//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------ Method ------------------
/**
 * This constructor method ?
 *
 * @param ctrl  ?
 * @param textArea  ?
 *
 */
  SwingMyEditorZ05Action (
      SwingMyEditorZ04JPanelHolder topPanel
//      SwingMyEditorZ07TextAreaHolder textArea
      )
  {
    this.myTopPanel = topPanel;
//
    this.MY_EDIT_COPY = new AbstractAction("copy") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) { SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myHandleCopy(); }
    };
    this.MY_EDIT_COPY.putValue(Action.SHORT_DESCRIPTION, "Copy selected text to system clipboard");
    if ( !SwingMyEditorCtrl.canWriteFile ) this.MY_EDIT_COPY.setEnabled(false);
//
//
    this.MY_FILE_OPEN = new AbstractAction("open file") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        try { SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myOpenFile(); }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    this.MY_FILE_OPEN.putValue(Action.SHORT_DESCRIPTION, "Open file");
    if ( !SwingMyEditorCtrl.canReadFile ) this.MY_FILE_OPEN.setEnabled(false);
//
    this.MY_FILE_SAVE = new AbstractAction("save file") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        try {
          SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().mySaveFile( 
              SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myGetDataFileName(),
              SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED
              );
        }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    this.MY_FILE_SAVE.putValue(Action.SHORT_DESCRIPTION, "Save encrypted file");
    if ( !SwingMyEditorCtrl.canWriteFile ) this.MY_FILE_SAVE.setEnabled(false);
//
    this.MY_FILE_SAVE_AS = new AbstractAction("save file as") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        try {
          SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().mySaveFile( null, SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED );
        }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    this.MY_FILE_SAVE_AS.putValue(Action.SHORT_DESCRIPTION, "Save un-encrypted file to new name");
    if ( !SwingMyEditorCtrl.canWriteFile ) this.MY_FILE_SAVE_AS.setEnabled(false);
//
//
    this.MY_FILE_SET_PASSWORD = new AbstractAction("set password") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        try {
          SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().mySetPassword();
        }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    this.MY_FILE_SET_PASSWORD.putValue(Action.SHORT_DESCRIPTION, "Set password for gpg encryption");
    if ( !SwingMyEditorCtrl.doEncrypt.isSelected() ) this.MY_FILE_SET_PASSWORD.setEnabled(false);
//
//
    this.MY_FILE_CLOSE = new AbstractAction("close file") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myCloseFile();
      }
    };
    this.MY_FILE_CLOSE.putValue(Action.SHORT_DESCRIPTION, "Close current file");
//
    this.MY_HELP_ABOUT_OPEN = new AbstractAction("about starting editor") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        SwingMyEditorZ05Action.myHandleDisplayText (
            SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myGetJTextArea(), // JPanel parent,
            "Starting Program",                                      // String title,
            SwingMyEditorZ03Handler.myGetHelp( "" )                  // String text
            );
      }
    };
    this.MY_HELP_ABOUT_OPEN.putValue(Action.SHORT_DESCRIPTION, "Syntac for starting this program");
//
//  
    this.MY_HELP_INFO =  new AbstractAction("about Java and system info") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        SwingMyEditorZ05Action.myHandleDisplayText (
            SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myGetJTextArea(),  // JPanel parent,
            "Info",                                                   // String title,
            MyMisc2.myGetJavaAndSystemInfo("")                        // String text
            );
      }
    };
    this.MY_HELP_INFO.putValue(Action.SHORT_DESCRIPTION, "Info on Java and system");
//
//
    this.MY_HELP_ABOUT_RUN = new AbstractAction("about running editor") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        SwingMyEditorZ05Action.myHandleDisplayText (
            SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myGetJTextArea(), // JPanel parent,
            "Program Information",                                   // String title,
            SwingMyEditorZ03Handler.myGetAppHelpString()             // String text
            );
      }
    };
    this.MY_HELP_ABOUT_RUN.putValue(Action.SHORT_DESCRIPTION, "Short description and cheat sheet");
//
//
    this.MY_HELP_COPYRIGHT = new AbstractAction("license info") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        Container topWind = SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myGetJTextArea();
        while ( topWind.getParent() != null ) topWind = topWind.getParent();
        String infoStr = new String(
          "Copyright (C)  2022  Jim Everitt\n" +
          "\n" +
          "This program is free software: you can redistribute it and/or modify\n" +
          "it under the terms of the GNU General Public Licenses published by\n" +
          "the Free Software Foundation, either version 3 of the License, or\n" +
          "(at your option) any later version.\n" +
          "\n" +
          "This program is distributed in the hope that it will be useful,\n" +
          "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
          "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
          "GNU General Public License for more details.\n" +
          "\n" +
          "You should have received a copy of the GNU General Public License\n" +
          "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n"
        );
//
        JOptionPane.showMessageDialog(topWind, infoStr, "License", JOptionPane.INFORMATION_MESSAGE);
      }
    };
    this.MY_HELP_COPYRIGHT.putValue(Action.SHORT_DESCRIPTION, "Display the license for this program");
//
//
    this.MY_VIEW_SET_TO_TOP = new AbstractAction("set to top") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        SwingMyEditorZ05Action.this.myTopPanel.myGetTextAreaHolder().myGetJTextArea().setCaretPosition(0);
      }
    };
    this.MY_VIEW_SET_TO_TOP.putValue(Action.SHORT_DESCRIPTION, "Set position to top of file");
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param e  ?
 *
 */
  private static final void myHandleDisplayText ( Container parent, String title, String text )
  {
    Container topWind = parent;
    while ( topWind.getParent() != null ) topWind = topWind.getParent();
//
    JTextArea txtArea = new JTextArea(text);
    txtArea.setFont(SwingMyEditorConst.MY_MONOSPACED_FONT);
    txtArea.setEditable(false);
    JScrollPane sp = new JScrollPane(txtArea);
    JDialog dlg = new JDialog((Window)topWind);
    dlg.setTitle(title);
    Image img = SwingMyEditorZ16Misc.myGetImage("my_proj/my_lib_resources/my_gifs/jedit_icon_26x26.gif");
    if ( img != null ) dlg.setIconImage(img);
    dlg.setContentPane(sp);
    dlg.pack();
    dlg.setLocationRelativeTo(topWind);
    dlg.setVisible(true);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param e  ?
 *
 */
  private final void myHandleException ( Exception e )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln(MyTrace.myInd() + MyTrace.myGetMethodName() + ": exc= " + e.getMessage() );
//
    String msg = ( e == null ) ? "no msg" : e.getMessage();
    String methodName = "SwingMyEditorZ02Action.myHandleException";
//
    if (SwingMyEditorConst.PRINT_STACK_TRACE) {
      System.out.println("Exception: " + methodName + " : exc= " + msg + "\n");
      System.out.flush();
      if (e != null && SwingMyEditorConst.PRINT_STACK_TRACE ) e.printStackTrace();
    }
//
    String dialogMsg = methodName + ":\n  " + msg;
    System.out.println(dialogMsg);
    Container cont = this.myTopPanel.myGetTextAreaHolder().myGetJTextArea();
    while ( cont.getParent() != null ) cont = cont.getParent();
    if ( cont != null && cont.isVisible() ) JOptionPane.showMessageDialog( cont, dialogMsg );
    else System.out.println( MyTrace.myGetMethodName() + ": msg= " + dialogMsg);
  } //End: Method


} // End: class SwingMyEditorZ02Action
