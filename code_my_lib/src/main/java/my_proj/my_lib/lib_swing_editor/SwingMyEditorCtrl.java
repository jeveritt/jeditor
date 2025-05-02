// SwingMyEditorCtrl.java
/*
 * Copyright (C) 2022,2025 James Everitt
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


//--------------------------------------------------------------------
//------------------  CLASS: SwingMyEditorCtrl  ----------------------
//--------------------------------------------------------------------
/**
 * This class defines the allowed values for the editor control integer used in internal API's. Arguments are OR'ed.
 * <br>
 *   Ex: 0x2|0x40
 *
 * @author James Everitt
 */
public final class SwingMyEditorCtrl {

/** -ctrl &lt;value&gt; : Specifies */
//  public static final String  ARG_CONTROL                    = "-ctrl";

// Editor controls
  
/** Edit control */
  public static final int     MY_ALLOW_UNENCRYP_READ         = 0x2;
/** Edit control */
  public static final int     MY_ALLOW_UNENCRYP_WRITE        = 0x4;
/** Edit control */
  public static final int     MY_ALLOW_ENCRYP_READ           = 0x8;
/** Edit control */
  public static final int     MY_ALLOW_ENCRYP_WRITE          = 0x10;
/** Edit control */
  public static final int     MY_ALLOW_ANY_WRITE             = MY_ALLOW_UNENCRYP_WRITE | MY_ALLOW_ENCRYP_WRITE;
/** Edit control */
  public static final int     MY_IS_ENCRYPT                  = 0x20;
/** Edit control */
  public static final int     MY_NO_MENU                     = 0x40;
/** Edit control */
  public static final int     MY_ALLOW_ONLY_SINGLE_FILE      = 0x80;
/** Edit control */
  public static final int     MY_ALLOW_ALL = MY_ALLOW_UNENCRYP_READ | MY_ALLOW_UNENCRYP_WRITE | MY_ALLOW_ENCRYP_READ | MY_ALLOW_ENCRYP_WRITE;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private SwingMyEditorCtrl ( ) {}


//--------------------------------------------------------
//------------------  STATIC METHODS  -------------------
//--------------------------------------------------------
        
//------------------  Static Method  ------------------
/**
 * This static method
 * 
 * @param ctrl  ?
 * @param sb  ?
 * @param ind  ?
 */
  static final void myGetEditorCtrlInfo ( int ctrl, StringBuffer sb, String ind )
  {
    sb.append( ind + SwingMyEditorArgs.class.getSimpleName() + " info:" + "\n");
    ind += "  ";
//    sb.append( ind + "numb old files    = " + MY_DEFAULT_NUMB_OLD_FILES_SAVED + "\n" );
//    sb.append( ind + "print stack trace = " + PRINT_STACK_TRACE + "\n" );
    sb.append( ind + "calling program   = " + "lib_swing_editor.SwingMyEditor<version>" + "\n" );
//    sb.append( ind + "font              = " + MY_FONT + "\n" );
    sb.append( ind + "ctrl = 0x" + Integer.toHexString(ctrl) + " : decodes to:" + "\n" );
    sb.append( ind + "  unencrypt read    = " + ((ctrl & MY_ALLOW_UNENCRYP_READ) != 0) + "\n" );
    sb.append( ind + "  unencrypt write   = " + ((ctrl & MY_ALLOW_UNENCRYP_WRITE) != 0) + "\n" );
    sb.append( ind + "  encrypt read      = " + ((ctrl & MY_ALLOW_ENCRYP_READ) != 0) + "\n" );
    sb.append( ind + "  encrypt write     = " + ((ctrl & MY_ALLOW_ENCRYP_WRITE) != 0) + "\n" );
    sb.append( ind + "  any write         = " + ((ctrl & MY_ALLOW_ANY_WRITE) != 0) + "\n" );
    sb.append( ind + "  file is encrypted = " + ((ctrl & MY_IS_ENCRYPT) != 0) + "\n" );
    sb.append( ind + "  only single file  = " + ((ctrl & MY_ALLOW_ONLY_SINGLE_FILE) != 0) + "\n" );
    sb.append( ind + "  no menu           = " + ((ctrl & MY_NO_MENU) != 0) + "\n" );
//    sb.append( ind + "  only view         = " + ((ctrl & MY_ALLOW_ONLY_VIEW) != 0) + "\n" );
    sb.append( ind + "  allow all         = " + ((ctrl & MY_ALLOW_ALL) != 0) + "\n" );
  } //End: Method


} //End: class SwingMyEditorCtrl
