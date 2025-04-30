// MyTrace.java
/*
 *
 * Copyright (C) 2013,2024 James Everitt
 *
 * This file is part of Open Schematic Capture.
 * 
 * Open Schematic Capture is free software: you can redistribute it
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
package my_proj.my_lib.lib;

//------------------  Import statements  ------------------

import java.awt.EventQueue;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;


//-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------
//----------------------  CLASS: MyTrace  ---------------------------------------
//-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------
/**
 * This class is used to trouble shoot a program by selectively generating output.
 *
 * @author James Everitt
*/
public final class MyTrace {
  
//  private static final boolean           DO_TRACE = true;

/** Levels of info to print */
  private static       int                myVerboseInfoLevel = 0;

/** This level */
  private static       int                myStackPresentLevel = 0;
  
  private static       int                myStackStartLevelForOffset = 0;
  
/** Maximum stack depth reached */
  private static       int                myMaxStackDepthReached = 0;

/** Threshold interest level. Higher level prints more info */
//  private static       int                myThresholdInterestLevel = 0;
  
  private static       long               myLastTimeInMs = -1;
  
  private static       boolean            myOnlyPrintFromEventQueue = false;

  private static       String             myFileName = null;
  private static       PrintWriter        myPw = null;
  
  private static final boolean            myTrackThreads = true;
  private static       ArrayList<MyThreadAndCnt> myThreadsAndCounts = null;
  
