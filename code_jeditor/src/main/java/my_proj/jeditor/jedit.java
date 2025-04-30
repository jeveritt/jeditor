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
 * This class runs the text editor.
 * It just calls the JFrame based editor in the library directory.
 * I stuck it here so it's easy to find.
 * 
 * @author James Everitt
 */
public final class jedit {
  

//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor
 * 
 */
  private jedit ( ) {}


// ------------------ Main Method ------------------
/**
 * This method is the runnable main method.
 * 
 * @param args String[] of input arguments
 */
  public static void main( String[] args )
  {
    new SwingMyEditorJFrame(args);
  } //End: Method

} //End: class jedit

