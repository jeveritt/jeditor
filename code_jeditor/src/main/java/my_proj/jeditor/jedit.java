// jedit.java
/*
 * Copyright (C) 2023 James Everitt
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

// Package statement
package my_proj.jeditor;

//------------------  Import statements  ------------------

import my_proj.my_lib.lib_swing_editor.SwingMyEditorConst;
import my_proj.my_lib.lib_swing_editor.SwingMyEditorZ03Handler;



//-----------------------------------------------------------
//-----------------------  CLASS: jedit  --------------------
//-----------------------------------------------------------
/**
 * This class runs the text editor.
 * <pre>
 *   Note to me: for my test use config:
 *     -file /home/jeveritt/mine/programs/java/my_java_code/jeditor/code_jeditor/src/data/jeditor_data/just_a_random_text_file.txt
 * </pre>
 * <p>
 * @author James Everitt
 */
public final class jedit {


// ------------------ Main Method ------------------
/**
 * This method is the runnable main method.
 * 
 * @param args String[] of input arguments that are ignored
 */
  public static void main( String[] args )
  {
    if ( args.length == 1 &&
         ( args[0].equals("-h")    || args[0].equals("--h") ||
           args[0].equals("-help") || args[0].equals("--help") ) )
    {
      String name = jedit.class.getName();
      SwingMyEditorZ03Handler.myPrintHelp(name);
    }
    else {
      if ( args.length == 0 ) args = new String[] {
          SwingMyEditorConst.ARG_ALLOW_FILE_OPS,
          SwingMyEditorConst.ARG_ALLOW_READ_WRITE,
          SwingMyEditorConst.ARG_TITLE, "Editor Test"
          };
      SwingMyEditorZ03Handler.myInitialize(args, false);
    }
  } //End: Method


} //End: class jedit

