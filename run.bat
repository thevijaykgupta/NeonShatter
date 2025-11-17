@echo off
echo Compiling Neon Shatter...
javac -source 8 -target 8 -d bin -sourcepath src src\*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo.
echo Starting game...
echo.
java -cp bin NeonShatter
pause

