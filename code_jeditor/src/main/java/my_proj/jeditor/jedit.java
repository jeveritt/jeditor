// jedit.java
/*
 * Copyright (C) 2023, 2025 James Everitt
 *
 * This file is part of a Swing based vi like text editor.
 * It just calls the JFrame based editor in the library directory.
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

// Package statement
package my_proj.jeditor;

//------------------  Import statements  ------------------

import my_proj.my_lib.lib_swing_editor.SwingMyEditorJFrame;


//-----------------------------------------------------------
//-----------------------  CLASS: jedit  --------------------
//-----------------------------------------------------------
/**
 * This class runs the stand alone text editor my_proj.jeditor.jedit.
 * It is similar to a simplified VIM program.
 * The program is implemented using the Swing library.
 * The GUI mode is built into the Swing library and behaves in a pretty standard way.
 * The command line mode has been added on and looks like a simplified vi with a limited set of commands.
 * <pre>
 * Modes are as follows:
 *   GUI: works in parallel with Vi->Text mode
 *   Vi:
 *     Text mode: enter from command mode by typing \"i\" or \"a\"
 *     Command mode: enter from text mode with &lt;esc&gt; key
 *       Direct: command executed by typing in edit window
 *       CmdLn: type \":\" for cmd line then enter command and return
 * </pre>

 * 
 * @author James Everitt
 */
public final class jedit {
  

//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This constructor calls my_proj.my_lib.lib_swing_editor.SwingMyEditorJFrame which
 * creates a stand alone text editor by extending JFrame, creating the editor,
 * and then setting the editor as the content pane of the JFrame.
 * 
 */
  private jedit ( ) {}


// ------------------ Main Method ------------------
/**
 * This method is the runnable main method.
 * <pre>
 *  Arguments are:
 *    -file &lt;file_name&gt;           : File to be opened at start of program
 *    -dir &lt;directory_name&gt;       : Start editor pointing to specified directory
 *    -ro                         : Set editor to read only
 *    -rw                         : Set editor to read/write
 *    -pw &lt;password&gt;              : Password for encrypted file
 *    -isenc                      : Indicates file is encrypted
 *    -enro                       : Set editor to encrypted read only
 *    -enrw                       : Set editor to encrypted read/write
 *    -nomenu                     : No menus implies only view starting file
 *    -sizex &lt;width&gt;              : Width of GUI
 *    -sizey &lt;height&gt;             : Height of GUI
 *    -help                       : Prints this message and exits
 * </pre>
 * 
 * @param args String[] of input arguments
 */
  public static void main( String[] args )
  {
    new SwingMyEditorJFrame(args);
  } //End: Method

} //End: class jedit

