// SwingMyEditorZ08CmdLineSub.java
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

import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_swing.SwingMyJTextFieldSecondaryLoop;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//----------------------  CLASS: SwingMyEditorZ08CmdLineSub  ------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class handles more complex command line commands.
 *
 * @author James Everitt
 */
final class SwingMyEditorZ11CmdLineSub {

//private static final boolean DO_TRACE = true;

//------------------------------------------------------------------------
//-----------------------  Static Methods:  ------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method handles writing and exiting.
 *
 * @param cmd  ?
 * @param topPanel  ?
 */
  static final void myHandle03CmdLnInputHandleWriteAndExit ( SwingMyEditorConst.MY_WRITE_AND_EXIT cmd, SwingMyEditorZ04JPanelHolder topPanel )
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() + ": cmd= " + cmd );
//
    SwingMyEditorZ07TextAreaHolder textArea = topPanel.myGetTextAreaHolder();
    boolean hasChanged = textArea.myGetTextHasChanged();
// Do: 'w'
    if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.w && hasChanged )    
      textArea.mySaveFile(
          textArea.myGetDataFileName(),                      // String  fileName,
          SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED // int     numbOldToSave
      );
// Do: 'q'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.q ) {
      topPanel.windowClosing(null);
    }
// Do: 'wq'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.wq ) {
      textArea.mySaveFile(
          textArea.myGetDataFileName(),                       // String  fileName,
          SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED  // int     numbOldToSave
      );
      topPanel.windowClosing(null);
    }
// Do: 'q!'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.forceQ ) {
      textArea.mySetTextHasChanged(false);
      topPanel.windowClosing(null);
    }
// Do: 'c'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.c ) {
      textArea.myCloseFile();
    }
// Do: 'wc'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.wc ) {
      textArea.mySaveFile(
          textArea.myGetDataFileName(),                       // String  fileName,
          SwingMyEditorConst.MY_DEFAULT_NUMB_OLD_FILES_SAVED  // int     numbOldToSave
      );
      textArea.myCloseFile();
    }
