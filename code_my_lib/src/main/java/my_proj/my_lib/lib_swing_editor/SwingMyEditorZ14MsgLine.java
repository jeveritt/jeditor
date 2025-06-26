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
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//----------------------  CLASS: SwingMyEditorZ07MsgLine  ---------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
/**
 * This class ?
 *
 * @author James Everitt
 */
final class SwingMyEditorZ14MsgLine extends JPanel {

  private static final long serialVersionUID = -2597574657608999675L;
  
  private JLabel myMode = null;
  private JLabel myWritable = null;
  private JLabel myIsEnc = null;
  private JLabel myMsg = null;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method ------------------
/**
 * This constructor method ?
 *
 */
  SwingMyEditorZ14MsgLine ( JRadioButton doEnc )
  {
    super();
//
    this.myWritable = new JLabel("Write");
    this.add(this.myWritable);
//
    this.myMode = new JLabel("Mode");
    this.add(this.myMode);
    this.mySetMode(SwingMyEditorZ07TextAreaHolder.MY_MODES.ins);
//
    this.myIsEnc = new JLabel("");
    this.add(this.myIsEnc);
    this.myIsEnc.setText("Enc: " + doEnc.isSelected() );
//
    ChangeListener encChng = new ChangeListener() {
      @Override  public void stateChanged(ChangeEvent e) {
        JRadioButton btn = (JRadioButton)e.getSource();
        SwingMyEditorZ14MsgLine.this.myIsEnc.setText("Enc: " + btn.isSelected() );
      }
    };
    doEnc.addChangeListener(encChng);
//
    this.myMsg = new JLabel("Msg");
    this.add(this.myMsg);
    this.mySetMsg(null);
//
    this.setLayout( new MyMsgLineLayoutManager() );
//
    this.repaint();
  } //End: Method


//------------------  Method ------------------
/**
 * This method ?
 *
 * @param msg  ?
 */
  final void mySetMsg ( String msg ) {
    if ( msg == null ) this.myMsg.setText("Msg:"); else this.myMsg.setText("Msg: " + msg);
  }
  

//------------------  Method ------------------
/**
 * This method ?
 *
 * @param mode  ?
 */
  final void mySetMode ( SwingMyEditorZ07TextAreaHolder.MY_MODES mode ) {
    this.myMode.setText("Mode: " + mode.toString() );
  }


//------------------  Method ------------------
/**
 * This method ?
 *
 * @param val  ?
 */
  final void mySetWritable ( boolean val ) {
    this.myWritable.setText("Write: " + val );
  }

} //End: class SwingMyEditorZ14MsgLine


//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
//------------------------  CLASS: MyMsgLineLayoutManager  -------------------------------
//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
/**
* This class
*
* @author James Everitt
*/
final class MyMsgLineLayoutManager implements LayoutManager {

  @Override public void addLayoutComponent(String name, Component comp) { }

  @Override public void removeLayoutComponent(Component comp) { }

  @Override public Dimension preferredLayoutSize(Container parent)
  {
    int width = parent.getParent().getPreferredSize().width;
    int height = parent.getComponent(0).getPreferredSize().height;
    return new Dimension(width, height);
  }

  @Override public Dimension minimumLayoutSize(Container parent) { return new Dimension(300,200); }

  @Override public void layoutContainer(Container parent) {
    Component[] comps = parent.getComponents();
//
    Dimension prefSize = this.preferredLayoutSize(parent);
    int totalWidth = prefSize.width;
    int height = prefSize.height;
//
    int x = 5;
    int space = 10;
//
    int compW = comps[0].getPreferredSize().width;
    comps[0].setBounds(x, 0, compW, height);
    x += compW + space;
//
    compW = comps[1].getPreferredSize().width;
    comps[1].setBounds(x, 0, compW, height);
    x += compW + space;
//
    compW = totalWidth - x - 5;
    comps[2].setBounds(x, 0, compW, height);
  } //End: Method

} //End: class MyMsgLineLayoutManager

