package net.minecraft.src;
 


public class ItemLog extends ItemBlock
{

    public ItemLog(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getIconIndex(ItemStack itemstack)
    {
        return Block.wood.getBlockTextureFromSideAndMetadata(2, itemstack.getItemDamage());
    }

    public int func_21012_a(int i)
    {
        return i;
    }
}
