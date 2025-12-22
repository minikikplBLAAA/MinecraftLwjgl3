package net.minecraft.src;
 

import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class ConvertProgressUpdater
    implements IProgressUpdate
{

    public ConvertProgressUpdater(MinecraftServer minecraftserver)
    {
    	super();
        field_22072_a = minecraftserver;
        field_22071_b = System.currentTimeMillis();
    }

    public void func_438_a(String s)
    {
    }

    public void setLoadingProgress(int i)
    {
        if(System.currentTimeMillis() - field_22071_b >= 1000L)
        {
            field_22071_b = System.currentTimeMillis();
            MinecraftServer.logger.info((new StringBuilder()).append("Converting... ").append(i).append("%").toString());
        }
    }

    public void displayLoadingString(String s)
    {
    }

    private long field_22071_b;
    final MinecraftServer field_22072_a; /* synthetic field */
}
