// MyStringOps.java
/*
 * Copyright (C) 2010 James Everitt
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
package my_proj.my_lib.lib_ops;

//------------------  Import statements  ------------------



//------------------  CLASS: MyStringOps  ------------------
/**
 * This class consists of static method operators on Strings.
 *
 * @author James Everitt
 */
public class MyStringOps {


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

} // End: public class MyStringOps
