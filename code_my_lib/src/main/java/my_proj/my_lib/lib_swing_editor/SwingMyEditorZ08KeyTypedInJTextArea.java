// SwingMyEditorZ10HandleKeyTyped.java
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

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JTextArea;

import my_proj.my_lib.lib.MyTrace;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//------------------  CLASS: SwingMyEditorZ10HandleKeyTyped  ------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class handles keys being typed when focus is in the main JTextArea
 *
 * @author James Everitt
 */
final class SwingMyEditorZ08KeyTypedInJTextArea {
  
//private static final boolean DO_TRACE = true;
  
/** Top JPanel */
  private SwingMyEditorZ04JPanelHolder myTopPanel = null;
  
/** Command currently being executed */
//  private MyCmdForCharTyped            myCmd = new MyCmdForCharTyped();
          StringBuffer                 myCmdSB = null;

/** Used for various purposes depending on operation */
  private Object                       myMisc = null;

/** Used to support yank and put */
  private String                       myYanked = null;


//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------  METHOD:  ------------------
/**
 * This method returns the previous new line char relative to the pointer or -1
 *
 * @param txt  ?
 * @param ptr Starting position in text
 * 
 * @return Returns previous new line char position or -1
 */
  static final int myZ01PreviousNewLine( String txt, int ptr )
  {
    do {
      ptr--;
    } while ( ptr >= 0 && txt.charAt(ptr) != '\n' );
    return ptr;
  } //End: Method


//------------------------------------------------------------------------
//------------------------------  Methods:  ------------------------------
//------------------------------------------------------------------------

//------------------  Method  ------------------
/**
 * This is the constructor method.
 *
 * @param topPanel  ?
 */
  SwingMyEditorZ08KeyTypedInJTextArea ( SwingMyEditorZ04JPanelHolder topPanel )
  {
    this.myTopPanel = topPanel;
  } //End: method


//------------------  Method  ------------------
/**
 * This method resets the command.
 *
 */
  final void myReset( ) {
    this.myCmdSB = null; // new MyCmdForCharTyped();
    this.myTopPanel.myGetCmdLineHolder().myGetJTextField().setText("");
//
    JTextArea textArea = this.myTopPanel.myGetTextAreaHolder().myGetJTextArea();
//    textArea.grabFocus();
    int caretPos = textArea.getCaretPosition();
    int textLength = textArea.getText().length();
//
    if ( caretPos < 0 ) textArea.setCaretPosition(0);
    else if ( caretPos > 0 && caretPos >= textLength) textArea.setCaretPosition(textLength-1);
//if(DO_TRACE) System.out.println("\n" + MyTrace.myGetMethodName() + ": textLen= " + textLength + ": caretPos= " + caretPos);
  } //End: method


//------------------  Method  ------------------
/**
 * This method handles multiple character commands typed into the text area in the command mode.
 *
 * @param keyChar  ?
 * @param canWrite  ?
 *
 */
  final void myHandle02CommandKey ( char keyChar, boolean canWrite ) throws Exception
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + "key= " + keyChar );
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + "key= " + keyChar );
    String ctrlRStr = String.valueOf(0x12); // <ctrl>r
// Get text area holder
    SwingMyEditorZ07TextAreaHolder textAreaHolder = this.myTopPanel.myGetTextAreaHolder();
// Store char and update command
    if ( this.myCmdSB == null ) this.myCmdSB = new StringBuffer();
    boolean sawEnter = keyChar == KeyEvent.VK_ENTER;
    if ( !sawEnter ) this.myCmdSB.append(keyChar);
    SwingMyEditorZ12Cmd cmd = new SwingMyEditorZ12Cmd(this.myCmdSB.toString(), textAreaHolder);
//
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": can write= " + canWrite + ": " + cmd);
//
// Get cmd int value
    int myCmdInitialIntVal = cmd.myIndexesFrom0 != null ? cmd.myIndexesFrom0[0] : 1;
