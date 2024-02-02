// SwingMyEditorJDialog.java
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

// Package statement
package my_proj.jeditor;

//------------------  Import statements  ------------------

import my_proj.my_lib.lib_swing_editor.SwingMyEditorConst;
import my_proj.my_lib.lib_swing_editor.SwingMyEditorZ03Handler;


//------------------  CLASS: SwingMyEditorJDialog  ------------------
/**
 * This class
 *
 * @author James Everitt
 */
public final class jedit_dialog
{


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------ Main Method ------------------
/**
 * This method is the runnable main method.
 * 
 * @param args String[] of input arguments
 * 
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
      SwingMyEditorZ03Handler.myInitialize(args, true);
    }
  } //End: Method


} //End: class SwingMyEditorJDialog

