package net.minecraft.src;

import org.lwjgl.glfw.GLFW;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;

public class MouseLwjgl3Helper
{
    private static long windowHandle;
    private static boolean[] buttonStates = new boolean[8];
    private static boolean[] prevButtonStates = new boolean[8];
    private static int eventButton;
    private static boolean eventButtonState;
    private static int eventX;
    private static int eventY;
    private static boolean hasNextEvent;
    private static int eventDWheel;
    
    // Pola do przechowywania ostatniej znanej pozycji (do obliczania dx/dy)
    private static int lastX;
    private static int lastY;
    
    public static void setWindowHandle(long handle)
    {
        windowHandle = handle;
    }
    
    // Ta metoda nie jest już potrzebna, bo liczymy skalę dynamicznie w next(), 
    // ale zostawiam ją pustą, żeby nie psuć kompatybilności z Minecraft.java
    public static void setScale(int physicalW, int physicalH, int logicalW, int logicalH)
    {
    }
    
    public static boolean isButtonDown(int button)
    {
        if(windowHandle == 0) return false;
        return GLFW.glfwGetMouseButton(windowHandle, button) == GLFW.GLFW_PRESS;
    }
    
    public static boolean next()
    {
        if(windowHandle == 0) return false;
        
        // Sprawdzamy zmianę stanu przycisków
        for(int i = 0; i < buttonStates.length; i++)
        {
            boolean currentState = GLFW.glfwGetMouseButton(windowHandle, i) == GLFW.GLFW_PRESS;
            if(currentState != prevButtonStates[i])
            {
                eventButton = i;
                eventButtonState = currentState;
                prevButtonStates[i] = currentState;
                
                updateCursorPos();
                
                hasNextEvent = true;
                return true;
            }
        }
        
        // Tutaj można by dodać obsługę ruchu myszki jako zdarzenia, 
        // ale w Beta 1.3 zwykle wystarcza sprawdzanie przycisków w pętli zdarzeń.
        
        hasNextEvent = false;
        return false;
    }
    
    private static void updateCursorPos()
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer xPos = stack.mallocDouble(1);
            DoubleBuffer yPos = stack.mallocDouble(1);
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer fbWidth = stack.mallocInt(1);
            IntBuffer fbHeight = stack.mallocInt(1);

            // 1. Pobierz pozycję kursora (w jednostkach ekranowych)
            GLFW.glfwGetCursorPos(windowHandle, xPos, yPos);
            double mouseX = xPos.get(0);
            double mouseY = yPos.get(0);

            // 2. Pobierz rozmiar okna (w jednostkach ekranowych)
            GLFW.glfwGetWindowSize(windowHandle, width, height);
            int w = width.get(0);
            int h = height.get(0);

            // 3. Pobierz rozmiar bufora ramki (w pikselach - to co widzi OpenGL)
            GLFW.glfwGetFramebufferSize(windowHandle, fbWidth, fbHeight);
            int fw = fbWidth.get(0);
            int fh = fbHeight.get(0);

            if (w > 0 && h > 0) {
                // Oblicz skalę DPI (np. 1.0, 1.25, 2.0 itd.)
                double scaleX = (double)fw / (double)w;
                double scaleY = (double)fh / (double)h;

                // Przelicz pozycję myszki na fizyczne piksele OpenGL
                int pixelX = (int)(mouseX * scaleX);
                int pixelY = (int)(mouseY * scaleY);

                // --- KLUCZOWA NAPRAWA ---
                // Odwracamy oś Y (OpenGL/LWJGL2 ma 0 na dole, GLFW ma 0 na górze)
                eventX = pixelX;
                eventY = fh - pixelY - 1; 
            } else {
                eventX = (int)mouseX;
                eventY = (int)mouseY;
            }
        }
    }
    
    public static int getEventButton()
    {
        return eventButton;
    }
    
    public static boolean getEventButtonState()
    {
        return eventButtonState;
    }
    
    public static int getEventDWheel()
    {
        return 0; // Scroll wymagałby callbacków, na razie 0
    }
    
    public static void destroy()
    {
        windowHandle = 0;
    }
    
    // Metody getX/getY zwracają bieżącą pozycję (niezależnie od zdarzenia)
    public static int getX()
    {
        updateCursorPos(); // Aktualizuj globalne zmienne eventX/eventY na bieżącą pozycję
        return eventX;
    }
    
    public static int getY()
    {
        updateCursorPos();
        return eventY;
    }
    
    public static int getEventX()
    {
        return eventX;
    }
    
    public static int getEventY()
    {
        return eventY;
    }
    
    public static int getDX()
    {
        int dx = eventX - lastX;
        lastX = eventX;
        return dx;
    }
    
    public static int getDY()
    {
        int dy = eventY - lastY;
        lastY = eventY;
        return dy;
    }
}