//
// Commands not requiring hitting the enter key followed by forwarding the command when the enter key is typed
    if ( cmd.myCmd != null ) {
// Handle move cursor
      if ( cmd.myCmd.equals("j") || cmd.myCmd.equals("k") || cmd.myCmd.equals("h") || cmd.myCmd.equals("l") ) { this.myHandle05CommandKey_h_j_k_l(keyChar); this.myReset(); }
// Else if handle undo
      else if  ( cmd.myCmd.equals("u") ) { if ( canWrite && textAreaHolder.myGetUndoManager().canUndo() ) textAreaHolder.myGetUndoManager().undo(); this.myReset(); }
// Else if handle redo
      else if ( cmd.myCmd.equals(ctrlRStr) ) { if ( canWrite && textAreaHolder.myGetUndoManager().canRedo() ) textAreaHolder.myGetUndoManager().redo(); this.myReset(); }
// Find next string
      else if ( cmd.myCmd.equals("n") ) { this.myHandle05CommandKey_n(true); this.myReset(); }
      else if ( cmd.myCmd.equals("N") ) { this.myHandle05CommandKey_n(false); this.myReset();; }
// Put back yanked stuff
      else if ( cmd.myCmd.equals("p") ) { if ( canWrite ) this.myHandle05CommandKey_p(); this.myReset(); }
//
      else if ( cmd.myCmd.equals("x") )  { if ( canWrite ) this.myHandle05CommandKey_x( myCmdInitialIntVal ); this.myReset(); }
      else if ( cmd.myCmd.equals("dd") ) { if ( canWrite ) myHandle05CommandKey_yy( myCmdInitialIntVal, true ); this.myReset(); }
      else if ( cmd.myCmd.equals("yy") ) { if ( canWrite ) myHandle05CommandKey_yy( myCmdInitialIntVal, false ); this.myReset(); }
// Else forward the command if the enter key was typed
      else if ( sawEnter ) {
        SwingMyEditorZ10CmdLineHolder cmdLnHolder = this.myTopPanel.myGetCmdLineHolder();
// This requires the command to be re-extracted, but so what
        cmdLnHolder.myHandle02CmdLnInput( this.myCmdSB.toString() );
        this.myReset();
      }
    } //End: if
  } //End: method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param numbLnsToSel  ?
 * @param doDelete  ?
 */
  private final void myHandle05CommandKey_yy ( int numbLnsToSel, boolean doDelete )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": doDel= " + doDelete + ": numbDel= " + numbLines );
    SwingMyEditorZ07TextAreaHolder textAreaHolder = this.myTopPanel.myGetTextAreaHolder();
    JTextArea textArea = textAreaHolder.myGetJTextArea();
    String txt = textArea.getText();
// Get range
    if ( numbLnsToSel == 0 ) numbLnsToSel = 1;
//
    int caretPos = textArea.getCaretPosition();
    if ( caretPos > txt.length() ) {
      caretPos = txt.length();
      textArea.setCaretPosition(caretPos);
    }
    int[] strt = textAreaHolder.myGetLineNumStrtNlAry( caretPos );
    ArrayList<int[]> lines = textAreaHolder.myGetTextArrayList();
//if(DO_TRACE)System.out.print(MyTrace.myGetMethodName() + ": doDel= " + doDelete + ": numbDel= " + numbLnsToSel + " : lines\n" + SwingMyEditorZ16Misc.myLineInfoToString(txt, lines) );
    int maxNumbLnsToSel = lines.size() - strt[0];
    if ( numbLnsToSel > maxNumbLnsToSel ) numbLnsToSel = maxNumbLnsToSel;
    int[] stop = lines.get(strt[0] + numbLnsToSel-1);
