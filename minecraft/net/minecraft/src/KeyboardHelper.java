package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class KeyboardHelper {
    private static boolean[] keyStates = new boolean[1024];
    private static List<KeyEvent> eventQueue = new ArrayList<>();
    
    private static int currentEventKey = 0;
    private static boolean currentEventState = false;
    private static char currentEventChar = '\0';

    private static class KeyEvent {
        int key;
        boolean state;
        char character;

        public KeyEvent(int key, boolean state, char character) {
            this.key = key;
            this.state = state;
            this.character = character;
        }
    }

    public static void setKeyState(int key, boolean state) {
        if (key >= 0 && key < keyStates.length) {
            keyStates[key] = state;
        }
    }

    public static void addEvent(int key, boolean state) {
        char c = '\0';
        
        // --- FIX NA BACKSPACE ---
        // Jeśli wciśnięto Backspace (kod 14 w starym MC) i jest stan wciśnięcia (true),
        // to ustawiamy znak na '\b' (kod ASCII 8), który oznacza usuwanie.
        if (state && key == 14) { 
            c = '\b'; 
        }
        // ------------------------

        synchronized(eventQueue) {
            eventQueue.add(new KeyEvent(key, state, c));
        }
    }

    public static void addCharEvent(char c) {
        synchronized(eventQueue) {
            // Próbujemy dokleić znak do ostatniego zdarzenia klawisza (np. wciśnięto 'A')
            if (!eventQueue.isEmpty()) {
                KeyEvent last = eventQueue.get(eventQueue.size() - 1);
                // Jeśli ostatnie zdarzenie to wciśnięcie klawisza i nie ma jeszcze znaku...
                if (last.state && last.character == '\0') {
                    last.character = c;
                    return;
                }
            }
            // Jeśli nie ma do czego dokleić, tworzymy osobne zdarzenie znaku
            eventQueue.add(new KeyEvent(0, true, c));
        }
    }

    // Stub dla kompatybilności
    public static void enableRepeatEvents(boolean enable) { }

    public static String getKeyName(int key) {
        if (key >= 65 && key <= 90) return String.valueOf((char)key);
        if (key >= 48 && key <= 57) return String.valueOf((char)key);
        switch (key) {
            case 14: return "BACKSPACE"; // Dodane dla jasności
            case 28: return "ENTER";
            case 15: return "TAB";
            case 57: return "SPACE";
            case 1: return "ESCAPE";
            case 42: return "LSHIFT";
            case 29: return "LCTRL";
            case 56: return "LALT";
            default: return "KEY_" + key;
        }
    }

    public static boolean isKeyDown(int key) {
        if (key < 0 || key >= keyStates.length) return false;
        return keyStates[key];
    }

    public static boolean next() {
        synchronized(eventQueue) {
            if (eventQueue.isEmpty()) {
                return false;
            }
            KeyEvent event = eventQueue.remove(0);
            currentEventKey = event.key;
            currentEventState = event.state;
            currentEventChar = event.character;
            return true;
        }
    }

    public static int getEventKey() {
        return currentEventKey;
    }

    public static boolean getEventKeyState() {
        return currentEventState;
    }

    public static char getEventCharacter() {
        return currentEventChar;
    }

    public static void destroy() {
        keyStates = new boolean[1024];
        eventQueue.clear();
    }
}