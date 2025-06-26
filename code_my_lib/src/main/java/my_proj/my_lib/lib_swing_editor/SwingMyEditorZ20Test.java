// SwingMyEditorZ20Test.java
/*
 * Copyright (C) 2025 James Everitt
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

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import my_proj.my_lib.lib.MyTrace;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//------------------------  CLASS: SwingMyEditorZ20Test  -----------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class creates a test class.
 *
 * @author James Everitt
 */
public final class SwingMyEditorZ20Test
{

//private static final boolean DO_TRACE = true;
  
  SwingMyEditorZ04JPanelHolder        myTopPanelHolder = null;
  SwingMyEditorZ07TextAreaHolder      myTxtAreaHolder  = null;
  JTextArea                           myTxtArea        = null;
  SwingMyEditorZ08KeyTypedInJTextArea myHandleKeyTyped = null;
  SwingMyEditorZ10CmdLineHolder       myCmdLnHolder    = null;
  JTextField                          myCmdLn          = null;
  
  KeyEvent                            myKeyEvent       = null;
  
//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//--------------------- Method --------------------
/**
 * This is the constructor
 * 
 */
  public SwingMyEditorZ20Test() throws Exception
  {
    int ctrl = 0;
    SwingMyEditorJFrame editor = new SwingMyEditorJFrame (
        "Editor For Test",                    // String     title,
        null,                                 // File       dataFile,
        ctrl,                                 // int        ctrl,
        SwingMyEditorConst.MY_DEFAULT_SIZE_X, // int        sizeX,
        SwingMyEditorConst.MY_DEFAULT_SIZE_Y  // int        sizeY
     );
//
    System.out.print( "\n" + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl) + "\n  Permissions:" + "\n" + SwingMyEditorCtrl.myGetPermissions("    ") );
// Get components
    this.myTopPanelHolder = editor.myGetTopPanel();
    this.myTxtAreaHolder = this.myTopPanelHolder.myGetTextAreaHolder();
    this.myTxtArea = this.myTxtAreaHolder.myGetJTextArea();
    this.myHandleKeyTyped = this.myTxtAreaHolder.myGetHandleKeyTyped();
    this.myCmdLnHolder = this.myTopPanelHolder.myGetCmdLineHolder();
    this.myCmdLn = this.myCmdLnHolder.myGetJTextField();
 //
    this.myKeyEvent = new KeyEvent(
        this.myCmdLn,            // Component source,
        0,                       // int id,
        0L,                      // long when,
        0,                       // int modifiers,
        KeyEvent.VK_ENTER,       // int keyCode
        (char)KeyEvent.VK_ENTER  // char keyChar
        );
//
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": enter" + ": isVis= " + editor.isVisible() + ": isValid= " + editor.isValid() + ": isActive= " + editor.isActive() + ": focusOwner= " + SwingMyEditorZ16Misc.myGetFocusOwner()  + ": txtAreaHasFocus= " + this.myTxtArea.hasFocus() + ": txtAreaIsFocusOwner= " + this.myTxtArea.isFocusOwner() + ": cmdLnHasFocus= " + this.myCmdLn.hasFocus()+ ": cmdLnIsFocusOwner " + this.myCmdLn.isFocusOwner() );
// Run tests
    boolean ok = true;
    if ( ok ) ok = myTst1( );
    if ( ok ) ok = myTst2( );
    if ( ok ) ok = myTst3( );
    if ( ok ) ok = myTst4( );
    if ( ok ) ok = myTst5( );
    if ( ok ) ok = myTst6( );
    if ( ok ) ok = myTst7( );
//
    System.out.println( "\n" +  MyTrace.myGetMethodName() + ": Successfully run test = " + ok);
  } //End: method


//--------------------- Method --------------------
/**
 * This method resets the configuration for each test
 */
  private final KeyEvent myGetKeyEvent ( char key )
  {
    this.myKeyEvent.setKeyChar(key);
    this.myKeyEvent.setKeyCode(key);
    return this.myKeyEvent;
  } //End: method


//--------------------- Method --------------------
/**
 * This method resets the configuration for each test
 */
  private final void myResetForTest ( )
  {
    this.myTxtAreaHolder.myUndo(200);
    this.myTxtAreaHolder.mySetMode( SwingMyEditorZ07TextAreaHolder.MY_MODES.cmd );
    this.myTxtArea.grabFocus();   // this.myTxtArea.requestFocusInWindow();
    this.myTxtArea.setCaretPosition(0);
  } //End: method


