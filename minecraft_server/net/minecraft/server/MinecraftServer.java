 

package net.minecraft.server;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderServer;
import net.minecraft.src.ConsoleCommandHandler;
import net.minecraft.src.ConsoleLogManager;
import net.minecraft.src.ConvertProgressUpdater;
import net.minecraft.src.EntityTracker;
import net.minecraft.src.ICommandListener;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.PropertyManager;
import net.minecraft.src.SaveConverterMcRegion;
import net.minecraft.src.SaveOldDir;
import net.minecraft.src.ServerCommand;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.ServerGUI;
import net.minecraft.src.ThreadCommandReader;
import net.minecraft.src.ThreadServerApplication;
import net.minecraft.src.ThreadSleepForever;
import net.minecraft.src.Vec3D;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinecraftServer
    implements Runnable, ICommandListener
{

    public MinecraftServer()
    {
        field_6025_n = true;
        field_6032_g = false;
        deathTime = 0;
        field_9010_p = new ArrayList();
        commands = Collections.synchronizedList(new ArrayList());
        new ThreadSleepForever(this);
    }

    private boolean func_6008_d() throws IOException
    {
        field_22005_o = new ConsoleCommandHandler(this);
        ThreadCommandReader threadcommandreader = new ThreadCommandReader(this);
        threadcommandreader.setDaemon(true);
        threadcommandreader.start();
        ConsoleLogManager.init();
        logger.info("Starting minecraft server version Beta 1.3");
        if(Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
        {
            logger.warning("**** NOT ENOUGH RAM!");
            logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }
        logger.info("Loading properties");
        propertyManagerObj = new PropertyManager(new File("server.properties"));
        String s = propertyManagerObj.getStringProperty("server-ip", "");
        onlineMode = propertyManagerObj.getBooleanProperty("online-mode", true);
        noAnimals = propertyManagerObj.getBooleanProperty("spawn-animals", true);
        field_9011_n = propertyManagerObj.getBooleanProperty("pvp", true);
        InetAddress inetaddress = null;
        if(s.length() > 0)
        {
            inetaddress = InetAddress.getByName(s);
        }
        int i = propertyManagerObj.getIntProperty("server-port", 25565);
        logger.info((new StringBuilder()).append("Starting Minecraft server on ").append(s.length() != 0 ? s : "*").append(":").append(i).toString());
        field_6036_c = new NetworkListenThread(this, inetaddress, i);
        if(!onlineMode)
        {
            logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            logger.warning("The server will make no attempt to authenticate usernames. Beware.");
            logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            logger.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
        }
        configManager = new ServerConfigurationManager(this);
        field_6028_k = new EntityTracker(this);
        long l = System.nanoTime();
        String s1 = propertyManagerObj.getStringProperty("level-name", "world");
        logger.info((new StringBuilder()).append("Preparing level \"").append(s1).append("\"").toString());
        func_6017_c(new SaveConverterMcRegion(new File(".")), s1);
        logger.info((new StringBuilder()).append("Done (").append(System.nanoTime() - l).append("ns)! For help, type \"help\" or \"?\"").toString());
        return true;
    }

    private void func_6017_c(ISaveFormat isaveformat, String s)
    {
        if(isaveformat.func_22102_a(s))
        {
            logger.info("Converting map!");
            isaveformat.func_22101_a(s, new ConvertProgressUpdater(this));
        }
        logger.info("Preparing start region");
        worldMngr = new WorldServer(this, new SaveOldDir(new File("."), s, true), s, propertyManagerObj.getBooleanProperty("hellworld", false) ? -1 : 0);
        worldMngr.addWorldAccess(new WorldManager(this));
        worldMngr.difficultySetting = propertyManagerObj.getBooleanProperty("spawn-monsters", true) ? 1 : 0;
        worldMngr.func_21116_a(propertyManagerObj.getBooleanProperty("spawn-monsters", true), noAnimals);
        configManager.setPlayerManager(worldMngr);
        char c = '\304';
        long l = System.currentTimeMillis();
        ChunkCoordinates chunkcoordinates = worldMngr.func_22078_l();
        for(int i = -c; i <= c && field_6025_n; i += 16)
        {
            for(int j = -c; j <= c && field_6025_n; j += 16)
            {
                long l1 = System.currentTimeMillis();
                if(l1 < l)
                {
                    l = l1;
                }
                if(l1 > l + 1000L)
                {
                    int k = (c * 2 + 1) * (c * 2 + 1);
                    int i1 = (i + c) * (c * 2 + 1) + (j + 1);
                    func_6019_a("Preparing spawn area", (i1 * 100) / k);
                    l = l1;
                }
                worldMngr.field_20911_y.loadChunk(chunkcoordinates.field_22216_a + i >> 4, chunkcoordinates.field_528_b + j >> 4);
                while(worldMngr.func_6156_d() && field_6025_n) ;
            }

        }

        func_6011_e();
    }

    private void func_6019_a(String s, int i)
    {
        field_9013_i = s;
        field_9012_j = i;
        logger.info((new StringBuilder()).append(s).append(": ").append(i).append("%").toString());
    }

    private void func_6011_e()
    {
        field_9013_i = null;
        field_9012_j = 0;
    }

    private void saveServerWorld()
    {
        logger.info("Saving chunks");
        worldMngr.saveWorld(true, null);
        worldMngr.func_22088_r();
    }

    private void func_6013_g()
    {
        logger.info("Stopping server");
        if(configManager != null)
        {
            configManager.savePlayerStates();
        }
        if(worldMngr != null)
        {
            saveServerWorld();
        }
    }

    public void func_6016_a()
    {
        field_6025_n = false;
    }

    public void run()
    {
        try
        {
            if(func_6008_d())
            {
                long l = System.currentTimeMillis();
                long l1 = 0L;
                while(field_6025_n) 
                {
                    long l2 = System.currentTimeMillis();
                    long l3 = l2 - l;
                    if(l3 > 2000L)
                    {
                        logger.warning("Can't keep up! Did the system time change, or is the server overloaded?");
                        l3 = 2000L;
                    }
                    if(l3 < 0L)
                    {
                        logger.warning("Time ran backwards! Did the system time change?");
                        l3 = 0L;
                    }
                    l1 += l3;
                    l = l2;
                    if(worldMngr.func_22084_q())
                    {
                        func_6018_h();
                        l1 = 0L;
                    } else
                    {
                        while(l1 > 50L) 
                        {
                            l1 -= 50L;
                            func_6018_h();
                        }
                    }
                    Thread.sleep(1L);
                }
            } else
            {
                while(field_6025_n) 
                {
                    commandLineParser();
                    try
                    {
                        Thread.sleep(10L);
                    }
                    catch(InterruptedException interruptedexception)
                    {
                        interruptedexception.printStackTrace();
                    }
                }
            }
        }
        catch(Throwable throwable1)
        {
            throwable1.printStackTrace();
            logger.log(Level.SEVERE, "Unexpected exception", throwable1);
            while(field_6025_n) 
            {
                commandLineParser();
                try
                {
                    Thread.sleep(10L);
                }
                catch(InterruptedException interruptedexception1)
                {
                    interruptedexception1.printStackTrace();
                }
            }
        }
    	finally
    	{
            try
            {
            func_6013_g();
              this.field_6032_g = true;
            }
            catch (Throwable localThrowable4)
            {
              localThrowable4.printStackTrace();
            }
            finally
            {
              System.exit(0);
            }
        }
    }

    private void func_6018_h()
    {
        ArrayList arraylist = new ArrayList();
        for(Iterator iterator = field_6037_b.keySet().iterator(); iterator.hasNext();)
        {
            String s = (String)iterator.next();
            int k = ((Integer)field_6037_b.get(s)).intValue();
            if(k > 0)
            {
                field_6037_b.put(s, Integer.valueOf(k - 1));
            } else
            {
                arraylist.add(s);
            }
        }

        for(int i = 0; i < arraylist.size(); i++)
        {
            field_6037_b.remove(arraylist.get(i));
        }

        AxisAlignedBB.clearBoundingBoxPool();
        Vec3D.initialize();
        deathTime++;
        if(deathTime % 20 == 0)
        {
            configManager.sendPacketToAllPlayers(new Packet4UpdateTime(worldMngr.func_22080_k()));
        }
        worldMngr.func_22077_g();
        while(worldMngr.func_6156_d()) ;
        worldMngr.func_459_b();
        field_6036_c.func_715_a();
        configManager.func_637_b();
        field_6028_k.func_607_a();
        for(int j = 0; j < field_9010_p.size(); j++)
        {
            ((IUpdatePlayerListBox)field_9010_p.get(j)).update();
        }

        try
        {
            commandLineParser();
        }
        catch(Exception exception)
        {
            logger.log(Level.WARNING, "Unexpected exception while parsing console command", exception);
        }
    }

    public void addCommand(String s, ICommandListener icommandlistener)
    {
        commands.add(new ServerCommand(s, icommandlistener));
    }

    public void commandLineParser()
    {
        ServerCommand servercommand;
        for(; commands.size() > 0; field_22005_o.func_22114_a(servercommand))
        {
            servercommand = (ServerCommand)commands.remove(0);
        }

    }

    public void func_6022_a(IUpdatePlayerListBox iupdateplayerlistbox)
    {
        field_9010_p.add(iupdateplayerlistbox);
    }

    public static void main(String args[])
    {
        try
        {
            MinecraftServer minecraftserver = new MinecraftServer();
            if(!GraphicsEnvironment.isHeadless() && (args.length <= 0 || !args[0].equals("nogui")))
            {
                ServerGUI.initGui(minecraftserver);
            }
            (new ThreadServerApplication("Server thread", minecraftserver)).start();
        }
        catch(Exception exception)
        {
            logger.log(Level.SEVERE, "Failed to start the minecraft server", exception);
        }
    }

    public File getFile(String s)
    {
        return new File(s);
    }

    public void log(String s)
    {
        logger.info(s);
    }

    public String getUsername()
    {
        return "CONSOLE";
    }

    public static boolean func_6015_a(MinecraftServer minecraftserver)
    {
        return minecraftserver.field_6025_n;
    }

    public static Logger logger = Logger.getLogger("Minecraft");
    public static HashMap field_6037_b = new HashMap();
    public NetworkListenThread field_6036_c;
    public PropertyManager propertyManagerObj;
    public WorldServer worldMngr;
    public ServerConfigurationManager configManager;
    private ConsoleCommandHandler field_22005_o;
    private boolean field_6025_n;
    public boolean field_6032_g;
    int deathTime;
    public String field_9013_i;
    public int field_9012_j;
    private java.util.List field_9010_p;
    private java.util.List commands;
    public EntityTracker field_6028_k;
    public boolean onlineMode;
    public boolean noAnimals;
    public boolean field_9011_n;

}
