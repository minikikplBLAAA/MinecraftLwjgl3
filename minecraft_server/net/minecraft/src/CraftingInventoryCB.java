package net.minecraft.src;
 

import java.util.*;

public abstract class CraftingInventoryCB
{

    public CraftingInventoryCB()
    {
        field_20136_d = new ArrayList();
        field_20135_e = new ArrayList();
        windowId = 0;
        field_20132_a = 0;
        field_20133_g = new ArrayList();
        field_20131_b = new HashSet();
    }

    protected void func_20122_a(Slot slot)
    {
        slot.field_20100_c = field_20135_e.size();
        field_20135_e.add(slot);
        field_20136_d.add(null);
    }

    public void func_20128_a(ICrafting icrafting)
    {
        field_20133_g.add(icrafting);
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < field_20135_e.size(); i++)
        {
            arraylist.add(((Slot)field_20135_e.get(i)).getStack());
        }

        icrafting.func_20054_a(this, arraylist);
        func_20125_a();
    }

    public void func_20125_a()
    {
        for(int i = 0; i < field_20135_e.size(); i++)
        {
            ItemStack itemstack = ((Slot)field_20135_e.get(i)).getStack();
            ItemStack itemstack1 = (ItemStack)field_20136_d.get(i);
            if(ItemStack.areItemStacksEqual(itemstack1, itemstack))
            {
                continue;
            }
            itemstack1 = itemstack != null ? itemstack.copy() : null;
            field_20136_d.set(i, itemstack1);
            for(int j = 0; j < field_20133_g.size(); j++)
            {
                ((ICrafting)field_20133_g.get(j)).func_20055_a(this, i, itemstack1);
            }

        }

    }

    public Slot func_20127_a(IInventory iinventory, int i)
    {
        for(int j = 0; j < field_20135_e.size(); j++)
        {
            Slot slot = (Slot)field_20135_e.get(j);
            if(slot.func_20090_a(iinventory, i))
            {
                return slot;
            }
        }

        return null;
    }

    public Slot func_20120_a(int i)
    {
        return (Slot)field_20135_e.get(i);
    }

    public ItemStack func_20123_a(int i, int j, EntityPlayer entityplayer)
    {
        ItemStack itemstack = null;
        if(j == 0 || j == 1)
        {
            InventoryPlayer inventoryplayer = entityplayer.inventory;
            if(i == -999)
            {
                if(inventoryplayer.getItemStack() != null && i == -999)
                {
                    if(j == 0)
                    {
                        entityplayer.dropPlayerItem(inventoryplayer.getItemStack());
                        inventoryplayer.setItemStack(null);
                    }
                    if(j == 1)
                    {
                        entityplayer.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));
                        if(inventoryplayer.getItemStack().stackSize == 0)
                        {
                            inventoryplayer.setItemStack(null);
                        }
                    }
                }
            } else
            {
                Slot slot = (Slot)field_20135_e.get(i);
                if(slot != null)
                {
                    slot.onSlotChanged();
                    ItemStack itemstack1 = slot.getStack();
                    ItemStack itemstack2 = inventoryplayer.getItemStack();
                    if(itemstack1 != null)
                    {
                        itemstack = itemstack1.copy();
                    }
                    if(itemstack1 == null)
                    {
                        if(itemstack2 != null && slot.isItemValid(itemstack2))
                        {
                            int k = j != 0 ? 1 : itemstack2.stackSize;
                            if(k > slot.getSlotStackLimit())
                            {
                                k = slot.getSlotStackLimit();
                            }
                            slot.putStack(itemstack2.splitStack(k));
                            if(itemstack2.stackSize == 0)
                            {
                                inventoryplayer.setItemStack(null);
                            }
                        }
                    } else
                    if(itemstack2 == null)
                    {
                        int l = j != 0 ? (itemstack1.stackSize + 1) / 2 : itemstack1.stackSize;
                        inventoryplayer.setItemStack(slot.decrStackSize(l));
                        if(itemstack1.stackSize == 0)
                        {
                            slot.putStack(null);
                        }
                        slot.onPickupFromSlot();
                    } else
                    if(slot.isItemValid(itemstack2))
                    {
                        if(itemstack1.itemID != itemstack2.itemID || itemstack1.getHasSubtypes() && itemstack1.getItemDamage() != itemstack2.getItemDamage())
                        {
                            if(itemstack2.stackSize <= slot.getSlotStackLimit())
                            {
                                ItemStack itemstack3 = itemstack1;
                                slot.putStack(itemstack2);
                                inventoryplayer.setItemStack(itemstack3);
                            }
                        } else
                        {
                            int i1 = j != 0 ? 1 : itemstack2.stackSize;
                            if(i1 > slot.getSlotStackLimit() - itemstack1.stackSize)
                            {
                                i1 = slot.getSlotStackLimit() - itemstack1.stackSize;
                            }
                            if(i1 > itemstack2.getMaxStackSize() - itemstack1.stackSize)
                            {
                                i1 = itemstack2.getMaxStackSize() - itemstack1.stackSize;
                            }
                            itemstack2.splitStack(i1);
                            if(itemstack2.stackSize == 0)
                            {
                                inventoryplayer.setItemStack(null);
                            }
                            itemstack1.stackSize += i1;
                        }
                    } else
                    if(itemstack1.itemID == itemstack2.itemID && itemstack2.getMaxStackSize() > 1 && (!itemstack1.getHasSubtypes() || itemstack1.getItemDamage() == itemstack2.getItemDamage()))
                    {
                        int j1 = itemstack1.stackSize;
                        if(j1 > 0 && j1 + itemstack2.stackSize <= itemstack2.getMaxStackSize())
                        {
                            itemstack2.stackSize += j1;
                            itemstack1.splitStack(j1);
                            if(itemstack1.stackSize == 0)
                            {
                                slot.putStack(null);
                            }
                            slot.onPickupFromSlot();
                        }
                    }
                }
            }
        }
        return itemstack;
    }

    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
        InventoryPlayer inventoryplayer = entityplayer.inventory;
        if(inventoryplayer.getItemStack() != null)
        {
            entityplayer.dropPlayerItem(inventoryplayer.getItemStack());
            inventoryplayer.setItemStack(null);
        }
    }

    public void onCraftMatrixChanged(IInventory iinventory)
    {
        func_20125_a();
    }

    public boolean func_20124_c(EntityPlayer entityplayer)
    {
        return !field_20131_b.contains(entityplayer);
    }

    public void func_20129_a(EntityPlayer entityplayer, boolean flag)
    {
        if(flag)
        {
            field_20131_b.remove(entityplayer);
        } else
        {
            field_20131_b.add(entityplayer);
        }
    }

    public abstract boolean func_20126_b(EntityPlayer entityplayer);

    public List field_20136_d;
    public List field_20135_e;
    public int windowId;
    private short field_20132_a;
    protected List field_20133_g;
    private Set field_20131_b;
}
