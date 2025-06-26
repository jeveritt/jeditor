// SwingMyEditorConst.java
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

import java.awt.Font;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//-------------------------  CLASS: SwingMyEditorConst  -----------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class defines program constants that are used in internal API's.
 *
 * @author James Everitt
 */
public final class SwingMyEditorConst {
    
/** Enum list of editor types : MY_TYPE = FRAME | INT_FRAME | DIALOG | UNKNOWN */
         static enum MY_TYPE {
/** FRAME => Stand alone editor in its own JFrame */
                             FRAME,
/** INT_FRAME => editor in a JInternalFrame */
                             INT_FRAME,
/** DIALOG => editor in a JDialog */
                             DIALOG,
/** UNKNOWN => error */
                             UNKNOWN
                             }
/** Type of editor */
         static       MY_TYPE myType = MY_TYPE.UNKNOWN;
  
/** Default width in pixels */
  public static final int     MY_DEFAULT_SIZE_X              = 800;
/** Default height in pixels */
  public static final int     MY_DEFAULT_SIZE_Y              = 700;
         
/** Enables printing of exception stack trace */
         static final boolean PRINT_STACK_TRACE              = false;
/** Encrypted file extension */
         static final String  MY_ENCRYPT_EXTENSION           = ".gpg";
         
/** Monospaced font */
         static final Font    MY_MONOSPACED_FONT             = new Font(Font.MONOSPACED, Font.PLAIN, 14);
/** Font */
         static       Font    MY_FONT                        = new Font(Font.MONOSPACED, Font.PLAIN, 14);

/** Number of files saved */
         static final int     MY_DEFAULT_NUMB_OLD_FILES_SAVED = 6;

/** VI save and exit keys: q, wq, forceQ, c, wc, c, forceC */
        static enum MY_WRITE_AND_EXIT {
/** q => exit editor */
          q,
/** wq => save edits then exit editor */
          wq,
/** forceQ => force exiting editor without writing */
          forceQ,
/** c => close file */
          c,
/** wc => save edits and close file */
          wc,
/** forceC => force closing file without writing */
          forceC,
/** w => save edits */
          w
          }
  

//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private SwingMyEditorConst ( ) {}


} //End: class SwingMyEditorConst
