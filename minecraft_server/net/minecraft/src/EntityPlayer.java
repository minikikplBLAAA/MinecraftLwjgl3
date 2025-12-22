package net.minecraft.src;
 

import java.util.List;
import java.util.Random;

public abstract class EntityPlayer extends EntityLiving
{

    public EntityPlayer(World world)
    {
        super(world);
        inventory = new InventoryPlayer(this);
        field_9152_am = 0;
        score = 0;
        isSwinging = false;
        swingProgressInt = 0;
        damageRemainder = 0;
        fishEntity = null;
        field_20053_ao = new CraftingInventoryPlayerCB(inventory, !world.multiplayerWorld);
        craftingInventory = field_20053_ao;
        yOffset = 1.62F;
        ChunkCoordinates chunkcoordinates = world.func_22078_l();
        setLocationAndAngles((double)chunkcoordinates.field_22216_a + 0.5D, chunkcoordinates.field_529_a + 1, (double)chunkcoordinates.field_528_b + 0.5D, 0.0F, 0.0F);
        health = 20;
        field_9116_aJ = "humanoid";
        field_9117_aI = 180F;
        fireResistance = 20;
        texture = "/mob/char.png";
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void onUpdate()
    {
        if(func_22057_E())
        {
            field_21902_c++;
            if(field_21902_c > 100)
            {
                field_21902_c = 100;
            }
            if(!func_22063_l())
            {
                func_22062_a(true, true);
            } else
            if(!worldObj.multiplayerWorld && worldObj.isDaytime())
            {
                func_22062_a(false, true);
            }
        } else
        if(field_21902_c > 0)
        {
            field_21902_c++;
            if(field_21902_c >= 110)
            {
                field_21902_c = 0;
            }
        }
        super.onUpdate();
        if(!worldObj.multiplayerWorld && craftingInventory != null && !craftingInventory.func_20126_b(this))
        {
            func_20043_I();
            craftingInventory = field_20053_ao;
        }
        field_20047_ay = field_20050_aB;
        field_20046_az = field_20049_aC;
        field_20051_aA = field_20048_aD;
        double d = posX - field_20050_aB;
        double d1 = posY - field_20049_aC;
        double d2 = posZ - field_20048_aD;
        double d3 = 10D;
        if(d > d3)
        {
            field_20047_ay = field_20050_aB = posX;
        }
        if(d2 > d3)
        {
            field_20051_aA = field_20048_aD = posZ;
        }
        if(d1 > d3)
        {
            field_20046_az = field_20049_aC = posY;
        }
        if(d < -d3)
        {
            field_20047_ay = field_20050_aB = posX;
        }
        if(d2 < -d3)
        {
            field_20051_aA = field_20048_aD = posZ;
        }
        if(d1 < -d3)
        {
            field_20046_az = field_20049_aC = posY;
        }
        field_20050_aB += d * 0.25D;
        field_20048_aD += d2 * 0.25D;
        field_20049_aC += d1 * 0.25D;
    }

    protected boolean func_22058_w()
    {
        return health <= 0 || func_22057_E();
    }

    protected void func_20043_I()
    {
        craftingInventory = field_20053_ao;
    }

    public void updateRidden()
    {
        super.updateRidden();
        field_9150_ao = field_9149_ap;
        field_9149_ap = 0.0F;
    }

    protected void updatePlayerActionState()
    {
        if(isSwinging)
        {
            swingProgressInt++;
            if(swingProgressInt == 8)
            {
                swingProgressInt = 0;
                isSwinging = false;
            }
        } else
        {
            swingProgressInt = 0;
        }
        swingProgress = (float)swingProgressInt / 8F;
    }

    public void onLivingUpdate()
    {
        if(worldObj.difficultySetting == 0 && health < 20 && (ticksExisted % 20) * 12 == 0)
        {
            heal(1);
        }
        inventory.decrementAnimations();
        field_9150_ao = field_9149_ap;
        super.onLivingUpdate();
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        float f1 = (float)Math.atan(-motionY * 0.20000000298023224D) * 15F;
        if(f > 0.1F)
        {
            f = 0.1F;
        }
        if(!onGround || health <= 0)
        {
            f = 0.0F;
        }
        if(onGround || health <= 0)
        {
            f1 = 0.0F;
        }
        field_9149_ap += (f - field_9149_ap) * 0.4F;
        field_9101_aY += (f1 - field_9101_aY) * 0.8F;
        if(health > 0)
        {
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(1.0D, 0.0D, 1.0D));
            if(list != null)
            {
                for(int i = 0; i < list.size(); i++)
                {
                    Entity entity = (Entity)list.get(i);
                    if(!entity.isDead)
                    {
                        func_171_h(entity);
                    }
                }

            }
        }
    }

