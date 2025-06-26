// MyStrTok.java
/*
 * Copyright (C) 2010 James Everitt
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
package my_proj.my_lib.lib;

//------------------  Import statements  ------------

import java.util.ArrayList;

import my_proj.my_lib.lib_ops.MyStringOps;


//  ----------------  CLASS:  ----------------
/**
 * This class is used to manipulate strings.
 *
 * @author James Everitt
 */
public class MyStrTok
{
  
//private static final boolean DO_TRACE = true;

/** This really speeds up the MyStrTok.myTokenizeToArrayList method when performance monitoring is turned on */
//  private static final String MY_TOK_TO_ARY_LST_MAME_FOR_SPEED_UP = "MyStrTok.myTokenizeToArrayList";
  
  public static final boolean TRUE_ELIMINATE_LEADING_AND_ENDING_BLANKS = true;

/** Turns on local fault trace */
  private static final boolean FAULT_TRACE = false;

//  tab = \o,  backspace = \b,  newline = \n,  form feed = \f,  carriage return = \myRealAry
//  quote = \',  double quote = \",  backslash = \\

/** Indicates that there are no separator options */
  public static final int MY_SEPARATE_NO_OPTIONS            = 0;
/** Option to separate before line return */
  public static final int MY_SEPARATE_BEFORE_LINE_RET       = 0x8;
/***/
  public static final int MY_SEPARATE_INTEGERS              = 0x10;
/** Option to preserve numbers */
  public static final int MY_SEPARATE_NUMBERS               = 0x20;
/***/
  public static final int MY_SEPARATE_SPACES                = 0x40;
/***/
  public static final int MY_SEPARATE_STRIP_OUTSIDE_QUOTES  = 0x80;
/***/
  public static final int MY_SEPARATE_REQUIRE_MATCH_QUOTES  = 0x100;
// This is more confusion than it's worth
//  public static final int MY_SEPARATE_C_AND_JAVA_COMMENTS   = 0x100;

/** Option to ignore case */
  public static final boolean MY_IGNORE_CASE_YES = true;
/** Option to enforce case */
  public static final boolean MY_IGNORE_CASE_NO  = false;

/** Option to remove matches */
  public static final boolean MY_REMOVE_MATCH_YES = true;
/** Option to keep matches */
  public static final boolean MY_REMOVE_MATCH_NO  = false;
  
/** Character types*/
  private static enum MY_CH_TYPE_VER_1 { NORMAL_CH, ELIMINATE_CH, KEEP_CH, ISOLATE_CH, GROUP_CH, BREAK_CH, KEEP_STR }
  
/** Special characters used for fragment C and Java statements */
  public static final String MY_SPECIAL_CHARS = "${}=()[],;:?><*/&|!+-%\t\r\f\n";
  
// Non static
  private        String   myBreakAndEliminateChars = null;
  private        String   myBreakAndKeepChars = null;
  private        String[] myBreakAndKeepStrings = null;
  private        String   myIsolateChars = null;
  private        int      mySeparatorOptions = MyStrTok.MY_SEPARATE_NO_OPTIONS;

//------------------------------------------------------------------------
//-----------------------  Static Methods:  ------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method finds a word in a word array and optionally removes the first occurance.
 *
 * @param wordArray  String array to search
 * @param wordToFind  String to find
 * @param ignoreCase  true to ignore case
 * @param remove  true to remove the first occurance from the array
 *
 * @return  returns true if word is found
 *
 */
  public static final boolean myFindAndRemove (
    String[] wordArray,
    String   wordToFind,
    boolean  ignoreCase,
    boolean  remove )
  {
//    if (MyPerformanceMonitor.MY_MONITOR_IS_ACTIVATABLE) MyPerformanceMonitor.myEnteringMethod();
//
    int i1;
    int match = -1;
    for ( i1=0 ; i1<wordArray.length ; i1++ ) {
      if ( ignoreCase == MyStrTok.MY_IGNORE_CASE_YES && wordToFind.equalsIgnoreCase(wordArray[i1]) ) match = i1;
      else if ( ignoreCase != MyStrTok.MY_IGNORE_CASE_YES && wordToFind.equals(wordArray[i1]) ) match = i1;
      if ( match >= 0 ) break;
    }

//if ( match >= 0 ) System.out.println("found " + lineFrags[match]);

    if ( remove == MyStrTok.MY_REMOVE_MATCH_YES && match >= 0 ) {
      for ( i1=match ; i1<wordArray.length-1 ; i1++ ) wordArray[i1] = wordArray[i1+1];
      wordArray[wordArray.length-1] = "";
    }
//
//    if (MyPerformanceMonitor.MY_MONITOR_IS_ACTIVATABLE) MyPerformanceMonitor.myExitingMethod();
//
    if ( match >= 0 ) return true;
    return false;

  } //End: Method


//------------------  Method  ------------------
/**
 * This static method breaks a string up into a returned string array.<br>
 * Ex. MyStrTok.myTokenize ("{aaa.bbb}", ".") returns array of "{aaa" , "bbb}"
 *
 * @param stringIn  String to break up
 * @param eliminateChars  String of characters that are eliminated as they fragment the input String
 *
 * @return  returns array of String fragments
 *
 */
  public static final String[] myTokenize (
    String   stringIn,
    String   eliminateChars )
  {
    return MyStrTok.myTokenize(stringIn,eliminateChars,null,null,null,MyStrTok.MY_SEPARATE_NO_OPTIONS);
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method breaks a string up into a returned string array.<br>
 * Ex. MyStrTok.myTokenize ("{aaa.bbb}", ".", "{}", null, MyStrTok.MY_SEPARATE_NO_OPTIONS)<br>
 *                 returns array of "{" , "aaa" , "bbb" , "}"
 *
 * @param stringIn  String to break up
 * @param breakAndEliminateChars  String of characters that are eliminated as they fragment the input String
 * @param breakAndKeepChars  String of characters that are saved as they fragment the input String
 * @param isolateChars  String of characters that isolate enclosed stuff from being fragmented
 * @param separatorOptions  separator options
 *
 * @return  returns array of String fragments
 *
 */
  public static final String[] myTokenize (
    String   stringIn,
    String   breakAndEliminateChars,
    String   breakAndKeepChars,
    String   isolateChars,
    int      separatorOptions )
  { return MyStrTok.myTokenize ( stringIn, breakAndEliminateChars, breakAndKeepChars, null, isolateChars, separatorOptions ); }


//------------------  Method  ------------------
  public static final String[] myTokenize (
    String   stringIn,
    String   breakAndEliminateChars,
    String   breakAndKeepChars,
    String[] breakAndKeepStrings,
    String   isolateChars,
    int      separatorOptions )
  {
    ArrayList<String> stringVect = MyStrTok.myTokenizeToArrayList(stringIn, breakAndEliminateChars, breakAndKeepChars, breakAndKeepStrings, isolateChars, separatorOptions);

//  Convert to string ArrayList
    String[] stringsOut = null;
    if ( stringVect != null ) {
      stringsOut = new String[stringVect.size()];
      for (int iC=0 ; iC<stringVect.size() ; iC++ ) stringsOut[iC] = stringVect.get(iC);
    }
//
    return stringsOut;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method breaks a string up into a returned string array.<br>
 * Ex. MyStrTok.myTokenize ("{aaa.bbb}", ".", "{}", null, null, MyStrTok.MY_SEPARATE_NO_OPTIONS)<br>
 *                      returns array of "{" , "aaa" , "bbb" , "}"
 *
 * @param stringIn  String to break up
 * @param breakAndEliminateChars  String of characters that are eliminated as they fragment the input String
 * @param breakAndKeepChars  String of characters that are saved as they fragment the input String
 * @param breakAndKeepStrings  array of Strings that are saved as they fragment the input String
 * @param isolateChars  String of characters that isolate enclosed stuff from being fragmented
 * @param separatorOptions  separator options
 *
 * @return  returns array of String fragments
 *
 */
/*
  public static final ArrayList<String> myTokenizeToArrayList (
    String   stringIn,
    String   breakAndEliminateChars,
    String   breakAndKeepChars,
    String[] breakAndKeepStrings,
    String   isolateChars,
    int      separatorOptions )
  {
    ArrayList<String> stringVect = new ArrayList<>(4);
    MyStrTok.myTokenizeToArrayList( stringVect, stringIn, breakAndEliminateChars, breakAndKeepChars, breakAndKeepStrings, isolateChars, separatorOptions );
    return stringVect;
  } //End: Method
*/

//------------------  Method  ------------------
/**
 * This static method breaks a string up into a returned string array.<br>
 * Ex. MyStrTok.myTokenize ("{aaa.bbb}", ".", "{}", null, null, MyStrTok.MY_SEPARATE_NO_OPTIONS)<br>
 *                      returns array of "{" , "aaa" , "bbb" , "}"
 *
 * @param stringIn  String to break up
 * @param breakAndEliminateChars  String of characters that are eliminated as they fragment the input String
 * @param breakAndKeepChars  String of characters that are saved as they fragment the input String
 * @param breakAndKeepStrings  array of Strings that are saved as they fragment the input String
 * @param isolateChars  String of characters that isolate enclosed stuff from being fragmented
 * @param separatorOptions  separator options
 * @param requireMatchingIcolationChars require matching isolation characters
 *
 * @return  returns array of String fragments
 *
 */
  public static final ArrayList<String> myTokenizeToArrayList (
    String   stringIn,
    String   breakAndEliminateChars,
    String   breakAndKeepChars,
    String[] breakAndKeepStrings,
    String   isolateChars,
    int      separatorOptions
//    boolean  requireMatchingIsolationChars
    )
  {
    ArrayList<String> stringVect = new ArrayList<>(4);
    MyStrTok.myTokenizeToArrayList( stringVect, stringIn, breakAndEliminateChars, breakAndKeepChars, breakAndKeepStrings, isolateChars, separatorOptions );
    return stringVect;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method breaks a string up into a returned string array.<br>
 * Ex. MyStrTok.myTokenize ("{aaa.bbb}", ".", "{}", null, null, MyStrTok.MY_SEPARATE_NO_OPTIONS)<br>
 *                      returns array of "{" , "aaa" , "bbb" , "}"
 *
 * @param stringVect output ArrayList
 * @param stringIn  String to break up
 * @param breakAndEliminateChars  String of characters that are eliminated as they fragment the input String
 * @param breakAndKeepChars  String of characters that are saved as they fragment the input String
 * @param breakAndKeepStrings  array of Strings that are saved as they fragment the input String
 * @param isolateChars  String of characters that isolate enclosed stuff from being fragmented
 * @param separatorOptions  separator options
 * @param requireMatchingIsolationChars require matching isolation characters
 *
 * @return  returns array of String fragments
 *
 */
  public static final ArrayList<String> myTokenizeToArrayList (
    ArrayList<String> stringVect,
    String         stringIn,
    String         breakAndEliminateChars,
    String         breakAndKeepChars,
    String[]       breakAndKeepStrings,
    String         isolateChars,
    int            separatorOptions
//    boolean        requireMatchingIsolationChars
//,int dummy
      )
  {
//if(DO_TRACE)System.out.println("\n------\n" + MyTrace.myGetMethodName() + "\n br&el= <" + breakAndEliminateChars + ">" + "\n br&keep= <" + breakAndKeepChars + ">"  + "\n br&keepStr= " + MyStringOps.myStringArrayToStringBracketed(breakAndKeepStrings)  + "\n strIn= <" + stringIn + ">" );
//
    if ( stringIn == null || stringIn.length() == 0 ) return null;
//
// Note: specifying name saves time: I think
//    if (MyPerformanceMonitor.MY_MONITOR_IS_ACTIVATABLE) MyPerformanceMonitor.myEnteringMethod("MyStrTok.myTokenizeToArrayList");
//
//    if ( MyStrTok.DO_TRACE > 0 ) System.out.println ( "Entered MyStrTok.myTokenize: str = <" + stringIn + ">, elim = <" + breakAndEliminateChars  + ">, sep = <" + breakAndKeepChars + ">, group quotes = <" + isolateChars + ">" );
//
    MyStrTok.MY_CH_TYPE_VER_1 chType;
    int tokStrt = -1;
    MyObjIntLocalSimple groupTypeOfChar = new MyObjIntLocalSimple(0);
    MyObjIntLocalSimple stringWidth = new MyObjIntLocalSimple(0);
    boolean  requireMatchingIsolationChars = (separatorOptions & MyStrTok.MY_SEPARATE_REQUIRE_MATCH_QUOTES) != 0;
//
    for ( int iC=0 ; iC<stringIn.length() ; iC++ ) {
      groupTypeOfChar.n = -1;
      chType = MyStrTok.myGetCharType(stringIn, iC, breakAndEliminateChars, breakAndKeepChars, breakAndKeepStrings, isolateChars, separatorOptions, groupTypeOfChar, stringWidth);
//
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 1" + ": iC= " + iC + ": ch= <" + stringIn.charAt(iC) + ">" + ": chTyp= " + chType + ": remaining= <" + stringIn.substring(iC) + ">" );
//
/* This doesn't really belong here
      char chM1 = iC == 0 ? ' ' : stringIn.charAt(iC-1);
      char ch0 = stringIn.charAt(iC);
      char ch1 = iC < stringIn.length() - 1 ? stringIn.charAt(iC+1) : ' ';
// If need to group C and Java comments in /[esc]* , *[esc]/ form or // form
      if ( iC < stringIn.length() - 2 &&  ( separatorOptions & MyStrTok.MY_SEPARATE_C_AND_JAVA_COMMENTS ) != 0
            && ( (ch0 == '/' && ch1 == '*') || (ch0 == '/' && ch1 == '/') ) ) {
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": doing comment" + ": ln= " + stringIn + "\n" );
//        boolean multiline = ch0 == '/' && ch1 == '*';
        tokStrt = iC;
        while (true) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": ic= " + iC + ": chM1,0,1= <" + chM1 + "> <" + ch0 + "> <" + ch1 + ">");
          if ( iC == stringIn.length() - 1 || ( chM1 == '*' && ch0 == '/' ) ) {
            String newStr = stringIn.substring(tokStrt, iC+1);
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + " : ended" + ": new= <" + newStr + ">\n" );
            stringVect.add(newStr);
            break;
          }
          iC++;
          chM1 = ch0;
          ch0 = ch1;
          ch1 = iC < stringIn.length()-1 ? stringIn.charAt(iC+1) : ' ';
        } //End: while (true)
      } //End: if ()
*/
// If char is isolation char then group start isolation char, stuff, and end isolation char together
      if ( chType == MyStrTok.MY_CH_TYPE_VER_1.ISOLATE_CH && stringIn.indexOf(stringIn.charAt(iC), iC+1) > 0 ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 2");
        tokStrt = iC;
        do {
          iC++;
        } while ( iC < stringIn.length() && !MyStrTok.myMatchCharWord ( stringIn, iC, isolateChars ) );
        if ( iC >= stringIn.length() ) {
          if (requireMatchingIsolationChars) throw new Error("MyStrTok.myTokenizeToArrayList: missed ending iso char: str = " + stringIn);
          else iC--;
        }
        if ( iC+1 > tokStrt ) {
          stringVect.add(stringIn.substring(tokStrt, iC+1));
        }
        tokStrt = -1;
      } //End: if ()
//
      else if ( chType == MyStrTok.MY_CH_TYPE_VER_1.KEEP_STR ) {
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": reach 3" + ": ch= <" + stringIn.charAt(iC) + ">" + " : remaining= <" + stringIn.substring(iC) + ">");
// Start
        StringBuffer sb = new StringBuffer(stringIn.substring(iC, iC+stringWidth.n));
// If require matching on both ends go thru until find end
        iC += stringWidth.n;
        if ( requireMatchingIsolationChars ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 3a" + ": len= " + stringWidth.n + " : remaining= <" + stringIn.substring(iC) + ">");
          while ( iC+1 < stringIn.length() && 
                  MyStrTok.myGetCharType(stringIn, iC, null, null, breakAndKeepStrings, null, MyStrTok.MY_SEPARATE_NO_OPTIONS, groupTypeOfChar, stringWidth)
                    != MyStrTok.MY_CH_TYPE_VER_1.KEEP_STR
              ) {
            sb.append(stringIn.charAt(iC));
            iC++;
          }
// Add end
          sb.append( stringIn.substring(iC, iC+stringWidth.n) );
          iC += stringWidth.n;
        }
        stringVect.add(sb.toString());
        iC--;
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 3b" + ": iC= " + iC + ": added <" + sb.toString() + ">" + " : remaining= <" + stringIn.substring(iC) + ">");
//
      } //End: if ()
//
      else if ( chType == MyStrTok.MY_CH_TYPE_VER_1.ELIMINATE_CH ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 4");
// Keep blank lines
        if ( iC > 0 && stringIn.charAt(iC) == '\n' && stringIn.charAt(iC-1) == '\n' ) stringVect.add("");
        tokStrt = -1;
      }
//
      else if ( chType == MyStrTok.MY_CH_TYPE_VER_1.KEEP_CH ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 5");
        stringVect.add(stringIn.substring(iC, iC+1));
        tokStrt = -1;
      }
//
      else if ( chType == MyStrTok.MY_CH_TYPE_VER_1.GROUP_CH ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 6" +  ": iC= " + iC +": ch= " + stringIn.charAt(iC) + ": type= " + groupTypeOfChar.n);
        tokStrt = iC;
        int firstCharGroupType = groupTypeOfChar.n;
//
        while ( iC+1 < stringIn.length() &&
                  MyStrTok.myGetCharType(stringIn, iC+1, breakAndEliminateChars, breakAndKeepChars, breakAndKeepStrings, isolateChars, separatorOptions, groupTypeOfChar, stringWidth)
                   == MyStrTok.MY_CH_TYPE_VER_1.GROUP_CH &&
                       firstCharGroupType == groupTypeOfChar.n
              ) iC++;
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 6b" +": sub= <" + stringIn.substring(tokStrt, iC+1) + ">" );
//
        stringVect.add(stringIn.substring(tokStrt, iC+1));
        tokStrt = -1;
      } //End: if ()
//
      else if ( chType == MyStrTok.MY_CH_TYPE_VER_1.BREAK_CH ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 7");
        tokStrt = iC;
        if ( iC == stringIn.length()-1 ) stringVect.add(stringIn.substring(tokStrt));
      }
//
      else if ( chType == MyStrTok.MY_CH_TYPE_VER_1.NORMAL_CH ) {
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": reach 8" + ": iC= " + iC ); //+ ": sub= <" + stringIn.substring(tokStrt, iC) + ">");
        if ( tokStrt < 0 ) tokStrt = iC;
        while ( iC+1 < stringIn.length() &&
                MyStrTok.myGetCharType(stringIn, iC+1, breakAndEliminateChars, breakAndKeepChars, breakAndKeepStrings, isolateChars, separatorOptions, groupTypeOfChar, stringWidth)
                                     == MyStrTok.MY_CH_TYPE_VER_1.NORMAL_CH
             ) iC++;
        stringVect.add(stringIn.substring(tokStrt, iC+1));
        tokStrt = -1;
      }
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": bottomOfLoop" + ": iC= " + iC + ": ch= <" + stringIn.charAt(iC) + ">" + ": chTyp= " + chType + ": remaining= <" + stringIn.substring(iC) + ">" );
//
    } //End: for ()
//
//    if ( (separatorOptions & MyStrTok.MY_RECOMBINE_SCIENTIFIC_NOTATION_NUMBERS) != 0 )
//      MyStrTok.myRecombineScientificNotationNumbers( stringVect );
//
//    if ( (separatorOptions & MyStrTok.MY_RECOMBINE_NEGATIVE_NUMBERS) != 0 )
//      MyStrTok.myRecombineNegativeNumbers( stringVect );
// Remove outter quotes
    if ( ( separatorOptions & MyStrTok.MY_SEPARATE_STRIP_OUTSIDE_QUOTES ) != 0 ) {
      for ( int i1=0 ; i1<stringVect.size() ; i1++ ) {
        String str = stringVect.get(i1);
        if ( str.startsWith("\"") && str.endsWith("\"") ) stringVect.set(i1, str.substring(1,str.length()-1));
      }
    }
//
//    if (MyPerformanceMonitor.MY_MONITOR_IS_ACTIVATABLE) MyPerformanceMonitor.myExitingMethod("MyStrTok.myTokenizeToArrayList");
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + ": exiting" + ": frags= " + MyArrayListOps.myArrayListToStringBracketed(stringVect));
//
    return stringVect;
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method 
 *
 * @param stringIn  String to break up
 * @param pos  ?
 * @param breakAndEliminateChars  ?
 * @param breakAndKeepChars  ?
 * @param breakAndKeepStrings  ?
 * @param isolateChars  ?
 * @param separatorOptions  ?
 * @param groupTypeOfChar  ?
 * @param stringWidth  ?
 *
 * @return  returns array of String fragments
 *
 */
  private static final MY_CH_TYPE_VER_1 myGetCharType (
      String    stringIn,
      int       pos,
      String    breakAndEliminateChars,
      String    breakAndKeepChars,
      String[]  breakAndKeepStrings,
      String    isolateChars,
      int       separatorOptions,
      MyObjIntLocalSimple groupTypeOfChar,
      MyObjIntLocalSimple stringWidth
    )
  {
    MY_CH_TYPE_VER_1 type = MyStrTok.MY_CH_TYPE_VER_1.NORMAL_CH;
    char ch = stringIn.charAt(pos);
/*
  How to break up numb signs
     for breakAndKeepChars = "+-"  and separatorOptions = MY_SEPARATE_INTEGERS or MY_SEPARATE_NUMBERS
*/
// Order is important
    if      ( isolateChars != null && MyStrTok.myMatchCharWord ( stringIn, pos, isolateChars ) )                type = MyStrTok.MY_CH_TYPE_VER_1.ISOLATE_CH;
    else if ( breakAndKeepStrings != null && MyStrTok.myMatchStringInString(stringIn.substring(pos), breakAndKeepStrings, stringWidth ) ) type = MyStrTok.MY_CH_TYPE_VER_1.KEEP_STR;
    else if ( separatorOptions > 0 && MyStrTok.mySeparatorOptionIsBreakChar (stringIn, pos, separatorOptions) ) type = MyStrTok.MY_CH_TYPE_VER_1.BREAK_CH;
    else if ( breakAndEliminateChars != null && MyStrTok.myMatchCharWord ( ch, breakAndEliminateChars ) )       type = MyStrTok.MY_CH_TYPE_VER_1.ELIMINATE_CH;
    else if ( breakAndKeepChars != null && MyStrTok.myMatchCharWord ( ch, breakAndKeepChars ) )                 type = MyStrTok.MY_CH_TYPE_VER_1.KEEP_CH;
    else if ( separatorOptions > 0 && MyStrTok.mySeparatorOptionIsGroupChar (stringIn, pos, separatorOptions, groupTypeOfChar) ) type = MyStrTok.MY_CH_TYPE_VER_1.GROUP_CH;

//if(DO_TRACE && stringIn.charAt(pos) == '*') System.out.println("--" + MyTrace.myGetMethodName() + ": exiting" + ": type= " + type + ": strIn= " + stringIn.substring(pos));
/*
    if      ( isolateChars != null && MyStrTok.myMatchCharWord ( stringIn, pos, isolateChars ) )                type = MyStrTok.MY_CH_TYPE_VER_1.ISOLATE_CH;
    else if ( separatorOptions > 0 && MyStrTok.mySeparatorOptionIsBreakChar (stringIn, pos, separatorOptions) ) type = MyStrTok.MY_CH_TYPE_VER_1.BREAK_CH;
    else if ( separatorOptions > 0 && MyStrTok.mySeparatorOptionIsGroupChar (stringIn, pos, separatorOptions, groupTypeOfChar) ) type = MyStrTok.MY_CH_TYPE_VER_1.GROUP_CH;
    else if ( breakAndEliminateChars != null && MyStrTok.myMatchCharWord ( ch, breakAndEliminateChars ) )       type = MyStrTok.MY_CH_TYPE_VER_1.ELIMINATE_CH;
    else if ( breakAndKeepStrings != null && MyStrTok.myMatchStringInString(stringIn.substring(pos), breakAndKeepStrings, stringWidth ) ) type = MyStrTok.MY_CH_TYPE_VER_1.KEEP_STR;
    else if ( breakAndKeepChars != null && MyStrTok.myMatchCharWord ( ch, breakAndKeepChars ) )                 type = MyStrTok.MY_CH_TYPE_VER_1.KEEP_CH;
*/
//
//System.out.println("MyStrTok.myGetCharType: ch= " + ch + " : myType= " + myType);
//
    return type;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method breaks a string up into a returned string array.
 * Ex. MyStrTok.myTokenize ("lcmd [1,2], [3,4], \"a string\"", ", ", new String[]{"[]"}, "\"")
 *                      returns array of "lcmd" "[1,2]" "[3,4]" "a string"
 *
 * @param stringIn  String to break up
 * @param breakAndEliminateChars  String of characters that are eliminated as they fragment the input String
 * @param isolatePairs String array of char pairs that bracket inmodified areas
 * @param isolateChars  String of characters that isolate enclosed stuff from being fragmented
 *
 * @return  returns array of String fragments
 *
 */
  private static enum MY_CH_TYPE_VER_2 { start, argChar, breakAndElim, isoPairStrt, isoPairStop, isoChar }

  public static final ArrayList<String> myTokenizeToArrayList (
    String      stringIn,
    String      breakAndEliminateChars,
    String[]    isolatePairs,
    String      isolateChars
//,int dummy
      )
  {
//int dummy2;
//boolean print=isolationPairs != null;
//
    if ( stringIn == null || stringIn.length() == 0 ) return null;
//
//    if (MyPerformanceMonitor.MY_MONITOR_IS_ACTIVATABLE) MyPerformanceMonitor.myEnteringMethod("MyStrTok.myTokenizeToArrayList");
//
    ArrayList<String> args = new ArrayList<>(4);
//
    int len = stringIn.length();
    int iC = 0;
    int argStrt = -1;
    MY_CH_TYPE_VER_2 type = MY_CH_TYPE_VER_2.start;
    boolean insideCharGrp = false;
    boolean insideIsolatePair = false;
//
    while ( iC < len ) {
// Get current char
      char ch = stringIn.charAt(iC);
// Save last char type
      MY_CH_TYPE_VER_2 lastType = type;
// Figure out this char type and update insideCharGrp and insideIsolationPair
      type = MY_CH_TYPE_VER_2.argChar;
      if ( !insideCharGrp && isolatePairs != null && isolatePairs.length > 0 ) {
        for ( String pair : isolatePairs ) {
          if ( pair == null || pair.length() != 2 ) continue;
          if ( ch == pair.charAt(0) ) { type = MY_CH_TYPE_VER_2.isoPairStrt; insideIsolatePair = true; break; }
          else if ( ch == pair.charAt(1) ) { type = MY_CH_TYPE_VER_2.isoPairStop; insideIsolatePair = false; break; }
        }
      }
      if  ( !insideIsolatePair && isolateChars != null && MyStrTok.myMatchCharWord(ch, isolateChars) ) {
        type = MY_CH_TYPE_VER_2.isoChar;
        insideCharGrp = !insideCharGrp;
      }
      if ( type == MY_CH_TYPE_VER_2.argChar && !insideIsolatePair && !insideCharGrp && breakAndEliminateChars != null && MyStrTok.myMatchCharWord(ch, breakAndEliminateChars) ) {
        type = MY_CH_TYPE_VER_2.breakAndElim;
      }
// Set arg start if necessary
      if ( type == MY_CH_TYPE_VER_2.argChar && argStrt < 0 ) argStrt = iC;
// Write out out last group
      if ( iC == len-1 && type == MY_CH_TYPE_VER_2.argChar ) args.add( new String( stringIn.substring(argStrt)) );
// Write out previous to last groups
      else if ( argStrt >= 0 && type != MY_CH_TYPE_VER_2.argChar && lastType == MY_CH_TYPE_VER_2.argChar ) {
        if ( type == MY_CH_TYPE_VER_2.isoPairStop ) args.add( new String( stringIn.substring(argStrt-1, iC+1)) );
        else  args.add( new String( stringIn.substring(argStrt, iC)) );
        argStrt = -1;
      }
// Update count
      iC++;
    } //End: while ( iC < len )
//
//    if (MyPerformanceMonitor.MY_MONITOR_IS_ACTIVATABLE) MyPerformanceMonitor.myExitingMethod("MyStrTok.myTokenizeToArrayList");
/*
if(print)System.out.println( MyTrace.myGetMethodName()
                        + ": br&El= <" + breakAndEliminateChars + ">"
                        + ": isoPairs= " + ( isolationPairs == null ? "null" : MyArrayOps.myStringArrayToStringBracketed(isolationPairs) )
                        + ": isoChars= " + ( isolateChars == null ? "null" : ("<" + isolateChars + ">" ) )
                        + ": str= <" + stringIn + ">"
                        + "\n out= " + MyArrayListOps.myArrayListToStringBracketed(args)
                        );
*/
    return args;
  } //End: method




//------------------ Method ------------------
/**
 * This static method fragments an expression in C or Java.
 *
 * @param expression  ?
 * @param separateOptions  ?
 * 
 * @return  ?
 */
  public static final ArrayList<String> myFragmentCorJavaExpressionToStringVect ( String expression, int separateOptions )
  { return myFragmentGeneralExpressionToStringAryLst ( expression, MY_SPECIAL_CHARS, separateOptions ); }


//------------------ Method ------------------
/**
 * This static method fragments an expression in C or Java.
 *
 * @param expression  ?
 * @param specialChars  ?
 * @param separateOptions  ?
 * 
 * @return  ?
 */
  public static final ArrayList<String> myFragmentGeneralExpressionToStringAryLst ( String expression, String specialChars, int separateOptions )
  {
//boolean  print = DO_TRACE && expression != null && expression.contains("case TX_LEFT_BUTTON:");
//if(print)System.out.println("\n---\n" + MyTrace.myGetMethodName() + ": ln= <" + expression + ">");
//
    ArrayList<String> tok = MyStrTok.myTokenizeToArrayList(expression, " ", specialChars, null, "\"", separateOptions);
//
//if(print)System.out.println(MyTrace.myGetMethodName() + "fragmented: frags= " + MyArrayListOps.myArrayListToStringBracketed(tok) + "\n");
//
//int dummy=1;
//if(expression.contains("aaa"))System.out.println(MyTrace.myGetMethodName() + ": str = " + expression + " -> " + MyStringOps.myVectorToString(tok, ','));
//
    String thisTok, nextTok, lastTok;
    char ch;
//
// Condense
    for (int i1=0 ; i1<tok.size()-1 ; i1++ ) {
      thisTok = tok.get(i1);
      nextTok = tok.get(i1+1);
      lastTok = ( i1==0 ) ? null : tok.get(i1-1);
//System.out.println("     tokSize= " + tok.size() + "  : lastTok= " + lastTok + "  : thisTok= " + thisTok + "   : nextTok= " + nextTok);
//
// Fix tabs
      if ( thisTok.equals("\t") ) tok.set( i1, MyStringOps.MY_SPACES_FOR_TAB );
// Fix form feed = 0x0C
      else if ( thisTok.equals("\f") ) tok.set( i1, "\n" );
// Eliminate carriage return
      else if ( thisTok.equals("\r") ) tok.set( i1, "" );
//
// Condense ops
      else if (
//            ( combineElseAndIf && thisTok.equals("else") && nextTok.equals("if") ) ||
            ( thisTok.equals("&") && nextTok.equals("&") ) ||
            ( thisTok.equals("&") && nextTok.equals("=") ) ||
            ( thisTok.equals("|") && nextTok.equals("|") ) ||
            ( thisTok.equals("|") && nextTok.equals("=") ) ||
            ( thisTok.equals(">") && nextTok.equals("=") ) ||
            ( thisTok.equals(">") && nextTok.equals(">") ) ||
            ( thisTok.equals("<") && nextTok.equals("=") ) ||
            ( thisTok.equals("<") && nextTok.equals("<") ) ||
            ( thisTok.equals("!") && nextTok.equals("=") ) ||
            ( thisTok.equals("=") && nextTok.equals("=") ) ||
            ( thisTok.equals("+") && nextTok.equals("+") ) ||
            ( thisTok.equals("-") && nextTok.equals("-") ) ||
            ( thisTok.equals("+") && nextTok.equals("=") ) ||
            ( thisTok.equals("-") && nextTok.equals("=") ) ||
            ( thisTok.equals("/") && nextTok.equals("=") ) ||
            ( thisTok.equals("*") && nextTok.equals("=") ) ||
// Comments
            ( thisTok.equals("*") && nextTok.equals("/") ) ||
            ( thisTok.equals("/") && nextTok.equals("*") ) ||
            ( thisTok.equals("/") && nextTok.equals("/") ) ||
// Negative numbers, not minus operations
            ( thisTok.equals("-") && i1==0 ) ||
            ( thisTok.equals("-") && lastTok != null && lastTok.startsWith(",") ) ||
            ( thisTok.equals("-") && lastTok != null && lastTok.startsWith("[") ) ||
            ( thisTok.equals("-") && lastTok != null && lastTok.startsWith("(") ) ||
//
            ( thisTok.equals("\\") && nextTok.equals("/") ) ||
            ( thisTok.equals("\\") && nextTok.equals("\"") ) ||
            ( thisTok.equals("\\") && nextTok.equals("n") ) 
//            ( MyStringOps.myIsRealNumber(thisTok) && thisTok.endsWith(".") && MyStringOps.myIsInteger(nextTok) )
// $,<val> -> $<val>  and  $,{<val> -> ${<val>
//            thisTok.equals("$") ||
// <whatever>,} -> <whatever>} ; net result: $
//            nextTok.equals("}")
        ) {
           tok.set( i1, (thisTok + nextTok) );
           tok.remove(i1+1);
           i1--;
        } //End: if ()
//
//if(print)System.out.println(MyTrace.myGetMethodName() + "combined: frags= " + MyArrayListOps.myArrayListToStringBracketed(tok) + "\n");
/*
// Condense number
//
// Condense +/- which are part of number and not an op: ie *,+,3 -> *,+3
      else if (
           lastTok != null &&
           (thisTok.equals("+") || thisTok.equals("-") ) && 
           ( i1 == 0 || lastTok.endsWith("+") || lastTok.endsWith("-") || lastTok.equals("*") || lastTok.equals("/") || lastTok.equals("(") ||
                        lastTok.endsWith("=") || lastTok.endsWith("|") || lastTok.endsWith("&")  ) &&
           ( ( ch = nextTok.charAt(0) ) == '.' || (ch >= '0' && ch <= '9') )
            ) {
        tok.set(i1, (thisTok + nextTok) );
        tok.remove(i1+1);
        i1--;
      } //End: else if ()
*/
//
// Condense scientific notation of form 3e+4, 3e-4, .3e+4, 3E+4, 3E-4 which have separated at +/-
      else if (
            ( (ch = thisTok.charAt(0)) == '+' || ch == '-' || ch == '.' || ( ch >= '0' && ch <= '9') ) &&
            ( thisTok.endsWith("e") || thisTok.endsWith("E") ) &&
            ( nextTok.equals("+") || nextTok.equals("-") ) &&
            ( i1 < tok.size()-2 && MyStringOps.myIsInteger(tok.get(i1+2)) )
                 ) {
          tok.set(i1, (thisTok + nextTok + tok.get(i1+2)) );
          thisTok = tok.get(i1);
//System.out.println("-------MyEvalValue.myFragmentExpression: compressed to " + thisTok);
          tok.remove(i1+2);
          tok.remove(i1+1);
      } //End: else if ()
//
// Condense positive and negative ints
      else if ( (thisTok.equals("+") || thisTok.equals("-") ) && nextTok != null && MyStringOps.myIsIntegerOrLong(nextTok) ) {
        tok.set(i1, (thisTok + nextTok) );
        tok.remove(i1+1);
      }
//
    } //End: for ()
//
//int dummy2=1;
//if(expression.contains("-1"))System.out.println(MyTrace.myGetMethodName() + ": exiting: exp= " + expression + "  : frags= " + MyArrayListOps.myArrayListToStringBracketed(tok));
//
    return tok;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method breaks the input string into 2 or 3 fragments based on
 *  whether or not the fragment character is retained. The string is broken at
 *  the last occurance of any character in the fragmenting character string.
 *
 * @param stringIn  input String
 * @param newFragmentChars  String of characters that can fragment the input String
 * @param includeFragChar  true if the fragmenting character is retained
 *
 * @return  returns array of String fragments
 *
 */
  public static final String[] myFragmentAtLastMatch (
    String   stringIn,
    String   newFragmentChars,
    boolean  includeFragChar )
  {
    String returnStr[] = null;
    if ( stringIn != null && stringIn.length() > 1 && newFragmentChars != null && newFragmentChars.length() > 0 ) {
      int iFr = -1;
      for (int iC=0 ; iC<stringIn.length() ; iC++) {
        if ( MyStrTok.myMatchCharWord ( stringIn.charAt(iC), newFragmentChars ) ) iFr = iC;
      } //End: for ()
      if ( iFr > 0 && iFr < stringIn.length() -1 ) {
        if (includeFragChar) returnStr = new String[3];
        else returnStr = new String[2];
        returnStr[0] = new String(stringIn.substring(0,iFr));
        String lastFrag = new String(stringIn.substring(iFr+1,stringIn.length()));
        if ( includeFragChar ) {
          returnStr[1] = new String(stringIn.substring(iFr,iFr+1));
          returnStr[2] = lastFrag;
        }
        else {
          returnStr[1] = lastFrag;
        }
      }
      else returnStr = new String[]{stringIn};
    } //End: if ()
//
    if ( MyStrTok.FAULT_TRACE ) {
      System.out.print( "MyStrTok.myFragmentLastMatch: str_in = <" + stringIn + "> : fr_ch= <" + newFragmentChars + ">");
      if ( returnStr != null && returnStr.length == 2 ) System.out.print(" : frags= <" + returnStr[0] + "> <" + returnStr[1] + ">");
      else if ( returnStr != null && returnStr.length == 3 ) System.out.print(" : frags= <" + returnStr[0] + "> <" + returnStr[1] + "> <" + returnStr[2] + ">");
      else System.out.print(" : returnStr = null");
      System.out.println();
      System.out.flush();
    }
//
    return returnStr;
  } //End: Method


//------------------  Method  ------------------
// Ex: MyStrTok.myFragment ("my?xx-yy","?-")  returns: my , ?xx , -yy
/**
 * This static method fragments a string based on the fragment characters.<br>
 * Ex. MyStrTok.myFragment ("my?xx-yy","?-")  returns array of "my" , "?xx" , "-yy"
 *
 * @param stringIn  input String
 * @param newFragmentChars  String of characters used to fragment input String
 *
 * @return  returns array of String fragments
 *
 */
  public static final ArrayList<String> myFragmentToVect (
    String   stringIn,
    String   newFragmentChars )
  {
//  try {
//
    if ( stringIn == null || stringIn.length() == 0 || newFragmentChars == null ) return null;
//
//    if (MyPerformanceMonitor.MY_MONITOR_IS_ACTIVATABLE) MyPerformanceMonitor.myEnteringMethod();
//
//System.out.println("MyStrTok.myFragment: str = <" + stringIn + ">, len = " + stringIn.length() + ", sep = <" + separateChars + ">" );
//
    ArrayList<String> stringVect = new ArrayList<>(4);
//
    int     iC = 0;
    int     strtWd = 0;
//
//Scan thru string
    while ( iC < stringIn.length() ) {
//
      if ( iC == stringIn.length() - 1 )
        stringVect.add ( new String(stringIn.substring(strtWd,iC+1)) );
      else if ( iC > strtWd && MyStrTok.myMatchCharWord ( stringIn.charAt(iC), newFragmentChars ) ) {
        stringVect.add ( new String(stringIn.substring(strtWd,iC)) );
        strtWd = iC;
      }
//
      iC++;
    } //End: while ()
//
//    if (MyPerformanceMonitor.MY_MONITOR_IS_ACTIVATABLE) MyPerformanceMonitor.myExitingMethod();
//
    return stringVect;
  } //End: Method


//------------------  Method  ------------------
// Ex: MyStrTok.myFragment ("my?xx-yy","?-")  returns: my , ?xx , -yy
/**
 *
 * @param stringIn  input String
 * @param fragPts  ?
 * @param eliminateLeadingAndEndingBlanks  ?
 *
 * @return  returns array of String fragments
 *
 */
  public static final ArrayList<String> myFragment (
    String   stringIn,
    int[]    fragPts,
    boolean  eliminateLeadingAndEndingBlanks )
  {
//System.out.println("MyStrTok.myFragment:  " + stringIn );
//
    ArrayList<String> stringVect = null;
//
    if ( stringIn != null && stringIn.length() > 0 && fragPts != null && fragPts.length > 0 ) {
      stringVect = new ArrayList<>();
      int p1,p2;
      String newStr;
      for ( int i1=0 ; i1<=fragPts.length ; i1++ ) {
        p1 = ( i1==0 ) ? 0 : fragPts[i1-1];
        p2 = ( i1<fragPts.length ) ? fragPts[i1] : stringIn.length();
        if ( p2 > stringIn.length() ) p2 = stringIn.length();
        newStr = stringIn.substring(p1, p2);
        if ( eliminateLeadingAndEndingBlanks ) newStr = MyStringOps.myStringRemoveLeadingAndEndingBlanks(newStr);
        stringVect.add(newStr);
//System.out.println(" MyStrTok.myFragment: i1= " + i1 + "  : pts= " + p1 + " , " + p2 + "    : strIn= " + stringIn);
        if ( p2 == stringIn.length() ) break;
      }
    } //End: if ()
//
    return stringVect;
  } //End: Method


//------------------  Method  ------------------
// Ex: MyStrTok.myFragment ("my?xx-yy","?-")  returns: my , ?xx , -yy
/**
 * This static method fragments a string based on the fragment characters.<br>
 * Ex. MyStrTok.myFragment ("my?xx-yy","?-")  returns array of "my" , "?xx" , "-yy"
 *
 * @param stringIn  input String
 * @param newFragmentChars  String of characters used to fragment input String
 *
 * @return  returns array of String fragments
 *
 */
  public static final String[] myFragment (
    String   stringIn,
    String   newFragmentChars )
  {
//
    ArrayList<String> stringVect = MyStrTok.myFragmentToVect(stringIn, newFragmentChars);
//
//  Convert to string ArrayList
    String stringsOut[] = new String[stringVect.size()];
    for ( int iC=0 ; iC<stringVect.size() ; iC++ ) stringsOut[iC] = stringVect.get(iC);
//
    return stringsOut;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method eliminates characters in a string.
 *
 * @param stringIn  input String
 *
 * @return  returns modified String
 *
 */
  public static final String myEliminateLeadingSpaces ( String stringIn )
  {
    if ( stringIn == null || stringIn.length() == 0 ) return stringIn;
//
    int strt = -1;
    for ( int i1=0 ; i1<stringIn.length() ; i1++ ) {
      if ( stringIn.charAt(i1) != ' ') break;
      strt = i1;
    } //End: for ()
//
    if ( strt < 0 || strt >= stringIn.length()-1 ) return stringIn;
    return stringIn.substring(strt+1);
  } // End: Method

//------------------  Method  ------------------
/**
 * This static method eliminates characters in a string.
 *
 * @param stringIn  input String
 * @param chars  String of characters to eliminate
 *
 * @return  returns modified String
 *
 */
  public static final String myEliminateCharacters (
    String   stringIn,
    String   chars )
  { return MyStrTok.myEliminateCharacters(stringIn, chars, false); }


//------------------  Method  ------------------
/**
 * This static method recombines scientific notation numbers
 *
 * @param args ?
 *
 */
/*
  public static final void myRecombineScientificNotationNumbers ( ArrayList<String> args )
  {
    for (int i1=0 ; i1<args.size()-2 ; i1++) {
      String arg0 = args.get(i1);
      if ( ( arg0.endsWith("e") || arg0.endsWith("E") ) &&
          MyStringOps.myIsIntOrRealNumber(arg0.substring(0, arg0.length()-1)) &&
          ( args.get(i1+1).equals("+") || args.get(i1+1).equals("-") ) &&
          MyStringOps.myIsInteger(args.get(i1+2) ) ) {
        String tmp = arg0 + args.get(i1+1) + args.get(i1+2);
        args.set(i1, tmp);
        args.remove(i1+2);
        args.remove(i1+1);
      }
    }
  } //End: Method
*/

//------------------  Method  ------------------
/**
 * This static method recombines negative numbers
 *
 * @param args ?
 *
 */
/*
  public static final void myRecombineNegativeNumbers ( ArrayList<String> args )
  {
    for (int i1=0 ; i1<args.size()-1 ; i1++) {
      String arg0 = args.get(i1);
      if ( arg0.equals("-") &&
           MyStringOps.myIsIntOrRealOrScientificNotationNumber(args.get(i1+1) ) &&
           ( i1 == 0 || args.get(i1-1).endsWith("\"") || args.get(i1-1).endsWith(",") || args.get(i1-1).endsWith("(") )
          ) {
        String tmp = arg0 + args.get(i1+1);
        args.set(i1, tmp);
        args.remove(i1+1);
      }
    }
  } //End: Method
*/

//------------------  Method  ------------------
/**
 * This static method method eliminates characters in a string.<br>
 * Ex: MyStrTok.myEliminateCharacters("aaXbb\"ccXdd\"", "X", MyStrTok.MY_SAVE_INSIDE_QUOTES_YES)<br>
 * returns "aabb\"ccXdd\""
 * 
 *
 * @param stringIn  input String
 * @param chars  String of characters to eliminate
 * @param saveInsideQuotes  true prevents characters inside double quotes from being eliminated
 *
 * @return  returns modified String
 *
 */
  public static final String myEliminateCharacters (
    String   stringIn,
    String   chars,
    boolean  saveInsideQuotes )
  {
    String returnStr = null;
    if ( stringIn != null && stringIn.length() > 0 && chars != null && chars.length() > 0 ) {
      char newStrChar[] = new char[stringIn.length()+1];
      int iP = 0;
      int iC;
      char strCh;
      boolean match;
      boolean insideQuotes = false;
      for (int iS=0 ; iS<stringIn.length() ; iS++) {
        strCh = stringIn.charAt(iS);
        if ( strCh == '\"' ) insideQuotes = !insideQuotes;
        match = false;
// skip if preserving inside quotes
        if ( !saveInsideQuotes || !insideQuotes ) {
          for ( iC=0 ; iC<chars.length() ; iC++ ) {
            if ( strCh == chars.charAt(iC) ) { match = true; break; }
          } //End: for ()
        } //End: if ()
        if ( !match ) newStrChar[iP++] = strCh;
      } //End: for ();
      if ( iP > 0 ) returnStr = String.valueOf(newStrChar,0,iP);
    } //End: if ()
   
    return returnStr;
//
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method returns true if a character exists in a string.
 *
 * @param strWithChar  String with char to look at
 * @param charPosition  Character position
 * @param wdOfMatchChars  String of possible match chars
 *
 * @return  returns true if char is found
 *
 */
  private static final boolean myMatchCharWord ( String strWithChar, int charPosition, String wdOfMatchChars )
  {
    if ( strWithChar == null ||
         charPosition < 0 || charPosition > strWithChar.length() -1 ||
         wdOfMatchChars == null ||
         ( charPosition > 0 && strWithChar.charAt(charPosition-1) == '\\' )  ) return false;
//
    return MyStrTok.myMatchCharWord(strWithChar.charAt(charPosition), wdOfMatchChars);
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character exists in a string.
 *
 * @param ch  ?
 * @param isolationPairs  ?
 * @param pairCnt  ?
 *
 * @return  ?
 *
 */
/*
  private static final int myMatchCharToIsolatePair( char ch, String[] isolationPairs, int pairCnt)
  {
    int pairCnt2 = pairCnt;
    if ( isolationPairs != null && isolationPairs.length > 0 ) {
      for ( int i1=0 ; i1<isolationPairs.length ; i1++ ) {
        if ( isolationPairs[i1].charAt(0) == ch ) { pairCnt2++; break; }
        else if ( isolationPairs[i1].charAt(1) == ch ) { pairCnt2--; break; }
      } //End: for ()
    } //End: if ()
    return pairCnt2;
  } //End: Method
*/
  
//------------------  Method  ------------------
/**
 * This static method returns true if a character exists in a string.
 *
 * @param charToMatch  char to look at
 * @param wdOfMatchChars  String of possible match chars
 *
 * @return  returns true if char is found
 *
 */
  private static final boolean myMatchCharWord ( char charToMatch, String wdOfMatchChars )
  {
    if ( wdOfMatchChars == null || wdOfMatchChars.length() == 0 ) return false;
    int i1;
    for ( i1=0 ; i1<wdOfMatchChars.length() ; i1++ ) {
      if ( wdOfMatchChars.charAt(i1) == charToMatch ) return true;
    }
    return false;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method looks for the occurance of one of an array of strings as its starting sequence
 * and returns the length of the match.
 *
 * @param str  String to examine starting sequence on
 * @param breakAndKeepStrings  array of Strings to compare against
 *
 * @return  length of matching string
 *
 */
  private static final boolean myMatchStringInString ( String str, String[] breakAndKeepStrings, MyObjIntLocalSimple info )
  {
//    int length = 0;
    info.n = 0;
    if ( str != null && str.length() > 0 && breakAndKeepStrings != null && breakAndKeepStrings.length > 0 ) {
      for (int i1=0 ; i1<breakAndKeepStrings.length ; i1++) {
        if ( str.startsWith(breakAndKeepStrings[i1]) ) {
          info.n = breakAndKeepStrings[i1].length();
//if(DO_TRACE)System.out.println("----" + MyTrace.myGetMethodName() + ": found " + breakAndKeepStrings[i1] + "  in " + str);
          break;
        } //End: if ()
      } //End: for ()
    } //End: if ()
    return info.n > 0;
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method indicates whether on not to break a string at a specified position
 * as a function of it meeting a separation option.
 *
 * @param stringIn  input String
 * @param iC  position in input String to look at
 * @param separateOption  separator options
 *
 * @return  true is input String needs to be broken at specified point
 *
 */
  private static final boolean mySeparatorOptionIsBreakChar (
    String stringIn,
    int    iC,
    int    separateOption
  ) {

    int strLen = stringIn.length();

    if ( iC < 0 || iC >= strLen || separateOption == MyStrTok.MY_SEPARATE_NO_OPTIONS ) return false;
//

//    boolean separateLetters       = ( separateOption & MyStrTok.MY_SEPARATE_LETTERS ) > 0 ? true : false;
//    boolean separateNumbers       = ( separateOption & MyStrTok.MY_SEPARATE_POSITIVE_NUMBERS ) > 0 ? true : false;
//    boolean separateIntegers      = ( separateOption & MyStrTok.MY_SEPARATE_POSITIVE_INTEGERS ) > 0 ? true : false;
    boolean separateBeforeLineReturn = ( separateOption & MyStrTok.MY_SEPARATE_BEFORE_LINE_RET ) > 0 ? true : false;
    
//    boolean t1;
//    boolean t2;
    boolean retVal = false;

/*
    if ( separateLetters ) {
      t1 = ( MyStrTok.myIsLetterChar(stringIn,iC) ) ? true : false;
      t2 = ( iC > 0 && MyStrTok.myIsLetterChar(stringIn,iC-1) ) ? true : false;
      if ( ( t1 || t2 ) && t1 != t2 ) retVal = true;
    }
*/
/*
    else if ( separateNumbers ) {
      t1 = ( MyStrTok.myIsNumberChar(stringIn,iC) ) ? true : false;
      t2 = (  iC > 0 && MyStrTok.myIsNumberChar(stringIn,iC-1) ) ? true : false;
      if ( ( t1 || t2 ) && t1 != t2 ) retVal = true;
System.out.println("MyStrTok.myBreakOnSeparatorOption: ch = " + stringIn.charAt(iC) + " : isnumb= " + retVal );
    }
*/
/*
    else if ( separateIntegers ) {
      t1 = ( MyStrTok.myIsIntegerChar(stringIn,iC) ) ? true : false;
      t2 = ( iC > 0 && MyStrTok.myIsIntegerChar(stringIn,iC-1) ) ? true : false;
      if ( ( t1 || t2 ) && t1 != t2 ) retVal = true;
    }
*/
    if ( separateBeforeLineReturn ) {
      if ( stringIn.charAt(iC) == '\n' ) retVal = true;
    }

    return retVal;

  } //End: Method

//------------------  Method  ------------------
/**
 * This static method 
 *
 * @param stringIn  input String
 * @param iC  position in input String to look at
 * @param separateOption  separator options
 *
 * @return  true is input String needs to be broken at specified point
 *
 */
  private static final boolean mySeparatorOptionIsGroupChar (
    String    stringIn,
    int       iC,
    int       separateOption,
    MyObjIntLocalSimple grpInfo )
  {
//
    int info = 0;
//
    if      ( (separateOption & MyStrTok.MY_SEPARATE_NUMBERS) > 0  && MyStringOps.myIsNumberChar(stringIn, iC) )  info = MyStrTok.MY_SEPARATE_NUMBERS;
    else if ( (separateOption & MyStrTok.MY_SEPARATE_INTEGERS) > 0 && MyStringOps.myIsIntegerChar(stringIn, iC) ) info = MyStrTok.MY_SEPARATE_INTEGERS;
    else if ( (separateOption & MyStrTok.MY_SEPARATE_SPACES) > 0   && stringIn.charAt(iC) == ' ' ) info = MyStrTok.MY_SEPARATE_SPACES;
//
    if ( grpInfo != null ) grpInfo.n = info;
//
    return ( info == 0 ) ? false : true;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is a letter.
 *
 * @param w  input String
 * @param myImaginaryAry  position in input String
 *
 * @return  true if character at position is a letter
 *
 */
/*
  public static final boolean myIsLetterChar ( char ch )
  {
    if ( ( ch >= 'A' && ch <= 'lengthInMeters' ) || ( ch >= 'a' && ch <= 'z' ) ) return true;
    return false;
  } //End: Method
*/


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is a letter.
 *
 * @param w  input String
 * @param myImaginaryAry  position in input String
 *
 * @return  true if character at position is a letter
 *
 */
/*
  private static final boolean myIsLetterChar ( String w, int myImaginaryAry )
  {
    if ( ( w.charAt(myImaginaryAry) >= 'A' && w.charAt(myImaginaryAry) <= 'lengthInMeters' ) || ( w.charAt(myImaginaryAry) >= 'a' && w.charAt(myImaginaryAry) <= 'z' ) ) return true;
    return false;
  } //End: Method
*/


//------------------  Method  ------------------
/**
 * This static method returns true if the input string is all tabs or blanks
 *
 * @param arg  input String
 *
 * @return  true if all tabs or blanks
 *
 */
  public static final boolean myAllBlankOrTab ( String arg )
  {
  	if ( arg == null ) return true;
  	for (int i1=0 ; i1<arg.length() ; i1++) {
      if ( arg.charAt(i1) != ' ' && arg.charAt(i1) != '\t' ) return false;
  	}
    return true;
  } //End: Method

//------------------  METHOD  ------------------
/**
 * This static method uses toString() on an object and then converts the string to an integer.
 *
 * @param obj  input object
 *
 * @return  return value
 *
 */
  public static final int myObjToInt ( Object obj )
  {
    int retVal = MyC.NOT_AN_INTEGER; // Integer.MIN_VALUE;
    if ( obj != null ) {
      String str = obj.toString();
      if ( str != null && MyStringOps.myIsInteger(str) ) retVal = Integer.parseInt ( str );
    }
    return retVal;
  } //End: Method

//------------------  METHOD  ------------------
/**
 * This static method converts a string to a float.
 *
 * @param str  input String
 *
 * @return  return float value
 *
 */
/*
  public static final float[] myGetFloatAryFrmString ( String str )
  {
    float[] numb = null;
    if ( str != null ) {
      String[] tok = MyStrTok.myTokenize ( str, " " );
      if ( tok != null && tok.length > 0 ) {
        numb = new float[tok.length];
        for (int i1=0 ; i1<numb.length ; i1++) {
          numb[i1] = Float.parseFloat(tok[i1]);
        } //End: for ()
      } //End: if ()
    } //End: if ()
    return numb;
  } //End: Method
*/

//------------------  METHOD  ------------------
/**
 * This static method concatonates a string array into a string.
 *
 * @param frags  input String array
 *
 * @return  return String
 *
 */
  public static final String myFragsToString ( String[] frags )
  {
    StringBuffer str = new StringBuffer("");
    if ( frags != null && frags.length > 0 ) {
      for (int i1=0 ; i1<frags.length ; i1++) {
        if ( i1 > 0 ) str.append(" ");
        str.append("<" + frags[i1] + ">");
      } //End: for ()
    } //End: if ()
    return str.toString();
  } //End: Method


//------------------------------------------------------------------------
//---------------------------  Methods:  ---------------------------------
//------------------------------------------------------------------------


//------------------  METHOD  ------------------
/**
 * This is the constructor. It can be used in lieu of the static methods to create a standard tokenizer object.
 *
 * @param breakAndEliminateChars  ?
 * @param breakAndKeepChars  ?
 * @param breakAndKeepStrings  ?
 * @param isolateChars  ?
 * @param separatorOptions  ?
 *
 */
  public MyStrTok (
      String   breakAndEliminateChars,
      String   breakAndKeepChars,
      String[] breakAndKeepStrings,
      String   isolateChars,
      int      separatorOptions
  )
  {
    this.myBreakAndEliminateChars = breakAndEliminateChars;
    this.myBreakAndKeepChars = breakAndKeepChars;
    this.myBreakAndKeepStrings = breakAndKeepStrings;
    this.myIsolateChars = isolateChars;
    this.mySeparatorOptions = separatorOptions;
  } //End: Method


//------------------  METHOD  ------------------
/**
 * This method
 *
 * @param String  ?
 *
 */
  public final ArrayList<String> myTokenizeToArrayList ( String str )
  { return MyStrTok.myTokenizeToArrayList(str, this.myBreakAndEliminateChars, this.myBreakAndKeepChars, this.myBreakAndKeepStrings, this.myIsolateChars, this.mySeparatorOptions); }


} //End: class MyStrTok


//---------------------------------------------------------------
//----------------  CLASS: MyObjIntLocalSimple  -----------------
//---------------------------------------------------------------
/**
* This class ?
*
* @author James Everitt
*/
final class MyObjIntLocalSimple
{

  public int n = MyC.NOT_AN_INTEGER;

//------------------  Method  ------------------
/**
* This constructor method ?
*
* @param nIn  ?
*/
  MyObjIntLocalSimple ( int nIn ) { this.n = nIn; }

} //End: class MyObjIntLocalSimple
