package net.minecraft.src;
 

import java.util.*;

public abstract class CraftingInventoryCB
{

    public CraftingInventoryCB()
    {
        field_20123_d = new ArrayList();
        field_20122_e = new ArrayList();
        windowId = 0;
        field_20917_a = 0;
        field_20121_g = new ArrayList();
        field_20918_b = new HashSet();
    }

    protected void func_20117_a(Slot slot)
    {
        slot.field_20007_a = field_20122_e.size();
        field_20122_e.add(slot);
        field_20123_d.add(null);
    }

    public void func_20114_a()
    {
        for(int i = 0; i < field_20122_e.size(); i++)
        {
            ItemStack itemstack = ((Slot)field_20122_e.get(i)).getStack();
            ItemStack itemstack1 = (ItemStack)field_20123_d.get(i);
            if(ItemStack.areItemStacksEqual(itemstack1, itemstack))
            {
                continue;
            }
            itemstack1 = itemstack != null ? itemstack.copy() : null;
            field_20123_d.set(i, itemstack1);
            for(int j = 0; j < field_20121_g.size(); j++)
            {
                ((ICrafting)field_20121_g.get(j)).func_20159_a(this, i, itemstack1);
            }

        }

    }

    public Slot func_20118_a(int i)
    {
        return (Slot)field_20122_e.get(i);
    }

    public ItemStack func_20116_a(int i, int j, EntityPlayer entityplayer)
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
                Slot slot = (Slot)field_20122_e.get(i);
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
        func_20114_a();
    }

    public void func_20119_a(int i, ItemStack itemstack)
    {
        func_20118_a(i).putStack(itemstack);
    }

    public void func_20115_a(ItemStack aitemstack[])
    {
        for(int i = 0; i < aitemstack.length; i++)
        {
            func_20118_a(i).putStack(aitemstack[i]);
        }

    }

    public void func_20112_a(int i, int j)
    {
    }

    public short func_20111_a(InventoryPlayer inventoryplayer)
    {
        field_20917_a++;
        return field_20917_a;
    }

    public void func_20113_a(short word0)
    {
    }

    public void func_20110_b(short word0)
    {
    }

    public abstract boolean func_20120_b(EntityPlayer entityplayer);

    public List field_20123_d;
    public List field_20122_e;
    public int windowId;
    private short field_20917_a;
    protected List field_20121_g;
    private Set field_20918_b;
}
