package net.minecraft.src;
 

import java.util.*;
import net.minecraft.server.MinecraftServer;

public class EntityPlayerMP extends EntityPlayer
    implements ICrafting
{

    public EntityPlayerMP(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager)
    {
        super(world);
        loadedChunks = new LinkedList();
        field_420_ah = new HashSet();
        field_9156_bu = 0xfa0a1f01;
        field_15004_bw = 60;
        field_20065_bH = 0;
        ChunkCoordinates chunkcoordinates = world.func_22078_l();
        int i = chunkcoordinates.field_22216_a;
        int j = chunkcoordinates.field_528_b;
        int k = chunkcoordinates.field_529_a;
        if(!world.worldProvider.field_4306_c)
        {
            i += rand.nextInt(20) - 10;
            k = world.findTopSolidBlock(i, j);
            j += rand.nextInt(20) - 10;
        }
        setLocationAndAngles((double)i + 0.5D, k, (double)j + 0.5D, 0.0F, 0.0F);
        mcServer = minecraftserver;
        stepHeight = 0.0F;
        iteminworldmanager.field_675_a = this;
        username = s;
        field_425_ad = iteminworldmanager;
        yOffset = 0.0F;
    }

    public void func_20057_k()
    {
        craftingInventory.func_20128_a(this);
    }

    public ItemStack[] getInventory()
    {
        return playerInventory;
    }

    protected void func_22064_l_()
    {
        yOffset = 0.0F;
    }

    public float getEyeHeight()
    {
        return 1.62F;
    }

    public void onUpdate()
    {
        field_425_ad.func_328_a();
        field_15004_bw--;
        craftingInventory.func_20125_a();
        for(int i = 0; i < 5; i++)
        {
            ItemStack itemstack = func_21073_a(i);
            if(itemstack != playerInventory[i])
            {
                mcServer.field_6028_k.func_12021_a(this, new Packet5PlayerInventory(entityId, i, itemstack));
                playerInventory[i] = itemstack;
            }
        }

    }

    public ItemStack func_21073_a(int i)
    {
        if(i == 0)
        {
            return inventory.getCurrentItem();
        } else
        {
            return inventory.armorInventory[i - 1];
        }
    }

    public void onDeath(Entity entity)
    {
        inventory.dropAllItems();
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        if(field_15004_bw > 0)
        {
            return false;
        }
        if(!mcServer.field_9011_n)
        {
            if(entity instanceof EntityPlayer)
            {
                return false;
            }
            if(entity instanceof EntityArrow)
            {
                EntityArrow entityarrow = (EntityArrow)entity;
                if(entityarrow.field_439_ah instanceof EntityPlayer)
                {
                    return false;
                }
            }
        }
        return super.attackEntityFrom(entity, i);
    }

    public void heal(int i)
    {
        super.heal(i);
    }

    public void func_22070_a(boolean flag)
    {
        super.onUpdate();
        if(flag && !loadedChunks.isEmpty())
        {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)loadedChunks.get(0);
            if(chunkcoordintpair != null)
            {
                boolean flag1 = false;
                if(field_20908_a.func_38_b() < 2)
                {
                    flag1 = true;
                }
                if(flag1)
                {
                    loadedChunks.remove(chunkcoordintpair);
                    field_20908_a.sendPacket(new Packet51MapChunk(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, 16, 128, 16, mcServer.worldMngr));
                    List list = mcServer.worldMngr.func_532_d(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 128, chunkcoordintpair.chunkZPos * 16 + 16);
                    for(int i = 0; i < list.size(); i++)
                    {
                        func_20063_a((TileEntity)list.get(i));
                    }

                }
            }
        }
        if(health != field_9156_bu)
        {
            field_20908_a.sendPacket(new Packet8(health));
            field_9156_bu = health;
        }
    }

    private void func_20063_a(TileEntity tileentity)
    {
        if(tileentity != null)
        {
            Packet packet = tileentity.func_20070_f();
            if(packet != null)
            {
                field_20908_a.sendPacket(packet);
            }
        }
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    public void onItemPickup(Entity entity, int i)
    {
        if(!entity.isDead)
        {
            if(entity instanceof EntityItem)
            {
                mcServer.field_6028_k.func_12021_a(entity, new Packet22Collect(entity.entityId, entityId));
            }
            if(entity instanceof EntityArrow)
            {
                mcServer.field_6028_k.func_12021_a(entity, new Packet22Collect(entity.entityId, entityId));
            }
        }
        super.onItemPickup(entity, i);
        craftingInventory.func_20125_a();
    }

    public void swingItem()
    {
        if(!isSwinging)
        {
            swingProgressInt = -1;
            isSwinging = true;
            mcServer.field_6028_k.func_12021_a(this, new Packet18ArmAnimation(this, 1));
        }
    }

    public void func_22068_s()
    {
    }

    public boolean func_22060_a(int i, int j, int k)
    {
        if(super.func_22060_a(i, j, k))
        {
            mcServer.field_6028_k.func_12021_a(this, new Packet17Sleep(this, 0, i, j, k));
            return true;
        } else
        {
            return false;
        }
    }

    public void func_22062_a(boolean flag, boolean flag1)
    {
        if(func_22057_E())
        {
            mcServer.field_6028_k.func_609_a(this, new Packet18ArmAnimation(this, 3));
        }
        super.func_22062_a(flag, flag1);
        field_20908_a.func_41_a(posX, posY, posZ, rotationYaw, rotationPitch);
    }

    public void mountEntity(Entity entity)
    {
        super.mountEntity(entity);
        field_20908_a.sendPacket(new Packet39(this, ridingEntity));
        field_20908_a.func_41_a(posX, posY, posZ, rotationYaw, rotationPitch);
    }

    protected void updateFallState(double d, boolean flag)
    {
    }

    public void func_9153_b(double d, boolean flag)
    {
        super.updateFallState(d, flag);
    }

    private void func_20060_R()
    {
        field_20065_bH = field_20065_bH % 100 + 1;
    }

    public void displayWorkbenchGUI(int i, int j, int k)
    {
        func_20060_R();
        field_20908_a.sendPacket(new Packet100(field_20065_bH, 1, "Crafting", 9));
        craftingInventory = new CraftingInventoryWorkbenchCB(inventory, worldObj, i, j, k);
        craftingInventory.windowId = field_20065_bH;
        craftingInventory.func_20128_a(this);
    }

    public void displayGUIChest(IInventory iinventory)
    {
        func_20060_R();
        field_20908_a.sendPacket(new Packet100(field_20065_bH, 0, iinventory.getInvName(), iinventory.getSizeInventory()));
        craftingInventory = new CraftingInventoryChestCB(inventory, iinventory);
        craftingInventory.windowId = field_20065_bH;
        craftingInventory.func_20128_a(this);
    }

    public void displayGUIFurnace(TileEntityFurnace tileentityfurnace)
    {
        func_20060_R();
        field_20908_a.sendPacket(new Packet100(field_20065_bH, 2, tileentityfurnace.getInvName(), tileentityfurnace.getSizeInventory()));
        craftingInventory = new CraftingInventoryFurnaceCB(inventory, tileentityfurnace);
        craftingInventory.windowId = field_20065_bH;
        craftingInventory.func_20128_a(this);
    }

    public void displayGUIDispenser(TileEntityDispenser tileentitydispenser)
    {
        func_20060_R();
        field_20908_a.sendPacket(new Packet100(field_20065_bH, 3, tileentitydispenser.getInvName(), tileentitydispenser.getSizeInventory()));
        craftingInventory = new CraftingInventoryDispenserCB(inventory, tileentitydispenser);
        craftingInventory.windowId = field_20065_bH;
        craftingInventory.func_20128_a(this);
    }

    public void func_20055_a(CraftingInventoryCB craftinginventorycb, int i, ItemStack itemstack)
    {
        if(craftinginventorycb.func_20120_a(i) instanceof SlotCrafting)
        {
            return;
        }
        if(field_20064_am)
        {
            return;
        } else
        {
            field_20908_a.sendPacket(new Packet103(craftinginventorycb.windowId, i, itemstack));
            return;
        }
    }

    public void func_20054_a(CraftingInventoryCB craftinginventorycb, List list)
    {
        field_20908_a.sendPacket(new Packet104(craftinginventorycb.windowId, list));
        field_20908_a.sendPacket(new Packet103(-1, -1, inventory.getItemStack()));
    }

    public void func_20056_a(CraftingInventoryCB craftinginventorycb, int i, int j)
    {
        field_20908_a.sendPacket(new Packet105(craftinginventorycb.windowId, i, j));
    }

    public void onItemStackChanged(ItemStack itemstack)
    {
    }

    public void func_20043_I()
    {
        field_20908_a.sendPacket(new Packet101(craftingInventory.windowId));
        func_20059_K();
    }

    public void func_20058_J()
    {
        if(field_20064_am)
        {
            return;
        } else
        {
            field_20908_a.sendPacket(new Packet103(-1, -1, inventory.getItemStack()));
            return;
        }
    }

    public void func_20059_K()
    {
        craftingInventory.onCraftGuiClosed(this);
        craftingInventory = field_20053_ao;
    }

    public void func_22069_a(float f, float f1, boolean flag, boolean flag1, float f2, float f3)
    {
        moveStrafing = f;
        moveForward = f1;
        isJumping = flag;
        func_21043_b(flag1);
        rotationPitch = f2;
        rotationYaw = f3;
    }

    public NetServerHandler field_20908_a;
    public MinecraftServer mcServer;
    public ItemInWorldManager field_425_ad;
    public double field_9155_d;
    public double field_9154_e;
    public List loadedChunks;
    public Set field_420_ah;
    private int field_9156_bu;
    private int field_15004_bw;
    private ItemStack playerInventory[] = {
        null, null, null, null, null
    };
    private int field_20065_bH;
    public boolean field_20064_am;
}
