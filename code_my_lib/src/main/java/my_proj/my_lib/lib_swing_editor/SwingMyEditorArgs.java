// SwingMyEditorArgs.java
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


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//----------------------  CLASS: SwingMyEditorArgs  ---------------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class defines editor program command line arguments.
 *
 * @author James Everitt
 */
final class SwingMyEditorArgs {

// Input arguments as Strings

/** -file &lt;file_name&gt; : Specifies the file being read */
  static final String ARG_FILE                       = "-file";
/** -dir &lt;working_dir&gt; : Specifies the working directory*/
  static final String ARG_WORKING_DIR                = "-dir";
/** -sizex &lt;width&gt; : Specifies width of editor GUI in pixels*/
  static final String ARG_SIZE_X                     = "-sizex";
/** -sizey &lt;height&gt; : Specifies height of editor GUI in pixels*/
  static final String ARG_SIZE_Y                     = "-sizey";
/** -title &lt;title&gt; : Specifies the title appearing in the title bar*/
  static final String ARG_TITLE                      = "-title";
/** -pw &lt;password&gt; : Specifies the password for an encrypted file*/
  static final String ARG_PASSWORD                   = "-pw";
/** -ro : Specifies that the file is read only */
  static final String ARG_ALLOW_READ_ONLY            = "-ro";
/** -wo : Specifies that only the file specified in the constructor can be read and written */
  static final String ARG_ALLOW_WRITE_INITIAL_FILE   = "-wo";
/** -rw : Specifies that the file is read/write */
  static final String ARG_ALLOW_READ_WRITE           = "-rw";
/** -view : Allows viewing of only 1 file */
  static final String ARGS_VIEW_ONE_FILE             = "-view";
/** -enc : Specifies do encryption */
  static final String ARG_DO_ENCRYPT                 = "-enc";
/** -nomenu : Specifies that a menu will not be shown */
  static final String ARG_NO_MENU                    = "-nomenu";
/** -nomenu : Specifies that a menu will not be shown */
  static final String ARG_NO_MENU_OR_CMD_LN          = "-justtext";

/** -verbose &lt;int&gt; : Specifies verbose level. Ex: -verbose 5*/
  static final String ARG_VERBOSE                    = "-verbose";
/** -trace_out &lt;file_name&gt; : Specifies verbose output file. Ex: -trace_out /tmp/zztrace.txt*/
  static final String ARG_TRACE_OUTPUT               = "-trace_out";


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private SwingMyEditorArgs ( ) {}


} //End: class SwingMyEditorArgs
