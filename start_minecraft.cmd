@echo off
echo Uruchamianie Minecraft 1.3.01 Ultimate Edition...
echo.

cd /d "%~dp0"

set CLASSPATH=target\minecraft-1.3.01-ultimate-edition-1.3.01-Ultimate.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-3.3.3.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-opengl-3.3.3.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-glfw-3.3.3.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-3.3.3-natives-windows.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-opengl-3.3.3-natives-windows.jar
set CLASSPATH=%CLASSPATH%;lib\lwjgl-glfw-3.3.3-natives-windows.jar

echo Uruchamianie z debugowaniem LWJGL...
java -Xmx1G -XX:+UseG1GC -Dorg.lwjgl.util.Debug=true -Dorg.lwjgl.util.DebugLoader=true -Dorg.lwjgl.librarypath=natives -Djava.awt.headless=false -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel -cp "%CLASSPATH%" net.minecraft.client.Minecraft

if errorlevel 1 (
    echo.
    echo Blad uruchomienia! Sprawdz czy:
    echo 1. Masz zainstalowana Jave 8 lub nowsza
    echo 2. Plik minecraft-1.3.01-ultimate-edition-1.3.01-Ultimate.jar istnieje w folderze target
    echo 3. Biblioteki LWJGL 3 sa dostepne w folderze lib
    echo 4. Wszystkie zaleznosci zostaly skopiowane (mvn dependency:copy-dependencies)
    echo 5. Folder "natives" istnieje i jest zapisywalny
    echo 6. Sterowniki graficzne sa aktualne
    echo.
    echo Sprawdz plik hs_err_pid*.log w folderze projektu dla wiecej informacji.
    echo.
    pause
)
