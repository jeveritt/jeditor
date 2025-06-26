// c.java
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

package my_proj.my_lib.lib;


//-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------
//----------------------------------  CLASS: MyC  -------------------------------
//-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------



//----------------  CLASS:  ----------------
/**
* This class contains project wide constants.
*
* @author James Everitt
*/
public class MyC {

/** Use the minimum possible value of an integer to indicate not an integer. */
  public static final int NOT_AN_INTEGER = Integer.MIN_VALUE;  // Note: Integer.MIN_VALUE is used independently several places
  

//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private MyC ( ) {}

} // End: public class MyC
