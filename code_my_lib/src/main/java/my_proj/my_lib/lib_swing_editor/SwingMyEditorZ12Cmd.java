// SwingMyEditorZ11Cmd.java
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

import my_proj.my_lib.lib.MyStrTok;
import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_ops.MyStringOps;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//-------------------------  CLASS: SwingMyEditorZ11Cmd  --------------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
* This class contains info on the current command.
*
* @author James Everitt
*/
final class SwingMyEditorZ12Cmd {

//private static final boolean DO_TRACE = true;
  
/*
 *  Possible syntax:
 *   <idx0> , <idx1> g / <arg> / <op>
 *   <indx>
 *   / <stuff_to_search_for>
 */

/** Empty String indicating that no command has yet been set */
  static final String NO_COMMAND = "";

/** Special characters used for fragment C and Java statements */
  static final String MY_VI_SPECIAL_CHARS = "^$.,:/\t\r\f\n";   //"${}=()[].,;:?><*/&|+-%\t\r\f\n";

//
/** Input command line */
  String   myCmdLn = null;
/** Lines over which the command is to be applied */
  int[]    myIndexesFrom0 = null;
/** Global search arg */
  String   myGlobalArg = null;
/** Command */
  String   myCmd = null;
/** Command line arguments */
  String[] myCmdArgs = null;
/** Last part of command: ie. "gc" for global and confirm */
  String   myLnEnd = null;


//------------------------------------------------------------------------
//------------------------------  Methods:  ------------------------------
//------------------------------------------------------------------------

//------------------  Method  ------------------
/**
 * This is the constructor method.
 *
 * @param cmdLn  ?
 * @param textArea  ?
 */
  SwingMyEditorZ12Cmd ( String cmdLn, SwingMyEditorZ07TextAreaHolder textArea ) throws Exception
  {
    if      (cmdLn == null ) throw new Exception(MyTrace.myGetMethodName() + ": cmdLn is null");
    else if ( cmdLn.length() == 0 ) throw new Exception(MyTrace.myGetMethodName() + ": cmdLn is empty");
//
    this.myCmdLn = cmdLn;
    this.myCmd = SwingMyEditorZ12Cmd.NO_COMMAND;
    ArrayList<Integer> lnStrtIdxs = null;
    int caret = textArea.myGetJTextArea().getCaretPosition();
//Fragment command
    ArrayList<String> args = myCarefullyFragCmdLn( cmdLn ); // MyStrTok.myTokenizeToArrayList(cmdLn, null, MY_VI_SPECIAL_CHARS, null, null, MyStrTok.MY_SEPARATE_INTEGERS );
//
//if(DO_TRACE) {System.out.print("\n------\n" + MyTrace.myGetMethodName() + ": cmdLn= " + cmdLn + ": numFr= " + args.size() + ": frags="); for ( String fr : args) System.out.print(" <" + fr + ">" );  System.out.println(); }
//
// Handle single args
    if ( args.size() == 1 ) {
      if ( MyStringOps.myIsInteger(args.get(0)) ) this.myIndexesFrom0 = new int[] { Integer.parseInt(args.get(0)) };
      else this.myCmd = args.get(0);
    }
// Handle multiple value args
    else {
// Search command
      if ( args.size() == 2 && args.get(0).equals("/") ) { this.myCmd = args.get(0); this.myCmdArgs = new String[] {args.get(1)}; }
// General commands
      else {
// If they exist, replace special range arguments
        if ( MyStringOps.myIsInteger(args.get(0)) || args.get(0).equals("$") || args.get(0).equals(".") ) {
          int lst = args.size() >= 2 && args.get(1).equals(",") ? 3 : 1;
// Remove '.' and '$'
          for ( int i1=0 ; i1<lst ; i1++ ) {
            String tmp = args.get(i1);
            if ( tmp.equals(".") ) {
              if ( lnStrtIdxs == null ) lnStrtIdxs = SwingMyEditorZ16Misc.myGetLineStartIndexes(textArea.myGetJTextArea().getText());
              int lnNumb = 0;
              for ( Integer lnStrt : lnStrtIdxs ) {
                if ( lnStrt.intValue() <= caret ) lnNumb++;
                else break;
              }
              args.set(i1, String.valueOf(lnNumb));
//if(DO_TRACE) System.out.print( MyTrace.myGetMethodName() + ": caret= " + caret + ": lnNumb= " + lnNumb);
            }
            else if ( tmp.equals("$") ) {
              if ( lnStrtIdxs == null ) lnStrtIdxs = SwingMyEditorZ16Misc.myGetLineStartIndexes(textArea.myGetJTextArea().getText());
              args.set(i1, String.valueOf( lnStrtIdxs.size() ) );
            }
          }
// Handle stuff like  .,+5 or -3,.
          if ( args.size() > 2 && args.get(2) != null && MyStringOps.myIsInteger(args.get(0)) && args.get(1).equals(",") && MyStringOps.myIsInteger(args.get(2)) ) {
            int sum = Integer.parseInt(args.get(0)) + Integer.parseInt(args.get(2));
            if ( args.get(0).charAt(0) == '+' || args.get(0).charAt(0) == '-' ) args.set(0, String.valueOf(sum));
            else if ( args.get(2).charAt(0) == '+' || args.get(2).charAt(0) == '-' ) args.set(2, String.valueOf(sum));
          }
        } //End: if ()
//
// Start scanning arguments
        int idx = 0;
// Get range
        if ( MyStringOps.myIsInteger(args.get(0)) ) {
          if ( args.get(1).equals(",") ) { this.myIndexesFrom0 = new int[] { Integer.parseInt(args.get(0)), Integer.parseInt(args.get(2)) }; idx = 3; }
          else                      { this.myIndexesFrom0 = new int[] { Integer.parseInt(args.get(0)), Integer.parseInt(args.get(0)) }; idx = 1; }
        }
// Get global arg: ie. "g / <something> /
        if ( args.get(idx).equals("g") ) {
          this.myGlobalArg = args.get(idx+2);
          idx += 4;
        }
// Get command
        this.myCmd = args.get(idx);
        idx++;
// Get command args -> "/ <something> / <something> /" or "/ <something> / /" or "/ <something> /"
        if ( args.size() - idx >= 5 && args.get(idx).equals("/") && args.get(idx+2).equals("/") && args.get(idx+4).equals("/") ) {
           this.myCmdArgs = new String[] { args.get(idx+1), args.get(idx+3) };
           idx += 5;
        }
        else if ( args.size() - idx >= 4 && args.get(idx).equals("/") && args.get(idx+2).equals("/") && args.get(idx+3).equals("/") ) {
          this.myCmdArgs = new String[] { args.get(idx+1), "" };
          idx += 4;
        }
        else if ( args.size() - idx >= 3 && args.get(idx).equals("/") && args.get(idx+2).equals("/") ) {
          this.myCmdArgs = new String[] { args.get(idx+1) };
          idx += 3;
        }
// Get end
        if ( idx < args.size() ) this.myLnEnd = args.get(idx);
//if(DO_TRACE)System.out.println( MyTrace.myGetMethodName() + ": reach 3" + ": cnd= " + this );
      } //End: else
    } //End: else
// Vi line count starts at 1 and the ArrayList<Integer> of line positions starts at 0.
// Can't do this here because sometimes command is not for line indexes
//    if ( this.myIndexesFrom0 != null ) {
//      for ( int i1=0 ; i1<this.myIndexesFrom0.length ; i1++ ) this.myIndexesFrom0[i1] = this.myIndexesFrom0[i1] - 1;
//    }
//if(DO_TRACE)System.out.println( MyTrace.myGetMethodName() + ": return at end" + ": this= " + this );
  } //End: method


//------------------  Method  ------------------
/**
 * This method fragments the command line.
 * It first fragments the command line using "/".
 * It then further fragments the first entry.
 *
 * @param cmdLn  Command line to be fragmented
 * 
 * @return returns ArrayList&lt;String&gt; of fragments
 */
  private static final ArrayList<String> myCarefullyFragCmdLn ( String cmdLn )
  {
    ArrayList<String> retVal = null;
    ArrayList<String> slashedAl = MyStrTok.myTokenizeToArrayList(cmdLn, null, "/", null, null, MyStrTok.MY_SEPARATE_NO_OPTIONS );
//
    if ( slashedAl.get(0).equals("/") ) retVal = slashedAl;
    else {
      ArrayList<String> firstArgAl = MyStrTok.myTokenizeToArrayList(slashedAl.get(0), null, MY_VI_SPECIAL_CHARS, null, null, MyStrTok.MY_SEPARATE_INTEGERS );
      retVal = new ArrayList<String>();
      for ( String tmp : firstArgAl ) retVal.add(tmp);
      slashedAl.remove(0);
      for ( String tmp : slashedAl ) retVal.add(tmp);
    }
//
    return retVal;
  } //End: method


//------------------  Method  ------------------
/**
 * This method decrements indexes by 1.
 * It is used to translate specified indexes starting at 1 to internal indexes starting at 0
 *
 */
  final void myDecrementIndexesBy1()
  {
    if ( this.myIndexesFrom0 != null )
      for ( int i1=0 ; i1<this.myIndexesFrom0.length ; i1++ ) this.myIndexesFrom0[i1]--;
  } //End: method


//------------------  Method  ------------------
/**
 * This method
 *
 */
  @Override public final String toString()
  {
    StringBuffer sb = new StringBuffer();
//
    sb.append(this.getClass().getSimpleName());
//
    sb.append(":cmdLn=" + this.myCmdLn + "->");
//
    boolean first = true;
    if ( this.myIndexesFrom0 != null ) {
      if ( this.myIndexesFrom0.length == 1 ) sb.append("idx=" + this.myIndexesFrom0[0]);
      else sb.append("idxs=" + this.myIndexesFrom0[0] + "," + this.myIndexesFrom0[1]);
      first = false;
    }
    if ( this.myGlobalArg != null ) { if (!first)sb.append(":"); sb.append("gl=" + this.myGlobalArg); first = false; }
    if ( this.myCmd.length() > 0 ) { if (!first)sb.append(":"); sb.append("cmd=" + this.myCmd); first = false; }
//
    if ( this.myCmdArgs != null && this.myCmdArgs.length > 0 ) {
      sb.append(":args=");
      for ( int i1=0 ; i1<this.myCmdArgs.length ; i1++) {
        if ( i1 > 0 ) sb.append(",");
        sb.append(this.myCmdArgs[i1]);
      }
    }
    if ( this.myLnEnd != null ) sb.append(":end=" + this.myLnEnd);
//
    return sb.toString();
  } //End: Method


} //End: class SwingMyEditorZ11Cmd
