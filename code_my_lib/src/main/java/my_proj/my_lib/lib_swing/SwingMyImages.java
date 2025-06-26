// SwingMyImages.java
/*
 *
 * Copyright (C) 2013,2024 James Everitt
 *
 * This program is free software: you can redistribute it
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
 */

// Package statement
package my_proj.my_lib.lib_swing;

//------------------  Import statements  ------------------

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import my_proj.my_lib.lib.MyTrace;


//  ----------------  CLASS: SwingMyImages  ----------------
/**
 * This class
 *
 * @author James Everitt
 */
public class SwingMyImages {

//private static final boolean DO_TRACE = true;
  
/** Standard image path */
  public static final String myStdImagePath = "my_proj/my_lib_resources/my_gifs/";


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private SwingMyImages ( ) {}


//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param imageFileName  ?
 *
 * @return  ?
 */
  public static final Image myGetImage ( String imageFileName )
  {
    Image img = null;
//
    if ( imageFileName != null ) {
// If in file_in_jar
      ClassLoader cldr = SwingMyImages.class.getClassLoader();
// Note: The name of a resource is a '/'-separated path name that identifies the resource.
      if ( imageFileName.contains("\\") ) imageFileName = imageFileName.replace('\\', '/');
      URL imageURL = cldr.getResource( imageFileName );
      if (imageURL != null ) img = Toolkit.getDefaultToolkit().getImage(imageURL);
/*
if(DO_TRACE) {
 System.out.println("\n" + MyTrace.myGetMethodName() + "\n imageFile= " + imageFileName + "\n URL= " + imageURL + "\n img = " + img);
 System.out.println(" packages:");
 Package[] packages = cldr.getDefinedPackages();
 Package found = null;
 for ( Package pack : packages ) {
   System.out.println("  " + pack );
   if ( pack.toString().contains("my_gifs") ) found = pack;
 }
 if ( found != null ) {
   System.out.println(" found package= " + found);
 }
}
*/
// if in class files
      if ( img == null ) {
        try {
          if ( new File(imageFileName).isFile() ) img = Toolkit.getDefaultToolkit().getImage(imageFileName);
        }
        catch (Exception e) { System.out.println(MyTrace.myGetMethodName() + ": could not find " + imageFileName); }
      }
    } //End: if ()
//
    return img;
  } //End: Method


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------ Main Method ------------------
/**
 * This method is the runnable main method.
 *
 * @param args  ?
 *
 */
  public static void main(String[] args)
  {
    try {
      JFrame fr = new JFrame("Test images");
      fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      fr.setSize(400,400);
      fr.setLocationRelativeTo(null);   // MyCenterFrame.myCenterFrameOnScreen(fr);
//
      String fullName = myStdImagePath + "osc_icon_26x26.gif";
      Image im = SwingMyImages.myGetImage(fullName);
      System.out.println("SwingMyImages.main im= " + im);
      fr.setIconImage(im);
//      if ( im != null ) fr.getContentPane().add(im);
//
      fr.setVisible(true);
//
    }
    catch (Exception e) {
      System.out.println(MyTrace.myGetMethodName() + ": exc= " + e.getMessage());
      e.printStackTrace();
    }
 } //End: Method


} //End: public class SwingMyImages
