package net.minecraft.src;
 

import java.util.*;
import net.minecraft.server.MinecraftServer;

public class WorldServer extends World
{

    public WorldServer(MinecraftServer minecraftserver, ISaveHandler isavehandler, String s, int i)
    {
        super(isavehandler, s, (new Random()).nextLong(), WorldProvider.func_4091_a(i));
        field_819_z = false;
        field_20912_E = new MCHashTable();
        field_6160_D = minecraftserver;
    }

    public void updateEntityWithOptionalForce(Entity entity, boolean flag)
    {
        if(!field_6160_D.noAnimals && ((entity instanceof EntityAnimals) || (entity instanceof EntityWaterMob)))
        {
            entity.setEntityDead();
        }
        if(entity.riddenByEntity == null || !(entity.riddenByEntity instanceof EntityPlayer))
        {
            super.updateEntityWithOptionalForce(entity, flag);
        }
    }

    public void func_12017_b(Entity entity, boolean flag)
    {
        super.updateEntityWithOptionalForce(entity, flag);
    }

    protected IChunkProvider func_22086_b()
    {
        IChunkLoader ichunkloader = worldFile.func_22092_a(worldProvider);
        field_20911_y = new ChunkProviderServer(this, ichunkloader, worldProvider.getChunkProvider());
        return field_20911_y;
    }

    public List func_532_d(int i, int j, int k, int l, int i1, int j1)
    {
        ArrayList arraylist = new ArrayList();
        for(int k1 = 0; k1 < loadedTileEntityList.size(); k1++)
        {
            TileEntity tileentity = (TileEntity)loadedTileEntityList.get(k1);
            if(tileentity.xCoord >= i && tileentity.yCoord >= j && tileentity.zCoord >= k && tileentity.xCoord < l && tileentity.yCoord < i1 && tileentity.zCoord < j1)
            {
                arraylist.add(tileentity);
            }
        }

        return arraylist;
    }

    public boolean func_6157_a(EntityPlayer entityplayer, int i, int j, int k)
    {
        int l = (int)MathHelper.abs(i - savePath.func_22184_c());
        int i1 = (int)MathHelper.abs(k - savePath.func_22189_e());
        if(l > i1)
        {
            i1 = l;
        }
        return i1 > 16 || field_6160_D.configManager.isOp(entityplayer.username);
    }

    protected void obtainEntitySkin(Entity entity)
    {
        super.obtainEntitySkin(entity);
        field_20912_E.addKey(entity.entityId, entity);
    }

    protected void releaseEntitySkin(Entity entity)
    {
        super.releaseEntitySkin(entity);
        field_20912_E.removeObject(entity.entityId);
    }

    public Entity func_6158_a(int i)
    {
        return (Entity)field_20912_E.lookup(i);
    }

    public void func_9206_a(Entity entity, byte byte0)
    {
        Packet38 packet38 = new Packet38(entity.entityId, byte0);
        field_6160_D.field_6028_k.func_609_a(entity, packet38);
    }

    public Explosion newExplosion(Entity entity, double d, double d1, double d2, 
            float f, boolean flag)
    {
        Explosion explosion = super.newExplosion(entity, d, d1, d2, f, flag);
        field_6160_D.configManager.func_12022_a(d, d1, d2, 64D, new Packet60(d, d1, d2, f, explosion.destroyedBlockPositions));
        return explosion;
    }

    public void playNoteAt(int i, int j, int k, int l, int i1)
    {
        super.playNoteAt(i, j, k, l, i1);
        field_6160_D.configManager.func_12022_a(i, j, k, 64D, new Packet54(i, j, k, l, i1));
    }

    public void func_22088_r()
    {
        worldFile.func_22093_e();
    }

    public ChunkProviderServer field_20911_y;
    public boolean field_819_z;
    public boolean field_816_A;
    private MinecraftServer field_6160_D;
    private MCHashTable field_20912_E;
}
