@echo off
echo ===================================================
echo   Compiling Sports Management System (Core Java)...
echo ===================================================
if not exist "bin" mkdir bin

javac -d bin src/com/sports/model/*.java src/com/sports/exception/*.java src/com/sports/util/*.java src/com/sports/service/*.java src/com/sports/Main.java

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation completed successfully into bin/ directory!
) else (
    echo [ERROR] Compilation failed. Please check errors above.
)
pause
