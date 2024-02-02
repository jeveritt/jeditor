// SwingMyEditorZ03Menu.java
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

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;


//--------------------------------------------------------------------
//------------------  CLASS: SwingMyEditorZ03Menu  -------------------
//--------------------------------------------------------------------
/**
 * This class
 *
 * @author James Everitt
 */
public final class SwingMyEditorZ06Menu {
  

//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------  METHOD: ------------------
/**
 * This static method ?
 *
 */
  static final JMenu myCreateZ01MenuFile ( SwingMyEditorZ05Action actions )
  {
//
    JMenu subMenu = new JMenu ("File");
//
    subMenu.add( actions.MY_FILE_OPEN_UNENCRYPTED );
    subMenu.add( actions.MY_FILE_SAVE_UNENCRYPTED );
    subMenu.add( actions.MY_FILE_SAVE_UNENCRYPTED_AS );
//
    subMenu.addSeparator();
    subMenu.add( actions.MY_FILE_OPEN_ENCRYPTED );
    subMenu.add( actions.MY_FILE_SAVE_ENCRYPTED );
    subMenu.add( actions.MY_FILE_SET_PASSWORD );
//
    subMenu.addSeparator();
    subMenu.add( actions.MY_FILE_CLOSE );
//
    subMenu.addSeparator();
    subMenu.add( actions.MY_FILE_CLOSE_WINDOW );
//
    return subMenu;
//
  } //End: Method


//------------------  METHOD: ------------------
/**
 * This static method ?
 *
 */
  static final JMenu myCreateZ02MenuEdit ( SwingMyEditorZ05Action actions )
  {
//
    JMenu subMenu = new JMenu ("Edit");
//
    subMenu.add( actions.MY_EDIT_COPY );
//
    return subMenu;
  } //End: Method


//------------------  METHOD: ------------------
/**
 * This static method ?
 *
 */
  static final JMenu myCreateZ04MenuHelp ( SwingMyEditorZ05Action actions )
  {
//
    JMenu subMenu = new JMenu ("Help");
//
    subMenu.add( actions.MY_HELP_ABOUT );
    subMenu.add( actions.MY_HELP_COPYRIGHT );
//
    return subMenu;
  } //End: Method


//------------------  METHOD: ------------------
/**
 * This static method ?
 *
 */
  static final void myCreateZ08Initialize
  ( JComponent menu, JMenu[] custom, SwingMyEditorZ05Action actions )
  {
//
    JPopupMenu popup = (menu instanceof JPopupMenu) ? (JPopupMenu)menu : null;
//
//--- File menu stuff --------
    menu.add( SwingMyEditorZ06Menu.myCreateZ01MenuFile( actions ) );
    if (popup != null) popup.addSeparator();
//
//--- Edit menu stuff --------
    menu.add( SwingMyEditorZ06Menu.myCreateZ02MenuEdit( actions ) );
    if (popup != null) popup.addSeparator();
//
//--- Add custom stuff -------
    if (custom != null) {
      for ( JMenu tmp : custom ) {
        menu.add(tmp);
        if (popup != null) popup.addSeparator();
      }
    }
//
//
//--- Help menu stuff --------
    menu.add( SwingMyEditorZ06Menu.myCreateZ04MenuHelp( actions ) );
//
  } //End: Method


//------------------  METHOD: ------------------
/**
 * This static method ?
 *
 */
  static final JPopupMenu myCreatePopupMenu ( JMenu[] custom, SwingMyEditorZ05Action actions )
  {
    JPopupMenu popup = new JPopupMenu();
//
    SwingMyEditorZ06Menu.myCreateZ08Initialize ( popup, custom, actions );
//
    return popup;
  } //End: Method


//------------------  METHOD: ------------------
/**
 * This static method ?
 *
 * @param custom  ?
 * @param actions  ?
 *
 * @return  ?
 */
  public static final JMenuBar myCreateMenuBar ( JMenu[] custom, SwingMyEditorZ05Action actions )
  {
    JMenuBar menuBar = new JMenuBar();
//
    SwingMyEditorZ06Menu.myCreateZ08Initialize ( menuBar, custom, actions );
//
    return menuBar;
  } //End: Method


} //End: class SwingMyEditorZ03Menu
