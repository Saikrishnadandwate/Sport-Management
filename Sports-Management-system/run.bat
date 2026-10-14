@echo off
echo ===================================================
echo   Starting Sports Management System...
echo ===================================================
if not exist "bin\com\sports\Main.class" (
    echo [INFO] Binaries not found. Compiling first...
    if not exist "bin" mkdir bin
    javac -d bin src/com/sports/model/*.java src/com/sports/exception/*.java src/com/sports/util/*.java src/com/sports/service/*.java src/com/sports/Main.java
)

java -cp bin com.sports.Main
pause