// Save old range
    this.myYanked = txt.substring(strt[1], stop[2] + 1);
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": yanked= " + this.myYanked);
// Cut out range
    if ( doDelete ) {
      textArea.replaceRange("", strt[1], stop[2] + 1);
      textAreaHolder.mySetTextHasChanged(true);
      caretPos = textArea.getCaretPosition();
      if ( caretPos > 0 && caretPos >= strt[1]-1 ) textArea.setCaretPosition(strt[1]-1);
    }
//
//if(DO_TRACE)System.out.println("SwingMyEditorZ10HandleKeyTyped.myHandleCommandKey_yy" + ": dlt= " + doDelete + ": strt= " + strt + ": stop= " + stop + ": yanked= <" + this.mySavedYanked + ">");
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param keyChar  ?
 */
  private final void myHandle05CommandKey_h_j_k_l ( char keyChar )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() );
    SwingMyEditorZ07TextAreaHolder textAreaHolder = this.myTopPanel.myGetTextAreaHolder();
    JTextArea textArea = textAreaHolder.myGetJTextArea();
    int pos = textArea.getCaretPosition();
//
// Move left
    if ( keyChar == 'h' && pos > 0 ) pos--;
// Move right
    else if ( keyChar == 'l' && pos < textArea.getText().length() ) pos++;  
//
// Update offset used for going up and down.
//
    boolean recalcOffset = this.myMisc == null ||
                      ( !(this.myMisc instanceof Integer) ) ||
                      keyChar == 'h' || keyChar == 'l'
                    ;
    if ( recalcOffset ) {
      int[] strtLn = textAreaHolder.myGetLineNumStrtNlAry(pos);
      this.myMisc = Integer.valueOf(pos-strtLn[1]);
    }
//
// If move move up or down
//
    if ( keyChar == 'k' || keyChar == 'j' ) {
// Get current line
      ArrayList<int[]> lines = textAreaHolder.myGetTextArrayList();
      int[] strtLn = textAreaHolder.myGetLineNumStrtNlAry(pos);
      int offset = this.myMisc == null || !(this.myMisc instanceof Integer) ? 0 : ((Integer)this.myMisc).intValue();
// Move up
      if ( keyChar == 'k' && strtLn[0] > 0 ) {
        int[] newLn = lines.get(strtLn[0]-1);
        pos = newLn[1] + offset;
        if ( pos > newLn[2] ) pos = newLn[2];
      }
// Move down
      else if ( keyChar == 'j' && strtLn[0] < lines.size()-1 ) {
        int[] newLn = lines.get(strtLn[0]+1);
        pos = newLn[1] + offset;
        if ( pos > newLn[2] ) pos = newLn[2];
      }
    } //End: else
// Do move
    textArea.setCaretPosition(pos);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method finds a string. It can be called from the command line or from the 'n' key.
 *
 * @param strToShiftTo  ?
 * @param startIndx  ?
 * @param goForward  ?
 */
  final void myHandleFindStr( String strToShiftTo, int startIndx, boolean goForward )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() );
    SwingMyEditorZ07TextAreaHolder textAreaHolder = this.myTopPanel.myGetTextAreaHolder();
    JTextArea textArea = textAreaHolder.myGetJTextArea();
    String txt =  textArea.getText();
    int indxOfStr = -1;
//
    if ( goForward ) {
      indxOfStr = txt.indexOf(strToShiftTo, startIndx + 1);
      if ( indxOfStr < 0 ) indxOfStr = txt.indexOf(strToShiftTo, 0);
    }
//
    else {
      indxOfStr = txt.lastIndexOf(strToShiftTo, startIndx - 1);
      if  ( indxOfStr < 0 ) indxOfStr = txt.lastIndexOf(strToShiftTo, txt.length()-1);
    }
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": fwrd= " + goForward + ": str= " + strToShiftTo + ": caretIndx= " + startIndx + ": newIndx= " + indxOfStr );
    if ( indxOfStr >= 0 ) {
      textArea.setCaretPosition( indxOfStr );
      this.myMisc = strToShiftTo;
    }
    else this.myTopPanel.myGetMsgLine().mySetMsg( "could not find text = " + strToShiftTo );
  } //End: Method


