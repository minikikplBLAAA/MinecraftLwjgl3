package net.minecraft.client;

import net.minecraft.src.*;
import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback; // Dodany import
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public abstract class Minecraft
    implements Runnable
{

    public long window;
    private int[] keyCodes = new int[512];

    public Minecraft(Component component, Canvas canvas, MinecraftApplet minecraftapplet, int i, int j, boolean flag)
    {
        fullscreen = false;
        timer = new Timer(20F);
        session = null;
        hideQuitButton = true;
        isWorldLoaded = false;
        currentScreen = null;
        loadingScreen = new LoadingScreenRenderer(this);
        entityRenderer = new EntityRenderer(this);
        ticksRan = 0;
        field_6282_S = 0;
        field_6307_v = false;
        unusedModelBiped = new ModelBiped(0.0F);
        objectMouseOver = null;
        sndManager = new SoundManager();
        textureWaterFX = new TextureWaterFX();
        textureLavaFX = new TextureLavaFX();
        running = true;
        debug = "";
        isTakingScreenshot = false;
        prevFrameTime = -1L;
        field_6289_L = false;
        field_6302_aa = 0;
        isFancyGraphics = false;
        systemTime = System.currentTimeMillis();
        field_6300_ab = 0;
        field_9235_U = j;
        fullscreen = flag;
        mcApplet = minecraftapplet;
        new ThreadSleepForever(this, "Timer hack thread");
        mcCanvas = canvas;
        displayWidth = i;
        displayHeight = j;
        fullscreen = flag;
        if(minecraftapplet == null || "true".equals(minecraftapplet.getParameter("stand-alone")))
        {
            hideQuitButton = false;
        }
        field_21900_a = this;
    }

    public abstract void displayUnexpectedThrowable(UnexpectedThrowable unexpectedthrowable);

    public void setServer(String s, int i)
    {
        serverName = s;
        serverPort = i;
    }

    public void startGame() throws Exception
    {
        try {
            System.out.println("Inicjalizacja GLFW...");
            if (!GLFW.glfwInit()) {
                throw new RuntimeException("Nie mozna zainicjalizowac GLFW");
            }
            
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
            // FIX: Zmiana na TRUE, żeby dało się zmieniać rozmiar okna
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
            
            // OpenGL 4.6 Compatibility Profile
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
            GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_DEBUG, GLFW.GLFW_TRUE);
            
            System.out.println("Tworzenie okna...");
        
        if(fullscreen)
        {
            GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, 8);
            GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, 8);
            GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, 8);
            GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, 60);
            window = GLFW.glfwCreateWindow(displayWidth, displayHeight, "Minecraft Minecraft Beta 1.3_01 Ultimate edition", GLFW.glfwGetPrimaryMonitor(), 0);
        } else
        {
            window = GLFW.glfwCreateWindow(displayWidth, displayHeight, "Minecraft Minecraft Beta 1.3_01 Ultimate edition", 0, 0);
        }
        
        if(window == 0)
        {
            throw new RuntimeException("Failed to create window");
        }

        // --- FIX: Callbacki do zmiany rozmiaru i klawiatury ---
        
        // 1. Obsługa zmiany rozmiaru okna
        GLFW.glfwSetWindowSizeCallback(window, (windowHandle, width, height) -> {
            this.displayWidth = width;
            this.displayHeight = height;
            this.resize(width, height);
        });

        // 2. Inicjalizacja kodów klawiszy (mapowanie GLFW -> Minecraft)
        initKeyCodes();

        // 3. Obsługa wciskania klawiszy (KeyCallback)
        GLFW.glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) -> {
            // Przekazujemy zdarzenia do KeyboardHelpera
            // Musisz upewnić się, że Twój KeyboardHelper ma metodę add/addEvent
            if (key >= 0 && key < keyCodes.length) {
                 int minecraftKey = keyCodes[key];
                 boolean state = (action != GLFW.GLFW_RELEASE);
                 
                 // Przekaż raw state do helpera (żeby działało isKeyDown)
                 KeyboardHelper.setKeyState(minecraftKey, state); 
                 
                 // Dodaj zdarzenie do kolejki (żeby działało .next())
                 if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_RELEASE || action == GLFW.GLFW_REPEAT) {
                     KeyboardHelper.addEvent(minecraftKey, state); 
                 }
            }
        });

        // 4. Obsługa wpisywania tekstu (CharCallback) - TO NAPRAWI SEEDY I NAZWY
        GLFW.glfwSetCharCallback(window, (windowHandle, codepoint) -> {
            // Przekazujemy wpisany znak do KeyboardHelpera
            KeyboardHelper.addCharEvent((char)codepoint);
        });

        // -----------------------------------------------------
        
        System.out.println("Inicjalizacja kontekstu OpenGL...");
        GLFW.glfwMakeContextCurrent(window);
        
        GL.createCapabilities();
        System.out.println("OpenGL capabilities utworzone pomyślnie");
        
        // Fix dla Intel/AMD - VAO
        int vao = org.lwjgl.opengl.GL30.glGenVertexArrays();
        org.lwjgl.opengl.GL30.glBindVertexArray(vao);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClearDepth(1.0);
        
        GLFW.glfwShowWindow(window);
        
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        
        // initKeyCodes() wywołane wcześniej dla callbacków, ale tu nie zaszkodzi
        RenderManager.instance.itemRenderer = new ItemRenderer(this);
        mcDataDir = getMinecraftDir();
        field_22008_V = new SaveConverterMcRegion(new File(mcDataDir, "saves"));
        gameSettings = new GameSettings(this, mcDataDir);
        texturePackList = new TexturePackList(this, mcDataDir);
        renderEngine = new RenderEngine(texturePackList, gameSettings);
        fontRenderer = new FontRenderer(gameSettings, "/font/default.png", renderEngine);
        
        renderGlobal = new RenderGlobal(this, renderEngine);
        effectRenderer = new EffectRenderer(null, renderEngine);
        
        ingameGUI = new GuiIngame(this);
        playerController = new PlayerController(this);
        mouseHelper = new MouseHelper(mcCanvas);
        
        MouseLwjgl3Helper.setWindowHandle(window);
        
        checkGLError("Pre startup");
        GL11.glClear(16640);
        GL11.glViewport(0, 0, displayWidth, displayHeight);
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2912 /*GL_FOG*/);
        
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, renderEngine.getTexture("/title/mojang.png"));
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0xffffff);
        tessellator.addVertexWithUV(0.0D, displayHeight, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(displayWidth, displayHeight, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(516, 0.1F);
        
        GLFW.glfwSwapBuffers(window);
        displayGuiScreen(null);
    }

    public void func_6274_a(int i, int j, int k, int l, int i1, int j1)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(i + 0, j + j1, 0.0D, (float)(k + 0) * f, (float)(l + j1) * f1);
        tessellator.addVertexWithUV(i + i1, j + j1, 0.0D, (float)(k + i1) * f, (float)(l + j1) * f1);
        tessellator.addVertexWithUV(i + i1, j + 0, 0.0D, (float)(k + i1) * f, (float)(l + 0) * f1);
        tessellator.addVertexWithUV(i + 0, j + 0, 0.0D, (float)(k + 0) * f, (float)(l + 0) * f1);
        tessellator.draw();
    }

    public static File getMinecraftDir()
    {
        if(minecraftDir == null)
        {
            minecraftDir = getAppDir("minecraft");
        }
        return minecraftDir;
    }

    public static File getAppDir(String s)
    {
        String s1 = System.getProperty("user.home", ".");
        File file;
        switch(EnumOSMappingHelper.enumOSMappingArray[getOs().ordinal()])
        {
        case 1: 
        case 2: 
            file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
            break;
        case 3: 
            String s2 = System.getenv("APPDATA");
            if(s2 != null)
            {
                file = new File(s2, (new StringBuilder()).append(".").append(s).append('/').toString());
            } else
            {
                file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
            }
            break;
        case 4: 
            file = new File(s1, (new StringBuilder()).append("Library/Application Support/").append(s).toString());
            break;
        default:
            file = new File(s1, (new StringBuilder()).append(s).append('/').toString());
            break;
        }
        if(!file.exists() && !file.mkdirs())
        {
            throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(file).toString());
        } else
        {
            return file;
        }
    }

    private static EnumOS2 getOs()
    {
        String s = System.getProperty("os.name").toLowerCase();
        if(s.contains("win")) return EnumOS2.windows;
        if(s.contains("mac")) return EnumOS2.macos;
        if(s.contains("solaris")) return EnumOS2.solaris;
        if(s.contains("sunos")) return EnumOS2.solaris;
        if(s.contains("linux")) return EnumOS2.linux;
        if(s.contains("unix")) return EnumOS2.linux;
        return EnumOS2.unknown;
    }

    public ISaveFormat func_22004_c()
    {
        return field_22008_V;
    }

    public void displayGuiScreen(GuiScreen guiscreen)
    {
        if(currentScreen instanceof GuiUnused) return;
        if(currentScreen != null) currentScreen.onGuiClosed();
        if(guiscreen == null && theWorld == null) guiscreen = new GuiMainMenu();
        else if(guiscreen == null && thePlayer.health <= 0) guiscreen = new GuiGameOver();
        
        currentScreen = guiscreen;
        if(guiscreen != null)
        {
            func_6273_f();
            ScaledResolution scaledresolution = new ScaledResolution(displayWidth, displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiscreen.setWorldAndResolution(this, i, j);
            field_6307_v = false;
        } else
        {
            func_6259_e();
        }
    }

    private void checkGLError(String s) { }

    public void shutdownMinecraftApplet()
    {
        if(mcApplet != null) mcApplet.clearApplet();
        try { if(downloadResourcesThread != null) downloadResourcesThread.closeMinecraft(); } catch(Exception exception) { }
        try
        {
            System.out.println("Stopping!");
            try { changeWorld1(null); } catch(Throwable throwable) { }
            try { GLAllocation.deleteTexturesAndDisplayLists(); } catch(Throwable throwable1) { }
            sndManager.closeMinecraft();
            MouseLwjgl3Helper.destroy();
            KeyboardHelper.destroy();
        }
        finally
        {
            GLFW.glfwDestroyWindow(window);
            System.out.println("Inicjalizacja zakończona - uruchamianie gry...");
        }
    }

    public void run()
    {
        System.out.println("Wchodzę do metody run()...");
        running = true;
        try
        {
            startGame();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            displayUnexpectedThrowable(new UnexpectedThrowable("Failed to start game", exception));
            return;
        }
        try
        {
            long l = System.currentTimeMillis();
            int i = 0;
            while(running && (mcApplet == null || mcApplet.isActive())) 
            {
                AxisAlignedBB.clearBoundingBoxPool();
                Vec3D.initialize();
                if(mcCanvas == null && GLFW.glfwWindowShouldClose(window))
                {
                    shutdown();
                }
                if(isWorldLoaded && theWorld != null)
                {
                    float f = timer.renderPartialTicks;
                    timer.updateTimer();
                    timer.renderPartialTicks = f;
                } else
                {
                    timer.updateTimer();
                }
                long l1 = System.nanoTime();
                for(int j = 0; j < timer.elapsedTicks; j++)
                {
                    ticksRan++;
                    try
                    {
                        runTick();
                        continue;
                    }
                    catch(MinecraftException minecraftexception)
                    {
                        theWorld = null;
                    }
                    changeWorld1(null);
                    displayGuiScreen(new GuiConflictWarning());
                }

                long l2 = System.nanoTime() - l1;
                checkGLError("Pre render");
                sndManager.func_338_a(thePlayer, timer.renderPartialTicks);
                GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
                if(theWorld != null && !theWorld.multiplayerWorld)
                {
                    theWorld.func_6465_g();
                }
                if(theWorld != null && theWorld.multiplayerWorld)
                {
                    theWorld.func_6465_g();
                }
                if(gameSettings.limitFramerate)
                {
                    Thread.sleep(5L);
                }
                if(!KeyboardHelper.isKeyDown(65))
                {
                    GLFW.glfwPollEvents();
                }
                if(!field_6307_v)
                {
                    if(playerController != null)
                    {
                        playerController.setPartialTime(timer.renderPartialTicks);
                    }
                    entityRenderer.func_4136_b(timer.renderPartialTicks);
                }
                if(GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_FOCUSED) == 0)
                {
                    if(fullscreen)
                    {
                        toggleFullscreen();
                    }
                    Thread.sleep(10L);
                }
                
                if(gameSettings.field_22276_A)
                {
                    displayDebugInfo(l2);
                } else
                {
                    // Render GUI in main menu when no world is loaded
                    if(currentScreen != null && theWorld == null)
                    {
                        // 1. Pobieramy fizyczne wymiary BUFORA (do rysowania OpenGL)
                        int[] fw = new int[1];
                        int[] fh = new int[1];
                        GLFW.glfwGetFramebufferSize(window, fw, fh);
                        int physicalWidth = fw[0];
                        int physicalHeight = fh[0];

                        // 2. Pobieramy logiczne wymiary OKNA (do myszki)
                        int[] ww = new int[1];
                        int[] wh = new int[1];
                        GLFW.glfwGetWindowSize(window, ww, wh);
                        int windowW = ww[0];
                        int windowH = wh[0];

                        if (physicalWidth > 0 && physicalHeight > 0 && windowW > 0 && windowH > 0) {
                            GL11.glViewport(0, 0, physicalWidth, physicalHeight);

                            // 3. Pobieramy logiczne wymiary GUI Minecrafta
                            ScaledResolution scaledresolution = new ScaledResolution(displayWidth, displayHeight);
                            int logicalWidth = scaledresolution.getScaledWidth();
                            int logicalHeight = scaledresolution.getScaledHeight();

                            // --- FIX MYSZKI I SKALOWANIA ---
                            // Pobieramy surową pozycję myszki z GLFW (w jednostkach okna)
                            double[] mx = new double[1];
                            double[] my = new double[1];
                            GLFW.glfwGetCursorPos(window, mx, my);
                            
                            // Skalujemy pozycję myszki względem logicznego GUI
                            // Używamy windowW/windowH, bo kursor jest w tych jednostkach!
                            int mouseX = (int)(mx[0] * (double)logicalWidth / (double)windowW);
                            int mouseY = (int)(my[0] * (double)logicalHeight / (double)windowH);
                            
                            // Przekazujemy poprawne dane do helpera (dla klikania)
                            // Helper potrzebuje wiedzieć, że jego inputy też trzeba tak skalować
                            MouseLwjgl3Helper.setScale(windowW, windowH, logicalWidth, logicalHeight);
                            // -------------------------------

                            // 4. Render GUI
                            GL11.glClearColor(0.2F, 0.3F, 0.8F, 1.0F);
                            GL11.glClear(16640);

                            GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
                            GL11.glLoadIdentity();
                            GL11.glOrtho(0.0D, (double)logicalWidth, (double)logicalHeight, 0.0D, -1000.0D, 3000.0D);
                            
                            GL11.glMatrixMode(5888 /*GL_MODELVIEW*/);
                            GL11.glLoadIdentity();

                            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
                            GL11.glDisable(2912 /*GL_FOG*/);
                            GL11.glDisable(2896 /*GL_LIGHTING*/);
                            GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
                            
                            GL11.glEnable(3042 /*GL_BLEND*/);
                            GL11.glBlendFunc(770, 771);
                            
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                            GL11.glAlphaFunc(516, 0.1F);

                            try {
                                // Teraz przekazujemy idealnie obliczone mouseX/mouseY
                                currentScreen.drawScreen(mouseX, mouseY, timer.renderPartialTicks);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                            GLFW.glfwSwapBuffers(window);
                        }
                    }
                }
                
                prevFrameTime = System.nanoTime();
                Thread.yield();
                if(KeyboardHelper.isKeyDown(65))
                {
                    GLFW.glfwPollEvents();
                }
                screenshotListener();
                if(mcCanvas != null && !fullscreen && (mcCanvas.getWidth() != displayWidth || mcCanvas.getHeight() != displayHeight))
                {
                    displayWidth = mcCanvas.getWidth();
                    displayHeight = mcCanvas.getHeight();
                    if(displayWidth <= 0) displayWidth = 1;
                    if(displayHeight <= 0) displayHeight = 1;
                    resize(displayWidth, displayHeight);
                }
                checkGLError("Post render");
                i++;
                isWorldLoaded = !isMultiplayerWorld() && currentScreen != null && currentScreen.doesGuiPauseGame();
                while(System.currentTimeMillis() >= l + 1000L) 
                {
                    debug = (new StringBuilder()).append(i).append(" fps, ").append(WorldRenderer.chunksUpdated).append(" chunk updates").toString();
                    WorldRenderer.chunksUpdated = 0;
                    l += 1000L;
                    i = 0;
                }
            }
        }
        catch(MinecraftError minecrafterror) { }
        catch(Throwable throwable)
        {
            theWorld = null;
            throwable.printStackTrace();
            displayUnexpectedThrowable(new UnexpectedThrowable("Unexpected error", throwable));
        }
        finally
        {
            shutdownMinecraftApplet();
        }
    }

    private void screenshotListener()
    {
        if(KeyboardHelper.isKeyDown(60))
        {
            if(!isTakingScreenshot)
            {
                isTakingScreenshot = true;
                if(KeyboardHelper.isKeyDown(42))
                {
                    ingameGUI.addChatMessage(func_21001_a(minecraftDir, displayWidth, displayHeight, 36450, 17700));
                } else
                {
                    ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, displayWidth, displayHeight));
                }
            }
        } else
        {
            isTakingScreenshot = false;
        }
    }

    private String func_21001_a(File file, int i, int j, int k, int l)
    {
        try
        {
            ByteBuffer bytebuffer = MemoryUtil.memAlloc(i * j * 3);
            ScreenShotHelper screenshothelper = new ScreenShotHelper(file, k, l, j);
            double d = (double)k / (double)i;
            double d1 = (double)l / (double)j;
            double d2 = d <= d1 ? d1 : d;
            for(int i1 = ((l - 1) / j) * j; i1 >= 0; i1 -= j)
            {
                for(int j1 = 0; j1 < k; j1 += i)
                {
                    int k1 = i;
                    int l1 = j;
                    GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, renderEngine.getTexture("/terrain.png"));
                    double d3 = ((double)(k - i) / 2D) * 2D - (double)(j1 * 2);
                    double d4 = ((double)(l - j) / 2D) * 2D - (double)(i1 * 2);
                    d3 /= i;
                    d4 /= j;
                    entityRenderer.func_21152_a(d2, d3, d4);
                    entityRenderer.renderWorld(1.0F);
                    entityRenderer.func_21151_b();
                    GLFW.glfwPollEvents();
                    try { Thread.sleep(10L); } catch(InterruptedException interruptedexception) { }
                    GLFW.glfwPollEvents();
                    bytebuffer.clear();
                    GL11.glPixelStorei(3333 /*GL_PACK_ALIGNMENT*/, 1);
                    GL11.glPixelStorei(3317 /*GL_UNPACK_ALIGNMENT*/, 1);
                    GL11.glReadPixels(0, 0, k1, l1, 32992 /*GL_BGR_EXT*/, 5121 /*GL_UNSIGNED_BYTE*/, bytebuffer);
                    screenshothelper.func_21189_a(bytebuffer, j1, i1, k1, l1);
                }
                screenshothelper.func_21191_a();
            }
            return screenshothelper.func_21190_b();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return (new StringBuilder()).append("Failed to save image: ").append(exception).toString();
        }
    }

    private void displayDebugInfo(long l)
    {
        long l1 = 0xfe502aL;
        if(prevFrameTime == -1L)
        {
            prevFrameTime = System.nanoTime();
        }
        long l2 = System.nanoTime();
        tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = l;
        frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = l2 - prevFrameTime;
        prevFrameTime = l2;
        GL11.glClear(256);
        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, displayWidth, displayHeight, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(7);
        int i = (int)(l1 / 0x30d40L);
        tessellator.setColorOpaque_I(0x20000000);
        tessellator.addVertex(0.0D, displayHeight - i, 0.0D);
        tessellator.addVertex(0.0D, displayHeight, 0.0D);
        tessellator.addVertex(frameTimes.length, displayHeight, 0.0D);
        tessellator.addVertex(frameTimes.length, displayHeight - i, 0.0D);
        tessellator.setColorOpaque_I(0x20200000);
        tessellator.addVertex(0.0D, displayHeight - i * 2, 0.0D);
        tessellator.addVertex(0.0D, displayHeight - i, 0.0D);
        tessellator.addVertex(frameTimes.length, displayHeight - i, 0.0D);
        tessellator.addVertex(frameTimes.length, displayHeight - i * 2, 0.0D);
        tessellator.draw();
        long l3 = 0L;
        for(int j = 0; j < frameTimes.length; j++)
        {
            l3 += frameTimes[j];
        }

        int k = (int)(l3 / 0x30d40L / (long)frameTimes.length);
        tessellator.startDrawing(7);
        tessellator.setColorOpaque_I(0x20400000);
        tessellator.addVertex(0.0D, displayHeight - k, 0.0D);
        tessellator.addVertex(0.0D, displayHeight, 0.0D);
        tessellator.addVertex(frameTimes.length, displayHeight, 0.0D);
        tessellator.addVertex(frameTimes.length, displayHeight - k, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(1);
        for(int i1 = 0; i1 < frameTimes.length; i1++)
        {
            int j1 = ((i1 - numRecordedFrameTimes & frameTimes.length - 1) * 255) / frameTimes.length;
            int k1 = (j1 * j1) / 255;
            k1 = (k1 * k1) / 255;
            int i2 = (k1 * k1) / 255;
            i2 = (i2 * i2) / 255;
            if(frameTimes[i1] > l1)
            {
                tessellator.setColorOpaque_I(0xff000000 + k1 * 0x10000);
            } else
            {
                tessellator.setColorOpaque_I(0xff000000 + k1 * 256);
            }
            long l4 = frameTimes[i1] / 0x30d40L;
            long l5 = tickTimes[i1] / 0x30d40L;
            tessellator.addVertex((float)i1 + 0.5F, (float)((long)displayHeight - l4) + 0.5F, 0.0D);
            tessellator.addVertex((float)i1 + 0.5F, (float)displayHeight + 0.5F, 0.0D);
            tessellator.setColorOpaque_I(0xff000000 + k1 * 0x10000 + k1 * 256 + k1 * 1);
            tessellator.addVertex((float)i1 + 0.5F, (float)((long)displayHeight - l4) + 0.5F, 0.0D);
            tessellator.addVertex((float)i1 + 0.5F, (float)((long)displayHeight - (l4 - l5)) + 0.5F, 0.0D);
        }

        tessellator.draw();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }

    public void shutdown()
    {
        running = false;
    }

    public void func_6259_e()
    {
        if(GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_FOCUSED) == 0)
        {
            return;
        }
        if(field_6289_L)
        {
            return;
        } else
        {
            field_6289_L = true;
            mouseHelper.func_774_a();
            displayGuiScreen(null);
            field_6302_aa = ticksRan + 10000;
            return;
        }
    }

    public void func_6273_f()
    {
        if(!field_6289_L)
        {
            return;
        }
        if(thePlayer != null)
        {
            thePlayer.resetPlayerKeyState();
        }
        field_6289_L = false;
        mouseHelper.func_773_b();
    }

    public void func_6252_g()
    {
        if(currentScreen != null)
        {
            return;
        } else
        {
            displayGuiScreen(new GuiIngameMenu());
            return;
        }
    }

    private void func_6254_a(int i, boolean flag)
    {
        if(playerController.field_1064_b)
        {
            return;
        }
        if(i == 0 && field_6282_S > 0)
        {
            return;
        }
        if(flag && objectMouseOver != null && objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && i == 0)
        {
            int j = objectMouseOver.blockX;
            int k = objectMouseOver.blockY;
            int l = objectMouseOver.blockZ;
            playerController.sendBlockRemoving(j, k, l, objectMouseOver.sideHit);
            effectRenderer.func_1191_a(j, k, l, objectMouseOver.sideHit);
        } else
        {
            playerController.func_6468_a();
        }
    }

    private void clickMouse(int i)
    {
        if(i == 0 && field_6282_S > 0)
        {
            return;
        }
        if(i == 0)
        {
            thePlayer.swingItem();
        }
        boolean flag = true;
        if(objectMouseOver == null)
        {
            if(i == 0 && !(playerController instanceof PlayerControllerTest))
            {
                field_6282_S = 10;
            }
        } else
        if(objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY)
        {
            if(i == 0)
            {
                playerController.func_6472_b(thePlayer, objectMouseOver.entityHit);
            }
            if(i == 1)
            {
                playerController.func_6475_a(thePlayer, objectMouseOver.entityHit);
            }
        } else
        if(objectMouseOver.typeOfHit == EnumMovingObjectType.TILE)
        {
            int j = objectMouseOver.blockX;
            int k = objectMouseOver.blockY;
            int l = objectMouseOver.blockZ;
            int i1 = objectMouseOver.sideHit;
            Block block = Block.blocksList[theWorld.getBlockId(j, k, l)];
            if(i == 0)
            {
                theWorld.onBlockHit(j, k, l, objectMouseOver.sideHit);
                if(block != Block.bedrock || thePlayer.field_9371_f >= 100)
                {
                    playerController.clickBlock(j, k, l, objectMouseOver.sideHit);
                }
            } else
            {
                ItemStack itemstack1 = thePlayer.inventory.getCurrentItem();
                int j1 = itemstack1 == null ? 0 : itemstack1.stackSize;
                if(playerController.sendPlaceBlock(thePlayer, theWorld, itemstack1, j, k, l, i1))
                {
                    flag = false;
                    thePlayer.swingItem();
                }
                if(itemstack1 == null)
                {
                    return;
                }
                if(itemstack1.stackSize == 0)
                {
                    thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
                } else
                if(itemstack1.stackSize != j1)
                {
                    entityRenderer.itemRenderer.func_9449_b();
                }
            }
        }
        if(flag && i == 1)
        {
            ItemStack itemstack = thePlayer.inventory.getCurrentItem();
            if(itemstack != null && playerController.sendUseItem(thePlayer, theWorld, itemstack))
            {
                entityRenderer.itemRenderer.func_9450_c();
            }
        }
    }

    public void toggleFullscreen()
    {
        try
        {
            fullscreen = !fullscreen;
            System.out.println("Toggle fullscreen!");
            if(fullscreen)
            {
                GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, displayWidth, displayHeight, GLFW.GLFW_DONT_CARE);
                // Update display size from monitor
                GLFWVidMode mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
                displayWidth = mode.width();
                displayHeight = mode.height();
                if(displayWidth <= 0)
                {
                    displayWidth = 1;
                }
                if(displayHeight <= 0)
                {
                    displayHeight = 1;
                }
            } else
            {
                if(mcCanvas != null)
                {
                    displayWidth = mcCanvas.getWidth();
                    displayHeight = mcCanvas.getHeight();
                } else
                {
                    displayWidth = field_9236_T;
                    displayHeight = field_9235_U;
                }
                if(displayWidth <= 0)
                {
                    displayWidth = 1;
                }
                if(displayHeight <= 0)
                {
                    displayHeight = 1;
                }
                GLFW.glfwSetWindowMonitor(window, 0, 100, 100, field_9236_T, field_9235_U, GLFW.GLFW_DONT_CARE);
            }
            func_6273_f();
            if(fullscreen)
            {
                GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, displayWidth, displayHeight, GLFW.GLFW_DONT_CARE);
            } else
            {
                GLFW.glfwSetWindowMonitor(window, 0, 100, 100, displayWidth, displayHeight, GLFW.GLFW_DONT_CARE);
            }
            GLFW.glfwPollEvents();
            Thread.sleep(1000L);
            if(fullscreen)
            {
                func_6259_e();
            }
            if(currentScreen != null)
            {
                func_6273_f();
                resize(displayWidth, displayHeight);
            }
            System.out.println((new StringBuilder()).append("Size: ").append(displayWidth).append(", ").append(displayHeight).toString());
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void resize(int i, int j)
    {
        if(i <= 0)
        {
            i = 1;
        }
        if(j <= 0)
        {
            j = 1;
        }
        displayWidth = i;
        displayHeight = j;
        if(currentScreen != null)
        {
            ScaledResolution scaledresolution = new ScaledResolution(i, j);
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            currentScreen.setWorldAndResolution(this, k, l);
        }
    }

    private void clickMiddleMouseButton()
    {
        if(objectMouseOver != null)
        {
            int i = theWorld.getBlockId(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
            if(i == Block.grass.blockID)
            {
                i = Block.dirt.blockID;
            }
            if(i == Block.stairDouble.blockID)
            {
                i = Block.stairSingle.blockID;
            }
            if(i == Block.bedrock.blockID)
            {
                i = Block.stone.blockID;
            }
            thePlayer.inventory.setCurrentItem(i, playerController instanceof PlayerControllerTest);
        }
    }

    public void runTick()
    {
        ingameGUI.func_555_a();
        entityRenderer.getMouseOver(1.0F);
        if(thePlayer != null)
        {
            IChunkProvider ichunkprovider = theWorld.func_21118_q();
            if(ichunkprovider instanceof ChunkProviderLoadOrGenerate)
            {
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
                int j = MathHelper.floor_float((int)thePlayer.posX) >> 4;
                int i1 = MathHelper.floor_float((int)thePlayer.posZ) >> 4;
                chunkproviderloadorgenerate.func_21110_c(j, i1);
            }
        }
        if(!isWorldLoaded && theWorld != null)
        {
            playerController.updateController();
        }
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, renderEngine.getTexture("/terrain.png"));
        if(!isWorldLoaded)
        {
            renderEngine.func_1067_a();
        }
        if(currentScreen == null && thePlayer != null)
        {
            if(thePlayer.health <= 0)
            {
                displayGuiScreen(null);
            } else
            if(thePlayer.func_22051_K() && theWorld != null && theWorld.multiplayerWorld)
            {
                displayGuiScreen(new GuiSleepMP());
            }
        } else
        if(currentScreen != null && (currentScreen instanceof GuiSleepMP) && !thePlayer.func_22051_K())
        {
            displayGuiScreen(null);
        }
        if(currentScreen != null)
        {
            field_6302_aa = ticksRan + 10000;
        }
        if(currentScreen != null)
        {
            currentScreen.handleInput();
            if(currentScreen != null)
            {
                currentScreen.updateScreen();
            }
        }
        if(currentScreen == null || currentScreen.field_948_f)
        {
            do
            {
                if(!MouseLwjgl3Helper.next())
                {
                    break;
                }
                long l = System.currentTimeMillis() - systemTime;
                if(l <= 200L)
                {
                    int k = MouseLwjgl3Helper.getEventDWheel();
                    if(k != 0)
                    {
                        thePlayer.inventory.changeCurrentItem(k);
                        if(gameSettings.field_22275_C)
                        {
                            if(k > 0)
                            {
                                k = 1;
                            }
                            if(k < 0)
                            {
                                k = -1;
                            }
                            gameSettings.field_22272_F += (float)k * 0.25F;
                        }
                    }
                    if(currentScreen == null)
                    {
                        if(!field_6289_L && MouseLwjgl3Helper.getEventButtonState())
                        {
                            func_6259_e();
                        } else
                        {
                            if(MouseLwjgl3Helper.getEventButton() == 0 && MouseLwjgl3Helper.getEventButtonState())
                            {
                                clickMouse(0);
                                field_6302_aa = ticksRan;
                            }
                            if(MouseLwjgl3Helper.getEventButton() == 1 && MouseLwjgl3Helper.getEventButtonState())
                            {
                                clickMouse(1);
                                field_6302_aa = ticksRan;
                            }
                            if(MouseLwjgl3Helper.getEventButton() == 2 && MouseLwjgl3Helper.getEventButtonState())
                            {
                                clickMiddleMouseButton();
                            }
                        }
                    } else
                    if(currentScreen != null)
                    {
                        currentScreen.handleMouseInput();
                    }
                }
            } while(true);
            if(field_6282_S > 0)
            {
                field_6282_S--;
            }
            do
            {
                if(!KeyboardHelper.next())
                {
                    break;
                }
                thePlayer.handleKeyPress(KeyboardHelper.getEventKey(), KeyboardHelper.getEventKeyState());
                if(KeyboardHelper.getEventKeyState())
                {
                    if(KeyboardHelper.getEventKey() == 87)
                    {
                        toggleFullscreen();
                    } else
                    {
                        if(currentScreen != null)
                        {
                            currentScreen.handleKeyboardInput();
                        } else
                        {
                            if(KeyboardHelper.getEventKey() == 1)
                            {
                                func_6252_g();
                            }
                            if(KeyboardHelper.getEventKey() == 31 && KeyboardHelper.isKeyDown(61))
                            {
                                forceReload();
                            }
                            if(KeyboardHelper.getEventKey() == 59)
                            {
                                gameSettings.field_22277_y = !gameSettings.field_22277_y;
                            }
                            if(KeyboardHelper.getEventKey() == 61)
                            {
                                gameSettings.field_22276_A = !gameSettings.field_22276_A;
                            }
                            if(KeyboardHelper.getEventKey() == 63)
                            {
                                gameSettings.thirdPersonView = !gameSettings.thirdPersonView;
                            }
                            if(KeyboardHelper.getEventKey() == 66)
                            {
                                gameSettings.field_22274_D = !gameSettings.field_22274_D;
                            }
                            if(KeyboardHelper.getEventKey() == gameSettings.keyBindInventory.keyCode)
                            {
                                displayGuiScreen(new GuiInventory(thePlayer));
                            }
                            if(KeyboardHelper.getEventKey() == gameSettings.keyBindDrop.keyCode)
                            {
                                thePlayer.func_20060_w();
                            }
                            if(isMultiplayerWorld() && KeyboardHelper.getEventKey() == gameSettings.keyBindChat.keyCode)
                            {
                                displayGuiScreen(new GuiChat());
                            }
                        }
                        for(int i = 0; i < 9; i++)
                        {
                            if(KeyboardHelper.getEventKey() == 2 + i)
                            {
                                thePlayer.inventory.currentItem = i;
                            }
                        }

                        if(KeyboardHelper.getEventKey() == gameSettings.keyBindToggleFog.keyCode)
                        {
                            gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, !KeyboardHelper.isKeyDown(42) && !KeyboardHelper.isKeyDown(54) ? 1 : -1);
                        }
                    }
                }
            } while(true);
            if(currentScreen == null)
            {
                if(MouseLwjgl3Helper.isButtonDown(0) && (float)(ticksRan - field_6302_aa) >= timer.ticksPerSecond / 4F && field_6289_L)
                {
                    clickMouse(0);
                    field_6302_aa = ticksRan;
                }
                if(MouseLwjgl3Helper.isButtonDown(1) && (float)(ticksRan - field_6302_aa) >= timer.ticksPerSecond / 4F && field_6289_L)
                {
                    clickMouse(1);
                    field_6302_aa = ticksRan;
                }
            }
            func_6254_a(0, currentScreen == null && MouseLwjgl3Helper.isButtonDown(0) && field_6289_L);
        }
        if(theWorld != null)
        {
            if(thePlayer != null)
            {
                field_6300_ab++;
                if(field_6300_ab == 30)
                {
                    field_6300_ab = 0;
                    theWorld.func_705_f(thePlayer);
                }
            }
            theWorld.difficultySetting = gameSettings.difficulty;
            if(theWorld.multiplayerWorld)
            {
                theWorld.difficultySetting = 3;
            }
            if(!isWorldLoaded)
            {
                entityRenderer.updateRenderer();
            }
            if(!isWorldLoaded)
            {
                if(renderGlobal != null)
                {
                    renderGlobal.func_945_d();
                }
            }
            if(!isWorldLoaded)
            {
                theWorld.func_633_c();
            }
            if(!isWorldLoaded || isMultiplayerWorld())
            {
                theWorld.func_21114_a(gameSettings.difficulty > 0, true);
                theWorld.tick();
            }
            if(!isWorldLoaded && theWorld != null)
            {
                theWorld.randomDisplayUpdates(MathHelper.floor_double(thePlayer.posX), MathHelper.floor_double(thePlayer.posY), MathHelper.floor_double(thePlayer.posZ));
            }
            if(!isWorldLoaded)
            {
                if(effectRenderer != null)
                {
                    effectRenderer.renderEffects();
                }
            }
        }
        systemTime = System.currentTimeMillis();
    }

    private void forceReload()
    {
        System.out.println("FORCING RELOAD!");
        sndManager = new SoundManager();
        sndManager.loadSoundSettings(gameSettings);
        downloadResourcesThread.reloadResources();
    }

    public boolean isMultiplayerWorld()
    {
        return theWorld != null && theWorld.multiplayerWorld;
    }

    public void startWorld(String s, String s1, long l)
    {
        changeWorld1(null);
        System.gc();
        if(field_22008_V.func_22175_a(s))
        {
            func_22002_b(s, s1);
        } else
        {
            ISaveHandler isavehandler = field_22008_V.func_22174_a(s, false);
            World world = new World(isavehandler, s1, l);
            if(world.field_1033_r)
            {
                changeWorld2(world, "Generating level");
            } else
            {
                changeWorld2(world, "Loading level");
            }
        }
    }

    public void usePortal()
    {
        if(thePlayer.dimension == -1)
        {
            thePlayer.dimension = 0;
        } else
        {
            thePlayer.dimension = -1;
        }
        theWorld.setEntityDead(thePlayer);
        thePlayer.isDead = false;
        double d = thePlayer.posX;
        double d1 = thePlayer.posZ;
        double d2 = 8D;
        if(thePlayer.dimension == -1)
        {
            d /= d2;
            d1 /= d2;
            thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
            theWorld.updateEntityWithOptionalForce(thePlayer, false);
            World world = new World(theWorld, new WorldProviderHell());
            changeWorld(world, "Entering the Nether", thePlayer);
        } else
        {
            d *= d2;
            d1 *= d2;
            thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
            theWorld.updateEntityWithOptionalForce(thePlayer, false);
            World world1 = new World(theWorld, new WorldProvider());
            changeWorld(world1, "Leaving the Nether", thePlayer);
        }
        thePlayer.worldObj = theWorld;
        thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
        theWorld.updateEntityWithOptionalForce(thePlayer, false);
        (new Teleporter()).func_4107_a(theWorld, thePlayer);
    }

    public void changeWorld1(World world)
    {
        changeWorld2(world, "");
    }

    public void changeWorld2(World world, String s)
    {
        changeWorld(world, s, null);
    }

    public void changeWorld(World world, String s, EntityPlayer entityplayer)
    {
        field_22009_h = null;
        loadingScreen.printText(s);
        loadingScreen.displayLoadingString("");
        sndManager.func_331_a(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        if(theWorld != null)
        {
            theWorld.func_651_a(loadingScreen);
        }
        theWorld = world;
        if(world != null)
        {
            playerController.func_717_a(world);
            if(!isMultiplayerWorld())
            {
                if(entityplayer == null)
                {
                    thePlayer = (EntityPlayerSP)world.func_4085_a(EntityPlayerSP.class);
                }
            } else
            if(thePlayer != null)
            {
                thePlayer.preparePlayerToSpawn();
                if(world != null)
                {
                    world.entityJoinedWorld(thePlayer);
                }
            }
            if(!world.multiplayerWorld)
            {
                func_6255_d(s);
            }
            if(thePlayer == null)
            {
                thePlayer = (EntityPlayerSP)playerController.func_4087_b(world);
                thePlayer.preparePlayerToSpawn();
                playerController.flipPlayer(thePlayer);
            }
            thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
            if(renderGlobal != null)
            {
                renderGlobal.func_946_a(world);
            }
            if(effectRenderer != null)
            {
                effectRenderer.func_1188_a(world);
            }
            playerController.func_6473_b(thePlayer);
            if(entityplayer != null)
            {
                world.func_6464_c();
            }
            IChunkProvider ichunkprovider = world.func_21118_q();
            if(ichunkprovider instanceof ChunkProviderLoadOrGenerate)
            {
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
                int i = MathHelper.floor_float((int)thePlayer.posX) >> 4;
                int j = MathHelper.floor_float((int)thePlayer.posZ) >> 4;
                chunkproviderloadorgenerate.func_21110_c(i, j);
            }
            world.func_608_a(thePlayer);
            if(world.field_1033_r)
            {
                world.func_651_a(loadingScreen);
            }
            field_22009_h = thePlayer;
        } else
        {
            thePlayer = null;
        }
        System.gc();
        systemTime = 0L;
    }

    private void func_22002_b(String s, String s1)
    {
        loadingScreen.printText((new StringBuilder()).append("Converting World to ").append(field_22008_V.func_22178_a()).toString());
        loadingScreen.displayLoadingString("This may take a while :)");
        field_22008_V.func_22171_a(s, loadingScreen);
        startWorld(s, s1, 0L);
    }

    private void func_6255_d(String s)
    {
        loadingScreen.printText(s);
        loadingScreen.displayLoadingString("Building terrain");
        char c = '\200';
        int i = 0;
        int j = (c * 2) / 16 + 1;
        j *= j;
        IChunkProvider ichunkprovider = theWorld.func_21118_q();
        ChunkCoordinates chunkcoordinates = theWorld.func_22137_s();
        if(thePlayer != null)
        {
            chunkcoordinates.field_22395_a = (int)thePlayer.posX;
            chunkcoordinates.field_22396_c = (int)thePlayer.posZ;
        }
        if(ichunkprovider instanceof ChunkProviderLoadOrGenerate)
        {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
            chunkproviderloadorgenerate.func_21110_c(chunkcoordinates.field_22395_a >> 4, chunkcoordinates.field_22396_c >> 4);
        }
        
        System.out.println("Building terrain...");

        for(int k = -c; k <= c; k += 16)
        {
            for(int l = -c; l <= c; l += 16)
            {
                loadingScreen.setLoadingProgress((i++ * 100) / j);
                theWorld.getBlockId(chunkcoordinates.field_22395_a + k, 64, chunkcoordinates.field_22396_c + l);
                
                // <<< FIX 1: Zabezpieczenie przed infinite loop w oświetleniu >>>
                int lightingUpdates = 0;
                while(theWorld.func_6465_g()) 
                {
                    lightingUpdates++;
                    if(lightingUpdates > 1000) 
                    {
                        // Przerywamy, jeśli oświetlenie mieli w miejscu
                        break; 
                    }
                }
                // <<< KONIEC FIX 1 >>>

                // <<< FIX 2: Utrzymanie życia okna w LWJGL 3 >>>
                // Bez tego system uzna aplikację za "brak odpowiedzi"
                GLFW.glfwPollEvents();
                GLFW.glfwSwapBuffers(window);
                // <<< KONIEC FIX 2 >>>
            }
        }
        
        System.out.println("Teren wygenerowany.");

        // UWAGA: Usunąłem symulację świata (theWorld.func_656_j()), 
        // bo ona też często powoduje infinite loop w portach.
    }

    public void installResource(String s, File file)
    {
        int i = s.indexOf("/");
        String s1 = s.substring(0, i);
        s = s.substring(i + 1);
        if(s1.equalsIgnoreCase("sound"))
        {
            sndManager.addSound(s, file);
        } else
        if(s1.equalsIgnoreCase("newsound"))
        {
            sndManager.addSound(s, file);
        } else
        if(s1.equalsIgnoreCase("streaming"))
        {
            sndManager.addStreaming(s, file);
        } else
        if(s1.equalsIgnoreCase("music"))
        {
            sndManager.addMusic(s, file);
        } else
        if(s1.equalsIgnoreCase("newmusic"))
        {
            sndManager.addMusic(s, file);
        }
    }

    public OpenGlCapsChecker func_6251_l()
    {
        return glCapabilities;
    }

    public String func_6241_m()
    {
        return renderGlobal.func_953_b();
    }

    public String func_6262_n()
    {
        return renderGlobal.func_957_c();
    }

    public String func_21002_o()
    {
        return theWorld.func_21119_g();
    }

    public String func_6245_o()
    {
        return (new StringBuilder()).append("P: ").append(effectRenderer.getStatistics()).append(". T: ").append(theWorld.func_687_d()).toString();
    }

    public void respawn()
    {
        if(!theWorld.worldProvider.canRespawnHere())
        {
            usePortal();
        }
        ChunkCoordinates chunkcoordinates = theWorld.func_22137_s();
        IChunkProvider ichunkprovider = theWorld.func_21118_q();
        if(ichunkprovider instanceof ChunkProviderLoadOrGenerate)
        {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
            chunkproviderloadorgenerate.func_21110_c(chunkcoordinates.field_22395_a >> 4, chunkcoordinates.field_22396_c >> 4);
        }
        theWorld.setSpawnLocation();
        theWorld.updateEntityList();
        int i = 0;
        if(thePlayer != null)
        {
            i = thePlayer.entityId;
            theWorld.setEntityDead(thePlayer);
        }
        field_22009_h = null;
        thePlayer = (EntityPlayerSP)playerController.func_4087_b(theWorld);
        field_22009_h = thePlayer;
        thePlayer.preparePlayerToSpawn();
        playerController.flipPlayer(thePlayer);
        theWorld.func_608_a(thePlayer);
        thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
        thePlayer.entityId = i;
        thePlayer.func_6420_o();
        playerController.func_6473_b(thePlayer);
        func_6255_d("Respawning");
        if(currentScreen instanceof GuiGameOver)
        {
            displayGuiScreen(null);
        }
    }

    public static void func_6269_a(String s, String s1)
    {
        startMainThread(s, s1, null);
    }

    public static void startMainThread(String s, String s1, String s2)
    {
        boolean flag = false;
        String s3 = s;
        // Disable AWT Frame/Canvas for LWJGL 3 - use direct GLFW window
        Frame frame = null;
        Canvas canvas = null;
        MinecraftImpl minecraftimpl = new MinecraftImpl(frame, canvas, null, 854, 480, flag, frame);
        Thread thread = new Thread(minecraftimpl, "Minecraft main thread");
        thread.setPriority(10);
        minecraftimpl.minecraftUri = "www.minecraft.net";
        if(s3 != null && s1 != null)
        {
            minecraftimpl.session = new Session(s3, s1);
        } else
        {
            minecraftimpl.session = new Session((new StringBuilder()).append("Player").append(System.currentTimeMillis() % 1000L).toString(), "");
        }
        if(s2 != null)
        {
            String as[] = s2.split(":");
            minecraftimpl.setServer(as[0], Integer.parseInt(as[1]));
        }
        // Skip frame operations for LWJGL 3 - no AWT window
        // frame.setVisible(true);
        // frame.addWindowListener(new GameWindowListener(minecraftimpl, thread));
        thread.start();
    }

    public NetClientHandler func_20001_q()
    {
        if(thePlayer instanceof EntityClientPlayerMP)
        {
            return ((EntityClientPlayerMP)thePlayer).sendQueue;
        } else
        {
            return null;
        }
    }

    private void initKeyCodes()
    {
        keyCodes[GLFW.GLFW_KEY_SPACE] = 32;
        keyCodes[GLFW.GLFW_KEY_APOSTROPHE] = 39;
        keyCodes[GLFW.GLFW_KEY_COMMA] = 44;
        keyCodes[GLFW.GLFW_KEY_MINUS] = 45;
        keyCodes[GLFW.GLFW_KEY_PERIOD] = 46;
        keyCodes[GLFW.GLFW_KEY_SLASH] = 47;
        keyCodes[GLFW.GLFW_KEY_0] = 48;
        keyCodes[GLFW.GLFW_KEY_1] = 49;
        keyCodes[GLFW.GLFW_KEY_2] = 50;
        keyCodes[GLFW.GLFW_KEY_3] = 51;
        keyCodes[GLFW.GLFW_KEY_4] = 52;
        keyCodes[GLFW.GLFW_KEY_5] = 53;
        keyCodes[GLFW.GLFW_KEY_6] = 54;
        keyCodes[GLFW.GLFW_KEY_7] = 55;
        keyCodes[GLFW.GLFW_KEY_8] = 56;
        keyCodes[GLFW.GLFW_KEY_9] = 57;
        keyCodes[GLFW.GLFW_KEY_SEMICOLON] = 59;
        keyCodes[GLFW.GLFW_KEY_EQUAL] = 61;
        keyCodes[GLFW.GLFW_KEY_A] = 65;
        keyCodes[GLFW.GLFW_KEY_B] = 66;
        keyCodes[GLFW.GLFW_KEY_C] = 67;
        keyCodes[GLFW.GLFW_KEY_D] = 68;
        keyCodes[GLFW.GLFW_KEY_E] = 69;
        keyCodes[GLFW.GLFW_KEY_F] = 70;
        keyCodes[GLFW.GLFW_KEY_G] = 71;
        keyCodes[GLFW.GLFW_KEY_H] = 72;
        keyCodes[GLFW.GLFW_KEY_I] = 73;
        keyCodes[GLFW.GLFW_KEY_J] = 74;
        keyCodes[GLFW.GLFW_KEY_K] = 75;
        keyCodes[GLFW.GLFW_KEY_L] = 76;
        keyCodes[GLFW.GLFW_KEY_M] = 77;
        keyCodes[GLFW.GLFW_KEY_N] = 78;
        keyCodes[GLFW.GLFW_KEY_O] = 79;
        keyCodes[GLFW.GLFW_KEY_P] = 80;
        keyCodes[GLFW.GLFW_KEY_Q] = 81;
        keyCodes[GLFW.GLFW_KEY_R] = 82;
        keyCodes[GLFW.GLFW_KEY_S] = 83;
        keyCodes[GLFW.GLFW_KEY_T] = 84;
        keyCodes[GLFW.GLFW_KEY_U] = 85;
        keyCodes[GLFW.GLFW_KEY_V] = 86;
        keyCodes[GLFW.GLFW_KEY_W] = 87;
        keyCodes[GLFW.GLFW_KEY_X] = 88;
        keyCodes[GLFW.GLFW_KEY_Y] = 89;
        keyCodes[GLFW.GLFW_KEY_Z] = 90;
        keyCodes[GLFW.GLFW_KEY_LEFT_BRACKET] = 91;
        keyCodes[GLFW.GLFW_KEY_BACKSLASH] = 92;
        keyCodes[GLFW.GLFW_KEY_RIGHT_BRACKET] = 93;
        keyCodes[GLFW.GLFW_KEY_GRAVE_ACCENT] = 96;
        keyCodes[GLFW.GLFW_KEY_ESCAPE] = 256;
        keyCodes[GLFW.GLFW_KEY_ENTER] = 257;
        keyCodes[GLFW.GLFW_KEY_TAB] = 258;
        keyCodes[GLFW.GLFW_KEY_BACKSPACE] = 259;
        keyCodes[GLFW.GLFW_KEY_INSERT] = 260;
        keyCodes[GLFW.GLFW_KEY_DELETE] = 261;
        keyCodes[GLFW.GLFW_KEY_RIGHT] = 262;
        keyCodes[GLFW.GLFW_KEY_LEFT] = 263;
        keyCodes[GLFW.GLFW_KEY_DOWN] = 264;
        keyCodes[GLFW.GLFW_KEY_UP] = 265;
        keyCodes[GLFW.GLFW_KEY_PAGE_UP] = 266;
        keyCodes[GLFW.GLFW_KEY_PAGE_DOWN] = 267;
        keyCodes[GLFW.GLFW_KEY_HOME] = 268;
        keyCodes[GLFW.GLFW_KEY_END] = 269;
        keyCodes[GLFW.GLFW_KEY_CAPS_LOCK] = 280;
        keyCodes[GLFW.GLFW_KEY_SCROLL_LOCK] = 281;
        keyCodes[GLFW.GLFW_KEY_NUM_LOCK] = 282;
        keyCodes[GLFW.GLFW_KEY_PRINT_SCREEN] = 283;
        keyCodes[GLFW.GLFW_KEY_PAUSE] = 284;
        keyCodes[GLFW.GLFW_KEY_F1] = 290;
        keyCodes[GLFW.GLFW_KEY_F2] = 291;
        keyCodes[GLFW.GLFW_KEY_F3] = 292;
        keyCodes[GLFW.GLFW_KEY_F4] = 293;
        keyCodes[GLFW.GLFW_KEY_F5] = 294;
        keyCodes[GLFW.GLFW_KEY_F6] = 295;
        keyCodes[GLFW.GLFW_KEY_F7] = 296;
        keyCodes[GLFW.GLFW_KEY_F8] = 297;
        keyCodes[GLFW.GLFW_KEY_F9] = 298;
        keyCodes[GLFW.GLFW_KEY_F10] = 299;
        keyCodes[GLFW.GLFW_KEY_F11] = 300;
        keyCodes[GLFW.GLFW_KEY_F12] = 301;
        keyCodes[GLFW.GLFW_KEY_F13] = 302;
        keyCodes[GLFW.GLFW_KEY_F14] = 303;
        keyCodes[GLFW.GLFW_KEY_F15] = 304;
        keyCodes[GLFW.GLFW_KEY_F16] = 305;
        keyCodes[GLFW.GLFW_KEY_F17] = 306;
        keyCodes[GLFW.GLFW_KEY_F18] = 307;
        keyCodes[GLFW.GLFW_KEY_F19] = 308;
        keyCodes[GLFW.GLFW_KEY_F20] = 309;
        keyCodes[GLFW.GLFW_KEY_F21] = 310;
        keyCodes[GLFW.GLFW_KEY_F22] = 311;
        keyCodes[GLFW.GLFW_KEY_F23] = 312;
        keyCodes[GLFW.GLFW_KEY_F24] = 313;
        keyCodes[GLFW.GLFW_KEY_KP_0] = 320;
        keyCodes[GLFW.GLFW_KEY_KP_1] = 321;
        keyCodes[GLFW.GLFW_KEY_KP_2] = 322;
        keyCodes[GLFW.GLFW_KEY_KP_3] = 323;
        keyCodes[GLFW.GLFW_KEY_KP_4] = 324;
        keyCodes[GLFW.GLFW_KEY_KP_5] = 325;
        keyCodes[GLFW.GLFW_KEY_KP_6] = 326;
        keyCodes[GLFW.GLFW_KEY_KP_7] = 327;
        keyCodes[GLFW.GLFW_KEY_KP_8] = 328;
        keyCodes[GLFW.GLFW_KEY_KP_9] = 329;
        keyCodes[GLFW.GLFW_KEY_KP_DECIMAL] = 330;
        keyCodes[GLFW.GLFW_KEY_KP_DIVIDE] = 331;
        keyCodes[GLFW.GLFW_KEY_KP_MULTIPLY] = 332;
        keyCodes[GLFW.GLFW_KEY_KP_SUBTRACT] = 333;
        keyCodes[GLFW.GLFW_KEY_KP_ADD] = 334;
        keyCodes[GLFW.GLFW_KEY_KP_ENTER] = 335;
        keyCodes[GLFW.GLFW_KEY_KP_EQUAL] = 336;
        keyCodes[GLFW.GLFW_KEY_LEFT_SHIFT] = 340;
        keyCodes[GLFW.GLFW_KEY_LEFT_CONTROL] = 341;
        keyCodes[GLFW.GLFW_KEY_LEFT_ALT] = 342;
        keyCodes[GLFW.GLFW_KEY_LEFT_SUPER] = 343;
        keyCodes[GLFW.GLFW_KEY_RIGHT_SHIFT] = 344;
        keyCodes[GLFW.GLFW_KEY_RIGHT_CONTROL] = 345;
        keyCodes[GLFW.GLFW_KEY_RIGHT_ALT] = 346;
        keyCodes[GLFW.GLFW_KEY_RIGHT_SUPER] = 347;
        keyCodes[GLFW.GLFW_KEY_MENU] = 348;
    }

    public static void main(String args[])
    {
        String s = (new StringBuilder()).append("Player").append(System.currentTimeMillis() % 1000L).toString();
        if(args.length > 0)
        {
            s = args[0];
        }
        String s1 = "-";
        if(args.length > 1)
        {
            s1 = args[1];
        }
        func_6269_a(s, s1);
    }

    public static boolean func_22006_t()
    {
        return field_21900_a == null || !field_21900_a.gameSettings.field_22277_y;
    }

    public static boolean func_22001_u()
    {
        return field_21900_a != null && field_21900_a.gameSettings.fancyGraphics;
    }

    public static boolean func_22005_v()
    {
        return field_21900_a != null && field_21900_a.gameSettings.field_22278_j;
    }

    public static boolean func_22007_w()
    {
        return field_21900_a != null && field_21900_a.gameSettings.field_22276_A;
    }

    public boolean func_22003_b(String s)
    {
        if(!s.startsWith("/"));
        return false;
    }

    private static Minecraft field_21900_a;
    public PlayerController playerController;
    private boolean fullscreen;
    public int displayWidth;
    public int displayHeight;
    private OpenGlCapsChecker glCapabilities;
    private Timer timer;
    public World theWorld;
    public RenderGlobal renderGlobal;
    public EntityPlayerSP thePlayer;
    public EntityLiving field_22009_h;
    public EffectRenderer effectRenderer;
    public Session session;
    public String minecraftUri;
    public Canvas mcCanvas;
    public boolean hideQuitButton;
    public volatile boolean isWorldLoaded;
    public RenderEngine renderEngine;
    public FontRenderer fontRenderer;
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;
    private ThreadDownloadResources downloadResourcesThread;
    private int ticksRan;
    private int field_6282_S;
    private int field_9236_T;
    private int field_9235_U;
    public GuiIngame ingameGUI;
    public boolean field_6307_v;
    public ModelBiped unusedModelBiped;
    public MovingObjectPosition objectMouseOver;
    public GameSettings gameSettings;
    protected MinecraftApplet mcApplet;
    public SoundManager sndManager;
    public MouseHelper mouseHelper;
    public TexturePackList texturePackList;
    private File mcDataDir;
    private ISaveFormat field_22008_V;
    public static long frameTimes[] = new long[512];
    public static long tickTimes[] = new long[512];
    public static int numRecordedFrameTimes = 0;
    private String serverName;
    private int serverPort;
    private TextureWaterFX textureWaterFX;
    private TextureLavaFX textureLavaFX;
    private static File minecraftDir = null;
    public volatile boolean running;
    public String debug;
    boolean isTakingScreenshot;
    long prevFrameTime;
    public boolean field_6289_L;
    private int field_6302_aa;
    public boolean isFancyGraphics;
    long systemTime;
    private int field_6300_ab;
}