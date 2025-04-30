// SwingMyEditorConst.java
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

import java.awt.Font;


//--------------------------------------------------------------------
//------------------  CLASS: SwingMyEditorConst  ------------------
//--------------------------------------------------------------------
/**
 * This class
 *
 * @author James Everitt
 */
public final class SwingMyEditorConst {
  
/** Enum list of editor types */
  static enum MY_TYPE { FRAME, INT_FRAME, DIALOG, UNKNOWN }
/** Type of editor */
  static MY_TYPE myType = MY_TYPE.UNKNOWN;

// Input arguments as Strings
  
/** ? */
  public static final String  ARG_FILE                       = "-file";
/** ? */
  public static final String  ARG_WORKING_DIR                = "-dir";
/** ? */
  public static final String  ARG_SIZE_X                     = "-sizex";
/** ? */
  public static final String  ARG_SIZE_Y                     = "-sizey";
/** ? */
  public static final String  ARG_TITLE                      = "-title";

/** ? */
  public static final String  ARG_PASSWORD                   = "-pw";
/** ? */
  public static final String  ARG_FILE_IS_ENCRYPTED          = "-isenc";
/** ? */
  public static final String  ARG_ALLOW_READ_ONLY            = "-ro";
/** ? */
  public static final String  ARG_ALLOW_READ_WRITE           = "-rw";
/** ? */
  public static final String  ARG_ALLOW_ENCRYPTED_READ_ONLY  = "-enro";
/** ? */
  public static final String  ARG_ALLOW_ENCRYPTED_READ_WRITE = "-enrw";
/** ? */
  public static final String  ARG_NO_MENU                    = "-nomenu";
/** ? */
  public static final String  ARG_CONTROL                    = "-ctrl";
  
/** Sets verbose level.<p>Ex: -verbose 5*/
  public static final String ARG_VERBOSE                     = "-verbose";
/** Sets verbose output file.<p>Ex: -trace_out /tmp/zztrace.txt*/
  public static final String ARG_TRACE_OUTPUT                = "-trace_out";

// Editor controls
  
/** Edit control */
  public static final int     MY_ALLOW_UNENCRYP_READ         = 0x2;
/** Edit control */
  public static final int     MY_ALLOW_UNENCRYP_WRITE        = 0x4;
/** Edit control */
  public static final int     MY_ALLOW_ENCRYP_READ           = 0x8;
/** Edit control */
         static final int     MY_ALLOW_ENCRYP_WRITE          = 0x10;
/** Edit control */
         static final int     MY_ALLOW_ANY_WRITE             = MY_ALLOW_UNENCRYP_WRITE | MY_ALLOW_ENCRYP_WRITE;
/** Edit control */
         static final int     MY_IS_ENCRYPT                  = 0x20;
/** Edit control */
  public static final int     MY_NO_MENU                     = 0x40;
/** Edit control */
  public static final int     MY_ALLOW_ONLY_SINGLE_FILE      = 0x80;
/** Edit control */
  public static final int     MY_ALLOW_ALL = MY_ALLOW_UNENCRYP_READ | MY_ALLOW_UNENCRYP_WRITE | MY_ALLOW_ENCRYP_READ | MY_ALLOW_ENCRYP_WRITE;

/** Number of files saved */
         static final int     MY_DEFAULT_NUMB_OLD_FILES_SAVED = 6;

/** ? */
  public static final int     MY_DEFAULT_SIZE_X              = 800;
/** ? */
  public static final int     MY_DEFAULT_SIZE_Y              = 700;
         
/** ? */
         static final boolean PRINT_STACK_TRACE              = false;
/** ? */
         static final String  MY_ENCRYPT_EXTENSION           = ".gpg";
         
/** ? */
         static final Font    MY_MONOSPACED_FONT             = new Font(Font.MONOSPACED, Font.PLAIN, 14);
/** ? */
         static       Font    MY_FONT                        = new Font(Font.MONOSPACED, Font.PLAIN, 14);

/** VI save and exit keys */
        static enum MY_WRITE_AND_EXIT { q, wq, forceQ, w }


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private SwingMyEditorConst ( ) {}


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
  static final void myGetEditorConstInfo ( int ctrl, StringBuffer sb, String ind )
  {
    sb.append( ind + SwingMyEditorConst.class.getSimpleName() + " info:" + "\n");
    ind += "  ";
    sb.append( ind + "numb old files    = " + MY_DEFAULT_NUMB_OLD_FILES_SAVED + "\n" );
    sb.append( ind + "print stack trace = " + PRINT_STACK_TRACE + "\n" );
    sb.append( ind + "calling program   = " + "lib_swing_editor.SwingMyEditor<version>" + "\n" );
    sb.append( ind + "font              = " + MY_FONT + "\n" );
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


} //End: class SwingMyEditorConst
