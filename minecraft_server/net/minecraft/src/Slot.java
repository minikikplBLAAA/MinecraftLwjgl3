package net.minecraft.src;
 


public class Slot
{

    public Slot(IInventory iinventory, int i, int j, int k)
    {
        inventory = iinventory;
        slotIndex = i;
        xDisplayPosition = j;
        yDisplayPosition = k;
    }

    public void onPickupFromSlot()
    {
        onSlotChanged();
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return true;
    }

    public ItemStack getStack()
    {
        return inventory.getStackInSlot(slotIndex);
    }

    public void putStack(ItemStack itemstack)
    {
        inventory.setInventorySlotContents(slotIndex, itemstack);
        onSlotChanged();
    }

    public void onSlotChanged()
    {
        inventory.onInventoryChanged();
    }

    public int getSlotStackLimit()
    {
        return inventory.getInventoryStackLimit();
    }

    public ItemStack decrStackSize(int i)
    {
        return inventory.decrStackSize(slotIndex, i);
    }

    public boolean func_20090_a(IInventory iinventory, int i)
    {
        return iinventory == inventory && i == slotIndex;
    }

    private final int slotIndex;
    private final IInventory inventory;
    public int field_20100_c;
    public int xDisplayPosition;
    public int yDisplayPosition;
}
