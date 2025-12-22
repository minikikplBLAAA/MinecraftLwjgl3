@echo off
echo Starting minecraft 1.3.01 Ultimate Edition...
echo.

cd /d "%~dp0"

set CLASSPATH=target\minecraft-1.3.01-ultimate-edition-1.3.01-Ultimate.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-3.3.3.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-opengl-3.3.3.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-glfw-3.3.3.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-3.3.3-natives-windows.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-opengl-3.3.3-natives-windows.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-glfw-3.3.3-natives-windows.jar

echo Running with LWJGL Debugging...
java -Xmx1G -XX:+UseG1GC -Dorg.lwjgl.util.Debug=true -Dorg.lwjgl.util.DebugLoader=true -Dorg.lwjgl.librarypath=natives -Djava.awt.headless=false -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel -cp "%CLASSPATH%" net.minecraft.client.Minecraft

if errorlevel 1 (
    echo.
    echo Startup error! Please check if:
    echo 1. You have Java 8 or newer installed.
    echo 2. The minecraft-1.3.01-ultimate-edition-1.3.01-Ultimate.jar file exists in the target folder.
    echo 3. The LWJGL 3 libraries are available in the lib folder.
    echo 4. All dependencies have been copied (mvn dependency:copy-dependencies).
    echo 5. The "natives" folder exists and is saved.
    echo 6. Your graphics drivers are up to date.
    echo.
    echo Check the hs_err_pid*.log file in your project folder for more information.
    echo.
    pause
)

