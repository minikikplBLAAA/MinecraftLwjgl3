package net.minecraft.src;
 


public class ItemSoup extends ItemFood
{

    public ItemSoup(int i, int j)
    {
        super(i, j);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        super.onItemRightClick(itemstack, world, entityplayer);
        return new ItemStack(Item.bowlEmpty);
    }
}
