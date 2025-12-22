package net.minecraft.src;
 


public class SlotCrafting extends Slot
{

    public SlotCrafting(IInventory iinventory, IInventory iinventory1, int i, int j, int k)
    {
        super(iinventory1, i, j, k);
        craftMatrix = iinventory;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    public void onPickupFromSlot()
    {
        for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
        {
            ItemStack itemstack = craftMatrix.getStackInSlot(i);
            if(itemstack == null)
            {
                continue;
            }
            craftMatrix.decrStackSize(i, 1);
            if(itemstack.getItem().func_21088_g())
            {
                craftMatrix.setInventorySlotContents(i, new ItemStack(itemstack.getItem().getContainerItem()));
            }
        }

    }

    private final IInventory craftMatrix;
}
