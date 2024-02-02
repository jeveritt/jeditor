@ECHO OFF

REM: If java executable not already in search path, then set to correct java path

set class_path="..\target\jeditor.jar"

java -cp %class_path% my_proj.jeditor.jedit %1 %2 %3 %4 %5

pause
