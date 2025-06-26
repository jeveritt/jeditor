// SwingMyJDialogForPassword.java
/*
 * Copyright (C) 2010 James Everitt
 * 
 * This program is free software: you can redistribute it
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
 */

// Package statement
package my_proj.my_lib.lib_swing;

//------------------  Import statements  ------------------

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;


//------------------  CLASS: SwingMyJDialogForPassword  ------------------
/**
 * This class
 *
 * @author James Everitt
 */
public class SwingMyJDialogForPassword {

  static final long serialVersionUID = 0;
  
  private   JPasswordField myPasswordField = null;
  

//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method is used to interface with the password dialog.
 *
 * @param parentFrame  Parent frame for the dialog.
 * 
 * @return  Returns the password or an empty String.
 */
  public static final String myGetPassword ( Window parentFrame )
  {
    SwingMyJDialogForPassword pwdDlg = new SwingMyJDialogForPassword ( parentFrame );
    char[] password = pwdDlg.myPasswordField.getPassword();
    return String.valueOf(password);
  } //End: Method


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor method using a JDialog.
 *
 * @param parentFrame  Parent frame for dialog
 */
  private SwingMyJDialogForPassword ( Window parentFrame )
  {
    JDialog dlg = new JDialog( parentFrame );
    dlg.setTitle("Password");
    dlg.setContentPane( this.myGetDialogPanel(dlg) );
 // Pack and center
    dlg.pack();
    dlg.setLocationRelativeTo(parentFrame);
// Lock out other stuff until password is entered
    dlg.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
// Set visible
    dlg.setVisible(true);
  } //End: Method


//------------------  Method  ------------------
/**
 * This is the constructor method.
 *
 * @param parentFrame  Parent frame for dialog
 */
  private final JPanel myGetDialogPanel ( Window window )
  {
    JPanel panel = new JPanel();
    panel.setLayout( new FlowLayout() );
// Password text field
    this.myPasswordField = new JPasswordField(16);
    this.myPasswordField.setText("");
    panel.add(this.myPasswordField);
// Handle hitting return to set password
    KeyListener listener = new KeyListener() {
      @Override  public void keyTyped(KeyEvent e) {
        if ( e.getKeyChar() == KeyEvent.VK_ENTER ) { window.dispose(); }
      }
//
      @Override public void keyPressed(KeyEvent e) {}
      @Override  public void keyReleased(KeyEvent e) {}
    };
    this.myPasswordField.addKeyListener(listener);
// Accept button
    AbstractAction accept = new AbstractAction("Accept") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        window.dispose();
      }
    };
    panel.add( new JButton(accept) );
// Cancel button
    AbstractAction cancel = new AbstractAction("Cancel") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        SwingMyJDialogForPassword.this.myPasswordField.setText("");
        window.dispose();
      }
    };
//
    panel.add( new JButton(cancel) );
//
    return panel;
  } //End: Method


//------------------ Main Method ------------------
/**
* This main method runs a test.
* Note that it will display the password in plain text.
* 
* @param args String[] of input arguments
*/
  public static void main (String[] args)
  {
// Set up JFrame
    JFrame mainFrame = new JFrame ( "Test SwingMyJDialogForPassword" );
    mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    mainFrame.setMinimumSize(new Dimension(300,150));
// Create button to activate password dialog
    AbstractAction getPassword = new AbstractAction("Password") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        try {
          String password = SwingMyJDialogForPassword.myGetPassword(mainFrame);
          System.out.println("SwingMyJDialogForPassword.main" + ": password= <" + password +">");
        }
        catch (Exception exc ) {
          exc.printStackTrace();
        }
      }
    };
//
    mainFrame.getContentPane().add( new JButton(getPassword) );
// Set visible
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
  } //End: Method


} //End: public class SwingMyJDialogForPassword

