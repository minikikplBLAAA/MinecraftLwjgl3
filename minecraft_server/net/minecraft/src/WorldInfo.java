package net.minecraft.src;
 

import java.util.List;

public class WorldInfo
{

    public WorldInfo(NBTTagCompound nbttagcompound)
    {
        field_22202_a = nbttagcompound.getLong("RandomSeed");
        field_22201_b = nbttagcompound.getInteger("SpawnX");
        field_22200_c = nbttagcompound.getInteger("SpawnY");
        field_22199_d = nbttagcompound.getInteger("SpawnZ");
        field_22198_e = nbttagcompound.getLong("Time");
        field_22197_f = nbttagcompound.getLong("LastPlayed");
        field_22196_g = nbttagcompound.getLong("SizeOnDisk");
        field_22193_j = nbttagcompound.getString("LevelName");
        field_22192_k = nbttagcompound.getInteger("version");
        if(nbttagcompound.hasKey("Player"))
        {
            field_22195_h = nbttagcompound.getCompoundTag("Player");
            field_22194_i = field_22195_h.getInteger("Dimension");
        }
    }

    public WorldInfo(long l, String s)
    {
        field_22202_a = l;
        field_22193_j = s;
    }

    public WorldInfo(WorldInfo worldinfo)
    {
        field_22202_a = worldinfo.field_22202_a;
        field_22201_b = worldinfo.field_22201_b;
        field_22200_c = worldinfo.field_22200_c;
        field_22199_d = worldinfo.field_22199_d;
        field_22198_e = worldinfo.field_22198_e;
        field_22197_f = worldinfo.field_22197_f;
        field_22196_g = worldinfo.field_22196_g;
        field_22195_h = worldinfo.field_22195_h;
        field_22194_i = worldinfo.field_22194_i;
        field_22193_j = worldinfo.field_22193_j;
        field_22192_k = worldinfo.field_22192_k;
    }

    public NBTTagCompound func_22185_a()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        func_22176_a(nbttagcompound, field_22195_h);
        return nbttagcompound;
    }

    public NBTTagCompound func_22183_a(List list)
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
        func_22176_a(nbttagcompound, nbttagcompound1);
        return nbttagcompound;
    }

    private void func_22176_a(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1)
    {
        nbttagcompound.setLong("RandomSeed", field_22202_a);
        nbttagcompound.setInteger("SpawnX", field_22201_b);
        nbttagcompound.setInteger("SpawnY", field_22200_c);
        nbttagcompound.setInteger("SpawnZ", field_22199_d);
        nbttagcompound.setLong("Time", field_22198_e);
        nbttagcompound.setLong("SizeOnDisk", field_22196_g);
        nbttagcompound.setLong("LastPlayed", System.currentTimeMillis());
        nbttagcompound.setString("LevelName", field_22193_j);
        nbttagcompound.setInteger("version", field_22192_k);
        if(nbttagcompound1 != null)
        {
            nbttagcompound.setCompoundTag("Player", nbttagcompound1);
        }
    }

    public long func_22187_b()
    {
        return field_22202_a;
    }

    public int func_22184_c()
    {
        return field_22201_b;
    }

    public int func_22179_d()
    {
        return field_22200_c;
    }

    public int func_22189_e()
    {
        return field_22199_d;
    }

    public long func_22186_f()
    {
        return field_22198_e;
    }

    public long func_22182_g()
    {
        return field_22196_g;
    }

    public int func_22178_h()
    {
        return field_22194_i;
    }

    public void func_22180_a(long l)
    {
        field_22198_e = l;
    }

    public void func_22177_b(long l)
    {
        field_22196_g = l;
    }

    public void func_22181_a(int i, int j, int k)
    {
        field_22201_b = i;
        field_22200_c = j;
        field_22199_d = k;
    }

    public void func_22190_a(String s)
    {
        field_22193_j = s;
    }

    public int func_22188_i()
    {
        return field_22192_k;
    }

    public void func_22191_a(int i)
    {
        field_22192_k = i;
    }

    private long field_22202_a;
    private int field_22201_b;
    private int field_22200_c;
    private int field_22199_d;
    private long field_22198_e;
    private long field_22197_f;
    private long field_22196_g;
    private NBTTagCompound field_22195_h;
    private int field_22194_i;
    private String field_22193_j;
    private int field_22192_k;
}
