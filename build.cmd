@echo off
echo Building Minecraft 1.3.01 Ultimate Edition...
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven and add it to your PATH
    pause
    exit /b 1
)

REM Clean and compile
echo Cleaning previous builds...
mvn clean
if %errorlevel% neq 0 (
    echo Error: Clean failed
    pause
    exit /b 1
)

echo Compiling source code...
mvn compile
if %errorlevel% neq 0 (
    echo Error: Compilation failed
    pause
    exit /b 1
)

echo Creating executable JAR...
mvn package
if %errorlevel% neq 0 (
    echo Error: Packaging failed
    pause
    exit /b 1
)

echo.
echo Build completed successfully!
echo Executable JAR created: target\minecraft-1.3.01-ultimate-edition.jar
echo.
pause
