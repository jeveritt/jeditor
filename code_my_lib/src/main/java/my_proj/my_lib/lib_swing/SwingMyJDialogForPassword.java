// SwingMyJDialogForPassword.java

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
public class SwingMyJDialogForPassword extends JDialog {

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
    SwingMyJDialogForPassword pwd = new SwingMyJDialogForPassword ( parentFrame );
    char[] password = pwd.myPasswordField.getPassword();
    return String.valueOf(password);
  } //End: Method


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor method.
 *
 * @param parentFrame  Parent frame for dialog
 */
  private SwingMyJDialogForPassword ( Window parentFrame )
  {
    super ( parentFrame );
//
    this.setTitle("Password");
// Horizontal bar containing everything
    this.getContentPane().setLayout( new FlowLayout() );
// Password text field
    this.myPasswordField = new JPasswordField(16);
    this.myPasswordField.setText("");
    this.getContentPane().add(this.myPasswordField);
// Handle hitting return to set password
    KeyListener listener = new KeyListener() {
      @Override  public void keyTyped(KeyEvent e) {
        if ( e.getKeyChar() == KeyEvent.VK_ENTER ) { SwingMyJDialogForPassword.this.dispose(); }
      }
//
      @Override public void keyPressed(KeyEvent e) {}
      @Override  public void keyReleased(KeyEvent e) {}
    };
    this.myPasswordField.addKeyListener(listener);
// Accept button
    AbstractAction accept = new AbstractAction("Accept") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) { SwingMyJDialogForPassword.this.dispose(); }
    };
    this.getContentPane().add( new JButton(accept) );
// Cancel button
    AbstractAction cancel = new AbstractAction("Cancel") {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e) {
        SwingMyJDialogForPassword.this.myPasswordField.setText("");
        SwingMyJDialogForPassword.this.dispose();
      }
    };
//
    this.getContentPane().add( new JButton(cancel) );
// Pack and center
    this.pack();
    this.setLocationRelativeTo(parentFrame);
// Lock out other stuff until password is entered
    this.setModalityType(ModalityType.APPLICATION_MODAL);
// Set visible
    this.setVisible(true);
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
//    MyCenterFrame.myCenterFrameOnScreen(mainFrame);
// Create button to activate password dialog
    AbstractAction getPassword = new AbstractAction("Password") {
      private static final long serialVersionUID = 1L;
//
      @Override public void actionPerformed(ActionEvent e) {
        String password = SwingMyJDialogForPassword.myGetPassword(mainFrame);
        System.out.println("SwingMyJDialogForPassword.main" + ": password= <" + password +">");
      }
    };
//
    mainFrame.getContentPane().add( new JButton(getPassword) );
// Set visible
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
  } //End: Method


} //End: public class SwingMyJDialogForPassword

