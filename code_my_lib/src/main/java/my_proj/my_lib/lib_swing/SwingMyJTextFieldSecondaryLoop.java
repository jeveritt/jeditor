// SwingMyJTextFieldSecondaryLoop.java
/*
 * Copyright (C) 2022,2025 James Everitt
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

import java.awt.EventQueue;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import my_proj.my_lib.lib.MyTrace;


//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
//-------------  CLASS: SwingMyJTextFieldSecondaryLoop  -------------------
//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
/**
 * This class allows for the monitoring of typed input from a JTextField instance while blocking the main event queue.
 * An action listener is notified as each key is typed and it is expected to provide the correct response.
 * The action listener needs to eventually shut down the SecondaryLoop created by this constructor to resume normal operation.
 * The main method of this class provides an example of how this class should be used.
 *
 * @author James Everitt
 */
public class SwingMyJTextFieldSecondaryLoop implements KeyListener
{

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 7995165365458226839L;

//static final boolean DO_TRACE = true;
  
  public static final int MY_KEY_TYPED_ID = 1374519780;
  
  private JTextField     myTxtField;
  private ActionListener myActionListener = null;
  private SecondaryLoop  mySecLoop = null;
  private char           myLastKey = 0;
  private ActionEvent    myKeyTypedActionEvent = null;

//------------------------------------------------------------------------
//----------------------  Static Methods:  -------------------------------
//------------------------------------------------------------------------


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This is the constructor.
 *
 * @param txtField         JTextField being monitored
 * @param actionListener  ActionListener that needs to be notified of key being typed
 */
  public SwingMyJTextFieldSecondaryLoop (
      JTextField      txtField,
      ActionListener  actionListener
  ) throws Exception
  {
    if ( txtField == null ) throw new Exception(MyTrace.myGetMethodName() + ": JTextField is null");
    else if ( actionListener == null ) throw new Exception(MyTrace.myGetMethodName() + ": ActionListener is null");
// Save input info
    this.myTxtField = txtField;
    this.myActionListener = actionListener;
// Initialize thread - directly from Java doc for SecondaryLoop
    Toolkit tk = Toolkit.getDefaultToolkit();
    EventQueue eq = tk.getSystemEventQueue();
    this.mySecLoop = eq.createSecondaryLoop();
  } //End: method


//------------------  Method ------------------
/**
 * This method enters the secondary loop.
 */
  public final void myEnter() throws Exception
  {
    this.myTxtField.addKeyListener(this);
    if ( !this.mySecLoop.enter() ) throw new Exception( MyTrace.myGetMethodName() );
  } //End: method


//------------------  Method ------------------
/**
 * This exits the secondary loop.
 */
  public final void myExit() {
    this.myTxtField.removeKeyListener(this);
    this.mySecLoop.exit();
  } //End: method


//------------------  Method ------------------
/**
 * This method gets the last key typed
 */
  public final char myGetLastKey() { return this.myLastKey; }


//---------- Stuff to implement KeyListener ------------------


//------------------  Method ------------------
/**
 * This method listens for a key being typed.
 * 
 * @param e  KeyEvent
 */
  @Override public void keyTyped(KeyEvent e) {
    this.myLastKey = e.getKeyChar();
// Create an action event for a key being typed
    if ( this.myKeyTypedActionEvent == null )
      this.myKeyTypedActionEvent = new ActionEvent(
        this,            // Object source,
        MY_KEY_TYPED_ID, // int id,
        null             // String command
        );
//
    this.myActionListener.actionPerformed(this.myKeyTypedActionEvent);
  } //End: method


//------------------  Method ------------------
/**
 * This method listens for a key being pressed.
 * 
 * @param e  KeyEvent
 */
  @Override public void keyPressed(KeyEvent e) { }


//------------------  Method ------------------
/**
 * This method listens for a key being released.
 * 
 * @param e  KeyEvent
 */
  @Override public void keyReleased(KeyEvent e) { }


// ------------------ Main Method ------------------
/**
 * This method is the runnable test main method.
 * 
 * @param args Unused String[] of input arguments
 */
  public static void main( String[] args )
  {
    try {
      JFrame mainFrame = new JFrame ( "Test SwingMyJTextFieldSecondaryLoop" );
      mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//
      mainFrame.setSize(200,100);
      mainFrame.setLocationRelativeTo(null);  // MyCenterFrame.myCenterFrameOnScreen(mainFrame);
// Create text area
      String txt = "type response? (y|n|q)";
      JTextField txtArea = new JTextField(txt);
      txtArea.setCaretPosition(txt.length());
// Create action listener
      ActionListener listener = new ActionListener() {
        @Override public void actionPerformed(ActionEvent e) {
//          System.out.println(MyTrace.myGetCurrentLevel() + ": got event" );
          SwingMyJTextFieldSecondaryLoop secLp = (SwingMyJTextFieldSecondaryLoop)e.getSource();
          char lastKey = secLp.myLastKey;
System.out.println( "  In secondaryLp" + ": typed " + lastKey );
          if ( lastKey == 'q' || lastKey == KeyEvent.VK_ENTER ) {
System.out.println( "  Closing secondaryLp" );
            secLp.myExit();
          }
        }
      };
// Set frame content pane and start event dispatch queue by setting frame visible
      mainFrame.setContentPane(txtArea);
      mainFrame.setVisible(true);
// Create secondary loop
      System.out.println(MyTrace.myGetMethodName() + ": entering secondary loop");
      SwingMyJTextFieldSecondaryLoop secLp = new SwingMyJTextFieldSecondaryLoop(
          txtArea,  // JTextField      txtArea,
          listener  // ActionListener listener
          );
// Activate secondary loop
      secLp.myEnter();
// Print finished
      System.out.println(MyTrace.myGetMethodName() + ": exited secondary loop");
    } //End: try
    catch (Exception e ) {
      e.printStackTrace();
    }
  } //End: Method

} //End: class SwingMyJTextFieldSecondaryLoop
