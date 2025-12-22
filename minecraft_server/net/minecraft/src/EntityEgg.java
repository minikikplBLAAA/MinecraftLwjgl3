package net.minecraft.src;
 

import java.util.List;
import java.util.Random;

public class EntityEgg extends Entity
{

    public EntityEgg(World world)
    {
        super(world);
        field_20086_b = -1;
        field_20085_c = -1;
        field_20084_d = -1;
        field_20082_e = 0;
        field_20080_f = false;
        field_20087_a = 0;
        field_20079_al = 0;
        setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
    }

    public EntityEgg(World world, EntityLiving entityliving)
    {
        super(world);
        field_20086_b = -1;
        field_20085_c = -1;
        field_20084_d = -1;
        field_20082_e = 0;
        field_20080_f = false;
        field_20087_a = 0;
        field_20079_al = 0;
        field_20083_aj = entityliving;
        setSize(0.25F, 0.25F);
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        float f = 0.4F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f;
        func_20078_a(motionX, motionY, motionZ, 1.5F, 1.0F);
    }

    public EntityEgg(World world, double d, double d1, double d2)
    {
        super(world);
        field_20086_b = -1;
        field_20085_c = -1;
        field_20084_d = -1;
        field_20082_e = 0;
        field_20080_f = false;
        field_20087_a = 0;
        field_20079_al = 0;
        field_20081_ak = 0;
        setSize(0.25F, 0.25F);
        setPosition(d, d1, d2);
        yOffset = 0.0F;
    }

    public void func_20078_a(double d, double d1, double d2, float f, 
            float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d1 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d2 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / 3.1415927410125732D);
        field_20081_ak = 0;
    }

    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        super.onUpdate();
        if(field_20087_a > 0)
        {
            field_20087_a--;
        }
        if(field_20080_f)
        {
            int i = worldObj.getBlockId(field_20086_b, field_20085_c, field_20084_d);
            if(i != field_20082_e)
            {
                field_20080_f = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                field_20081_ak = 0;
                field_20079_al = 0;
            } else
            {
                field_20081_ak++;
                if(field_20081_ak == 1200)
                {
                    setEntityDead();
                }
                return;
            }
        } else
        {
            field_20079_al++;
        }
        Vec3D vec3d = Vec3D.createVector(posX, posY, posZ);
        Vec3D vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3D.createVector(posX, posY, posZ);
        vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        if(movingobjectposition != null)
        {
            vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }
        if(!worldObj.multiplayerWorld)
        {
            Entity entity = null;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
            double d = 0.0D;
            for(int i1 = 0; i1 < list.size(); i1++)
            {
                Entity entity1 = (Entity)list.get(i1);
                if(!entity1.canBeCollidedWith() || entity1 == field_20083_aj && field_20079_al < 5)
                {
                    continue;
                }
                float f4 = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
                MovingObjectPosition movingobjectposition1 = axisalignedbb.func_706_a(vec3d, vec3d1);
                if(movingobjectposition1 == null)
                {
                    continue;
                }
                double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
                if(d1 < d || d == 0.0D)
                {
                    entity = entity1;
                    d = d1;
                }
            }

            if(entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }
        if(movingobjectposition != null)
        {
            if(movingobjectposition.entityHit != null)
            {
                if(!movingobjectposition.entityHit.attackEntityFrom(field_20083_aj, 0));
            }
            if(!worldObj.multiplayerWorld && rand.nextInt(8) == 0)
            {
                byte byte0 = 1;
                if(rand.nextInt(32) == 0)
                {
                    byte0 = 4;
                }
                for(int k = 0; k < byte0; k++)
                {
                    EntityChicken entitychicken = new EntityChicken(worldObj);
                    entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    worldObj.entityJoinedWorld(entitychicken);
                }

            }
            for(int j = 0; j < 8; j++)
            {
                worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
            }

            setEntityDead();
        }
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
        for(rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f1 = 0.99F;
        float f2 = 0.03F;
        if(handleWaterMovement())
        {
            for(int l = 0; l < 4; l++)
            {
                float f3 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * (double)f3, posY - motionY * (double)f3, posZ - motionZ * (double)f3, motionX, motionY, motionZ);
            }

            f1 = 0.8F;
        }
        motionX *= f1;
        motionY *= f1;
        motionZ *= f1;
        motionY -= f2;
        setPosition(posX, posY, posZ);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)field_20086_b);
        nbttagcompound.setShort("yTile", (short)field_20085_c);
        nbttagcompound.setShort("zTile", (short)field_20084_d);
        nbttagcompound.setByte("inTile", (byte)field_20082_e);
        nbttagcompound.setByte("shake", (byte)field_20087_a);
        nbttagcompound.setByte("inGround", (byte)(field_20080_f ? 1 : 0));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        field_20086_b = nbttagcompound.getShort("xTile");
        field_20085_c = nbttagcompound.getShort("yTile");
        field_20084_d = nbttagcompound.getShort("zTile");
        field_20082_e = nbttagcompound.getByte("inTile") & 0xff;
        field_20087_a = nbttagcompound.getByte("shake") & 0xff;
        field_20080_f = nbttagcompound.getByte("inGround") == 1;
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        if(field_20080_f && field_20083_aj == entityplayer && field_20087_a <= 0 && entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
        {
            worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.onItemPickup(this, 1);
            setEntityDead();
        }
    }

    private int field_20086_b;
    private int field_20085_c;
    private int field_20084_d;
    private int field_20082_e;
    private boolean field_20080_f;
    public int field_20087_a;
    private EntityLiving field_20083_aj;
    private int field_20081_ak;
    private int field_20079_al;
}
