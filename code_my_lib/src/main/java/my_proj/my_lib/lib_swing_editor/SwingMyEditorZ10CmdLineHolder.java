// SwingMyEditorZ08CmdLine.java
/*
 * Copyright (C) 2022, 2025 James Everitt
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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import my_proj.my_lib.lib.MyStrTok;
import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_swing.SwingMyJTextFieldSecondaryLoop;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//-----------------------  CLASS: SwingMyEditorZ08CmdLine  --------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class either handles simple command line commands or forwards more complex command line commands.
 *
 * @author James Everitt
 */
final class SwingMyEditorZ10CmdLineHolder
{
  
//private static final boolean DO_TRACE = true;
          
/** The command JTextField created by the constructor */
  private              JTextField               myCmdLineTextField = null;
  
/** The top panel as entered in the constructor */
  private              SwingMyEditorZ04JPanelHolder   myTopPanelHolder = null;
  
/** Secondary loop for listening to yes/no input */
  private              SwingMyJTextFieldSecondaryLoop mySecLoop = null;

  
//--------------------------------------------------------------------------------
//------------------------------  Methods  ---------------------------------------
//--------------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This constructor method ?
 *
 * @param topPanelHolder  Top JPanel of editor
 */
  SwingMyEditorZ10CmdLineHolder ( SwingMyEditorZ04JPanelHolder topPanelHolder ) throws Exception
  {
//    super();
    this.myTopPanelHolder = topPanelHolder;
    this.myCmdLineTextField = new JTextField();
//
    this.myCmdLineTextField.setFocusable(true);
    this.myCmdLineTextField.setMargin( new Insets(2,6,2,6) );  // top, left, bottom, right
// Set my key typed listener
    KeyListener listener = new KeyListener() {
      @Override public void keyTyped(KeyEvent e) {
        SwingMyEditorZ10CmdLineHolder.this.myHandle01KeyTyped(e);
      }
      @Override public void keyPressed(KeyEvent e) { }
      @Override public void keyReleased(KeyEvent e) { }
    };
    this.myCmdLineTextField.addKeyListener(listener);
// Create secondary loop action listener, which directly handles yes or no (y|n) keys
    ActionListener changeActionListener = new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
//        System.out.println(MyTrace.myGetCurrentLevel() + ": got event" );
        SwingMyJTextFieldSecondaryLoop secLp = (SwingMyJTextFieldSecondaryLoop)e.getSource();
        char lastKey = secLp.myGetLastKey();
        if ( lastKey == 'y' || lastKey == 'n' ) secLp.myExit();
//if(DO_TRACE)System.out.println( "  " + "ActionListener.actionPerformed" + ": key= " + lastKey);
      }
    };
// Create secondary loop, which when activated pauses the primary loop and looks for yes or no (y|n) keys in myCmdLineTextField
    this.mySecLoop = new SwingMyJTextFieldSecondaryLoop (
        this.myCmdLineTextField, // JTextField      txtField,
        changeActionListener // ActionListener actionListener
    );
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @return  Returns the JTextField this class contains
 */
  final JTextField myGetJTextField ( ) { return this.myCmdLineTextField; }


//------------------  Method ------------------
/**
 * This  method ?
 *
 * @param e  ?
 */
  final void myHandle01KeyTyped ( KeyEvent e )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": key= " + e.getKeyChar());
    try {
// Clear cmd line
      char keyChar = e.getKeyChar();
      JTextArea textArea = this.myTopPanelHolder.myGetTextAreaHolder().myGetJTextArea();
// Handle escape
      if  ( keyChar == KeyEvent.VK_ESCAPE ) this.myTopPanelHolder.myGetCmdLineHolder().myGetJTextField().setText("");
// Hitting enter reads the line and does the command
      else if ( keyChar == KeyEvent.VK_ENTER ) {
// Return caret to text editor
        textArea.grabFocus();   // textArea.requestFocusInWindow();
// Let text area handle this
        String txt = this.myCmdLineTextField.getText();
        if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": saw return" + ": txt= " + txt );
        this.myCmdLineTextField.setText("");
        if ( txt != null && txt.length() > 0 ) this.myHandle02CmdLnInput(txt);
        this.myTopPanelHolder.myGetTextAreaHolder().myGetJTextArea().grabFocus();   //.requestFocusInWindow();
      }
    } //End: try
    catch (Exception e2) {
      e2.printStackTrace();
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method handles all command line commands
 *
 * @param cmdLn  Command string to be processed
 */
  final void myHandle02CmdLnInput( String cmdLn ) throws Exception
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": cmd= " + cmdLn );
//
    SwingMyEditorZ12Cmd cmd = new SwingMyEditorZ12Cmd(cmdLn, this.myTopPanelHolder.myGetTextAreaHolder());
