package net.minecraft.src;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import java.util.LinkedList;
import java.util.Queue;

public class KeyboardLwjgl3Helper
{
    private static long windowHandle;
    private static final Queue<KeyEvent> eventQueue = new LinkedList<>();
    private static KeyEvent currentEvent;
    
    // Prosta klasa do przechowywania zdarzenia
    private static class KeyEvent {
        int key;
        char character;
        boolean state; // true = pressed, false = released
        
        public KeyEvent(int k, char c, boolean s) {
            key = k;
            character = c;
            state = s;
        }
    }

    public static void setWindowHandle(long handle)
    {
        windowHandle = handle;
        
        // Callback dla klawiszy (sterowanie: WSAD, ESC itp.)
        GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_RELEASE) {
                // Mapowanie klawiszy specjalnych na znaki (np. Backspace)
                char c = 0;
                if (key == 259) c = 8; // Backspace (kluczowe dla usuwania tekstu!)
                if (key == 256) c = 27; // Escape
                
                eventQueue.add(new KeyEvent(key, c, action == GLFW.GLFW_PRESS));
            }
        });

        // Callback dla znaków (wpisywanie tekstu: seedy, nazwy światów)
        GLFW.glfwSetCharCallback(handle, (window, codepoint) -> {
            // Dodajemy zdarzenie wciśnięcia z konkretnym znakiem
            eventQueue.add(new KeyEvent(0, (char)codepoint, true));
        });
    }
    
    public static boolean next()
    {
        if (eventQueue.isEmpty()) {
            currentEvent = null;
            return false;
        }
        currentEvent = eventQueue.poll();
        return true;
    }
    
    public static int getEventKey()
    {
        return currentEvent == null ? 0 : currentEvent.key;
    }
    
    public static char getEventCharacter()
    {
        return currentEvent == null ? 0 : currentEvent.character;
    }
    
    public static boolean getEventKeyState()
    {
        return currentEvent == null ? false : currentEvent.state;
    }
    
    public static boolean isKeyDown(int key)
    {
        if (windowHandle == 0) return false;
        return GLFW.glfwGetKey(windowHandle, key) == GLFW.GLFW_PRESS;
    }
    
    public static void destroy()
    {
        // Cleanup callbacks
        if (windowHandle != 0) {
            org.lwjgl.glfw.Callbacks.glfwFreeCallbacks(windowHandle);
        }
    }
}