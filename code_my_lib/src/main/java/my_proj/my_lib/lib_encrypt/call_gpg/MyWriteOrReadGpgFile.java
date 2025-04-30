// MyWriteOrReadGpgFile.java
/*
 * Copyright (C) 2022 James Everitt
 *
 * This file is part of Open Schematic Capture.
 * 
 * Open Schematic Capture is free software: you can redistribute it
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
package my_proj.my_lib.lib_encrypt.call_gpg;

//----------  Import statements  ----------

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

import my_proj.my_lib.lib.MyMisc;
import my_proj.my_lib.lib.MyTrace;


//  ----------------  CLASS: MyWriteOrReadGpgFile  ----------------
/**
 * This class uses the system program gpg2 to encrypt and decrypt data.
 *
 * @author James Everitt
 */
public class MyWriteOrReadGpgFile  {

//private static final boolean DO_TRACE = true;

/** GPG extension .gpg */
  public static final String MY_ENCRYPT_EXTENSION = ".gpg";


//------------------------------------------------------------------------
//--------------------------  Methods:  ----------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This is the constructor that should never be called
 * 
 */
  private MyWriteOrReadGpgFile ( ) {}


//------------------------------------------------------------------------
//----------------------  Static Methods:  -------------------------------
//------------------------------------------------------------------------


//------------------  Method  ------------------
/**
 * This static method tries to find my standard password.
 *
 * @return Returns the password or null
 */
  public static final String myGetStandardPas ()
  {
    try {
      String nm = System.getProperty("user.home") + MyMisc.myGetTmpDirectory() + "/aapas";
      File file = new File(nm);
      if ( !file.isFile() ) return null;
      ArrayList<String> dec = MyWriteOrReadGpgFile.myReadDecryptedFile(nm, "itsamypassword123", null);
      String retVal = ( dec == null || dec.size() < 1) ? null : dec.get(0);
//System.out.println(MyTrace.myGetMethodName() + ": val = " + retVal);
      return retVal;
    }
    catch (Exception e) {
      System.out.println("MyWriteOrReadGpgFile.myGetStandardPas" + ": exc= " + e);
      return null;
    }
  } //End: Method


//------------------  Method  ------------------
/**
 * This static method prints out data from a buffered reader and then closes the reader.
 *
 * @param reader  Buffered reader to read from and then close.
 * @param title  Title to print in front of the data
 * 
 * @throws IOException 
 */
  private static final void myPrintOutputAndClose ( BufferedReader reader, String title ) throws IOException
  {
    String line;
    boolean first = true;
    while ( (line = reader.readLine()) != null ) {
      if ( first ) System.out.println( title );
      first = false;
      System.out.println(line);
    }
    reader.close();
  } //End: Method



//------------------  Method  ------------------
/**
 * This static method finds a file at the current location or in the executable path.
 * 
 * @param fileName  Simple name of file to be found
 * @param mustBeExecutable  File must be executable
 *
 * @return  Returns File
 */
  private static final File myGetFileInEnvironmentPath( String fileName, boolean mustBeExecutable )
  {
    if ( fileName == null ) return null;
    File retVal = null;
//
    String path = System.getenv("PATH");
    while ( path != null && path.length() > 0 ) {
// Break path into segments
      int indx = path.indexOf(';');
      if (indx < 0 ) indx = path.indexOf(':');
      String folderNm;
      if ( indx < 0 ) {
        folderNm = path;
        path = null;
      }
      else {
        folderNm = path.substring(0,indx);
        path = path.substring(indx+1);
      }
// Scan thru segments
      File folder = new File(folderNm);
      if ( !folder.isDirectory() ) continue;
      File[] files = folder.listFiles();
      for ( File file : files ) {
        if ( file != null && file.getName().equalsIgnoreCase(fileName) ) { retVal = file; break; }
      }
    } //End: while
//
    if ( retVal != null &&
        ( !retVal.isFile() || (mustBeExecutable && !retVal.canExecute() ) ) ) retVal = null;
//
    return retVal;
  } //End: Method

//------------------  Method  ------------------
/**
 * This method finds the encryption/decryption system command.
 *
 * @return Returns found command or null.
 */
  public static final String myGetPgpCommand (  )
  {
    File gpgExe = myGetFileInEnvironmentPath( "gpg2", true );
    if ( gpgExe == null || !gpgExe.isFile() ) gpgExe = myGetFileInEnvironmentPath( "gpg", true );
    if ( gpgExe != null && !gpgExe.isFile()  ) gpgExe = null;
    return gpgExe != null ? gpgExe.getName() : null;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method passes data in the form of a string to the gpg2 encryptor which sends it to a file.
 *
 * @param outputFileName  The name of the file which will contain the encrypted data.
 * @param data  Data to be encrypted in the form of a String.
 * @param passwordOrNull  The password if passphrase arg is used, otherwise null to flag using the key ring.
 * 
 * @throws IOException  Throws IO exception
 * @throws InterruptedException  Throws interrupt exception
 * 
 * @return Returns exit status int
 */
  public static final int myWriteEncryptedFile(
      String outputFileName,
      String data,
      String passwordOrNull
      ) throws IOException, InterruptedException
  {
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln(MyTrace.myGetMethodName() + ": outFile= " + outputFileName);
// Check that gpg2 is on system
    String gpgCmd = myGetPgpCommand();
    if ( gpgCmd == null ) throw new IOException( "MyWriteOrReadGpgFile.myWriteEncryptedFile" + ": no pgp executable");
// Check file name
    if ( outputFileName == null ) throw new IOException( "MyWriteOrReadGpgFile.myWriteEncryptedFile" + ": no file name");
    if ( !outputFileName.endsWith(MY_ENCRYPT_EXTENSION) && !outputFileName.endsWith(".gpg") && !outputFileName.endsWith(".gpg2") ) {
      if ( passwordOrNull != null && !outputFileName.contains(".pw") ) outputFileName += ".pw";
      outputFileName += MY_ENCRYPT_EXTENSION;
    }
// Delete old file
    File oldFile = new File(outputFileName);
    if ( oldFile.isFile() ) oldFile.delete();
// Check on key ring and/or create command
    String[] cmd;
// Asymmetric encryption - use key ring
    if ( passwordOrNull == null ) {
      File keyRing = new File( System.getProperty("user.home") + File.separator + ".gnupg" );
      if ( !keyRing.isDirectory() ) throw new IOException( "MyWriteOrReadGpgFile.myWriteEncryptedFile" + ": no key ring at " + keyRing.getAbsolutePath());
      cmd = new String[] { gpgCmd, "-e", "--force-mdc", "-r", "jeveritt@surewest.net", "-o", outputFileName };
    }
// Symmetric encryption - use supplied password
    else {
      cmd = new String[] { gpgCmd, "-c", "--batch", "--passphrase",  passwordOrNull, "-o", outputFileName };
    }
//
//if(DO_TRACE)System.out.println( MyTrace.myGetMethodName() + ": reach 1" + "\n file= " + outputFileName + "\n cmd= " + MyArrayOps.myArrayToString(cmd, ' ') );
//
// Create process builder
    ProcessBuilder pb = new ProcessBuilder(cmd);
// This is already the default
    pb.redirectInput(Redirect.PIPE);
// Start process
    Process proc = pb.start();
// Stream data into gpg2 process for encryption
    BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(proc.getOutputStream()) );
    writer.write(data);
    writer.close();
// Monitor errors and output
    BufferedReader outputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
    myPrintOutputAndClose ( outputReader, "\nOutput:" );
    myPrintOutputAndClose ( errorReader, "\nErrors:" );
// Wait for process to exit
    int exit = proc.waitFor();
//
    if ( exit != 0 ) throw new IOException ("MyWriteOrReadGpgFile.myWriteEncryptedFile" + ": exit error = " + exit);
//
    return exit;
  } //End: Method


//------------------  Method  ------------------
/**
 * This method reads in encrypted data from a file, decrypts it, and returns it as a ArrayList.
 *
 * @param inputFileName  Encrypted data file to be read.
 * @param passwordOrNull   The password if passphrase arg is used, otherwise null to flag using the key ring.
 * @param data   Output ArrayList or null, in which case the method creates the ArrayList itself.
 * 
 * @return  Returns an ArrayList of data
 * 
 * @throws IOException   Throws IO exception
 * @throws InterruptedException  Throws interrupt exception
 */
  public static final ArrayList<String> myReadDecryptedFile(
      String            inputFileName,
      String            passwordOrNull,
      ArrayList<String> data
      ) throws IOException, InterruptedException
  {
//if(DO_TRACE)System.out.println("\n" + MyTrace.myGetMethodName() + ": pw= " + passwordOrNull + ": file= " + inputFileName);
//
    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln(MyTrace.myGetMethodName() + ": inFile= " + inputFileName);
//
    if ( data == null ) data = new ArrayList<>();
// Check that gpg2 is on system
    String gpgCmd = myGetPgpCommand();
    if ( gpgCmd == null ) throw new IOException( "MyWriteOrReadGpgFile.myReadDecryptedFile" + ": no pgp executable");
// Check file name
    if ( inputFileName == null ) throw new IOException( "MyWriteOrReadGpgFile.myReadDecryptedFile" + ": no file name");
    File inputFile = new File(inputFileName);
    if ( !inputFile.exists() ) {
      if ( !inputFileName.endsWith(MY_ENCRYPT_EXTENSION) && !inputFileName.endsWith(".gpg") && !inputFileName.endsWith(".gpg2") ) {
        if ( passwordOrNull != null && !inputFileName.contains(".pw") ) inputFileName += ".pw";
        inputFileName += MY_ENCRYPT_EXTENSION;
      }
    }
    inputFile = new File(inputFileName);
    if ( !inputFile.isFile() ) throw new IOException ( "MyWriteOrReadGpgFile.myReadDecryptedFile" + ": no file: " + inputFileName);
// Check on key ring and/or create command
    String[] cmd;
    if ( passwordOrNull == null ) {
      File keyRing = new File( System.getProperty("user.home") + File.separator + ".gnupg" );
      if ( !keyRing.isDirectory() ) throw new IOException( "MyWriteOrReadGpgFile.myReadDecryptedFile" + ": no key ring at " + keyRing.getAbsolutePath());
      cmd = new String[] { gpgCmd, "-d", inputFileName };
    }
    else {
      cmd = new String[] { gpgCmd, "-d", "--batch", "--passphrase",  passwordOrNull, inputFileName };
    }
//
//if(DO_TRACE)System.out.println( MyTrace.myGetMethodName() + "\n file= " + inputFileName + "\n cmd= " + MyArrayOps.myArrayToString(cmd, ' ') );
//
// Create process builder
    ProcessBuilder pb = new ProcessBuilder(cmd);
// This is already the default
    pb.redirectInput(Redirect.PIPE);
// Set output directory - don't need here
//    pb.directory( new File( MyMisc.myGetTmpDirectory() ) );
// Start process
    Process proc = pb.start();
// Read decryted output
    BufferedReader outputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    String line;
    while ( (line = outputReader.readLine()) != null ) data.add(line);
    outputReader.close();
// Monitor errors
    BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
    myPrintOutputAndClose ( errorReader, "\nFeedback:" );
// Wait for process to exit
    int exit = proc.waitFor();
//
    if ( exit != 0 ) throw new IOException ("MyWriteOrReadGpgFile.myReadDecryptedFile" + ": exit error = " + exit);
//
    return data;
  } //End: Method


//------------------ Method ------------------
/**
 * This method runs the test.
 * 
 * @param  outFile  Output file
 *
 * @return Returns true on success
 */
  public static final boolean myRunGpgTest ( String outFile )
  {
//
//    if ( MyTrace.myDoPrint() ) MyTrace.myPrintln( MyTrace.myInd() + MyTrace.myGetMethodName() );
//
// Put test here
//
    try {
      String data = "This is my test phrase for the encoder\n" +
                    "  Random stuff  1: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff  2: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff  3: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff  4: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff  5: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff  6: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff  7: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff  8: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff  9: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" +
                    "  Random stuff 10: 1fdicmewpsadmfeied9173043ucd&JJNT$$#(LMHG!^YIUJNUIU_+x\n" ;
//      String outFile = MyMisc.myGetTmpDirectory() + "/zz_file_out";
//
// Test 1 - write encrypted file using key ring
      int retVal = MyWriteOrReadGpgFile.myWriteEncryptedFile(
         outFile,  // String fileName,
         data,     // String data,
         "zzzdummypassword"      // String passwordOrNull
        );
      System.out.println("\n" + "MyWriteOrReadGpgFile.myRunGpgTest" + ": encrypt" + ": file= " + outFile + ": password= null" + ": retVal= " + retVal);
//
// Test 2 - write encrypted file using password
      String password = "abcd2";
      retVal = MyWriteOrReadGpgFile.myWriteEncryptedFile(
         outFile,  // String fileName,
         data,     // String data,
         password  // String passwordOrNull
        );
      System.out.println("\n" + "MyWriteOrReadGpgFile.myRunGpgTest" + ": encrypt" + ": file= " + outFile + ": password= " + password + ": retVal= " + retVal);
//
// Test 3 - read back password encrypted data
      System.out.println("\n" + "MyWriteOrReadGpgFile.myRunGpgTest" + ": decrypt and list results" + ": file= " + outFile + ": password= " + password );
      ArrayList<String> dataBack = MyWriteOrReadGpgFile.myReadDecryptedFile(
          outFile,  // String            inputFileName,
          password, // String            passwordOrNull,
          null      // ArrayList<String> data
          );
      System.out.println( "\nRead Back Data:" );
      for ( String line : dataBack ) System.out.println( "  " + line );
//
      return true;
    }
    catch (Exception e) {
      System.out.println("MyWriteOrReadGpgFile.myRunGpgTest" + ": exc= " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  } //End: Method


//------------------ Main Method ------------------
/**
* This main method is strictly for testing.
* 
* @param args String[] of input arguments
* 
*/
  static void main( String[] args ) { myRunGpgTest ( MyMisc.myGetTmpDirectory() + "/zz_file_out" ); }

} //End: Class MyWriteOrReadGpgFile
