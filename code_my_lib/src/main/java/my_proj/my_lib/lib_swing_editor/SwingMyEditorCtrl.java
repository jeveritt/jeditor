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

import javax.swing.JRadioButton;

//------------------  Import statements  ------------------

import my_proj.my_lib.lib.MyReadOption;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//--------------------------  CLASS: SwingMyEditorCtrl  -----------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class defines the allowed values for the editor control integer used in internal API's. Arguments are OR'ed.
 * <br>
 *   Ex: 0x2|0x40
 *
 * @author James Everitt
 */
public final class SwingMyEditorCtrl {
  
//private static final boolean DO_TRACE = true;

// Editor controls
  
/** Edit control */
  public static final int     MY_DO_ENCRYPT             = 0x1;
/** Edit control */
  public static final int     MY_ALLOW_READ             = 0x2;
/** Edit control */
  public static final int     MY_ALLOW_WRITE            = 0x4;
/** Enabling prevent menu from being displayed */
  public static final int     MY_NO_MENU                = 0x10;
/** Edit control */
  public static final int     MY_NO_CMD_LINE            = 0x20;
  
/** Final edit control int */
  private static int  myFinalCtrl;

/** Edit control */
//  public static boolean doEncrypt;
         static final JRadioButton  doEncrypt = new JRadioButton( "Use Encryption", false );
/** Edit control */
  public static boolean canReadFile;
/** Edit control */
  public static boolean canWriteFile;
/** Edit control */
  public static boolean noMenu;
/** Edit control */
  public static boolean noCmdLn;



//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called.
 */
  private SwingMyEditorCtrl () {}
  

//--------------------------------------------------------
//------------------  STATIC METHODS  -------------------
//--------------------------------------------------------


//------------------  Static Method  ------------------
/**
 * This method sets control parameters from a control integer.
 * 
 * @param ctrl  Control int
 */
  static final void mySetControlFromControlInt ( int ctrl )
  {
//if (DO_TRACE ) System.out.print( "\n" + MyTrace.myGetMethodName() + ": ctrl= 0x" + Integer.toHexString(ctrl) );
// If ctrl == 0 which means you can't do anything then set for read/write
    if ( ctrl == 0 ) ctrl = SwingMyEditorCtrl.MY_ALLOW_READ | SwingMyEditorCtrl.MY_ALLOW_WRITE;
//if ( DO_TRACE ) System.out.println( " -> 0x" +  Integer.toHexString(ctrl) );
    
    boolean doEncrypt2 =  ( ctrl & SwingMyEditorCtrl.MY_DO_ENCRYPT ) != 0;
    doEncrypt.setSelected(doEncrypt2);

    canReadFile       = ( ctrl & SwingMyEditorCtrl.MY_ALLOW_READ ) != 0;
    
    canWriteFile      = ( ctrl & SwingMyEditorCtrl.MY_ALLOW_WRITE ) != 0;

    noMenu            = ( ctrl & SwingMyEditorCtrl.MY_NO_MENU ) != 0;
    
    noCmdLn           = ( ctrl & SwingMyEditorCtrl.MY_NO_CMD_LINE ) != 0;
    
    myFinalCtrl = ctrl;
    
//if ( DO_TRACE ) System.out.print( MyTrace.myGetMethodName() + ": exiting" + "\n Permissions: \n" + myGetPermissions ( "    " ) );
  } //End: method


//------------------  Static Method  ------------------
/**
 * This method calculates a control integer from input arguments.
 * 
 * @param args  Array of input arguments
 * 
 * @return  Returns a control int
 */
  static final int myGetCtrlIntFromInputArgs ( String[] args )
  {
    int ctrl = 0;
    
    boolean readOnly         = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_READ_ONLY);
    boolean readWrite        = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_READ_WRITE);
    boolean viewOne          = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARGS_VIEW_ONE_FILE);
    boolean writeInitialOnly = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_ALLOW_WRITE_INITIAL_FILE);
    boolean noMenu           = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_NO_MENU);
    boolean noMenuOrCmdLn    = MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_NO_MENU_OR_CMD_LN);
    
    if ( !viewOne && !writeInitialOnly && ( readOnly || readWrite ) ) ctrl |= SwingMyEditorCtrl.MY_ALLOW_READ;
    
    if ( !viewOne && !readOnly && ( writeInitialOnly || readWrite ) ) ctrl |= SwingMyEditorCtrl.MY_ALLOW_WRITE;
    
    if ( viewOne || noMenu || noMenuOrCmdLn ) ctrl = ctrl | SwingMyEditorCtrl.MY_NO_MENU;
    
    if ( viewOne || noMenuOrCmdLn ) ctrl = SwingMyEditorCtrl.MY_NO_CMD_LINE;

    if ( MyReadOption.myKeyExists(args, SwingMyEditorArgs.ARG_DO_ENCRYPT) )  ctrl |= SwingMyEditorCtrl.MY_DO_ENCRYPT;

    return ctrl;
  } //End: method


//------------------  Static Method  ------------------
/**
 * This method sets control parameters from input arguments.
 * 
 * @param args  Array of input arguments
 * 
 */
  static final void mySetControlFromInputArgs ( String[] args )
  {
    int ctrl = myGetCtrlIntFromInputArgs ( args );
    mySetControlFromControlInt ( ctrl );
  } //End: method


//------------------  Method  ------------------
/**
 * This method returns a String listing control settings.
 *
 * @param indent  String defines how much lines are indented.
 * 
 * @return  Return String of info
 */
  public static final String myGetPermissions ( String indent )
  {
    if ( indent == null ) indent = "  ";
    String retVal = new String(
        indent + "final ctrl int   = 0x" + Integer.toHexString(myFinalCtrl) + "\n" +
        indent + "do encrypt       = " + SwingMyEditorCtrl.doEncrypt + "\n" +
        indent + "can read         = " + SwingMyEditorCtrl.canReadFile + "\n" +
        indent + "can write        = " + SwingMyEditorCtrl.canWriteFile + "\n" +
//        indent + "view only 1 file = " + SwingMyEditorCtrl.viewOnlyOnle1File + "\n" +
        indent + "no menu          = " + SwingMyEditorCtrl.noMenu + "\n" +
        indent + "no cmd line      = " + SwingMyEditorCtrl.noCmdLn + "\n"
        );
    return retVal;
  } //End: method
        

} //End: class SwingMyEditorCtrl
