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

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


//------------------  CLASS: SwingMyEditorZ02Action  ------------------
/**
 * This class
 *
 * @author James Everitt
 */
final class SwingMyEditorZ05Action {

//  private static final boolean DO_TRACE = true;
  
  static final long serialVersionUID = 0;
//
  AbstractAction MY_EDIT_COPY = null;
  
  AbstractAction MY_FILE_OPEN_UNENCRYPTED = null;
  AbstractAction MY_FILE_OPEN_ENCRYPTED = null;
  AbstractAction MY_FILE_SAVE_UNENCRYPTED = null;
  AbstractAction MY_FILE_SAVE_ENCRYPTED = null;
  AbstractAction MY_FILE_SAVE_UNENCRYPTED_AS = null;
  AbstractAction MY_FILE_CLOSE = null;
  AbstractAction MY_FILE_CLOSE_WINDOW = null;
  AbstractAction MY_FILE_SET_PASSWORD = null;
  
  AbstractAction MY_HELP_ABOUT = null;
  AbstractAction MY_HELP_COPYRIGHT = null;
  
  AbstractAction MY_VIEW_SET_TO_TOP = null;
//
  private SwingMyEditorZ07TextArea  myTextArea = null;
  

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
 * @param textArea  ?
 *
 */
  SwingMyEditorZ05Action (
      int                      ctrl,
      SwingMyEditorZ07TextArea textArea
      )
  {
    this.myTextArea = textArea;
//
    boolean readOnly = ( ctrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_WRITE ) == 0 && ( ctrl & SwingMyEditorConst.MY_ALLOW_UNENCRYP_WRITE ) == 0 ;
    boolean openUnencrypFile = ( ctrl & SwingMyEditorConst.MY_ALLOW_UNENCRYP_READ ) != 0;
    boolean openEncrypFile = ( ctrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_READ ) != 0;
    boolean saveFileUnencrypt = ( ctrl & SwingMyEditorConst.MY_ALLOW_FILE_OPS ) != 0 && ( ctrl & SwingMyEditorConst.MY_ALLOW_UNENCRYP_WRITE ) != 0;
    boolean saveFileEncrypted = ( ctrl & SwingMyEditorConst.MY_ALLOW_FILE_OPS ) != 0 && ( ctrl & SwingMyEditorConst.MY_ALLOW_ENCRYP_WRITE ) != 0;
//
    this.MY_EDIT_COPY = new AbstractAction("copy") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) { SwingMyEditorZ05Action.this.myTextArea.myHandleCopy(); }
    };
    if ( readOnly ) this.MY_EDIT_COPY.setEnabled(false);
//
//
    this.MY_FILE_OPEN_UNENCRYPTED = new AbstractAction("open file") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        try { SwingMyEditorZ05Action.this.myTextArea.myOpenFile(); }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    if ( !openUnencrypFile ) this.MY_FILE_OPEN_UNENCRYPTED.setEnabled(false);
//
//
    this.MY_FILE_OPEN_ENCRYPTED = new AbstractAction("open encrypted file") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        try { SwingMyEditorZ05Action.this.myTextArea.myOpenEncryptedFile(); }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    if ( !openEncrypFile ) this.MY_FILE_OPEN_ENCRYPTED.setEnabled(false);
//
//
    this.MY_FILE_SAVE_UNENCRYPTED = new AbstractAction("save file") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        try {
          SwingMyEditorZ05Action.this.myTextArea.mySaveFile(false);
        }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    if ( !saveFileUnencrypt ) this.MY_FILE_SAVE_UNENCRYPTED.setEnabled(false);
//
//
    this.MY_FILE_SAVE_ENCRYPTED = new AbstractAction("save file as encrypted") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        try {
          SwingMyEditorZ05Action.this.myTextArea.mySaveFile(true);
        }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    if ( !saveFileEncrypted ) this.MY_FILE_SAVE_ENCRYPTED.setEnabled(false);
//
//
    this.MY_FILE_SAVE_UNENCRYPTED_AS = new AbstractAction("save file as") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        try {
          SwingMyEditorZ05Action.this.myTextArea.mySaveFileAs( );
        }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    if ( !saveFileUnencrypt ) this.MY_FILE_SAVE_UNENCRYPTED_AS.setEnabled(false);
//
//
    this.MY_FILE_SET_PASSWORD = new AbstractAction("set password") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        try {
          SwingMyEditorZ05Action.this.myTextArea.mySetPassword();
        }
        catch (Exception exc) { SwingMyEditorZ05Action.this.myHandleException(exc); }
      }
    };
    if ( !openEncrypFile && !saveFileEncrypted ) this.MY_FILE_SET_PASSWORD.setEnabled(false);
//
//
    this.MY_FILE_CLOSE = new AbstractAction("close") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        SwingMyEditorZ05Action.this.myTextArea.myCloseFile();
      }
    };
//
//
    this.MY_FILE_CLOSE_WINDOW = new AbstractAction("exit") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) { SwingMyEditorZ05Action.this.myTextArea.myCloseWindow(); }
    };
//
//
    this.MY_HELP_ABOUT = new AbstractAction("about editor") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        Window topWind = SwingMyEditorZ11Misc.myGetParentWindow(SwingMyEditorZ05Action.this.myTextArea);
        String data = SwingMyEditorZ03Handler.myGetAppHelpString();
    //
        JTextArea txtArea = new JTextArea(data);
        txtArea.setFont(SwingMyEditorConst.MY_FONT);
        JScrollPane sp = new JScrollPane(txtArea);
        JDialog dlg = new JDialog(topWind);
        dlg.setContentPane(sp);
        dlg.pack();
        dlg.setLocationRelativeTo(topWind);
        dlg.setVisible(true);
      }
    };
//
//
    this.MY_HELP_COPYRIGHT = new AbstractAction("license info") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        Window topWind = SwingMyEditorZ11Misc.myGetParentWindow(SwingMyEditorZ05Action.this.myTextArea);
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
//
//
    this.MY_VIEW_SET_TO_TOP = new AbstractAction("set to top") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
//        Container par = SwingMyEditorZ02Action.this.myTextArea.getParent();
//        if ( par != null ) par = par.getParent();
//        JScrollPane sp = ( par != null && par instanceof JScrollPane ) ? (JScrollPane)par : null;
//        if ( sp != null ) sp.myForceViewToTop();
      }
    };
//
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
    JOptionPane.showMessageDialog( this.myTextArea, dialogMsg );
  } //End: Method


} // End: class SwingMyEditorZ02Action
