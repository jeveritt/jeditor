// SwingMyStandardFonts.java

//------------------  Package statement  ------------------
package my_proj.my_lib.lib_swing;

//------------------  Import statements  ------------------

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import my_proj.my_lib.lib.MyTrace;


//----------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------  CLASS: SwingMyStandardFonts  ----------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------

//------------------  METHOD  ------------------
/**
 * This class
 * <pre>
 * Java supports Serif, SansSerif, Monospaced, Dialog, and DialogInput logical fonts that are mapped into physical fonts and which are guaranteed to exist.
 * </pre>
 * 
 * @author James Everitt
 */
public final class SwingMyStandardFonts
{
//  private static final boolean DO_TRACE = true;
  
/** This is the a permanently fixed font size that should be the staring point for all font sizes. */
  public static final int MY_FIXED_STANDARD_STARTING_FONT_SIZE = 12;
//
/** This is the base font size for all buttons, labels, menus, etc. and can be interactively scaled to enlarge or shrink text. */
  private static int MY_DEFAULT_FONT_SIZE = SwingMyStandardFonts.MY_FIXED_STANDARD_STARTING_FONT_SIZE;
//
//  Note: Monospaced font type and bold style is not monospaced for all characters - go figure
//
/** Enum of supported font type */
  public static enum MY_FONTS {
/** Mono spaced font */
    Monospaced,
/** Dialog font */
    Dialog
    }
//
/** Mono Fonts */
  private static final ArrayList<Font> MY_FONTS_MONOSPACED = new ArrayList<>();
//
/** Fancy Fonts */
  private static final ArrayList<Font> MY_FONTS_DIALOG = new ArrayList<>();
//
// Standard Fonts
//
/** Default general font */
  private static Font MY_DFLT_GENERAL_FONT = SwingMyStandardFonts.myGetMonospacedFont(SwingMyStandardFonts.MY_FIXED_STANDARD_STARTING_FONT_SIZE);
/** Default button font */
  private static Font MY_DFLT_BUTTON_FONT = SwingMyStandardFonts.myGetMonospacedFont(SwingMyStandardFonts.MY_FIXED_STANDARD_STARTING_FONT_SIZE);
/** Default border font */
  public  static Font MY_BORDER_FONT  = SwingMyStandardFonts.myGetMonospacedFont(SwingMyStandardFonts.MY_FIXED_STANDARD_STARTING_FONT_SIZE);
//
/** Margins for buttons */
  public static final Insets myButtonMargins = new Insets(0,0,0,0);
//
/** Default bold size */
  private static final int TO_BOLD_SIZE = 10;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private SwingMyStandardFonts ( ) {}


//-----------------------------------------------------------
//------------------  Static Methods:  ----------------------
//-----------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method gets a font name from a font enum
 *
 * @param fontEnum  ?
 *
 * @return  Return font name
 */
  public static final String myFontName ( MY_FONTS fontEnum )
  {
    if ( fontEnum == MY_FONTS.Dialog ) return "Dialog";
// Note: Monospaced font type and bold style is not monospaced for all characters - go figure
    else if ( fontEnum == MY_FONTS.Monospaced ) return "Monospaced";
//    else if ( fontEnum == MY_FONTS.Monospaced ) return "Courier New";
//    else if ( fontEnum == MY_FONTS.Monospaced ) return "Consolas";
    else return "unknown_font_enum";
  } //End: Method
  
  
//------------------  Method  ------------------
/**
 * This static method recursively sets component and its children to a new font.
 *
 * @param comp  ?
 * @param newFont  ?
 *
 */
  public static final void myRecursivelySetFonts ( Component comp, Font newFont )
  {
    if ( comp == null ) return;
    if ( comp instanceof Container ) {
      Component[] subComps = ((Container)comp).getComponents();
      for ( Component subComp : subComps ) myRecursivelySetFonts(subComp, newFont);
    }
//
    comp.setFont(newFont);
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method recursively sets component and its children to pre-set default fonts.
 *
 * @param comp  ?
 *
 */
  public static final void myRecursivelySetDefaultFontSizes ( Component comp )
  {
    if ( comp == null ) return;
    if ( comp instanceof Container ) {
      Component[] subComps = ((Container)comp).getComponents();
      for ( Component subComp : subComps ) myRecursivelySetDefaultFontSizes(subComp);
    }
//
    SwingMyStandardFonts.mySetDefaultsForComp(comp);
  } //End: Method


//------------------  Method ------------------
/**
 * This static method ?
 *
 * @param globalFont  ?
 *
 */
  public static final void mySetGlobalFonts ( Font globalFont )
  {
    UIManager.put("Button.font", globalFont);
    UIManager.put("ToggleButton.font", globalFont);
    UIManager.put("RadioButton.font", globalFont);
    UIManager.put("CheckBox.font", globalFont);
    UIManager.put("ColorChooser.font", globalFont);
    UIManager.put("ComboBox.font", globalFont);
    UIManager.put("Label.font", globalFont);
    UIManager.put("List.font", globalFont);
    UIManager.put("MenuBar.font", globalFont);
    UIManager.put("MenuItem.font", globalFont);
    UIManager.put("RadioButtonMenuItem.font", globalFont);
    UIManager.put("CheckBoxMenuItem.font", globalFont);
    UIManager.put("Menu.font", globalFont);
    UIManager.put("PopupMenu.font", globalFont);
    UIManager.put("OptionPane.font", globalFont);
    UIManager.put("Panel.font", globalFont);
    UIManager.put("ProgressBar.font", globalFont);
    UIManager.put("ScrollPane.font", globalFont);
    UIManager.put("Viewport.font", globalFont);
    UIManager.put("TabbedPane.font", globalFont);
    UIManager.put("Table.font", globalFont);
    UIManager.put("TableHeader.font", globalFont);
    UIManager.put("TextField.font", globalFont);
    UIManager.put("PasswordField.font", globalFont);
    UIManager.put("TextArea.font", globalFont);
    UIManager.put("TextPane.font", globalFont);
    UIManager.put("EditorPane.font", globalFont);
    UIManager.put("TitledBorder.font", globalFont);
    UIManager.put("ToolBar.font", globalFont);
    UIManager.put("ToolTip.font", globalFont);
    UIManager.put("Tree.font", globalFont);
  } //End: Method


//------------------  Method ------------------
/**
 * This static method ?
 *
 * @param defaultSize  ?
 *
 */
  public static final void mySetStandardFontSize( int defaultSize )
  {
//System.out.println("\n" + MyTrace.myGetMethodName() + ": dfltSize= " + defaultSize + "\n");
//
    SwingMyStandardFonts.MY_DEFAULT_FONT_SIZE = defaultSize;
//
    SwingMyStandardFonts.MY_DFLT_GENERAL_FONT = SwingMyStandardFonts.myGetMonospacedFont(defaultSize);
    SwingMyStandardFonts.MY_DFLT_BUTTON_FONT = SwingMyStandardFonts.myGetMonospacedFont(defaultSize);
    SwingMyStandardFonts.MY_BORDER_FONT  = SwingMyStandardFonts.myGetMonospacedFont(defaultSize);
  } //End: Method


//------------------  Method ------------------
/**
 * This static method rounds off integer font sizes to even integer sizes, increasing size of odd integers.
 *
 * @param sizeIn  Starting size
 *
 * @return  New size
 *
 */
  private static final int myRoundOffFontSize( int sizeIn )
  {
//    if ( sizeIn >= TO_BOLD_SIZE-1 && sizeIn <= TO_BOLD_SIZE-1 ) return sizeIn;
//
    int res = sizeIn % 2;
    return sizeIn + res;
  } //End: Method


//------------------  Method ------------------
/**
 * This static method returns the standard font size.
 *
 * @return  ?
 *
 */
  public static final int myGetStandardFontSize( ) { return SwingMyStandardFonts.MY_DEFAULT_FONT_SIZE; }

  
//------------------  Method ------------------
/**
 * This static method ?
 *
 * @return  ?
 *
 */
  public static final Font myGetStandardMonospacedFont ( )
  {
    return SwingMyStandardFonts.myGetMonospacedFont(SwingMyStandardFonts.MY_DEFAULT_FONT_SIZE);
  }

  
//------------------  Method ------------------
/**
 * This static method ?
 *
 * @param scaleFactor  ?
 * 
 * @return  ?
 *
 */
  public static final Font myGetScaledMonospacedFont ( float scaleFactor )
  {
    int scaledFontSize = (int)(scaleFactor * SwingMyStandardFonts.MY_DEFAULT_FONT_SIZE);
    return SwingMyStandardFonts.myGetMonospacedFont(scaledFontSize);
  }


//------------------  Method ------------------
/**
 * This static method ?
 *
 * @param fontSize  ?
 *
 * @return  ?
 *
 */
  public static final Font myGetMonospacedFont ( int fontSize )
  {
//
    fontSize = myRoundOffFontSize( fontSize );
    Font font = null;
//
    ArrayList<Font> fonts = SwingMyStandardFonts.MY_FONTS_MONOSPACED;
//
    for ( Font tmp : fonts ) {
      if ( tmp.getSize() == fontSize ) { font = tmp; break; }
    }
//
    if ( font == null ) {
// Note: bold screws up spacing but is a lot easier to read
      int bold = ( fontSize >= TO_BOLD_SIZE ) ? Font.BOLD : Font.PLAIN;
      font = new Font(SwingMyStandardFonts.myFontName(MY_FONTS.Monospaced), bold, fontSize);
      SwingMyStandardFonts.MY_FONTS_MONOSPACED.add(font);
    }
//
//if(DO_TRACE) {
//System.out.println("\n---\n" + MyTrace.myGetMethodName() + ": exiting" + ": Monospaced fonts" + ": size= " + fontSize);
//for ( Font tmp : fonts ) System.out.println("   " + tmp);
//System.out.println(" Returning: " + font);
//}
//
    return font;
  } //End: Method


//------------------  Method ------------------
/**
 * This static method ?
 *
 * @param fontSize  ?
 *
 * @return  ?
 *
 */
  public static final Font myGetDialogFont ( int fontSize )
  {
//
    fontSize = myRoundOffFontSize( fontSize );
    Font font = null;
//
    for ( Font tmp : SwingMyStandardFonts.MY_FONTS_DIALOG ) {
      if ( tmp.getSize() == fontSize ) { font = tmp; break; }
    }
//
    if ( font == null ) {
      int bold = ( fontSize >= TO_BOLD_SIZE ) ? Font.BOLD : Font.PLAIN;
      font = new Font(SwingMyStandardFonts.myFontName(MY_FONTS.Dialog), bold, fontSize);
      SwingMyStandardFonts.MY_FONTS_DIALOG.add(font);
    }
//
    return font;
  } //End: Method
  

//------------------  Method ------------------
/**
 * This static method ?
 *
 * @return  ?
 *
 */
//  public static final MyObjIntWithPersistence myGetFontSizeObj() { return SwingMyStandardFonts.MY_DEFAULT_FONT_SIZE; }


//------------------  Method  ------------------
/**
 * This static method sets default font for a component as a function of the component type.
 *
 * @param comp  ?
 *
 */
  public static final void mySetDefaultsForComp ( Component comp )
  {
    if ( comp == null ) return;
//
    if ( comp instanceof JButton ) {
      comp.setFont(SwingMyStandardFonts.MY_DFLT_BUTTON_FONT);
      ((JButton) comp).setMargin(SwingMyStandardFonts.myButtonMargins);
    } //End: if ()
//
    else {
      comp.setFont(SwingMyStandardFonts.MY_DFLT_GENERAL_FONT);
    } //End: else
//
// Handle special case of JPanel border
    if ( comp instanceof JPanel ) {
      JPanel hb = (JPanel)comp;
      TitledBorder myBorder = (TitledBorder)hb.getBorder();
      if (myBorder != null) myBorder.setTitleFont(SwingMyStandardFonts.MY_DFLT_GENERAL_FONT);
    }
  } //End: Method
  

  private static final FontRenderContext renderCont = new FontRenderContext(null, false, false);

//------------------  Method ------------------
/**
 * This static method ?
 *
 * @param str  ?
 * @param fontType  ?
 * @param fontSize  ?
 *
 * @return  ?
 */
  public static int[] myGetStringSize( String str, MY_FONTS fontType, int fontSize )
  {
    int[] retVal = null;
    Font font = null;
    if      ( fontType == MY_FONTS.Monospaced ) font = myGetMonospacedFont(fontSize);
    else if ( fontType == MY_FONTS.Dialog )     font = myGetDialogFont(fontSize);
    if ( font != null ) {
      Rectangle2D bounds = font.getStringBounds(str, renderCont);
      retVal = new int[] {(int)bounds.getWidth(), (int)bounds.getHeight()};
    }
    return retVal;
  } //End: Method


//------------------ Main Method ------------------
/**
* This main method tests this class.
* 
* @param  inputArgs  ?
* 
*/
  public static void main(String[] inputArgs)
  {
// List available fonts
    String fonts[] = 
        GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    System.out.println("\n" + MyTrace.myGetMethodName() + ": available fonts:");
    for ( int i = 0; i < fonts.length; i++ ) System.out.println("  " + fonts[i]);
    System.out.println();
    
// Display standard font at various sizes
    JFrame mainFrame = new JFrame ( "Test SwingMyStandardFonts" );
    mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//
    zzTestSwingMyStandardFonts fontsCont = new zzTestSwingMyStandardFonts();
    fontsCont.setPreferredSize( new Dimension(1300,1100) );
//
    JScrollPane scroll = new JScrollPane( fontsCont );
//
    mainFrame.setContentPane(scroll);
//
    mainFrame.setResizable(false);
//
    mainFrame.pack();
    mainFrame.setLocationRelativeTo(null);
//    MyCenterFrame.myCenterFrameOnScreen(mainFrame);
//
    mainFrame.setVisible(true);
  } //End: Method


} //End: class SwingMyStandardFonts



//----------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------
//---------------------------------------  CLASS: zzTestSwingMyStandardFonts  ------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------
/**
* This class tests the SwingMyStandardFonts class.
* @author James Everitt
*/
final class zzTestSwingMyStandardFonts extends JPanel  {

