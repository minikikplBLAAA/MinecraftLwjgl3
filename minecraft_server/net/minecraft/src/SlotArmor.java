package net.minecraft.src;
 


class SlotArmor extends Slot
{

    SlotArmor(CraftingInventoryPlayerCB craftinginventoryplayercb, IInventory iinventory, int i, int j, int k, int l)
    {
    	super(iinventory, i, j, k);
        field_20101_b = craftinginventoryplayercb;
        field_20102_a = l;
    }

    public int getSlotStackLimit()
    {
        return 1;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        if(itemstack.getItem() instanceof ItemArmor)
        {
            return ((ItemArmor)itemstack.getItem()).armorType == field_20102_a;
        }
        if(itemstack.getItem().shiftedIndex == Block.pumpkin.blockID)
        {
            return field_20102_a == 0;
        } else
        {
            return false;
        }
    }

    final int field_20102_a; /* synthetic field */
    final CraftingInventoryPlayerCB field_20101_b; /* synthetic field */
}
