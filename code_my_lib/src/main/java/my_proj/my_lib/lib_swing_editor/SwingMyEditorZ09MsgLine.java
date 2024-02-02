// SwingMyEditorZ07MsgLine.java
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;


//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
//------------------------  CLASS: myMsgLineLayoutManager  -------------------------------
//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
/**
 * This class
 *
 * @author James Everitt
 */
final class myMsgLineLayoutManager implements LayoutManager {
  
//  private static final boolean DO_TRACE = true;
  
  final int sectSep = 20;

  @Override public void addLayoutComponent(String name, Component comp) { }

  @Override public void removeLayoutComponent(Component comp) { }

  @Override public Dimension minimumLayoutSize(Container parent) { return new Dimension(300,200); }

  @Override public Dimension preferredLayoutSize(Container parent)
  {
    Component[] comps = parent.getComponents();
    Dimension prefSize = new Dimension(0,0);
    for ( Component comp : comps ) {
      Dimension compPref = comp.getPreferredSize();
      if ( prefSize.height < compPref.height ) prefSize.height = compPref.height;
      prefSize.width += compPref.width + this.sectSep;
    }
    return prefSize;
  } //End: Method

  @Override public void layoutContainer(Container parent) {
    Component[] comps = parent.getComponents();
    Component mode = comps[0];
    Component msg = comps[1];
    Dimension modePref = mode.getPreferredSize();
    Dimension size = parent.getSize();
    mode.setBounds(0, 0, modePref.width, size.height);
    msg.setBounds(modePref.width + this.sectSep, 0, size.width-modePref.width-this.sectSep, size.height);
//if(DO_TRACE)System.out.println(MyTrace.myGetMethodName() + "\n mode= " + mode + "\n msg= " + msg);
  } //End: Method

} //End: class myMsgLineLayoutManager


//--------------------------------------------------------------------
//------------------  CLASS: SwingMyEditorZ07MsgLine  ------------------
//--------------------------------------------------------------------
/**
 * This class ?
 *
 * @author James Everitt
 */
final class SwingMyEditorZ09MsgLine extends JPanel {
//
  static final long serialVersionUID = 0;
  
  private JLabel myMode = null;
  private JLabel myMsg = null;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This constructor method ?
 *
 */
  SwingMyEditorZ09MsgLine ( )
  {
    super();
//
    this.myMode = new JLabel("Mode");
    this.add(this.myMode);
    this.mySetMode(SwingMyEditorZ07TextArea.MY_MODES.INSERT);
//
    this.myMsg = new JLabel("Msg");
    this.add(this.myMsg);
    this.mySetMsg(null);
//
    this.setLayout( new myMsgLineLayoutManager() );
//
    this.repaint();
  } //End: Method


//------------------  Method ------------------
/**
 * This method ?
 *
 */
  final void mySetMsg ( String msg ) {
    if ( msg == null ) this.myMsg.setText("Msg:"); else this.myMsg.setText("Msg: " + msg);
  }
  

//------------------  Method ------------------
/**
 * This method ?
 *
 */
  final void mySetMode ( SwingMyEditorZ07TextArea.MY_MODES mode ) {
    this.myMode.setText("Mode: " + mode.toString() );
  }


} //End: Class SwingMyEditorZ07MsgLine
