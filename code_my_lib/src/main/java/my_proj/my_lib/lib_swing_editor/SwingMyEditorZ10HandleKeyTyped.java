// SwingMyEditorZ08HandleKeyTyped.java
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

import java.util.ArrayList;

import my_proj.my_lib.lib.MyTrace;


//--------------------------------------------------------------------
//------------------  CLASS: SwingMyEditorZ08HandleKeyTyped  -------------------
//--------------------------------------------------------------------
/**
 * This class
 *
 * @author James Everitt
 */
final class SwingMyEditorZ10HandleKeyTyped {
  
//private static final boolean DO_TRACE = true;
  
  private SwingMyEditorZ07TextArea myTextArea = null;

  private SwingMyEditorZ09MsgLine  myMsgLine = null;
  
  private String                   myTextToFind = null;
  
  private StringBuffer             myCmdStrBuf = null;

  private String                   mySavedYanked = null;


//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------------------------------------------------------------
//------------------------------  Methods:  ------------------------------
//------------------------------------------------------------------------

//------------------  Method  ------------------
/**
 * This is the constructor method.
 *
 * @param textArea  ?
 * @param msgLine  ?
 */
  SwingMyEditorZ10HandleKeyTyped ( SwingMyEditorZ07TextArea textArea, SwingMyEditorZ09MsgLine msgLine )
  {
    this.myTextArea = textArea;
//    this.myCmdLine = cmdLine;
    this.myMsgLine = msgLine;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method is the entry into handling most keys typed into the text area in the command mode.
 *
 * @param keyChar  ?
 */
  final void myHandleCommandKey( char keyChar, boolean canWrite )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": key= " + keyChar + ": equals n = " + (keyChar == 'n') );
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + "key= " + keyChar );
// Move cursor
    if ( keyChar == 'j' || keyChar == 'k' || keyChar == 'h' || keyChar == 'l' ) this.myHandleCommandKey_h_j_k_l(keyChar);
// Delete char
    else if ( keyChar == 'x' ) { if ( canWrite ) this.myHandleCommandKey_x(); }
// Find next string
    else if ( keyChar == 'n' ) this.myHandleCommandKey_n(true);
    else if ( keyChar == 'N' ) this.myHandleCommandKey_n(false);
// Put back yanked stuff
    else if ( keyChar == 'p' ) { if ( canWrite ) this.myHandleCommandKey_p(); }
// Handle multiple key commands
    else this.myHandleCommandKey_multiple( keyChar, canWrite );
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param keyChar  ?
 *
 */
  private final void myHandleCommandKey_multiple ( char keyChar, boolean canWrite )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + "key= " + keyChar );
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + "key= " + keyChar );
// Add character to command string buffer
    if ( this.myCmdStrBuf == null ) this.myCmdStrBuf = new StringBuffer();
    this.myCmdStrBuf.append(keyChar);
// Need at least 2 characters
    if ( this.myCmdStrBuf.length() >= 2 ) {
      String cmdStr = this.myCmdStrBuf.toString();
// Fragment command
      ArrayList<String> frags = SwingMyEditorZ11Misc.myStringParser(cmdStr, "/,", true);
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + "key= " + keyChar + ": sb= " + this.myCmdStrBuf.toString() + ": frags= " + SwingMyEditorZ11Misc.myArrayListToString(frags));
// Decide if first word is an integer
      char ch00 = frags.get(0).charAt(0);
      int firstInt = ch00 >= '0' && ch00 <= '9' ? Integer.parseInt(frags.get(0)) : 1;
// See if I can execute line in command string buffer
      boolean foundCmd = true;
//
      if ( ( frags.size() == 1 && frags.get(0).equals("dd") ) ||
           ( frags.size() == 2 && firstInt > 0 && frags.get(1).equals("dd") ) )
                         { if ( canWrite ) myHandleCommandKey_yy(firstInt, true); }
      else if ( ( frags.size() == 1 && frags.get(0).equals("yy") ) ||
                ( frags.size() == 2 && firstInt > 0 && frags.get(1).equals("yy") ) )
                        myHandleCommandKey_yy(firstInt, false);
//
      else foundCmd = false;
// If I can execute command then clear command string buffer
      if ( foundCmd ) this.myCmdStrBuf = null;
// Else if command string buffer gets too full then a command was missed.
      else if ( cmdStr.length() > 4 ) {
        this.myMsgLine.mySetMsg( "key/keys not supported: " + cmdStr);
        this.myCmdStrBuf = null;
      }
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param numbLines  ?
 * @param doDelete  ?
 */
  private final void myHandleCommandKey_yy ( int numbLines, boolean doDelete )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() );
    String txt = this.myTextArea.getText();
// Get range
    int pos = this.myTextArea.getCaretPosition();
    int strt = myZ01PreviousNewLine(pos) + 1;
    int stop = ( strt <= 0 ) ? 0 : strt;
    for ( int i1=0 ; i1<numbLines ; i1++ ) {
      stop = myZ01NextNewLine( stop );
      if ( stop >= txt.length() ) break;
    }
    if ( stop < txt.length() ) stop++;
// Save old range
    this.mySavedYanked = txt.substring(strt, stop);