    private void func_171_h(Entity entity)
    {
        entity.onCollideWithPlayer(this);
    }

    public void onDeath(Entity entity)
    {
        super.onDeath(entity);
        setSize(0.2F, 0.2F);
        setPosition(posX, posY, posZ);
        motionY = 0.10000000149011612D;
        if(username.equals("Notch"))
        {
            dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
        }
        inventory.dropAllItems();
        if(entity != null)
        {
            motionX = -MathHelper.cos(((attackedAtYaw + rotationYaw) * 3.141593F) / 180F) * 0.1F;
            motionZ = -MathHelper.sin(((attackedAtYaw + rotationYaw) * 3.141593F) / 180F) * 0.1F;
        } else
        {
            motionX = motionZ = 0.0D;
        }
        yOffset = 0.1F;
    }

    public void addToPlayerScore(Entity entity, int i)
    {
        score += i;
    }

    public void func_161_a()
    {
        dropPlayerItemWithRandomChoice(inventory.decrStackSize(inventory.currentItem, 1), false);
    }

    public void dropPlayerItem(ItemStack itemstack)
    {
        dropPlayerItemWithRandomChoice(itemstack, false);
    }

    public void dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag)
    {
        if(itemstack == null)
        {
            return;
        }
        EntityItem entityitem = new EntityItem(worldObj, posX, (posY - 0.30000001192092896D) + (double)getEyeHeight(), posZ, itemstack);
        entityitem.delayBeforeCanPickup = 40;
        float f = 0.1F;
        if(flag)
        {
            float f2 = rand.nextFloat() * 0.5F;
            float f4 = rand.nextFloat() * 3.141593F * 2.0F;
            entityitem.motionX = -MathHelper.sin(f4) * f2;
            entityitem.motionZ = MathHelper.cos(f4) * f2;
            entityitem.motionY = 0.20000000298023224D;
        } else
        {
            float f1 = 0.3F;
            entityitem.motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f1;
            entityitem.motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f1;
            entityitem.motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f1 + 0.1F;
            f1 = 0.02F;
            float f3 = rand.nextFloat() * 3.141593F * 2.0F;
            f1 *= rand.nextFloat();
            entityitem.motionX += Math.cos(f3) * (double)f1;
            entityitem.motionY += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            entityitem.motionZ += Math.sin(f3) * (double)f1;
        }
        joinEntityItemWithWorld(entityitem);
    }

    protected void joinEntityItemWithWorld(EntityItem entityitem)
    {
        worldObj.entityJoinedWorld(entityitem);
    }

    public float getCurrentPlayerStrVsBlock(Block block)
    {
        float f = inventory.getStrVsBlock(block);
        if(isInsideOfMaterial(Material.water))
        {
            f /= 5F;
        }
        if(!onGround)
        {
            f /= 5F;
        }
        return f;
    }

    public boolean canHarvestBlock(Block block)
    {
        return inventory.canHarvestBlock(block);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory");
        inventory.readFromNBT(nbttaglist);
        dimension = nbttagcompound.getInteger("Dimension");
        field_21900_a = nbttagcompound.getBoolean("Sleeping");
        field_21902_c = nbttagcompound.getShort("SleepTimer");
        if(field_21900_a)
        {
            field_21901_b = new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
            func_22062_a(true, true);
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        nbttagcompound.setInteger("Dimension", dimension);
        nbttagcompound.setBoolean("Sleeping", field_21900_a);
        nbttagcompound.setShort("SleepTimer", (short)field_21902_c);
    }

    public void displayGUIChest(IInventory iinventory)
    {
    }

    public void displayWorkbenchGUI(int i, int j, int k)
    {
    }

    public void onItemPickup(Entity entity, int i)
    {
    }

    public float getEyeHeight()
    {
        return 0.12F;
    }

    protected void func_22064_l_()
    {
        yOffset = 1.62F;
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        field_9132_bn = 0;
        if(health <= 0)
        {
            return false;
        }
        if(func_22057_E())
        {
            func_22062_a(true, true);
        }
        if((entity instanceof EntityMobs) || (entity instanceof EntityArrow))
        {
            if(worldObj.difficultySetting == 0)
            {
                i = 0;
            }
            if(worldObj.difficultySetting == 1)
            {
                i = i / 3 + 1;
            }
            if(worldObj.difficultySetting == 3)
            {
                i = (i * 3) / 2;
            }
        }
        if(i == 0)
        {
            return false;
        } else
        {
            return super.attackEntityFrom(entity, i);
        }
    }

    protected void damageEntity(int i)
    {
        int j = 25 - inventory.getTotalArmorValue();
        int k = i * j + damageRemainder;
        inventory.damageArmor(i);
        i = k / 25;
        damageRemainder = k % 25;
        super.damageEntity(i);
    }

    public void displayGUIFurnace(TileEntityFurnace tileentityfurnace)
    {
    }

    public void displayGUIDispenser(TileEntityDispenser tileentitydispenser)
    {
    }

    public void displayGUIEditSign(TileEntitySign tileentitysign)
    {
    }

    public void useCurrentItemOnEntity(Entity entity)
    {
        if(entity.interact(this))
        {
            return;
        }
        ItemStack itemstack = getCurrentEquippedItem();
        if(itemstack != null && (entity instanceof EntityLiving))
        {
            itemstack.useItemOnEntity((EntityLiving)entity);
            if(itemstack.stackSize <= 0)
            {
                itemstack.func_577_a(this);
                destroyCurrentEquippedItem();
            }
        }
    }

    public ItemStack getCurrentEquippedItem()
    {
        return inventory.getCurrentItem();
    }

    public void destroyCurrentEquippedItem()
    {
        inventory.setInventorySlotContents(inventory.currentItem, null);
    }

    public double getYOffset()
    {
        return (double)(yOffset - 0.5F);
    }

    public void swingItem()
    {
        swingProgressInt = -1;
        isSwinging = true;
    }

    public void attackTargetEntityWithCurrentItem(Entity entity)
    {
        int i = inventory.getDamageVsEntity(entity);
        if(i > 0)
        {
            entity.attackEntityFrom(this, i);
            ItemStack itemstack = getCurrentEquippedItem();
            if(itemstack != null && (entity instanceof EntityLiving))
            {
                itemstack.hitEntity((EntityLiving)entity);
                if(itemstack.stackSize <= 0)
                {
                    itemstack.func_577_a(this);
                    destroyCurrentEquippedItem();
                }
            }
        }
    }

    public void onItemStackChanged(ItemStack itemstack)
    {
    }

    public void setEntityDead()
    {
        super.setEntityDead();
        field_20053_ao.onCraftGuiClosed(this);
        if(craftingInventory != null)
        {
            craftingInventory.onCraftGuiClosed(this);
        }
    }

    public boolean func_91_u()
    {
        return !field_21900_a && super.func_91_u();
    }

    public boolean func_22060_a(int i, int j, int k)
    {
        if(func_22057_E() || !isEntityAlive())
        {
            return false;
        }
        if(worldObj.worldProvider.field_6167_c)
        {
            return false;
        }
        if(worldObj.isDaytime())
        {
            return false;
        }
        if(Math.abs(posX - (double)i) > 3D || Math.abs(posY - (double)j) > 2D || Math.abs(posZ - (double)k) > 3D)
        {
            return false;
        }
        setSize(0.2F, 0.2F);
        yOffset = 0.2F;
        if(worldObj.blockExists(i, j, k))
        {
            int l = worldObj.getBlockMetadata(i, j, k);
            int i1 = BlockBed.func_22019_c(l);
            float f = 0.5F;
            float f1 = 0.5F;
            switch(i1)
            {
            case 0: // '\0'
                f1 = 0.9F;
                break;

            case 2: // '\002'
                f1 = 0.1F;
                break;

            case 1: // '\001'
                f = 0.1F;
                break;

            case 3: // '\003'
                f = 0.9F;
                break;
            }
            func_22059_e(i1);
            setPosition((float)i + f, (float)j + 0.9375F, (float)k + f1);
        } else
        {
            setPosition((float)i + 0.5F, (float)j + 0.9375F, (float)k + 0.5F);
        }
        field_21900_a = true;
        field_21902_c = 0;
        field_21901_b = new ChunkCoordinates(i, j, k);
        motionX = motionZ = motionY = 0.0D;
        if(!worldObj.multiplayerWorld)
        {
            worldObj.func_22082_o();
        }
        return true;
    }

    private void func_22059_e(int i)
    {
        field_22066_z = 0.0F;
        field_22067_A = 0.0F;
        switch(i)
        {
        case 0: // '\0'
            field_22067_A = -1.8F;
            break;

        case 2: // '\002'
            field_22067_A = 1.8F;
            break;

        case 1: // '\001'
            field_22066_z = 1.8F;
            break;

        case 3: // '\003'
            field_22066_z = -1.8F;
            break;
        }
    }

    public void func_22062_a(boolean flag, boolean flag1)
    {
        setSize(0.6F, 1.8F);
        func_22064_l_();
        ChunkCoordinates chunkcoordinates = field_21901_b;
        if(chunkcoordinates != null && worldObj.getBlockId(chunkcoordinates.field_22216_a, chunkcoordinates.field_529_a, chunkcoordinates.field_528_b) == Block.field_9037_S.blockID)
        {
            BlockBed.func_22022_a(worldObj, chunkcoordinates.field_22216_a, chunkcoordinates.field_529_a, chunkcoordinates.field_528_b, false);
            ChunkCoordinates chunkcoordinates1 = BlockBed.func_22021_g(worldObj, chunkcoordinates.field_22216_a, chunkcoordinates.field_529_a, chunkcoordinates.field_528_b, 0);
            setPosition((float)chunkcoordinates1.field_22216_a + 0.5F, (float)chunkcoordinates1.field_529_a + yOffset + 0.1F, (float)chunkcoordinates1.field_528_b + 0.5F);
        }
        field_21900_a = false;
        if(!worldObj.multiplayerWorld && flag1)
        {
            worldObj.func_22082_o();
        }
        if(flag)
        {
            field_21902_c = 0;
        } else
        {
            field_21902_c = 100;
        }
    }

    private boolean func_22063_l()
    {
        return worldObj.getBlockId(field_21901_b.field_22216_a, field_21901_b.field_529_a, field_21901_b.field_528_b) == Block.field_9037_S.blockID;
    }

    public boolean func_22057_E()
    {
        return field_21900_a;
    }

    public boolean func_22065_F()
    {
        return field_21900_a && field_21902_c >= 100;
    }

    public void func_22061_a(String s)
    {
    }

    public InventoryPlayer inventory;
    public CraftingInventoryCB field_20053_ao;
    public CraftingInventoryCB craftingInventory;
    public byte field_9152_am;
    public int score;
    public float field_9150_ao;
    public float field_9149_ap;
    public boolean isSwinging;
    public int swingProgressInt;
    public String username;
    public int dimension;
    public double field_20047_ay;
    public double field_20046_az;
    public double field_20051_aA;
    public double field_20050_aB;
    public double field_20049_aC;
    public double field_20048_aD;
    private boolean field_21900_a;
    private ChunkCoordinates field_21901_b;
    private int field_21902_c;
    public float field_22066_z;
    public float field_22067_A;
    private int damageRemainder;
    public EntityFish fishEntity;
}
