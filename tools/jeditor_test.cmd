@ECHO OFF

REM: If java executable not already in search path, then set to correct java path

set class_path="..\target\jeditor.jar"

java -cp %class_path% my_proj.jeditor.jedit ^
 -file "..\code_jeditor\src\test\jeditor_data\just_a_random_text_file.txt" ^
 -ops ^
 -rw ^
 -sizex 600 ^
 -sizey 900 ^
 -title "jeditor with args"

REM: -dir '/tmp' ^

pause
