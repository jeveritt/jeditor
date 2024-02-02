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
  
// Input arguments as Strings
  public static final String  ARG_FILE                       = "-file";
  public static final String  ARG_WORKING_DIR                = "-dir";
  public static final String  ARG_SIZE_X                     = "-sizex";
  public static final String  ARG_SIZE_Y                     = "-sizey";
  public static final String  ARG_TITLE                      = "-title";

  public static final String  ARG_PASSWORD                   = "-pw";
  public static final String  ARG_FILE_IS_ENCRYPTED          = "-isenc";
  public static final String  ARG_ALLOW_FILE_OPS             = "-ops";
  public static final String  ARG_ALLOW_READ_ONLY            = "-ro";
  public static final String  ARG_ALLOW_READ_WRITE           = "-rw";
  public static final String  ARG_ALLOW_ENCRYPTED_READ_ONLY  = "-enro";
  public static final String  ARG_ALLOW_ENCRYPTED_READ_WRITE = "-enrw";
  public static final String  ARG_NO_MENU                    = "-nomenu";
  public static final String  ARG_CONTROL                    = "-ctrl";

// Editor control
         static final int     MY_ALLOW_FILE_OPS              = 0x1;
         static final int     MY_ALLOW_UNENCRYP_READ         = 0x2;
         static final int     MY_ALLOW_UNENCRYP_WRITE        = 0x4;
         static final int     MY_ALLOW_ENCRYP_READ           = 0x8;
         static final int     MY_ALLOW_ENCRYP_WRITE          = 0x10;
         static final int     MY_IS_ENCRYPT                  = 0x20;
         static final int     MY_NO_MENU                     = 0x100;

// Input args composite as control int
  public static final int     MY_ALLOW_ALL = MY_ALLOW_FILE_OPS | MY_ALLOW_UNENCRYP_READ | MY_ALLOW_UNENCRYP_WRITE;
  public static final int     MY_ALLOW_ONLY_VIEW = MY_NO_MENU;
  public static final int     MY_ALLOW_EDIT_SINGLE_FILE = MY_ALLOW_FILE_OPS | MY_ALLOW_UNENCRYP_WRITE;
  public static final int     MY_ALLOW_EDIT_BUT_NOT_SAVE = MY_ALLOW_FILE_OPS;
  public static final int     MY_DO_ENCRYPTED = MY_ALLOW_FILE_OPS | MY_ALLOW_UNENCRYP_READ | MY_ALLOW_ENCRYP_READ | MY_ALLOW_ENCRYP_WRITE | MY_IS_ENCRYPT;

// Number of files saved
         static final int     MY_DEFAULT_NUMB_OLD_FILES_SAVED = 6;
         
// 
  public static final int     MY_DEFAULT_SIZE_X              = 800;
  public static final int     MY_DEFAULT_SIZE_Y              = 700;
         
         static final boolean PRINT_STACK_TRACE              = false;
         static final String  MY_ENCRYPT_EXTENSION           = ".gpg";
         
         static       Font    MY_FONT = new Font(Font.MONOSPACED, Font.BOLD, 12);

//VI save and exit keys
        static enum MY_WRITE_AND_EXIT { q, wq, forceQ, w }

//--------------------------------------------------------
//------------------  STATIC METHODS  -------------------
//--------------------------------------------------------


//--------------------------------------------------------
//------------------  METHODS  --------------------------
//--------------------------------------------------------


} //End: class SwingMyEditorConst
