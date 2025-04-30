// SwingMyEditorJInternalFrame.java
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
package my_proj.my_lib.lib_swing_editor;

//------------------  Import statements  ------------------

import java.awt.Image;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import my_proj.my_lib.lib.MyTrace;
import my_proj.my_lib.lib_swing.SwingMyImages;


//------------------  CLASS: SwingMyEditorJInternalFrame  ------------------
/**
 * This class
 *
 * @author James Everitt
 */
public final class SwingMyEditorJInternalFrame extends JInternalFrame
{

//private static final boolean DO_TRACE = true;

/** serial UID */
  private static final long serialVersionUID = -2166254680157953484L;

/** Program version */
  public static final String OSC_PROJ_PROGRAM_VERSION           = "0.02";
/** Copyright dates */
  public static final String OSC_PROJ_PROGRAM_COPYRIGHT_DATE    = "2024,2025";

/** Main editor panel */
  private SwingMyEditorZ04JPanel myEditorPanel = null;

  
//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This constructor method ?
 * 
 * @param title  ?
 * @param dataFile  ?
 * @param ctrl  ?
 * @param sizeX  ?
 * @param sizeY  ?
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJInternalFrame (
      String     title,
      File       dataFile,
      int        ctrl,
      int        sizeX,
      int        sizeY ) throws Exception
  {
    super();
    SwingMyEditorConst.myType = SwingMyEditorConst.MY_TYPE.INT_FRAME;
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromFile(this, title, dataFile, false, ctrl, sizeX, sizeY, null, null, null);
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
  } //End: Method


//------------------  Method  ------------------
/**
 * This constructor method ?
 *
 * @param title  ?
 * @param dataFile  ?
 * @param ctrl  ?
 *
 * @throws Exception  Throws standard Java exception
 */
  public SwingMyEditorJInternalFrame (
      String     title,
      File       dataFile,
      int        ctrl
      ) throws Exception
  {
    super();
    this.myEditorPanel = SwingMyEditorZ03Handler.myInitializeFromFile(this,title, dataFile, false, ctrl, 600, 400, null, null, null);
    if ( this.myEditorPanel == null ) throw new Exception ( MyTrace.myGetMethodName() + ": no edit panel" );
  } //End: Method


//------------------  Method  ------------------
/**
 * This method ?
 *
 * @return  ?
 *
 */
  public final SwingMyEditorZ04JPanel myGetEditorPanel () { return this.myEditorPanel; }


// ------------------ Main Method ------------------
/**
 * This method is the runnable test main method.
 * 
 * @param args String[] of input arguments
 * 
 */
  public static void main( String[] args )
  {
    try {
      SwingMyEditorJInternalFrame intFr = new SwingMyEditorJInternalFrame(
          "Test Int Fr", // String     title,
          null,          // File       dataFile,
          0,             // int        ctrl,
          700,           // int        sizeX,
          500            // int        sizeY
          );
//
      JFrame mainFrame = new JFrame ( "Test SwingMyEditorJInternalFrame" );
      mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//
      mainFrame.setSize(900,700);
      mainFrame.setLocationRelativeTo(null);  // MyCenterFrame.myCenterFrameOnScreen(mainFrame);
      Image img = SwingMyImages.myGetImage("my_proj/my_lib_resources/my_gifs/jedit_icon_26x26.gif");
      if ( img != null ) mainFrame.setIconImage(img);
//
      mainFrame.getContentPane().setLayout(null);
      mainFrame.getContentPane().add(intFr);
//
      mainFrame.setVisible(true);
    }
    catch (Exception e ) {
      e.printStackTrace();
    }
  } //End: Method


} //End: class SwingMyEditorJInternalFrame

