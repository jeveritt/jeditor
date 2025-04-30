// MyReadOption.java
package my_proj.my_lib.lib;

//------------------  Import statements  ------------

import java.util.ArrayList;


//  ----------------  CLASS:  ----------------
/**
 * This class handles read options
 *
 * @author James Everitt
 */
public class MyReadOption {

/** Specifies no argument */
  public final static int MY_READ_OPT_NO_ARG = -1;
/** Specifies error */
  public final static int MY_READ_OPT_ERROR  = -2;


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private MyReadOption ( ) {}

  
//------------------------------------------------------------------------
//--------------------------  Static Methods:  ---------------------------
//------------------------------------------------------------------------


/**
 * This static method returns a pointer to matched arguments
 * <pre>
 *  Usage example:
 *  
 *   import library.MyMisc.*;
 *
 *   String args[] = {"-arg1", "val1", "-arg2", "val2"};
 *   int  argIndex;
 *   String foundArg;
 *
 *   if ( (argIndex = MyReadOption.myGetKeyIndex (args,"-arg2",1) ) &gt;= 0 )
 *                foundArg = args[argIndex+1]);
 *
 *  Returns
 *     MyReadOption.MY_READ_OPT_NO_ARG = -1 if arg not found
 *     MyReadOption.MY_READ_OPT_ERROR = -2 if not enough arg entries
 * </pre>
 * 
 * @param argsIn  input String array
 * @param key  String argument to search for
 * @param numbArgsAfter  number of String args before next String starting with -
 *
 * @return  position of option or -1 or -2
 *
 */
  public static final int myGetKeyIndex ( String argsIn[], String key, int numbArgsAfter )
  { return MyReadOption.myGetKeyIndex(argsIn, key, numbArgsAfter, 0); }


/**
 * This static method
 * 
 * @param argsIn  ?
 * @param option  ?
 * @param numbArgsAfter  ?
 * @param startingIndx  ?
 *
 * @return ?
 */
 public static final int myGetKeyIndex (
    String      argsIn[],        // Pointers to argument words
    String      option,          // Option word being searched for
    int         numbArgsAfter,   // Number argv's before next -
    int         startingIndx     // Index to start search
  )
  {
    int i1;
    int index = MyReadOption.MY_READ_OPT_NO_ARG;

    if ( argsIn != null && option != null && startingIndx < argsIn.length ) {
// find arg
      for (i1=startingIndx ; i1<argsIn.length ; i1++) {
        if  (option.equals(argsIn[i1]) ) {
          index = i1;
          break;
        }
      } //End: for ()

// check enough args after
      if ( index > MyReadOption.MY_READ_OPT_NO_ARG && argsIn.length - index - 1 < numbArgsAfter ) index = MyReadOption.MY_READ_OPT_ERROR;

// check enough args after without hyphen
      if ( index > MyReadOption.MY_READ_OPT_NO_ARG && numbArgsAfter > 0 ) {
        for ( i1=index+1 ; i1<argsIn.length ; i1++ ) {
          if ( argsIn[i1] != null && argsIn[i1].length() > 0 && argsIn[i1].charAt(0) == '-' ) {
            if ( i1 - index - 1 < numbArgsAfter ) index = MyReadOption.MY_READ_OPT_ERROR;
            break;
          } //End: if ()
        } //End: for ()
      } //End: if ()
    } //End: if ()
//
    return index;

  } //End: Method


//------------------  Method  ------------------
/**
 * This static method
 * 
 * @param argsIn  ?
 * @param key  ?
 * @param numbArgsAfter  ?
 *
 * @return ?
 */
  public static final int myGetKeyIndex (
      ArrayList<String> argsIn,          // Pointers to argument words
      String            key,             // Option word being searched for
      int               numbArgsAfter    // Number argv's before next
    )
  {
    int i1;
    int index = MyReadOption.MY_READ_OPT_NO_ARG;
    if ( argsIn != null && key != null ) {
// find arg
      for (i1=0 ; i1<argsIn.size() ; i1++) {
        if  (key.equals(argsIn.get(i1)) ) {
          index = i1;
          break;
        }
      } //End: for ()
// check enough args after
      if ( index > MyReadOption.MY_READ_OPT_NO_ARG && argsIn.size() - index - 1 < numbArgsAfter ) index = MyReadOption.MY_READ_OPT_ERROR;
// check enough args after without hyphen
      if ( index > MyReadOption.MY_READ_OPT_NO_ARG && numbArgsAfter > 0 ) {
        for ( i1=index+1 ; i1<argsIn.size() ; i1++ ) {
          if ( argsIn.get(i1).charAt(0) == '-' ) {
            if ( i1 - index - 1 < numbArgsAfter ) index = MyReadOption.MY_READ_OPT_ERROR;
            break;
          } //End: if ()
        } //End: for ()
      } //End: if ()
    } //End: if ()
//
    return  index;
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method returns string array of args after key
 * <pre>
 *  Usage example:
 *  
 *   import library.MyMisc.*;
 *
 *   String args[] = {"-arg1", "val1", "-arg2", "val2"};
 *
 *   argValues = MyReadOption.myArgsAfterKey (args,"-arg2");
 *
 * </pre>
 *
 * @param argsIn  input String array
 * @param key  String argument to search for
 *
 * @return  String array of arg values or null arg does not exist or no values
 *
 */
  public static final String[] myArgsAfterKey( String argsIn[], String key )
  {
    String argsOut[] = null;
//
    if ( argsIn != null && key != null ) {
//
// Figure out range of args
      int i1;
      int strtIndex = -1;
      int stopIndex = -1;
      for ( i1=0 ; i1<argsIn.length ; i1++ ) {
        if ( strtIndex < 0 ) {
          if ( key.equals(argsIn[i1]) ) { strtIndex = i1+1; }
        } //End: if ()
        else if ( i1 == argsIn.length-1 ) stopIndex = argsIn.length;
        else if ( argsIn[i1].charAt(0) == '-' ) { stopIndex = i1; break;}
      } //End: for ()
//
// Put args into new array
      if ( strtIndex >= 0 && stopIndex > strtIndex ) {
        argsOut = new String[stopIndex-strtIndex];
        for ( i1=0 ; i1<argsOut.length ; i1++ ) argsOut[i1] = argsIn[strtIndex+i1];
      } //End: if ()
//
// Get rid of quotes
      for ( i1=0 ; argsOut != null && i1<argsOut.length ; i1++ ) {
        if ( argsOut[i1].startsWith("\"") || argsOut[i1].startsWith("'") ) {
          argsOut[i1] = argsOut[i1].substring(1, argsOut[i1].length() - 2);
        }
      } //End: for ()
    } //End: if ()
//
    return argsOut;
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method returns string array of args after key
 * <pre>
 *  Usage example:
 *  
 *   import library.MyMisc.*;
 *
 *   String args[] = {"-arg1", "val1", "-arg2", "val2"};
 *
 *   argValues = MyReadOption.myArgsAfterKey (args,"-arg2");
 *
 * </pre>
 *
 * @param argsIn  input String array
 * @param key  String argument to search for
 *
 * @return  String array of arg values or null arg does not exist or no values
 *
 */
  public static final ArrayList<String> myArgsAryLstAfterKey( String argsIn[], String key )
  {
    ArrayList<String> argsOut = new ArrayList<>();
//
    if ( argsIn != null && key != null ) {
//
// Figure out range of args
      int i1;
      boolean found = false;
      for ( String arg : argsIn ) {
        if ( !found && key.equals(arg) ) found = true;
        else if ( found && arg.startsWith("-") ) break;
        else if ( found ) argsOut.add(arg);
      } //End: for ()
//
// Get rid of quotes
      for ( i1=0 ; argsOut != null && i1<argsOut.size() ; i1++ ) {
        String arg = argsOut.get(i1);
        if ( arg.startsWith("\"") || arg.startsWith("'") ) {
          argsOut.set(i1, arg.substring(1, arg.length() - 2) );
        }
      } //End: for ()
    } //End: if ()
//
    return argsOut;
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param argsIn  ?
 * @param key  ?
 *
 * @return  ?
 *
 */
  public static final ArrayList<String> myAllValuesAfterKeyStartingWith ( String argsIn[] , String key )
  {
    ArrayList<String> retVal = new ArrayList<>();
//
    if ( argsIn != null && argsIn.length > 1 && key != null ) {
      String key2 = ( key.length() > 1 && key.startsWith("-") ) ? key.substring(1) : null;
      for ( int i1=0 ; i1<argsIn.length-1 ; i1++ ) {
        if ( argsIn[i1].startsWith(key) ||
             (key2 != null && argsIn[i1].startsWith(key2))
           ) retVal.add(argsIn[i1+1]);
      } //End: for ()
    } //End: if ()
//
    return retVal;
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param argsIn  ?
 * @param key  ?
 *
 * @return  ?
 *
 */
  public static final String[] myAddKeyIfMissing ( String argsIn[] , String key )
  {
    boolean found = false;
    for (String tmp : argsIn ) {
      if ( tmp.equals(key) ) { found = true; break; }
    }
//
    String[] retVal;
    if ( found ) retVal = argsIn;
    else {
      retVal = new String[argsIn.length + 1];
      retVal[argsIn.length] = key;
    }
//
    return retVal;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param argsIn  ?
 * @param key  ?
 *
 * @return  ?
 *
 */
  public static final boolean myKeyExists( String argsIn[] , String key )
  {
    boolean exists = false;
//
    if ( argsIn != null && key != null ) {
      for ( int i1=0 ; i1<argsIn.length ; i1++ ) {
        if ( key.equals(argsIn[i1]) ) { exists = true; break; }
      }
    }
    return exists;
  } //End: Method

  
//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param defaultVal  ?
 * @param argsIn  ?
 * @param key  ?
 *
 * @return  ?
 *
 */
  public static final String myValueAfterKey( String defaultVal, String argsIn[] , String key )
  {
    int indx;
    String retVal = ( ( indx = MyReadOption.myGetKeyIndex(argsIn, key, 1, 0) ) >= 0 ) ? argsIn[indx+1] : defaultVal;
    return retVal;
  } //End: Method

//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param argsIn  ?
 * @param key  ?
 *
 * @return  ?
 *
 */
  public static final String myValueAfterKey( String argsIn[] , String key )
  {
    int indx;
    String retVal = ( ( indx = MyReadOption.myGetKeyIndex(argsIn, key, 1, 0) ) >= 0 ) ? argsIn[indx+1] : null;
    return retVal;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param defaultVal ?
 * @param argsIn  ?
 * @param key  ?
 *
 * @return  ?
 *
 */
  public static final float myFloatValueAfterKey( float defaultVal, String argsIn[] , String key )
  {
    String valStr = myValueAfterKey(argsIn , key);
    if ( valStr == null ) return defaultVal;
    float retVal = defaultVal;
    try { retVal = Float.parseFloat(valStr); }
    catch ( Exception e ) {}
//
    return retVal;
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param defaultVal ?
 * @param argsIn  ?
 * @param key  ?
 *
 * @return  ?
 *
 */
  public static final int myIntValueAfterKey( int defaultVal, String argsIn[] , String key )
  {
    String valStr = myValueAfterKey(argsIn , key);
    if ( valStr == null ) return defaultVal;
    return Integer.parseInt(valStr);
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param argsIn  ?
 * @param key  ?
 * @param argValue  ?
 *
 * @return  ?
 *
 * @throws Exception  Java standard exception
 */
  public static final String[] mySetArgValue( String[] argsIn, String key, String argValue) throws Exception
  {
    if ( key == null || argValue == null ) return argsIn;
//
    if ( argsIn == null ) return new String[] {key, argValue};
//
    String[] retVal = null;
    if ( !MyReadOption.myKeyExists(argsIn, key) ) {
      retVal = new String[argsIn.length+2];
      for (int i1=0 ; i1<argsIn.length ; i1++ ) retVal[i1] = argsIn[i1];
      retVal[argsIn.length] = key;
      retVal[argsIn.length+1] = argValue;
    }
    else {
      int indx = MyReadOption.myGetKeyIndex(argsIn, key, 1);
      if ( indx < 0 ) throw new Exception ("MyReadOption.mySetArgValue" + ": can't replace " + key + " with value " + argValue);
      argsIn[indx+1] = argValue;
      retVal = argsIn;
    }
//
    return retVal;
  } //End: Mehod

//------------------  Method  ------------------
/**
 * This static method ?
 *
 * @param defaultVal ?
 * @param argsIn  ?
 * @param key  ?
 *
 * @return  ?
 *
 */
  public static final boolean myBoolValueAfterKey( boolean defaultVal, String argsIn[] , String key )
  {
    String valStr = myValueAfterKey(argsIn , key);
    if ( valStr == null ) return defaultVal;
    boolean retVal;
    if ( valStr.equalsIgnoreCase("true") || valStr.equalsIgnoreCase("yes") || valStr.equalsIgnoreCase("t") || valStr.equalsIgnoreCase("y") ) retVal = true;
    else retVal = false;
    return retVal;
  } //End: Method

  
  
} //End: class MyReadOption
