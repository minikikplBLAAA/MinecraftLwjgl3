package net.minecraft.src;
 


public class EntityWaterMob extends EntityCreature
    implements IAnimals
{

    public EntityWaterMob(World world)
    {
        super(world);
    }

    public boolean canBreatheUnderwater()
    {
        return true;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    public boolean getCanSpawnHere()
    {
        return worldObj.checkIfAABBIsClear(boundingBox);
    }

    public int func_146_b()
    {
        return 120;
    }
}