  private static final long serialVersionUID = 0L;


//------------------  Method ------------------
/**
* This static method ?
*
*/
  private static final String myGetText ( int index, int width, int height, int asc, int desc )
  { return "font size = " + index + " : asc = " + asc + " : desc = " + desc + " : text size = " + width + " x " + height + " : ex = yjJ/HqQ{x"; }


//------------------  Method ------------------
/**
* This method paints the mono text at various sizes.
*
*/
  @Override public final void paintComponent ( Graphics g )
  {
    super.paintComponent (g);
//
    int xOffset = 10;
    int y = 10;
    Graphics2D g2d = (Graphics2D)g;
    int textWidth = 0;
    int textHeight = 0;
//
    for ( int iFontSize=6 ; iFontSize<=40 ; iFontSize += 2 ) {
      for ( int type = 0 ; type < 2 ; type++ ) {
        Font font;
        if ( type == 0 ) font = SwingMyStandardFonts.myGetDialogFont(iFontSize);
        else font = SwingMyStandardFonts.myGetMonospacedFont(iFontSize);
        g2d.setFont(font);
//
        FontMetrics metrics = g2d.getFontMetrics(font);
        textHeight = metrics.getHeight();
        int textDescent = metrics.getDescent();
        int textAscent = metrics.getAscent();
// Note: This funnyness needed to get proper width, given that width is part of the string being displayed.
        String txt = zzTestSwingMyStandardFonts.myGetText(iFontSize, 123, textHeight, textAscent, textDescent);
        textWidth = metrics.stringWidth(txt);
        if ( textWidth > 990 ) {
          txt = zzTestSwingMyStandardFonts.myGetText(iFontSize, 1234, textHeight, textAscent, textDescent);
          textWidth = metrics.stringWidth(txt);
        }
//
        txt = zzTestSwingMyStandardFonts.myGetText(iFontSize, textWidth, textHeight, textAscent, textDescent);
        g2d.drawString( txt, xOffset, y-textDescent );
//
        g2d.drawRect(xOffset, y-textHeight, textWidth, textHeight);
        y += textHeight + 4;
      }
      y += 10;
    }
//
    this.setPreferredSize( new Dimension (
        xOffset + textWidth + xOffset,
        y - textHeight
        ));
    this.setSize(this.getPreferredSize());
//
    g2d.dispose();
  } //End: Method

} //End: class zzTestSwingMyStandardFonts
