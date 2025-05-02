
  INTRODUCTION:

    This program is a simple text editor written in Java and using the
    Java Swing graphics library.

    It provides the ability to switch between a graphics mode and a
    Vi like command line mode in a fashion similar to Vim.

    The graphical mode comes pre-baked from the Java Swing components.
    The Vi like command line mode has been added. Note that it
    supports a much more limited number of operations than Vi.

    The program is also able to read and write GPG encrypted files if
    it can find the gpg executable.

    The program is pure Java and has no external dependencies other
    than the Java Standard Development Kit (SDK) libraries. This and
    the fact that it is fairly minimal allows the editor to be easily
    merged into other Java programs.

    To compile and run the code you need to install the SDK.


  To Try It Out:

    Ensure the Java Runtime Environment (JRE) or the JDK is installed.

    Download target/jeditor.jar

    Type:
       java  -cp  <path_to_jar>/jeditor.jar  my_proj.jeditor.jedit


  ORGINIZATION:

    The orginization of the project evolved as follows:

    Started with this Java project example which was found on the
    internet and which seems to be fairly standard:

      helloworld = project directory
      +-- helloworld-master = master/only library
          |-- pom.xml
          |-- README.md
          +-- src
              +-- main
                  +-- java
                      +-- com
                          +-- cerveros = company
                              +-- demo = top grouping
                                  +-- helloworld = second level grouping
                                      +-- HelloWorld.java = only java file

    Replaced with jeditor project:

      jeditor = project directory
      |-- code_jeditor = master library
      |   +-- src
      |       |-- data
      |       |   +-- Nothing here
      |       |-- main
      |       |   +-- java
      |       |       +-- my_proj = sort of my company
      |       |           +-- jeditor = holder of top level program
      |       |               +-- jedit.java
      |       +-- test
      |           +-- Nothing here
      +-- code_my_lib = secondary library
          +-- src
              |-- data
              |   +-- Nothing here
              |-- main
              |   +-- java
              |       +-- my_proj = sort of my company
              |           |-- my_lib = grouping of commonly used packages
              |           |   |-- lib              = second level grouping
              |           |   |-- lib_encrypt      = second level grouping
              |           |   |   + call_gpg       = third  level grouping
              |           |   |-- lib_ops          = second level grouping
              |           |   |-- lib_swing        = second level grouping
              |           |   +-- lib_swing_editor = second level grouping
              |           +-- my_lib_resources
              |               +-- my_gifs
              +-- test
                  +-- Nothing here

      If grouping within jeditor seems more fine grained than necessary it
      is because this project was pulled out of a much larger project.

    Modified to be a GitHub project using ant to build:

      jeditor = project directory
      |-- .git = git directory
      |-- .gitignore
      |-- LICENSE
      |-- README.txt = this file
      |-- java_editor_documentation.zip = compressed documentation
      |-- tools = various scripts
      |   |-- build
      |   |   |-- build.xml = file called by ant to build jeditor.jar
      |   |   +-- README.txt
      |   |-- jeditor = Linux script to run jedit
      |   |-- jeditor.cmd = Windows script to run jedit
      |   +-- other scripts
      |-- code_jeditor = master library
      |   +-- Same as above
      |-- code_my_lib = secondary library
      |   +-- Same as above
      +-- target
          +-- jeditor.jar = program compiled by ant

  USAGE:

    Command:
      java  -cp  <path_to_jar>/jeditor.jar  my_proj.jeditor.jedit  [options]
    Base:
      [<java_path>/]java          : Path and name for the java virtual machine executable
                                  : Path not needed if included in executable path variable PATH
      [-cp <class_path>]          : Path to java archive
                                  : Not needed if set in java variable CLASS_PATH
      my_proj.jeditor.jedit       : Program name
    Options:
      -file <file_name>           : File to be opened
      -dir <directory_name>       : Start editor specified directory
      -ops                        : Allow various file operations
      -ro                         : Set file to read only
      -rw                         : Set file to read/write
      -pw <password>              : Password for encrypted file
      -isenc                      : Indicates file is encrypted
      -enro                       : Set file to encrypted read only
      -enrw                       : Set file to encrypted read/write
      -nomenu                     : Causes menu tool bar to not be shown
      -sizex <width>              : Width of GUI
      -sizey <height>             : Height of GUI
      --help|-help|--h|-h         : Prints help message
    Example:
       java \
         -cp <path_to_jar_dir>/jeditor.jar \
           my_proj.jeditor.jedit \
             -file <full_path_file_name> \
               -ops -rw