//--------------------- Method --------------------
/**
 * Test 1: Reads in a known String
 * @param topPanelHolder  Top edit panel
 * @return  Return true if no problems
 */
  private final boolean myTst1 ( )
  {
    String data = new String (
        "01aa45" + "\n" +
        "7aa01" + "\n" +
        "3tagaa9aa" + "\n" +
        "3xx6" + "\n" +
        "\n" +
        "9aa2"
        );
    this.myTxtArea.setText(data);
    this.myTxtAreaHolder.myGetUndoManager().discardAllEdits();
//
    ArrayList<int[]> lines = this.myTxtAreaHolder.myGetTextArrayList();
    System.out.print( "\n" +  MyTrace.myGetMethodName() + ": Lines:\n" + SwingMyEditorZ16Misc.myLineInfoToString( this.myTxtArea.getText(), lines ) );
    return this.myTxtAreaHolder.myGetLine(5).equals("9aa2");
  } //End: method


//--------------------- Method --------------------
/**
 * Test 2: Test go to line 3
 * @return  Return true if no problems
 */
  private final boolean myTst2 ( )
  {
    this.myResetForTest();
    this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('3') );
    this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent( (char)KeyEvent.VK_ENTER) );
    int caretPos = this.myTxtArea.getCaretPosition();
//System.out.println(MyTrace.myGetMethodName() + ": caret = " + caretPos);
    boolean ok = caretPos == 13;
    if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": caret wrong at " + caretPos);
    return ok;
  } //End: method
  

//--------------------- Method --------------------
/**
 * Test 3: Text character delete
 * @return  Return true if no problems
 */
  private final boolean myTst3 ( )
  {
    this.myResetForTest();
    boolean ok = true;
//
    if ( ok ) {
      this.myTxtArea.setCaretPosition(4);
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('2') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('x') );
      ok = this.myTxtArea.getText().charAt(4) == '\n';
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": failed deleting 2 chars");
    } //End: if ( ok )
    if ( ok ) {
      this.myTxtArea.setCaretPosition(0);
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('x') );
      ok = this.myTxtArea.getText().charAt(0) == '1';
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": failed deleting first chars");
    } //End: if ( ok )
//
    return ok;
  } //End: method


//--------------------- Method --------------------
/**
 * Test 4: Test the line move, yank, and delete commands
 * @return  Return true if no problems
 */
  private final boolean myTst4 ( )
  {
    boolean ok = true;
// Try yank and put
    if ( ok ) {
      this.myResetForTest();
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('j') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('y') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('y') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('j') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('p') );
      String ln1Frm0 = this.myTxtAreaHolder.myGetLine(1);
      String ln3Frm0 = this.myTxtAreaHolder.myGetLine(3);
      ok = ln3Frm0.equals(ln1Frm0);
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": failed yank and put");
    } //End: if ( ok )
// Try delete 2 lines
    if ( ok ) {
      this.myResetForTest();
//
      String ln3Frm0AtStart = this.myTxtAreaHolder.myGetLine(3);
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('j') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('2') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('d') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('d') );
      String ln1Frm0 = this.myTxtAreaHolder.myGetLine(1);
      ok = ln1Frm0.equals(ln3Frm0AtStart);
//
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": failed delete");
    } //End: if ( ok )
//
    return ok;
  } //End: method


//--------------------- Method --------------------
/**
 * Test 5: Test the substitution command
 * @return  Return true if no problems
 */
  private final boolean myTst5 ( )
  {
//if(DO_TRACE)System.out.println( MyTrace.myGetMethodName() + ": entered" + ": focusOwner= " + SwingMyEditorZ16Misc.myGetFocusOwner() );
//if(DO_TRACE)System.out.println( MyTrace.myGetMethodName() + ": entered" + ": txtAreaHasFocus= " + this.myTxtArea.hasFocus() + ": cmdLnHasFocus= " + this.myCmdLn.hasFocus() );
    boolean ok = true;
//
    if ( ok ) {
      this.myResetForTest();
//
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent(':') );
      this.myCmdLn.setText("1,$s/aa/-zz-/");
      this.myCmdLnHolder.myHandle01KeyTyped( this.myGetKeyEvent( (char)KeyEvent.VK_ENTER) );
      ok           = this.myTxtAreaHolder.myGetLine(0).equals("01-zz-45");
      if ( ok ) ok = this.myTxtAreaHolder.myGetLine(2).equals("3tag-zz-9aa");
//
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": cmd= <1,$s/aa/-zz-/>");
    }
