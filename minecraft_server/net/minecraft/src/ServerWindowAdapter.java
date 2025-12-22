package net.minecraft.src;
 

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.minecraft.server.MinecraftServer;

final class ServerWindowAdapter extends WindowAdapter
{

    ServerWindowAdapter(MinecraftServer minecraftserver)
    {
        mcServer = minecraftserver;
//        super();
    }

    public void windowClosing(WindowEvent windowevent)
    {
        mcServer.func_6016_a();
        while(!mcServer.field_6032_g) 
        {
            try
            {
                Thread.sleep(100L);
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
        System.exit(0);
    }

    final MinecraftServer mcServer; /* synthetic field */
}
