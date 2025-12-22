package net.minecraft.src;
 

import java.util.List;

public class WorldInfo
{

    public WorldInfo(NBTTagCompound nbttagcompound)
    {
        field_22320_a = nbttagcompound.getLong("RandomSeed");
        field_22319_b = nbttagcompound.getInteger("SpawnX");
        field_22318_c = nbttagcompound.getInteger("SpawnY");
        field_22317_d = nbttagcompound.getInteger("SpawnZ");
        field_22316_e = nbttagcompound.getLong("Time");
        field_22315_f = nbttagcompound.getLong("LastPlayed");
        field_22314_g = nbttagcompound.getLong("SizeOnDisk");
        field_22311_j = nbttagcompound.getString("LevelName");
        field_22310_k = nbttagcompound.getInteger("version");
        if(nbttagcompound.hasKey("Player"))
        {
            field_22313_h = nbttagcompound.getCompoundTag("Player");
            field_22312_i = field_22313_h.getInteger("Dimension");
        }
    }

    public WorldInfo(long l, String s)
    {
        field_22320_a = l;
        field_22311_j = s;
    }

    public WorldInfo(WorldInfo worldinfo)
    {
        field_22320_a = worldinfo.field_22320_a;
        field_22319_b = worldinfo.field_22319_b;
        field_22318_c = worldinfo.field_22318_c;
        field_22317_d = worldinfo.field_22317_d;
        field_22316_e = worldinfo.field_22316_e;
        field_22315_f = worldinfo.field_22315_f;
        field_22314_g = worldinfo.field_22314_g;
        field_22313_h = worldinfo.field_22313_h;
        field_22312_i = worldinfo.field_22312_i;
        field_22311_j = worldinfo.field_22311_j;
        field_22310_k = worldinfo.field_22310_k;
    }

    public NBTTagCompound func_22299_a()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        func_22291_a(nbttagcompound, field_22313_h);
        return nbttagcompound;
    }

    public NBTTagCompound func_22305_a(List list)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        EntityPlayer entityplayer = null;
        NBTTagCompound nbttagcompound1 = null;
        if(list.size() > 0)
        {
            entityplayer = (EntityPlayer)list.get(0);
        }
        if(entityplayer != null)
        {
            nbttagcompound1 = new NBTTagCompound();
            entityplayer.writeToNBT(nbttagcompound1);
        }
        func_22291_a(nbttagcompound, nbttagcompound1);
        return nbttagcompound;
    }

    private void func_22291_a(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1)
    {
        nbttagcompound.setLong("RandomSeed", field_22320_a);
        nbttagcompound.setInteger("SpawnX", field_22319_b);
        nbttagcompound.setInteger("SpawnY", field_22318_c);
        nbttagcompound.setInteger("SpawnZ", field_22317_d);
        nbttagcompound.setLong("Time", field_22316_e);
        nbttagcompound.setLong("SizeOnDisk", field_22314_g);
        nbttagcompound.setLong("LastPlayed", System.currentTimeMillis());
        nbttagcompound.setString("LevelName", field_22311_j);
        nbttagcompound.setInteger("version", field_22310_k);
        if(nbttagcompound1 != null)
        {
            nbttagcompound.setCompoundTag("Player", nbttagcompound1);
        }
    }

    public long func_22288_b()
    {
        return field_22320_a;
    }

    public int func_22293_c()
    {
        return field_22319_b;
    }

    public int func_22295_d()
    {
        return field_22318_c;
    }

    public int func_22300_e()
    {
        return field_22317_d;
    }

    public long func_22304_f()
    {
        return field_22316_e;
    }

    public long func_22306_g()
    {
        return field_22314_g;
    }

    public NBTTagCompound func_22303_h()
    {
        return field_22313_h;
    }

    public int func_22290_i()
    {
        return field_22312_i;
    }

    public void func_22294_a(int i)
    {
        field_22319_b = i;
    }

    public void func_22308_b(int i)
    {
        field_22318_c = i;
    }

    public void func_22298_c(int i)
    {
        field_22317_d = i;
    }

    public void func_22307_a(long l)
    {
        field_22316_e = l;
    }

    public void func_22297_b(long l)
    {
        field_22314_g = l;
    }

    public void func_22309_a(NBTTagCompound nbttagcompound)
    {
        field_22313_h = nbttagcompound;
    }

    public void func_22292_a(int i, int j, int k)
    {
        field_22319_b = i;
        field_22318_c = j;
        field_22317_d = k;
    }

    public String func_22302_j()
    {
        return field_22311_j;
    }

    public void func_22287_a(String s)
    {
        field_22311_j = s;
    }

    public int func_22296_k()
    {
        return field_22310_k;
    }

    public void func_22289_d(int i)
    {
        field_22310_k = i;
    }

    public long func_22301_l()
    {
        return field_22315_f;
    }

    private long field_22320_a;
    private int field_22319_b;
    private int field_22318_c;
    private int field_22317_d;
    private long field_22316_e;
    private long field_22315_f;
    private long field_22314_g;
    private NBTTagCompound field_22313_h;
    private int field_22312_i;
    private String field_22311_j;
    private int field_22310_k;
}