  private static       boolean            myDoIndent = true;
  private static       boolean            myAddPrefixAndIndentDots = true;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private MyTrace ( ) {}


//----------------------------------------------------------
//------------------  Static Methods  ----------------------
//----------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param onlyEventQueue  ?
 *
 */
  public static final void mySetTrackOnlyEventQueue ( boolean onlyEventQueue ) { MyTrace.myOnlyPrintFromEventQueue = onlyEventQueue; }


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param verboseInfoLevel  ?
 * @param outputFile  ?
 *
 */
  public static final void mySetVerboseLevel ( int verboseInfoLevel, String outputFile ) { mySetVerboseLevel ( verboseInfoLevel,  outputFile, true ); }


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param verboseInfoLevel  ?
 * @param outputFile  ?
 * @param resetStackReferenceLevel  ?
 *
 */
//static int setCnt=0;
  synchronized public static final void mySetVerboseLevel ( int verboseInfoLevel, String outputFile, boolean resetStackReferenceLevel )
  {
//System.out.println("\n---xxx---\n" + MyTrace.myGetMethodName() + ": cnt= " + ++setCnt + ": newLvl= " + verboseInfoLevel + ": file= " + outputFile);
//if(setCnt==3)throw new Error(MyTrace.myGetMethodName() + ": force error");
//
    if ( verboseInfoLevel > 0 ) {
      if ( outputFile != null ) MyTrace.mySetTraceOutput(outputFile);
      String caller = MyTrace.myGetMethodName(2);
      System.out.println("---- " + MyTrace.myGetMethodName() + ": verbLvl= " + verboseInfoLevel + ": setBy= " + caller + ": outFile= " + outputFile);
      System.out.flush();
      if ( MyTrace.myPw != null ) MyTrace.myPw.append(MyTrace.myGetMethodName() + ": enable trace" + ": verbLvl= " + verboseInfoLevel + ": setBy= " + caller + "\n");
// Thread tracking
      if ( myTrackThreads && myThreadsAndCounts == null ) myThreadsAndCounts = new ArrayList<>();
// Set up to reset stack reference
//      myResetStackReferenceLevel = resetStackReferenceLevel;
//
// If scheduled to reset reference stack depth then set desired start level and clear reset request
//       if ( myResetStackReferenceLevel ) {
//         myResetStackReferenceLevel = false;
//         myStackStartLevelForOffset = myStackPresentLevel;
//       }
      if ( resetStackReferenceLevel ) myStackStartLevelForOffset = Thread.currentThread().getStackTrace().length;
    }
    else if ( verboseInfoLevel == 0 ) {
      MyTrace.myClose();
    }
// Finally set verbose level
    MyTrace.myVerboseInfoLevel = verboseInfoLevel;
//    if ( MyTrace.myPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myFileName + ": verboseLev= " + verboseInfoLevel);
//
//System.out.println("\n---\n" + MyTrace.myGetMethodName() + ": exiting" + ": verbLvl= " + MyTrace.myVerboseInfoLevel + ": presLev= " + myStackPresentLevel + ": strtOfst= " + myStackStartLevelForOffset + ": doprint= " + MyTrace.myDoPrint() );
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param verboseInfoLevel  ?
 *
 */
  synchronized public static final void mySetVerboseLevel( int verboseInfoLevel ) { MyTrace.mySetVerboseLevel ( verboseInfoLevel, null, false ); }


//------------------  Method  ------------------
/**
 * This static method allows or suppresses indents. Suppressing indents is used for generating output that can be compared to "C" output.
 *
 * @param doIndent  ?
 */
  public static final void mySetDoIndents ( boolean doIndent ) { MyTrace.myDoIndent = doIndent; }


//------------------  Method  ------------------
/**
 * This static method 
 *
 * @param prefAndDots  ?
 */
  public static final void mySetMyAddPrefixAndIndentDots ( boolean prefAndDots ) { MyTrace.myAddPrefixAndIndentDots = prefAndDots; }


//------------------  Method  ------------------
/**
 * This static method sets the trace output file.
 *
 * @param outputFileName  ?
 *
 */
//static int traceOutCnt = 0;
  public static final void mySetTraceOutput( String outputFileName )
  {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": cnt= " + traceOutCnt++);
    try {
// If trying to re-open the same file, don't do anything
      if ( MyTrace.myFileName != null && outputFileName != null && outputFileName.equals(MyTrace.myFileName) ) return;
// Else if outputFileName is null, this signals to turn off possible open file output
      else if ( outputFileName == null ) MyTrace.myClose();
// Else if requesting new output, possibly close old output file and create new output file
      else {
        if ( MyTrace.myPw != null )  MyTrace.myClose();
        MyTrace.myFileName = outputFileName;
        File outputFile = new File(outputFileName);
        File outputDir = new File(outputFile.getParent());
        if ( !outputDir.exists() ) {
          boolean ok = outputDir.mkdirs();
          if ( !ok ) throw new Exception(MyTrace.myFileName + ": could not create " + outputDir.getAbsolutePath());
        }
        MyTrace.myPw = new PrintWriter(outputFile);
//if(print)System.out.println("\n---\n" + MyTrace.myGetMethodName() + ": pw= " + MyTrace.myPw + "\n");
      } //End: else
    } //End: try
    catch (Exception e) {
      System.out.println(MyTrace.myGetMethodName() + ": could not open" + outputFileName + ": exc= " + e.getMessage());
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 *
 */
  public static final void myClose(  )
  {
// Handle thread info
    if ( myTrackThreads && myThreadsAndCounts != null ) myPrintThreadInfo();
    System.out.flush();
// If required, close file
    if ( MyTrace.myPw != null ) {
      System.out.println( "---- " + MyTrace.myGetMethodName() + ": closing trace: file = " + MyTrace.myFileName);
      MyTrace.myPrintln("\n" + MyTrace.myGetMethodName() + ": closing trace");
      MyTrace.myPw.flush();
      MyTrace.myPw.close();
      MyTrace.myPw = null;
    }
    MyTrace.myFileName = null;
    MyTrace.myVerboseInfoLevel = 0;
    MyTrace.myStackPresentLevel = 0;
    MyTrace.myStackStartLevelForOffset = 0;
    MyTrace.myMaxStackDepthReached = 0;
//    MyTrace.myThresholdInterestLevel = 0;
    MyTrace.myLastTimeInMs = -1;
    MyTrace.myThreadsAndCounts = null;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method is obsolete.
 *
 * @return  ?
 *
 */
  synchronized public static final boolean myDoPrint( )
  { return ( MyTrace.myVerboseInfoLevel < 1 ) ? false : MyTrace.myDoPrintSub(); }



//------------------  Method  ------------------
/**
 * This static method ?
 * 
 * @param levelsBelow  ?
 * @param resetPrintStackAtMe  ?
 *
 * @return  ?
 *
 */
  private static final boolean myDoPrintSub(  )
  {
//
// Just return if not set for doing trace
    if ( MyTrace.myVerboseInfoLevel < 1 ) return false;
//
// See if only printing event queue stuff
    if ( MyTrace.myOnlyPrintFromEventQueue && !EventQueue.isDispatchThread() ) return false;
//
// Get current thread for determining current stack depth
    Thread curThread = Thread.currentThread();
//
// Determint current level
    myStackPresentLevel = curThread.getStackTrace().length;
//
// If scheduled to reset reference stack depth then set desired start level and clear reset request
//    if ( myResetStackReferenceLevel ) {
//      myResetStackReferenceLevel = false;
//      myStackStartLevelForOffset = myStackPresentLevel;
//    }
//
// Keep track of max level reached
    if ( myStackPresentLevel > myMaxStackDepthReached ) myMaxStackDepthReached = myStackPresentLevel;
//
// Decide if should print
    boolean doPrint = MyTrace.myStackPresentLevel - myStackStartLevelForOffset < MyTrace.myVerboseInfoLevel;
    return doPrint;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 * 
 *
 */
  public static final void myPrintStateOfTrace( )
  {
    System.out.println("--------------------- Trace Status From " + MyTrace.myGetMethodName() + " -----------------------");
    System.out.println("  myVerboseInfoLevel        = " + myVerboseInfoLevel);
    System.out.println("  myStackPresentLevel  = " + myStackPresentLevel );
//    System.out.println("  myRequestedPrintLevels    = " + MyArrayListOps.myArrayListToString(myRequestedPrintLevels, ','));
    System.out.println("  myLastTimeInMs            = " + myLastTimeInMs );
    System.out.println("  myOnlyPrintFromEventQueue = " + myOnlyPrintFromEventQueue);
    System.out.println("  myFileName                = " + myFileName);
    System.out.println("  myPw                      = " + myPw );
    System.out.println("---------------------------------------------------------------");
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @return  ?
 *
 */
  public static final int myGetCurrentLevel () { return MyTrace.myStackPresentLevel; } // Thread.currentThread().getStackTrace().length; }


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @return  ?
 *
 */
  public static final String myGetMethodName( ) { return MyTrace.myGetMethodName(1); }


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @return  ?
 *
 */
  public static final int myGetVerboseInfoLevel( ) { return MyTrace.myVerboseInfoLevel; }


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @return  ?
 *
 */
  public static final PrintWriter myGetPrintWriter() { return MyTrace.myPw; }


//------------------  Method  ------------------
/**
 * This static method ?
 * 
 * @param numbDwn  ?
 *
 * @return  ?
 *
 */
  public static final String myGetMethodName( int numbDwn )
  {
    final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
//
    String name;
//
    int indx = 2 + numbDwn;
    if ( indx >= 0 && indx < ste.length ) {
      StackTraceElement el = ste[indx];
      String fn = el.getClassName();
      int last = fn.lastIndexOf('.');
      name = fn.substring(last+1) + "." + el.getMethodName();
//
    }
    else name = "null";
//
    return name;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 * 
 * @param first  ?
 * @param last ?
 *
 * @return  ?
 *
 */
  public static final String myGetMethodNames ( int first, int last )
  {
    int cnt = Math.abs(first - last) + 1;
    StringBuffer sb = new StringBuffer();
    int indx = first;
//
    for ( int i1=0 ; i1<cnt ; i1++ ) {
      if ( i1 > 0 ) sb.append(" , ");
      sb.append(MyTrace.myGetMethodName(indx));
      if ( first < last ) indx++;
      else indx--;
    }
//
    return sb.toString();
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 * 
 */
  synchronized public static final void myPrintDelTime( )
  {
    long newTime = System.currentTimeMillis();
//
    String str;
    if ( MyTrace.myLastTimeInMs < 0 ) str = "DelTime= starting";
    else {
      float delTimeInSec = ( newTime - MyTrace.myLastTimeInMs ) / 1000.0f;
      str = "DelTime= " + String.format("%6.2f S", delTimeInSec); //  MyFloatOps.myFloatToString(delTimeInSec, 1) + " S";
    }
//
    MyTrace.myLastTimeInMs = newTime;
//
    myPrintLnSub(true, str);
//    if ( myPw == null ) System.out.println(str);
//    else myPw.println(str);
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method is a better approach.
 * It ensures that the decision to print and the actual printing are synchronized.
 *
 * @param args The print arguments
 */
//  synchronized public static final void myMaybePrint( Object... args )
//  {
//    if  ( MyTrace.myVerboseInfoLevel < 1 || !MyTrace.myDoPrintSub() ) return;
//    myPrintLnSub(false, args);
//  } //End: Method


//------------------  Method  ------------------
/**
 * This static method
 * 
 * @param args The print arguments
 */
  synchronized public static final void myPrint( Object... args ) { myPrintLnSub(false, args); }


//------------------  Method  ------------------
/**
 * This static method is a better approach.
 * It ensures that the decision to print and the actual printing are synchronized.
 *
 * @param args The print arguments
 */
//  synchronized public static final void myMaybePrintln( Object... args )
//  {
//    if  ( MyTrace.myVerboseInfoLevel < 1 || !MyTrace.myDoPrintSub() ) return;
//    myPrintLnSub(true, args);
//  } //End: Method


//------------------  Method  ------------------
/**
 * This static method
 * 
 * @param args The print arguments
 */
  synchronized public static final void myPrintln( Object... args ) { myPrintLnSub(true, args); }


//------------------  Method  ------------------
/**
 * This static method is a better approach.
 * It ensures that the decision to print and the actual printing are synchronized.
 *
 * @param args The print arguments
 */
//  synchronized public static final void myMaybePrintlnAndFlush( Object... args )
//  {
//    if  ( MyTrace.myVerboseInfoLevel < 1 || !MyTrace.myDoPrintSub() ) return;
//    myPrintLnSub(true, args);
//    MyTrace.myFlush();
//  } //End: Method

//------------------  Method  ------------------
/**
 * This static method
 * 
 * @param args The print arguments
 */
  public static final void myPrintlnAndFlush( Object... args ) { myPrintLnSub(true, args); MyTrace.myFlush(); }


//------------------  Method  ------------------
/**
 * This static method ?
 * 
 * @param addLineReturn If true put line return at end of string
 * @param args The print arguments
 */
  private static final void myPrintLnSub( boolean addLineReturn, Object... args  )
  {
    StringBuffer sb = new StringBuffer();
    for ( Object obj : args ) sb.append(obj.toString());
//System.out.println(MyTrace.myFileName + ": pw= " + MyTrace.myPw + "    : ln= " + sb.toString());
    String str = sb.toString();
//System.out.println(MyTrace.myGetMethodName() + ": ln= " + str);
    if ( myPw == null ) { System.out.print(str); System.out.flush(); }
    else myPw.print(str);
// If add line return
    if ( addLineReturn ) {
      if ( myPw == null ) System.out.println();
      else myPw.println();
    }
// Handle trace info
    if ( myTrackThreads ) myAddTrackThreadInfo();
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 * 
 */
  public static final void myPrintStack( )
  {
    System.out.println("  stack:");
    final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
    StackTraceElement el;
    String cl;
    for(int i1=2 ; i1<ste.length ; i1++) {
      el = ste[i1];
      cl = el.getClassName();
//      if ( cl.startsWith("java.awt") || cl.startsWith("javax.swing") ) break;
      System.out.println("    " + cl + "." + el.getMethodName());
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 */
  public static final void myFlush ( ) { if ( myPw != null ) myPw.flush(); }


//------------------  Method  ------------------
/**
 * This method provides a new line followed by the indent
 *
 * @return  ?
 */
  public static final String myNlInd ( ) { return "\n" + MyTrace.myIndSub1( true ); }


//------------------  Method  ------------------
/**
 * This method provides just an indent
 *
 * @return  ?
 */
  public static final String myInd ( ) { return MyTrace.myIndSub1( false ); }


//------------------  Method  ------------------
/**
 * This method provides just an indent
 *
 * @return  ?
 */
  public static final String myIndWithoutPrefix ( ) { return MyTrace.myIndSub1( true ); }


//------------------  Method  ------------------
/**
 * This method provides just an indent
 *
 * @return  ?
 */
//  public static final String myIndNoNum ( ) { return MyTrace.myIndSub1( true ); }


//------------------  Method  ------------------
/**
 * This method provides indents, and adds .
 * <pre>
 *   "E " for event on event dispatch thread or "  "
 *   Number of indents being done
 *   Actual indents
 * </pre>
 * 
 * @param isContinuation  True means replace indent count with spaces
 *
 * @return  ?
 */
  private static final String myIndSub1 ( boolean isContinuation )
  {
    if ( !MyTrace.myDoIndent ) return "";
    else if ( !MyTrace.myAddPrefixAndIndentDots )  return MyTrace.myIndSub2( "" );
    else {
      String prefix = javax.swing.SwingUtilities.isEventDispatchThread() ? "E " : "  ";
      String indentCnt = isContinuation ? "    " : myStringFixWidthBase( String.valueOf(myStackPresentLevel), 4, false, false);
      return MyTrace.myIndSub2(prefix + indentCnt + " ");
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 */
  private static final ArrayList<String> myIndents = new ArrayList<String>(30);

  private static final String myIndSub2 ( String prefix )
  {
// Look for start level
    if ( myStackStartLevelForOffset < 0 ) myStackStartLevelForOffset = myStackPresentLevel;
// Get desired indent from start and present levels
    int stackLevelAdjusted = myStackPresentLevel - myStackStartLevelForOffset;
    if ( stackLevelAdjusted < 0 ) {
      myStackStartLevelForOffset = myStackPresentLevel;
      MyTrace.myPw.println("--- indent reset ---");
      stackLevelAdjusted = 0;
    }
// Ensure array is big enough
    while ( myIndents.size() <= stackLevelAdjusted ) myIndents.add(null);
// Ensure requested indent exists
    if ( myIndents.get(stackLevelAdjusted) == null ) {
      StringBuffer sb = new StringBuffer();
//
      for ( int i1 = 0 ; i1 < stackLevelAdjusted ; i1++ ) {
        if ( MyTrace.myAddPrefixAndIndentDots && i1%2 == 0 ) sb.append(". ");
        else sb.append("  ");
      }
//
      myIndents.set(stackLevelAdjusted, sb.toString());
    }
//
    return prefix + myIndents.get(stackLevelAdjusted);
  } //End: Method


//------------------  Method  ------------------
/**
 * This method keys of of print statement and records info about thread print statement is in.
 *
 */
  private static final void myAddTrackThreadInfo()
  {
    if ( myThreadsAndCounts != null ) {
      Thread th = Thread.currentThread();
      MyThreadAndCnt found = null;
//
// Look for existing thread
      for ( int i1=0 ; i1<myThreadsAndCounts.size() ; i1++ ) {
        MyThreadAndCnt tmp = myThreadsAndCounts.get(i1);
        if ( th.equals(tmp.thread) ) { found = tmp ; break; }
      }
// Add if required
      if ( found == null ) {
        found = new MyThreadAndCnt(th);
        myThreadsAndCounts.add(found);
      }
// Or update count
      else found.cnt++;
    }
  } //End: Method
  

//------------------  Method  ------------------
/**
 * This method prints out thread info
 *
 */
  private static final void myPrintThreadInfo()
  {
//System.out.println(MyTrace.myGetMethodName() + ": numb threads= " + myThreadsAndCounts.size());
    MyTrace.myPrintln();
//
    MyTrace.myPrintln("------------------\nClosing trace file: file= " + MyTrace.myFileName);
//
    MyTrace.myPrintln("------------------\nTrace info:" );
    MyTrace.myPrintln("  Max stack depth reached = " + myMaxStackDepthReached );
    MyTrace.myPrintln("  Start print stack depth = " + myStackStartLevelForOffset );
//
    MyTrace.myPrintln("------------------\nThread info:");
    for ( int i1=0 ; i1<myThreadsAndCounts.size() ; i1++ ) {
      MyThreadAndCnt tmp = myThreadsAndCounts.get(i1);
      String generNm = myStringFixWidthBase( tmp.thread.getName(), 20, true, false);
      String generStr = myStringFixWidthBase( tmp.thread.toString(), 40, true, false);
      MyTrace.myPrintln("  " + generNm + " = " + generStr + " : cnt= " + tmp.cnt );
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param strIn  ?
 * @param width  ?
 * @param leftJust  ?
 * @param limitWidth  ?
 *
 * @return  ?
 *
 */
  private static final String myStringFixWidthBase ( String strIn, int width, boolean leftJust, boolean limitWidth )
  {
    int strInLen = (strIn == null) ? 0 : strIn.length();

    StringBuffer pad = new StringBuffer("");
    for (int i1=0 ; i1<width-strInLen ; i1++) pad.append(" ");
//
    String strOut;
//
    if ( strIn == null ) strOut = pad.toString();
    else if ( limitWidth && strInLen >= width ) strOut = strIn.substring(0, width);
    else if ( leftJust ) strOut = strIn + pad.toString();
    else strOut = pad.toString() + strIn;
//
//System.out.println(MyTrace.myGetMethodName() + " : out=<" + strOut + ">" + " : width=" + width + " : in=<" + strIn + ">");
//
    return strOut;
  } //End: Method


} //End: class MyTrace


//-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------
//----------------------  CLASS: MyTrace  ---------------------------------------
//-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------
/**
* This class is used to keep trace of threads.
*
* @author James Everitt
*/
final class MyThreadAndCnt {
int cnt;
Thread thread;
//
MyThreadAndCnt ( Thread thIn ) { this.thread = thIn; }
}


