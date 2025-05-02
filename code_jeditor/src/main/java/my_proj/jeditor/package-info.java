/**
 * This package contains the runnable program "jedit" which is a text editor for Java.
 * It is similar to a simplified VIM program.
 * The program is implemented using the Swing library.
 * The GUI mode is built into the Swing library and behaves in a pretty standard way.
 * The command line mode has been added on and looks like a simplified vi with a limited set of commands.
 * <pre>
 * Modes are as follows:
 *   GUI: works in parallel with Vi->Text mode
 *   Vi:
 *     Text mode: enter from command mode by typing \"i\" or \"a\"
 *     Command mode: enter from text mode with &lt;esc&gt; key
 *       Direct: command executed by typing in edit window
 *       CmdLn: type \":\" for cmd line then enter command and return
 * </pre>
 * @author James Everitt
 */
//
package my_proj.jeditor;
//