//
    if ( ok ) {
      this.myResetForTest();
//
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent(':') );
      this.myCmdLn.setText("1,$g/tag/s/aa/-zz-/");
      this.myCmdLnHolder.myHandle01KeyTyped( this.myGetKeyEvent( (char)KeyEvent.VK_ENTER) );
//
      ok           = this.myTxtAreaHolder.myGetLine(0).equals("01aa45");
      if ( ok ) ok = this.myTxtAreaHolder.myGetLine(2).equals("3tag-zz-9aa");
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": cmd= <1,$g/tag/s/aa/-zz-/>");
    }
//
    if ( ok ) {
      this.myResetForTest();
//
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent(':') );
      this.myCmdLn.setText("3s/aa/-zz-/g");
      this.myCmdLnHolder.myHandle01KeyTyped( this.myGetKeyEvent( (char)KeyEvent.VK_ENTER) );
      ok           = this.myTxtAreaHolder.myGetLine(0).equals("01aa45");
      if ( ok ) ok = this.myTxtAreaHolder.myGetLine(2).equals("3tag-zz-9-zz-");
//
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": cmd= <3s/aa/-zz-/g>");
    }
//
    return ok;
  } //End: method


//--------------------- Method --------------------
/**
 * Test 6: Do search
 * @return  Return true if no problems
 */
  private final boolean myTst6 ( )
  {
    boolean ok = true;
//
    if ( ok ) {
      this.myResetForTest();
//
// Search from text area
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('1') );
      this.myTxtAreaHolder.myHandleKeyTyped( this.myGetKeyEvent( (char)KeyEvent.VK_ENTER) );
//
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('/') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('7') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('a') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('a') );
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent('0') );
      this.myTxtAreaHolder.myHandleKeyTyped( this.myGetKeyEvent( (char)KeyEvent.VK_ENTER) );
//
      int[] ln = this.myTxtAreaHolder.myGetLineNumStrtNlAry( this.myTxtAreaHolder.myGetJTextArea().getCaretPosition() );
      ok = ln != null && ln[0] == 1;
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": search from text area" + ": cmd= </7aa0>");
    }
//
// Search from command line
    if ( ok ) {
      this.myResetForTest();
//
      this.myTxtAreaHolder.myHandleKeyTyped ( this.myGetKeyEvent(':') );
      this.myCmdLn.setText("/tagaa9aa");
      this.myCmdLnHolder.myHandle01KeyTyped( this.myGetKeyEvent( (char)KeyEvent.VK_ENTER) );
      //
      int[] ln = this.myTxtAreaHolder.myGetLineNumStrtNlAry( this.myTxtAreaHolder.myGetJTextArea().getCaretPosition() );
      ok = ln != null && ln[0] == 2;
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName()+ ": search from command line" + ": cmd= </tagaa9aa>");
    }
//
    return ok;
  } //End: method


//--------------------- Method --------------------
/**
 * Test 7: -- For now, just restore initial text --
 * @return  Return true if no problems
 */
  private final boolean myTst7 ( )
  {
    boolean ok = true;
//
    if ( ok ) {
      this.myResetForTest();
//

//
      if ( !ok ) System.out.println( "failed " + MyTrace.myGetMethodName() + ": ??");
    }
//
    return ok;
  } //End: method


// ------------------ Main Method ------------------
/**
 * This method is the runnable main method.
 * <pre>
 * </pre>
 * 
 * @param args String[] of input arguments
 */
  public static void main( String[] args )
  {
    try {
      new SwingMyEditorZ20Test();
    }
    catch (Exception e) {
      System.out.println( MyTrace.myGetMethodName() + ": exc= " + e.getMessage() );
      e.printStackTrace();
    }
  } //End: method


} //End: class SwingMyEditorZ20Test