// Cut out range
    if ( doDelete ) {
      this.myTextArea.replaceRange("", strt, stop);
      this.myTextArea.mySetNeedToWrite(true);
    }
//
//if(DO_TRACE)System.out.println("SwingMyEditorZ08HandleKeyTyped.myHandleCommandKey_yy" + ": dlt= " + doDelete + ": strt= " + strt + ": stop= " + stop + ": yanked= <" + this.mySavedYanked + ">");
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @param keyChar  ?
 */
  private final void myHandleCommandKey_h_j_k_l ( char keyChar )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() );
    int pos = this.myTextArea.getCaretPosition();
    String txt = this.myTextArea.getText();
    int len = txt.length();
// Move left
    if ( keyChar == 'h' && pos > 0 ) pos--;
// Move right
    else if ( keyChar == 'l' && pos < len ) pos++;
// Move up
    else if ( keyChar == 'k' ) {
// Get previous 2 new line character positions
      int prevNl0 = myZ01PreviousNewLine( pos );
      int prevNl1 = myZ01PreviousNewLine( prevNl0 );
// New position is 2 new line characters back plus current position offset from last new line character
      int newPos = prevNl1 + pos - prevNl0;
// Ensure new position does not over-run previous line length
      if ( newPos > prevNl0 ) newPos = prevNl1;
// If not already on first line, set new position
      if ( newPos >= 0 ) pos = newPos;
    }
// Move down
    else if ( keyChar == 'j' ) {
// Get my current position offset from last new line character
      int prevNl = myZ01PreviousNewLine( pos );
      int myOffset = pos - prevNl;
// Get next new line char position
      int nextNl = this.myZ02ThisOrNextNewLine(pos);
//if(DO_TRACE)System.out.println(" reach 1" + "nextNl= " + nextNl);
// Calculate new position
      pos = nextNl + myOffset;
      if ( pos > len ) pos = len;
    }
// Do move
    this.myTextArea.setCaretPosition(pos);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method finds a string. It can be called from the command line or from the 'n' key.
 *
 * @param strToShiftTo  ?
 */
  final void myHandleFindStr( String strToShiftTo, int startIndx, boolean goForward )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() );
    String txt =  this.myTextArea.getText();
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
      this.myTextArea.setCaretPosition( indxOfStr );
      this.myTextToFind = strToShiftTo;
    }
    else this.myMsgLine.mySetMsg( "could not find text = " + strToShiftTo );
  } //End: Method


//------------------  METHOD:  ------------------
/**
 * This method ?
 *
 */
  private final void myHandleCommandKey_n( boolean goForward )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": fwd= " + goForward);
    if ( this.myTextToFind != null ) {
     int pos = this.myTextArea.getCaretPosition();
     this.myHandleFindStr( this.myTextToFind, pos, goForward );
    }
    else this.myMsgLine.mySetMsg( "no text specified" );
  } //End: Method


//------------------  METHOD:  ------------------
/**
 * This method ?
 *
 */
  private final void myHandleCommandKey_x()
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() );
    int pos = this.myTextArea.getCaretPosition();
    if ( pos < this.myTextArea.getText().length() ) {
      this.myTextArea.replaceRange(null, pos, pos+1);
      this.myTextArea.mySetNeedToWrite(true);
    }
  } //End: Method


//------------------  METHOD:  ------------------
/**
 * This method ?
 *
 */
  private final void myHandleCommandKey_p()
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() );
    if ( this.mySavedYanked != null ) {
      int pos = this.myTextArea.getCaretPosition();
      int strt = this.myZ02ThisOrNextNewLine(pos) + 1;
      this.myTextArea.insert(this.mySavedYanked, strt);
      this.myTextArea.mySetNeedToWrite(true);
//if(DO_TRACE)System.out.println("SwingMyEditorZ08HandleKeyTyped.myHandleCommandKey_p" + ": pos= " + pos + ": strt= " + strt);
    }
  } //End: Method


//------------------  METHOD:  ------------------
/**
 * This method returns the previous new line char relative to the pointer or -1
 *
 * @param ptr Starting position in text
 * 
 * @return Returns previous new line char position or -1
 */
  private final int myZ01PreviousNewLine( int ptr ) { return myZ01PreviousNewLine( this.myTextArea.getText(), ptr ); }


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


//------------------  METHOD:  ------------------
/**
 * This method returns the previous new line char relative to the pointer or -1
 *
 * @param ptr Starting position in text
 * 
 * @return Returns previous new line char position or -1
 */
  private final int myZ01NextNewLine( int ptr )
  {
    String txt = this.myTextArea.getText();
    do {
      ptr++;
    } while ( ptr < txt.length() && txt.charAt(ptr) != '\n' );
    return ptr;
  } //End: Method


//------------------  METHOD:  ------------------
/**
 * This method returns the current or next new line char relative to the pointer or the length of the text
 *
 * @param ptr Starting position in text
 * 
 * @return Returns current or next new line char relative to the pointer or the length of the text
 */
  private final int myZ02ThisOrNextNewLine( int ptr )
  {
    String txt = this.myTextArea.getText();
    while ( ptr < txt.length() && txt.charAt(ptr) != '\n' ) ptr++;
    return ptr;
  } //End: Method


} //End: class SwingMyEditorZ08HandleKeyTyped