//------------------  METHOD:  ------------------
/**
 * This method ?
 *
 */
  private final void myHandle05CommandKey_n( boolean goForward )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": fwd= " + goForward);
    SwingMyEditorZ07TextAreaHolder textAreaHolder = this.myTopPanel.myGetTextAreaHolder();
    String textToFind = this.myMisc == null ? null : (String)this.myMisc;
    if ( textToFind != null ) {
     int pos = textAreaHolder.myGetJTextArea().getCaretPosition();
     this.myHandleFindStr( textToFind, pos, goForward );
    }
    else this.myTopPanel.myGetMsgLine().mySetMsg( "no text specified" );
  } //End: Method


//------------------  METHOD:  ------------------
/**
 * This method ?
 *
 */
  private final void myHandle05CommandKey_x( int numbDel )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": numbDel= " + numbDel );
    SwingMyEditorZ07TextAreaHolder textAreaHolder = this.myTopPanel.myGetTextAreaHolder();
    if ( numbDel == 0 ) numbDel = 1;
    for ( int i1 = 0 ; i1<numbDel ; i1++ ) {
      int pos = textAreaHolder.myGetJTextArea().getCaretPosition();
      if ( pos < textAreaHolder.myGetJTextArea().getText().length() ) {
        textAreaHolder.myGetJTextArea().replaceRange(null, pos, pos+1);
        textAreaHolder.mySetTextHasChanged(true);
      }
    }
  } //End: Method


//------------------  METHOD:  ------------------
/**
 * This method ?
 *
 */
  private final void myHandle05CommandKey_p()
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() );
    SwingMyEditorZ07TextAreaHolder textAreaHolder = this.myTopPanel.myGetTextAreaHolder();
    String savedYanked = this.myYanked == null ? null : this.myYanked;
    if ( savedYanked != null ) {
      int pos = textAreaHolder.myGetJTextArea().getCaretPosition();
      int[] ln = textAreaHolder.myGetLineNumStrtNlAry(pos);
      int strt = ln[2] + 1;
      textAreaHolder.myGetJTextArea().insert(savedYanked, strt);
      textAreaHolder.mySetTextHasChanged(true);
//if(DO_TRACE)System.out.println("SwingMyEditorZ10HandleKeyTyped.myHandleCommandKey_p" + ": pos= " + pos + ": strt= " + strt);
    }
  } //End: Method


} //End: class SwingMyEditorZ10HandleKeyTyped


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//-------------------------  CLASS: MyCmdForCharTyped  ------------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
* This class contains info on the current command.
*
* @author James Everitt
*/
final class MyCmdForCharTyped {

/** Command StringBuffer */
  StringBuffer             myCmdStrBuf = null;
/** Command initial int value */
  int                      myCmdInitialIntVal = 0;
/** Boolean for saw letter */
  boolean                  myCmdSawLetter = false;
/** Command number of typed characters */
  int                      myCmdNumbTypedChar = 0;


//------------------------------------------------------------------------
//------------------------------  Methods:  ------------------------------
//------------------------------------------------------------------------
  

//------------------  Method  ------------------
/**
 * This method provides info
 *
 * @return  Returns info String
 */
  @Override public final String toString ( )
  { return this.getClass().getSimpleName() + ":intVal=" + this.myCmdInitialIntVal + ":cmd=<" + this.myCmdStrBuf.toString() + ">"; }

//------------------  Method  ------------------
/**
 * This is the constructor method.
 *
 */
  MyCmdForCharTyped ( )
  {
    this.myCmdStrBuf = new StringBuffer();
    this.myCmdInitialIntVal = 0;
    this.myCmdSawLetter = false;
    this.myCmdNumbTypedChar = 0;
  } //End: Method

} //End: class MyCmdForCharTyped
