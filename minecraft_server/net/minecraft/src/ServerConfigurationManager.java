package net.minecraft.src;
 

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class ServerConfigurationManager
{

    public ServerConfigurationManager(MinecraftServer minecraftserver)
    {
        playerEntities = new ArrayList();
        field_9252_f = new HashSet();
        bannedIPs = new HashSet();
        ops = new HashSet();
        field_22174_i = new HashSet();
        mcServer = minecraftserver;
        bannedPlayersFile = minecraftserver.getFile("banned-players.txt");
        ipBanFile = minecraftserver.getFile("banned-ips.txt");
        opFile = minecraftserver.getFile("ops.txt");
        field_22173_m = minecraftserver.getFile("white-list.txt");
        playerManagerObj = new PlayerManager(minecraftserver);
        maxPlayers = minecraftserver.propertyManagerObj.getIntProperty("max-players", 20);
        field_22172_o = minecraftserver.propertyManagerObj.getBooleanProperty("white-list", false);
        readBannedPlayers();
        loadBannedList();
        loadOps();
        func_22168_m();
        writeBannedPlayers();
        saveBannedList();
        saveOps();
        func_22160_n();
    }

    public void setPlayerManager(WorldServer worldserver)
    {
        playerNBTManagerObj = worldserver.func_22075_m().func_22090_d();
    }

    public int func_640_a()
    {
        return playerManagerObj.func_542_b();
    }

    public void playerLoggedIn(EntityPlayerMP entityplayermp)
    {
        playerEntities.add(entityplayermp);
        playerNBTManagerObj.readPlayerData(entityplayermp);
        mcServer.worldMngr.field_20911_y.loadChunk((int)entityplayermp.posX >> 4, (int)entityplayermp.posZ >> 4);
        for(; mcServer.worldMngr.getCollidingBoundingBoxes(entityplayermp, entityplayermp.boundingBox).size() != 0; entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0D, entityplayermp.posZ)) { }
        mcServer.worldMngr.entityJoinedWorld(entityplayermp);
        playerManagerObj.addPlayer(entityplayermp);
    }

    public void func_613_b(EntityPlayerMP entityplayermp)
    {
        playerManagerObj.func_543_c(entityplayermp);
    }

    public void playerLoggedOut(EntityPlayerMP entityplayermp)
    {
        playerNBTManagerObj.writePlayerData(entityplayermp);
        mcServer.worldMngr.func_22085_d(entityplayermp);
        playerEntities.remove(entityplayermp);
        playerManagerObj.removePlayer(entityplayermp);
    }

    public EntityPlayerMP login(NetLoginHandler netloginhandler, String s, String s1)
    {
        if(field_9252_f.contains(s.trim().toLowerCase()))
        {
            netloginhandler.kickUser("You are banned from this server!");
            return null;
        }
        if(!func_22166_g(s))
        {
            netloginhandler.kickUser("You are not white-listed on this server!");
            return null;
        }
        String s2 = netloginhandler.netManager.getRemoteAddress().toString();
        s2 = s2.substring(s2.indexOf("/") + 1);
        s2 = s2.substring(0, s2.indexOf(":"));
        if(bannedIPs.contains(s2))
        {
            netloginhandler.kickUser("Your IP address is banned from this server!");
            return null;
        }
        if(playerEntities.size() >= maxPlayers)
        {
            netloginhandler.kickUser("The server is full!");
            return null;
        }
        for(int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)playerEntities.get(i);
            if(entityplayermp.username.equalsIgnoreCase(s))
            {
                entityplayermp.field_20908_a.func_43_c("You logged in from another location");
            }
        }

        return new EntityPlayerMP(mcServer, mcServer.worldMngr, s, new ItemInWorldManager(mcServer.worldMngr));
    }

    public EntityPlayerMP func_9242_d(EntityPlayerMP entityplayermp)
    {
        mcServer.field_6028_k.func_9238_a(entityplayermp);
        mcServer.field_6028_k.func_610_b(entityplayermp);
        playerManagerObj.removePlayer(entityplayermp);
        playerEntities.remove(entityplayermp);
        mcServer.worldMngr.func_22073_e(entityplayermp);
        EntityPlayerMP entityplayermp1 = new EntityPlayerMP(mcServer, mcServer.worldMngr, entityplayermp.username, new ItemInWorldManager(mcServer.worldMngr));
        entityplayermp1.entityId = entityplayermp.entityId;
        entityplayermp1.field_20908_a = entityplayermp.field_20908_a;
        mcServer.worldMngr.field_20911_y.loadChunk((int)entityplayermp1.posX >> 4, (int)entityplayermp1.posZ >> 4);
        for(; mcServer.worldMngr.getCollidingBoundingBoxes(entityplayermp1, entityplayermp1.boundingBox).size() != 0; entityplayermp1.setPosition(entityplayermp1.posX, entityplayermp1.posY + 1.0D, entityplayermp1.posZ)) { }
        entityplayermp1.field_20908_a.sendPacket(new Packet9());
        entityplayermp1.field_20908_a.func_41_a(entityplayermp1.posX, entityplayermp1.posY, entityplayermp1.posZ, entityplayermp1.rotationYaw, entityplayermp1.rotationPitch);
        playerManagerObj.addPlayer(entityplayermp1);
        mcServer.worldMngr.entityJoinedWorld(entityplayermp1);
        playerEntities.add(entityplayermp1);
        entityplayermp1.func_20057_k();
        entityplayermp1.func_22068_s();
        return entityplayermp1;
    }

    public void func_637_b()
    {
        playerManagerObj.func_538_a();
    }

    public void func_622_a(int i, int j, int k)
    {
        playerManagerObj.func_535_a(i, j, k);
    }

    public void sendPacketToAllPlayers(Packet packet)
    {
        for(int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)playerEntities.get(i);
            entityplayermp.field_20908_a.sendPacket(packet);
        }

    }

    public String getPlayerList()
    {
        String s = "";
        for(int i = 0; i < playerEntities.size(); i++)
        {
            if(i > 0)
            {
                s = (new StringBuilder()).append(s).append(", ").toString();
            }
            s = (new StringBuilder()).append(s).append(((EntityPlayerMP)playerEntities.get(i)).username).toString();
        }

        return s;
    }

    public void func_22159_a(String s)
    {
        field_9252_f.add(s.toLowerCase());
        writeBannedPlayers();
    }

    public void func_22161_b(String s)
    {
        field_9252_f.remove(s.toLowerCase());
        writeBannedPlayers();
    }

    private void readBannedPlayers()
    {
        try
        {
            field_9252_f.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(bannedPlayersFile));
            for(String s = ""; (s = bufferedreader.readLine()) != null;)
            {
                field_9252_f.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to load ban list: ").append(exception).toString());
        }
    }

    private void writeBannedPlayers()
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new FileWriter(bannedPlayersFile, false));
            String s;
            for(Iterator iterator = field_9252_f.iterator(); iterator.hasNext(); printwriter.println(s))
            {
                s = (String)iterator.next();
            }

            printwriter.close();
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to save ban list: ").append(exception).toString());
        }
    }

    public void func_22162_c(String s)
    {
        bannedIPs.add(s.toLowerCase());
        saveBannedList();
    }

    public void func_22163_d(String s)
    {
        bannedIPs.remove(s.toLowerCase());
        saveBannedList();
    }

    private void loadBannedList()
    {
        try
        {
            bannedIPs.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(ipBanFile));
            for(String s = ""; (s = bufferedreader.readLine()) != null;)
            {
                bannedIPs.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to load ip ban list: ").append(exception).toString());
        }
    }

    private void saveBannedList()
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new FileWriter(ipBanFile, false));
            String s;
            for(Iterator iterator = bannedIPs.iterator(); iterator.hasNext(); printwriter.println(s))
            {
                s = (String)iterator.next();
            }

            printwriter.close();
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to save ip ban list: ").append(exception).toString());
        }
    }

    public void func_22164_e(String s)
    {
        ops.add(s.toLowerCase());
        saveOps();
    }

    public void func_22165_f(String s)
    {
        ops.remove(s.toLowerCase());
        saveOps();
    }

    private void loadOps()
    {
        try
        {
            ops.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(opFile));
            for(String s = ""; (s = bufferedreader.readLine()) != null;)
            {
                ops.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to load ip ban list: ").append(exception).toString());
        }
    }

    private void saveOps()
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new FileWriter(opFile, false));
            String s;
            for(Iterator iterator = ops.iterator(); iterator.hasNext(); printwriter.println(s))
            {
                s = (String)iterator.next();
            }

            printwriter.close();
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to save ip ban list: ").append(exception).toString());
        }
    }

    private void func_22168_m()
    {
        try
        {
            field_22174_i.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(field_22173_m));
            for(String s = ""; (s = bufferedreader.readLine()) != null;)
            {
                field_22174_i.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to load white-list: ").append(exception).toString());
        }
    }

    private void func_22160_n()
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new FileWriter(field_22173_m, false));
            String s;
            for(Iterator iterator = field_22174_i.iterator(); iterator.hasNext(); printwriter.println(s))
            {
                s = (String)iterator.next();
            }

            printwriter.close();
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to save white-list: ").append(exception).toString());
        }
    }

    public boolean func_22166_g(String s)
    {
        s = s.trim().toLowerCase();
        return !field_22172_o || ops.contains(s) || field_22174_i.contains(s);
    }

    public boolean isOp(String s)
    {
        return ops.contains(s.trim().toLowerCase());
    }

    public EntityPlayerMP getPlayerEntity(String s)
    {
        for(int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)playerEntities.get(i);
            if(entityplayermp.username.equalsIgnoreCase(s))
            {
                return entityplayermp;
            }
        }

        return null;
    }

    public void sendChatMessageToPlayer(String s, String s1)
    {
        EntityPlayerMP entityplayermp = getPlayerEntity(s);
        if(entityplayermp != null)
        {
            entityplayermp.field_20908_a.sendPacket(new Packet3Chat(s1));
        }
    }

    public void func_12022_a(double d, double d1, double d2, double d3, Packet packet)
    {
        for(int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)playerEntities.get(i);
            double d4 = d - entityplayermp.posX;
            double d5 = d1 - entityplayermp.posY;
            double d6 = d2 - entityplayermp.posZ;
            if(d4 * d4 + d5 * d5 + d6 * d6 < d3 * d3)
            {
                entityplayermp.field_20908_a.sendPacket(packet);
            }
        }

    }

    public void sendChatMessageToAllPlayers(String s)
    {
        Packet3Chat packet3chat = new Packet3Chat(s);
        for(int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)playerEntities.get(i);
            if(isOp(entityplayermp.username))
            {
                entityplayermp.field_20908_a.sendPacket(packet3chat);
            }
        }

    }

    public boolean sendPacketToPlayer(String s, Packet packet)
    {
        EntityPlayerMP entityplayermp = getPlayerEntity(s);
        if(entityplayermp != null)
        {
            entityplayermp.field_20908_a.sendPacket(packet);
            return true;
        } else
        {
            return false;
        }
    }

    public void savePlayerStates()
    {
        for(int i = 0; i < playerEntities.size(); i++)
        {
            playerNBTManagerObj.writePlayerData((EntityPlayer)playerEntities.get(i));
        }

    }

    public void sentTileEntityToPlayer(int i, int j, int k, TileEntity tileentity)
    {
    }

    public void func_22169_k(String s)
    {
        field_22174_i.add(s);
        func_22160_n();
    }

    public void func_22170_l(String s)
    {
        field_22174_i.remove(s);
        func_22160_n();
    }

    public Set func_22167_e()
    {
        return field_22174_i;
    }

    public void func_22171_f()
    {
        func_22168_m();
    }

    public static Logger logger = Logger.getLogger("Minecraft");
    public List playerEntities;
    private MinecraftServer mcServer;
    private PlayerManager playerManagerObj;
    private int maxPlayers;
    private Set field_9252_f;
    private Set bannedIPs;
    private Set ops;
    private Set field_22174_i;
    private File bannedPlayersFile;
    private File ipBanFile;
    private File opFile;
    private File field_22173_m;
    private IPlayerFileData playerNBTManagerObj;
    private boolean field_22172_o;

}
