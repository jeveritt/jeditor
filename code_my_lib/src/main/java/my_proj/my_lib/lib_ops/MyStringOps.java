// MyStringOps.java
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

//------------------  Package statement  ------------------
package my_proj.my_lib.lib_ops;

//------------------  Import statements  ------------------



//------------------  CLASS: MyStringOps  ------------------
/**
 * This class consists of static method operators on Strings.
 *
 * @author James Everitt
 */
public class MyStringOps {
  
  public static final String MY_SPACES_FOR_TAB = "        ";


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private MyStringOps ( ) {}


// ----------------------------------------------------------
// ------------------ Static Methods: -----------------------
// ----------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is a desimal character
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is a desimal character
 *
 */
  private static final boolean myIsDesimalChar ( String w, int i )
  {
    if ( w.charAt(i) == '.' && ( ( i > 0 && MyStringOps.myIsPositiveIntegerChar(w,i-1) ) || ( i+1<w.length() && MyStringOps.myIsPositiveIntegerChar(w,i+1) ) ) ) return true;
    return false;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is a 
 *
 */
  private static final boolean myIsExpEChar ( String w, int i )
  {
    if ( ( w.charAt(i) == 'e' || w.charAt(i) == 'E' ) && i > 0 && MyStringOps.myIsPositiveIntegerOrDesimalChar(w,i-1) && i+1<w.length() && MyStringOps.myIsIntegerOrSignChar(w,i+1) ) return true;
    return false;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is an exponent sign.
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is an exponent sign
 *
 */
  private static final boolean myIsExpSignChar ( String w, int i )
  {
    if ( ( w.charAt(i) == '+' || w.charAt(i) == '-' ) && i > 0 && MyStringOps.myIsExpEChar(w,i-1) ) return true;
    return false;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if an input string is completely an integer
 *
 * @param arg  input string
 *
 * @return  true if integer
 *
 */
  public static final boolean myIsInteger ( String arg )
  {
    boolean isInt = MyStringOps.myIsIntegerOrLong(arg);
//
    if ( isInt ) {
      long longInt = Long.parseLong(arg);
      if ( longInt > Integer.MAX_VALUE || longInt < Integer.MIN_VALUE ) isInt = false;
//if(!isInt)System.out.println(MyTrace.myGetMethodName() + ": maxMin= " + Integer.MAX_VALUE + "," + Integer.MIN_VALUE + ": found long= " + longInt);
    }
//
    return isInt;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is an integer
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is an integer
 *
 */
  public static final boolean myIsIntegerChar ( String w, int i )
  {
    return MyStringOps.myIsLeadingSignChar(w,i) || (w.charAt(i) >= '0' && w.charAt(i) <= '9');
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if an input string is completely an integer
 *
 * @param arg  input string
 *
 * @return  true if integer
 *
 */
  public static final boolean myIsIntegerOrLong ( String arg )
  {
    boolean isInt = true;
    if ( arg == null || arg.length() == 0 ) isInt = false;
    else if ( arg.length() == 1 ) {
      if ( ! MyStringOps.myIsPositiveIntegerChar(arg,0) ) isInt = false;
    }
    else {
      if ( ! MyStringOps.myIsIntegerOrSignChar(arg,0) ) isInt = false;
      else {
        for (int i1=1 ; i1<arg.length() ; i1++) {
          if ( ! MyStringOps.myIsPositiveIntegerChar(arg,i1) ) { isInt = false; break; }
        }
      }
    }
//
    return isInt;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is an integer or + or -
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is an integer or + or -
 *
 */
  private static final boolean myIsIntegerOrSignChar ( String w, int i )
  {
    if ( ( w.charAt(i) >= '0' && w.charAt(i) <= '9' ) || w.charAt(i) == '+' || w.charAt(i) == '-' ) return true;
    return false;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at 1 below the specified position in the input string +/-.
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is an exponent sign
 *
 */
  public static final boolean myIsLeadingSignChar ( String w, int i )
  {
    return ( w.charAt(i) == '+' || w.charAt(i) == '-' ) && ( w.length() > i+1 && MyStringOps.myIsPositiveIntegerOrDesimalChar(w, i+1) );
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is part of a number
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is part of a number
 *
 */
  public static final boolean myIsNumberChar ( String w, int i )
  {
    boolean retVal = false;
    if ( MyStringOps.myIsPositiveIntegerChar(w,i) || MyStringOps.myIsDesimalChar(w,i) ||
         MyStringOps.myIsExpEChar(w,i) || MyStringOps.myIsExpSignChar(w,i) || MyStringOps.myIsLeadingSignChar(w,i) ) retVal = true;
//System.out.println("  myIsNumberChar:  w= " + w + "  : myImaginaryAry= " + myImaginaryAry + " : char= <" + w.charAt(myImaginaryAry) + ">  : val= " + retVal);
    return retVal;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is an integer
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is an integer
 *
 */
  public static final boolean myIsPositiveIntegerChar ( String w, int i )
  {
    return w.charAt(i) >= '0' && w.charAt(i) <= '9';
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if a character at the specified position in the input string is an integer or desimal
 *
 * @param w  input String
 * @param i  position in input String
 *
 * @return  true if character at position is an integer or desimal
 *
 */
  private static final boolean myIsPositiveIntegerOrDesimalChar ( String w, int i )
  {
    if ( ( w.charAt(i) >= '0' && w.charAt(i) <= '9' ) || w.charAt(i) == '.' ) return true;
    return false;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method returns true if an input string is completely an integer
 *
 * @param arg  input string
 *
 * @return  true if real or scientific notation
 *
 */
  public static final boolean myIsRealOrScientificNotationNumber ( String arg )
  {
// Note: don't allow real numbers
    return false;
  } //End: method


//------------------ Method ------------------
/**
 * This static method ?
 *
 * @param ary  ?
 * @param str  ?
 * @param ignoreCase  ?
 *
 * @return  ?
 *
 */
  public static final boolean myStringArrayContainsString ( String[] ary, String str, boolean ignoreCase )
  {
//System.out.println(MyTrace.myGetMethodName() + ": str= " + str + "  : ary= " + MyStringOps.myStringArrayToString(ary, ','));
//
    boolean found = false;
//
    if ( ary != null && ary.length > 0 && str != null && str.length() > 0 ) {
      for (int i1=0 ; i1<ary.length ; i1++) {
        if ( (ignoreCase && ary[i1].equals(str)) || (!ignoreCase && ary[i1].equalsIgnoreCase(str)) ) {
          found = true;
          break;
        } //End: if ()
      } //End: for ()
    } //End: if ()
//
    return found;
  } //End: Method


//------------------ Method ------------------
/**
 * This static method ?
 *
 * @param str  ?
 *
 * @return  ?
 *
 */
  public static final String myStringRemoveLeadingAndEndingBlanks(String str) {
//    if ( MyC.MY_THROW_DATA_CHECK_ERRORS && str == null ) throw new Error ("Error: MyStringOps.myRemoveLeadingBlanks: str = null");
//
    if ( str == null ) return null;
//
    int strt = 0;
    for ( int i1=0 ; i1<str.length() ; i1++ ) {
      if ( str.charAt(i1) != ' ' ) { strt = i1; break; }
    }
//
    int end = str.length();
    for ( int i1=str.length()-1 ; i1>=0 ; i1-- ) {
      if ( str.charAt(i1) != ' ' ) break;
      end--;
    }
//
//    return (strt == 0) ? str : str.substring(strt,end);
    return str.substring(strt,end);
  } // End: Method

} // End: public class MyStringOps
