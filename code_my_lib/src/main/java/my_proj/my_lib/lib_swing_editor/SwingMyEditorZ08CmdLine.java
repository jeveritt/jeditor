// SwingMyEditorZ06CmdLine.java
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

import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import my_proj.my_lib.lib.MyTrace;


//--------------------------------------------------------------------
//------------------  CLASS: SwingMyEditorZ06CmdLine  ------------------
//--------------------------------------------------------------------
/**
 * This class
 *
 * @author James Everitt
 */
final class SwingMyEditorZ08CmdLine extends JTextField {
  
//  private static final boolean DO_TRACE = true;
  
  private static final long serialVersionUID = -7505439763844420457L;

  private SwingMyEditorZ07TextArea myTextArea = null;
  
  private SwingMyEditorZ09MsgLine  myMsgLine = null;
  

//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This constructor method ?
 *
 */
  SwingMyEditorZ08CmdLine ( )
  {
    super();
//
    super.setMargin( new Insets(2,6,2,6) );  // top, left, bottom, right
//
    KeyListener listener = new KeyListener() {
      @Override public void keyTyped(KeyEvent e) { SwingMyEditorZ08CmdLine.this.myHandle01KeyTyped(e); }
      @Override public void keyPressed(KeyEvent e) { }
      @Override public void keyReleased(KeyEvent e) { }
    };
    super.addKeyListener(listener);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 * 
 * @param textArea  ?
 * @param msgLine  ?
 */
  final void mySetTextAreaAndMsgLines( SwingMyEditorZ07TextArea textArea, SwingMyEditorZ09MsgLine msgLine ) { this.myTextArea = textArea; this.myMsgLine = msgLine; }


//------------------  Method ------------------
/**
 * This  method ?
 *
 * @param e  ?
 */
  private final void myHandle01KeyTyped ( KeyEvent e )
  {
//
    this.myMsgLine.mySetMsg(null);
//
// Hitting enter reads the line and does the command
    if ( e.getKeyChar() == KeyEvent.VK_ENTER ) {
// Return caret to text editor
      SwingMyEditorZ07TextArea.mySetCaretTo(this.myTextArea);
// Let text area handle this
      String txt = super.getText();
      if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": saw return" + ": txt= " + txt );
      super.setText("");
      if ( txt != null && txt.length() > 0 ) this.myHandle02CmdLnInput(txt);
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method handles all command line commands
 *
 * @param cmd  Command string to be processed
 */
  private final void myHandle02CmdLnInput( String cmd )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": cmd= " + cmd );
//
    if ( cmd.charAt(0) == '/' && cmd.length() > 1 ) this.myTextArea.myGetHandleKeyTyped().myHandleFindStr( cmd.substring(1), this.myTextArea.getCaretPosition(), true );
    else if ( cmd.equals("w") )  myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.w );
    else if ( cmd.equals("q") )  myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.q );
    else if ( cmd.equals("wq") ) myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.wq );
    else if ( cmd.equals("q!") ) myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT.forceQ );
    else if ( cmd.equals("1") ) this.myTextArea.setCaretPosition(0);
    else if ( cmd.equals("$") ) {
      String txt = this.myTextArea.getText();
      int lastNl = 1 + SwingMyEditorZ10HandleKeyTyped.myZ01PreviousNewLine(txt, txt.length()-1);
      this.myTextArea.setCaretPosition(lastNl);
    }
//
    else this.myMsgLine.mySetMsg( "cmd line cmd not understood: " + cmd);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method handles writing and exiting.
 *
 * @param strToShiftTo  ?
 * @param strtIndx  ?
 */
  private final void myHandle03CmdLnInputHandleWriteAndExit( SwingMyEditorConst.MY_WRITE_AND_EXIT cmd )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": cmd= " + cmd );
//
    boolean hasChanged = this.myTextArea.myGetHasChanged();
// Do: 'w'
    if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.w && hasChanged ) {
      try {
        this.myTextArea.mySaveFile();
      }
      catch (Exception e) { this.myMsgLine.mySetMsg( "could not save file" + ": exc= " + e.getMessage() ); }
    } //End: if ( justWrite )
// Do: 'q'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.q ) {
      if ( hasChanged ) this.myMsgLine.mySetMsg( "need to write before exit");
      else this.myTextArea.myCloseWindow();
    }
// Do: 'wq'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.wq ) {
      if ( hasChanged && this.myTextArea.myGetDataFile() == null ) this.myMsgLine.mySetMsg( "no place to save data");
      else if ( hasChanged ) {
        try {
          this.myTextArea.mySaveFile();
          this.myTextArea.myCloseWindow();
        }
        catch (Exception e) { this.myMsgLine.mySetMsg( "could not save file" + ": exc= " + e.getMessage() ); }
      }
      else {
        this.myTextArea.myCloseWindow();
      }
    }
// Do: 'q!'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.forceQ ) {
      this.myTextArea.mySetNeedToWrite(false);
      this.myTextArea.myCloseWindow();
    }
  } //End: Method


} //End: Class SwingMyEditorZ06CmdLine
