package net.minecraft.src;
 

import net.minecraft.server.MinecraftServer;

public class WorldManager
    implements IWorldAccess
{

    public WorldManager(MinecraftServer minecraftserver)
    {
        mcServer = minecraftserver;
    }

    public void spawnParticle(String s, double d, double d1, double d2, 
            double d3, double d4, double d5)
    {
    }

    public void obtainEntitySkin(Entity entity)
    {
        mcServer.field_6028_k.func_611_a(entity);
    }

    public void releaseEntitySkin(Entity entity)
    {
        mcServer.field_6028_k.func_610_b(entity);
    }

    public void playSound(String s, double d, double d1, double d2, 
            float f, float f1)
    {
    }

    public void markBlockRangeNeedsUpdate(int i, int j, int k, int l, int i1, int j1)
    {
    }

    public void updateAllRenderers()
    {
    }

    public void func_683_a(int i, int j, int k)
    {
        mcServer.configManager.func_622_a(i, j, k);
    }

    public void playRecord(String s, int i, int j, int k)
    {
    }

    public void doNothingWithTileEntity(int i, int j, int k, TileEntity tileentity)
    {
        mcServer.configManager.sentTileEntityToPlayer(i, j, k, tileentity);
    }

    private MinecraftServer mcServer;
}