//
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": " + cmd);
//
    if      ( cmdLn.equals("w") )  SwingMyEditorZ11CmdLineSub.myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.w,      this.myTopPanelHolder );
    else if ( cmdLn.equals("q") )  SwingMyEditorZ11CmdLineSub.myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.q,      this.myTopPanelHolder );
    else if ( cmdLn.equals("wq") ) SwingMyEditorZ11CmdLineSub.myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.wq,     this.myTopPanelHolder );
    else if ( cmdLn.equals("q!") ) SwingMyEditorZ11CmdLineSub.myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.forceQ, this.myTopPanelHolder );
    else if ( cmdLn.equals("c") )  SwingMyEditorZ11CmdLineSub.myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.c,      this.myTopPanelHolder );
    else if ( cmdLn.equals("wc") ) SwingMyEditorZ11CmdLineSub.myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.wc,     this.myTopPanelHolder );
    else if ( cmdLn.equals("c!") ) SwingMyEditorZ11CmdLineSub.myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.forceC, this.myTopPanelHolder );
    else if ( cmdLn.equals("$") )  SwingMyEditorZ11CmdLineSub.myHandle05CommandKey_go_to_last ( this.myTopPanelHolder );
    else if ( cmd.myCmd.equals("/") && cmd.myCmdArgs != null ) this.myTopPanelHolder.myGetTextAreaHolder().myGetHandleKeyTyped().myHandleFindStr( cmd.myCmdArgs[0], this.myTopPanelHolder.myGetTextAreaHolder().myGetJTextArea().getCaretPosition(), true );
    else if ( cmd.myCmd.equals("s") && cmd.myCmdArgs != null ) SwingMyEditorZ11CmdLineSub.myHandle03CmdLnInputHandleReplace ( this.mySecLoop, cmd, this.myTopPanelHolder );
    else if ( cmd.myCmd.equals(SwingMyEditorZ12Cmd.NO_COMMAND) && cmd.myIndexesFrom0 != null && cmd.myIndexesFrom0.length == 1 )
                                       SwingMyEditorZ11CmdLineSub.mySetCharetToLine( cmd.myIndexesFrom0[0], this.myTopPanelHolder.myGetTextAreaHolder() );
    else SwingMyEditorZ16Misc.myShowDialog (
        this.myTopPanelHolder.myGetTextAreaHolder().myGetJTextArea(),                 // Component comp,
        "WARNING: Unsupported cmd line command", // String title,
        "Bad cmd = " + cmdLn             // String msg 
        );
   } //End: Method


//------------------ Main Method ------------------
/**
 * This main method highlights covered commands
 * 
 * @param args String[] of input arguments
 */
  public static void main( String[] args )
  {
    String[] cmds = new String[] {
      "1,$s/aaa/bbb/gc",
      "1d",
      "1,5d",
      ".,$d",
      "-3,.d",
      ".,+4d",
      ".,+3co8",
      "1,5mo9",
      "1,3g/abc/s/def/ghi/",
      ".,+50g/abc/d"
    };
//
    for ( String str : cmds ) {
      ArrayList<String> frags = MyStrTok.myFragmentGeneralExpressionToStringAryLst(str, SwingMyEditorZ12Cmd.MY_VI_SPECIAL_CHARS, MyStrTok.MY_SEPARATE_INTEGERS);
      System.out.print("   ");
      for ( String str2 : frags ) System.out.print(" " + str2);
      System.out.println();
    }
  } //End: main

} //End: Class SwingMyEditorZ08CmdLine
