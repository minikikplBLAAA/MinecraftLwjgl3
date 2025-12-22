package net.minecraft.src;
 

import java.util.*;

public class EntityTrackerEntry
{

    public EntityTrackerEntry(Entity entity, int i, int j, boolean flag)
    {
        field_9221_t = 0;
        field_12020_u = false;
        field_900_j = false;
        field_899_k = new HashSet();
        field_909_a = entity;
        field_9235_d = i;
        field_9234_e = j;
        field_9220_u = flag;
        field_9233_f = MathHelper.floor_double(entity.posX * 32D);
        field_9232_g = MathHelper.floor_double(entity.posY * 32D);
        field_9231_h = MathHelper.floor_double(entity.posZ * 32D);
        field_9230_i = MathHelper.floor_float((entity.rotationYaw * 256F) / 360F);
        field_9229_j = MathHelper.floor_float((entity.rotationPitch * 256F) / 360F);
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof EntityTrackerEntry)
        {
            return ((EntityTrackerEntry)obj).field_909_a.entityId == field_909_a.entityId;
        } else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return field_909_a.entityId;
    }

    public void func_605_a(List list)
    {
        field_900_j = false;
        if(!field_12020_u || field_909_a.getDistanceSq(field_9224_q, field_9223_r, field_9222_s) > 16D)
        {
            field_9224_q = field_909_a.posX;
            field_9223_r = field_909_a.posY;
            field_9222_s = field_909_a.posZ;
            field_12020_u = true;
            field_900_j = true;
            func_601_b(list);
        }
        if(++field_9221_t % field_9234_e == 0)
        {
            int i = MathHelper.floor_double(field_909_a.posX * 32D);
            int j = MathHelper.floor_double(field_909_a.posY * 32D);
            int k = MathHelper.floor_double(field_909_a.posZ * 32D);
            int l = MathHelper.floor_float((field_909_a.rotationYaw * 256F) / 360F);
            int i1 = MathHelper.floor_float((field_909_a.rotationPitch * 256F) / 360F);
            int j1 = i - field_9233_f;
            int k1 = j - field_9232_g;
            int l1 = k - field_9231_h;
            Object obj = null;
            boolean flag = Math.abs(i) >= 8 || Math.abs(j) >= 8 || Math.abs(k) >= 8;
            boolean flag1 = Math.abs(l - field_9230_i) >= 8 || Math.abs(i1 - field_9229_j) >= 8;
            if(j1 < -128 || j1 >= 128 || k1 < -128 || k1 >= 128 || l1 < -128 || l1 >= 128)
            {
                obj = new Packet34EntityTeleport(field_909_a.entityId, i, j, k, (byte)l, (byte)i1);
            } else
            if(flag && flag1)
            {
                obj = new Packet33RelEntityMoveLook(field_909_a.entityId, (byte)j1, (byte)k1, (byte)l1, (byte)l, (byte)i1);
            } else
            if(flag)
            {
                obj = new Packet31RelEntityMove(field_909_a.entityId, (byte)j1, (byte)k1, (byte)l1);
            } else
            if(flag1)
            {
                obj = new Packet32EntityLook(field_909_a.entityId, (byte)l, (byte)i1);
            }
            if(field_9220_u)
            {
                double d = field_909_a.motionX - field_9228_k;
                double d1 = field_909_a.motionY - field_9227_l;
                double d2 = field_909_a.motionZ - field_9226_m;
                double d3 = 0.02D;
                double d4 = d * d + d1 * d1 + d2 * d2;
                if(d4 > d3 * d3 || d4 > 0.0D && field_909_a.motionX == 0.0D && field_909_a.motionY == 0.0D && field_909_a.motionZ == 0.0D)
                {
                    field_9228_k = field_909_a.motionX;
                    field_9227_l = field_909_a.motionY;
                    field_9226_m = field_909_a.motionZ;
                    func_603_a(new Packet28(field_909_a.entityId, field_9228_k, field_9227_l, field_9226_m));
                }
            }
            if(obj != null)
            {
                func_603_a(((Packet) (obj)));
            }
            DataWatcher datawatcher = field_909_a.getDataWatcher();
            if(datawatcher.hasObjectChanged())
            {
                func_12018_b(new Packet40(field_909_a.entityId, datawatcher));
            }
            if(flag)
            {
                field_9233_f = i;
                field_9232_g = j;
                field_9231_h = k;
            }
            if(flag1)
            {
                field_9230_i = l;
                field_9229_j = i1;
            }
        }
        if(field_909_a.beenAttacked)
        {
            func_12018_b(new Packet28(field_909_a));
            field_909_a.beenAttacked = false;
        }
    }

    public void func_603_a(Packet packet)
    {
        EntityPlayerMP entityplayermp;
        for(Iterator iterator = field_899_k.iterator(); iterator.hasNext(); entityplayermp.field_20908_a.sendPacket(packet))
        {
            entityplayermp = (EntityPlayerMP)iterator.next();
        }

    }

    public void func_12018_b(Packet packet)
    {
        func_603_a(packet);
        if(field_909_a instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)field_909_a).field_20908_a.sendPacket(packet);
        }
    }

    public void func_604_a()
    {
        func_603_a(new Packet29DestroyEntity(field_909_a.entityId));
    }

    public void func_12019_a(EntityPlayerMP entityplayermp)
    {
        if(field_899_k.contains(entityplayermp))
        {
            field_899_k.remove(entityplayermp);
        }
    }

    public void func_606_a(EntityPlayerMP entityplayermp)
    {
        if(entityplayermp == field_909_a)
        {
            return;
        }
        double d = entityplayermp.posX - (double)(field_9233_f / 32);
        double d1 = entityplayermp.posZ - (double)(field_9231_h / 32);
        if(d >= (double)(-field_9235_d) && d <= (double)field_9235_d && d1 >= (double)(-field_9235_d) && d1 <= (double)field_9235_d)
        {
            if(!field_899_k.contains(entityplayermp))
            {
                field_899_k.add(entityplayermp);
                entityplayermp.field_20908_a.sendPacket(func_602_b());
                if(field_9220_u)
                {
                    entityplayermp.field_20908_a.sendPacket(new Packet28(field_909_a.entityId, field_909_a.motionX, field_909_a.motionY, field_909_a.motionZ));
                }
                ItemStack aitemstack[] = field_909_a.getInventory();
                if(aitemstack != null)
                {
                    for(int i = 0; i < aitemstack.length; i++)
                    {
                        entityplayermp.field_20908_a.sendPacket(new Packet5PlayerInventory(field_909_a.entityId, i, aitemstack[i]));
                    }

                }
            }
        } else
        if(field_899_k.contains(entityplayermp))
        {
            field_899_k.remove(entityplayermp);
            entityplayermp.field_20908_a.sendPacket(new Packet29DestroyEntity(field_909_a.entityId));
        }
    }

    public void func_601_b(List list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            func_606_a((EntityPlayerMP)list.get(i));
        }

    }

    private Packet func_602_b()
    {
        if(field_909_a instanceof EntityItem)
        {
            EntityItem entityitem = (EntityItem)field_909_a;
            Packet21PickupSpawn packet21pickupspawn = new Packet21PickupSpawn(entityitem);
            entityitem.posX = (double)packet21pickupspawn.xPosition / 32D;
            entityitem.posY = (double)packet21pickupspawn.yPosition / 32D;
            entityitem.posZ = (double)packet21pickupspawn.zPosition / 32D;
            return packet21pickupspawn;
        }
        if(field_909_a instanceof EntityPlayerMP)
        {
            return new Packet20NamedEntitySpawn((EntityPlayer)field_909_a);
        }
        if(field_909_a instanceof EntityMinecart)
        {
            EntityMinecart entityminecart = (EntityMinecart)field_909_a;
            if(entityminecart.minecartType == 0)
            {
                return new Packet23VehicleSpawn(field_909_a, 10);
            }
            if(entityminecart.minecartType == 1)
            {
                return new Packet23VehicleSpawn(field_909_a, 11);
            }
            if(entityminecart.minecartType == 2)
            {
                return new Packet23VehicleSpawn(field_909_a, 12);
            }
        }
        if(field_909_a instanceof EntityBoat)
        {
            return new Packet23VehicleSpawn(field_909_a, 1);
        }
        if(field_909_a instanceof IAnimals)
        {
            return new Packet24MobSpawn((EntityLiving)field_909_a);
        }
        if(field_909_a instanceof EntityFish)
        {
            return new Packet23VehicleSpawn(field_909_a, 90);
        }
        if(field_909_a instanceof EntityArrow)
        {
            return new Packet23VehicleSpawn(field_909_a, 60);
        }
        if(field_909_a instanceof EntitySnowball)
        {
            return new Packet23VehicleSpawn(field_909_a, 61);
        }
        if(field_909_a instanceof EntityEgg)
        {
            return new Packet23VehicleSpawn(field_909_a, 62);
        }
        if(field_909_a instanceof EntityTNTPrimed)
        {
            return new Packet23VehicleSpawn(field_909_a, 50);
        }
        if(field_909_a instanceof EntityFallingSand)
        {
            EntityFallingSand entityfallingsand = (EntityFallingSand)field_909_a;
            if(entityfallingsand.blockID == Block.sand.blockID)
            {
                return new Packet23VehicleSpawn(field_909_a, 70);
            }
            if(entityfallingsand.blockID == Block.gravel.blockID)
            {
                return new Packet23VehicleSpawn(field_909_a, 71);
            }
        }
        if(field_909_a instanceof EntityPainting)
        {
            return new Packet25((EntityPainting)field_909_a);
        } else
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Don't know how to add ").append(field_909_a.getClass()).append("!").toString());
        }
    }

    public void func_9219_b(EntityPlayerMP entityplayermp)
    {
        if(field_899_k.contains(entityplayermp))
        {
            field_899_k.remove(entityplayermp);
            entityplayermp.field_20908_a.sendPacket(new Packet29DestroyEntity(field_909_a.entityId));
        }
    }

    public Entity field_909_a;
    public int field_9235_d;
    public int field_9234_e;
    public int field_9233_f;
    public int field_9232_g;
    public int field_9231_h;
    public int field_9230_i;
    public int field_9229_j;
    public double field_9228_k;
    public double field_9227_l;
    public double field_9226_m;
    public int field_9221_t;
    private double field_9224_q;
    private double field_9223_r;
    private double field_9222_s;
    private boolean field_12020_u;
    private boolean field_9220_u;
    public boolean field_900_j;
    public Set field_899_k;
}