// Do: 'c!'
    else if ( cmd == SwingMyEditorConst.MY_WRITE_AND_EXIT.forceC) {
      textArea.mySetTextHasChanged(false);
      textArea.myCloseFile();
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method sets the text area caret to a specific line.
 *
 * @param lineNumFrm1  ?
 * @param textArea  ?
 */
  static final void mySetCharetToLine ( int lineNumFrm1, SwingMyEditorZ07TextAreaHolder textArea )
  {
    ArrayList<Integer> lines = SwingMyEditorZ16Misc.myGetLineStartIndexes(textArea.myGetJTextArea().getText());
// Recalc for line numbers from 0
    lineNumFrm1--;
    if ( lineNumFrm1 >= 0 && lineNumFrm1 < lines.size() ) {
      int pos = lines.get(lineNumFrm1).intValue();
      textArea.myGetJTextArea().setCaretPosition(pos);
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  static final void myHandle05CommandKey_go_to_last ( SwingMyEditorZ04JPanelHolder topPanel )
  {
    SwingMyEditorZ07TextAreaHolder textAreaHolder = topPanel.myGetTextAreaHolder();
    JTextArea textArea = textAreaHolder.myGetJTextArea();
    ArrayList<Integer> lnStrtIdxs = SwingMyEditorZ16Misc.myGetLineStartIndexes(textArea.getText());
    textArea.setCaretPosition(lnStrtIdxs.getLast().intValue());
    textAreaHolder.myGetHandleKeyTyped().myReset();
  } //End: method


//------------------  Method  ------------------
/**
 * This static method 
 *
 * @param cmd  ?
 * @param topPanel  ?
 */
  static final void myHandle03CmdLnInputHandleReplace (
      SwingMyJTextFieldSecondaryLoop secLoop,
      SwingMyEditorZ12Cmd            cmd,
      SwingMyEditorZ04JPanelHolder   topPanel
      )
  {
    try {
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": cmd= " + cmd);
    if ( cmd.myCmdArgs == null || cmd.myCmdArgs.length != 2 )
      SwingMyEditorZ16Misc.myShowDialog(topPanel.myGetTopJPanel(), "Warning: Missing args", "Missing args: cmd= " + cmd);
    else {
// Decrement indexes to account for line numbers starting at 0
      cmd.myDecrementIndexesBy1();
//
      SwingMyEditorZ07TextAreaHolder textAreaHolder = topPanel.myGetTextAreaHolder();
      JTextArea textArea = textAreaHolder.myGetJTextArea();
//      String txt = textArea.getText();
      boolean global = cmd.myLnEnd != null && cmd.myLnEnd.contains("g");
      boolean confirm = cmd.myLnEnd != null && cmd.myLnEnd.contains("c");
      JTextField cmdLn = topPanel.myGetCmdLineHolder().myGetJTextField();
      String src = cmd.myCmdArgs[0];
      String replace = cmd.myCmdArgs[1];
// Get document
//      PlainDocument doc = (PlainDocument)textArea.myGetJTextArea().getDocument();
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": cmd= " + cmd + ": doc= " + doc);
//
      int loopCnt = 0;
      ArrayList<int[]> lines = textAreaHolder.myGetTextArrayList();
// Get index within text for starting ln
 //     int[] currentLn = lines.get(cmd.myIndexesFrom0[0]);
      int lnIdx = cmd.myIndexesFrom0[0];
// Set caret position to start of first line of interest
      int caret = lines.get(lnIdx)[1];
//
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 1" + ": gl= " + global + ": conf= " + confirm + "\n");
//
// Start loop, looking for all matches
//
      String txt;
      while ( ++loopCnt < 100000 && caret < ( txt = textArea.getText() ).length() ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": in while 1" + ":lnStrt= " + lnStrtIdx + ": caret= " + caret + ": inLnCnt= " + inLnCnt);
//
// Update caret position and caret position corresponding line
//  If special start or end of line, then advance by 1 line after first pass
        if ( src.equals("^") || src.equals("$") ) {
          if ( loopCnt > 1 ) lnIdx++; // currentLn = lines.get(currentLn[0]+1);
          if ( lnIdx >= lines.size() ) break;
          if ( src.equals("^") ) caret = lines.get(lnIdx)[1]; //   currentLn[1];
          else  caret = lines.get(lnIdx)[2]; // currentLn[2];
        }
// Else advance caret to next string match and update line index
        else {
          caret = txt.indexOf(src, caret);
          if ( caret < 0 ) break;
          int[] currentLn = textAreaHolder.myGetLineNumStrtNlAry(caret);
          lnIdx = currentLn[0];
        }
//
// Break on caret outside of text area
//
// If could not find new match
        int[] finalLn = lines.get(cmd.myIndexesFrom0[1]);
        if ( caret < 0 ) break;
// Else check if still in range
        else if ( caret >= finalLn[2] ) break;
//if(DO_TRACE)System.out.println( "\n" + "  " + MyTrace.myGetMethodName() + ": in while 2" + ":lnStrt= " + lnStrtIdx + ": caret= " + caret + ": inLnCnt= " + inLnCnt);
// Finally update caret position in text area
        textArea.setCaretPosition(caret);
//
// Continue if not acceptable line
//
        if ( cmd.myGlobalArg != null ) {
//          SwingMyEditorZ12LineInternal curLn = new SwingMyEditorZ12LineInternal(caret, txt);
          int glIdx = txt.indexOf(cmd.myGlobalArg, lines.get(lnIdx)[1]); // currentLn[1]);
          if ( glIdx < 0 || glIdx > lines.get(lnIdx)[2] ) { // currentLn[2] ) {
            caret = lines.get(lnIdx)[2]; // currentLn[2];
            continue;
          }
        }
//
// If need to confirm
//
        boolean change = true;
        if ( confirm ) {
//if(DO_TRACE)System.out.println( "  " + MyTrace.myGetMethodName()+ ": start confirm" );
          cmdLn.grabFocus();  // cmdLn.requestFocusInWindow();
          try {
            secLoop.myEnter();
// Note: secondary loop will be exited by typing y|n in cmdLn under control of ActionListener attached to secLoop.
            if ( secLoop.myGetLastKey() != 'y' ) change = false;
          }
          catch (Exception e) { System.out.println(MyTrace.myGetMethodName() + ": exc= " + e.getMessage()); }
        } //End: if ( confirm )
//if(DO_TRACE)System.out.println( "  " + MyTrace.myGetMethodName()+ ": in while 4" );
        if ( !change ) {
          if ( !src.equals("^") && !src.equals("$") ) caret = caret + src.length();
          continue;
        }
//
// Do substitution
//
// If add new text at start of line
        if      ( src.equals("^") ) textArea.insert(replace, caret);
// Else if add new text at end of line
        else if ( src.equals("$") ) textArea.insert(replace, caret);
// Else do replacement
        else  textArea.replaceRange(replace, caret, caret+src.length() );
// This will force the lines ArrayList to be recalculated
        textAreaHolder.mySetTextHasChanged(true);
// Need to update lines after change;
        lines = textAreaHolder.myGetTextArrayList();
//if(DO_TRACE) {
//  System.out.println(MyTrace.myGetMethodName() + ": caret= " + caret + " = " + textArea.getCaretPosition() + ": updated txt ary");
//  System.out.print( SwingMyEditorZ16Misc.myLineInfoToString( textArea.getText(), lines ) );
//}
// Set has changed. This will force recalculation of textAreaHolde TextArrayList
        textAreaHolder.mySetTextHasChanged(true);
// Step caret forward
        if ( !src.equals("^") && !src.equals("$") ) {
// If not global then advance caret to end of line
          if ( !global ) caret = lines.get(lnIdx)[2];
// Else advance caret to end of replaced text
          else caret = caret - src.length() + replace.length();
        }
        textArea.setCaretPosition(caret);
      } //End: while
//
      topPanel.myGetTextAreaHolder().myGetHandleKeyTyped().myReset();
    } //End: else
    } //End: try
    catch (Exception e) {
      SwingMyEditorZ16Misc.myShowDialog(topPanel.myGetTopJPanel(), "Warning", MyTrace.myGetMethodName() + ": exc= " + e);
      e.printStackTrace();
    }
  } //End: Method


//------------------------------------------------------------------------
//-----------------------  Static Methods:  ------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private SwingMyEditorZ11CmdLineSub ( ) {}


} //End: Class SwingMyEditorZ08CmdLineSub

