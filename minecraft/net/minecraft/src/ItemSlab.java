package net.minecraft.src;
 


public class ItemSlab extends ItemBlock
{

    public ItemSlab(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getIconIndex(ItemStack itemstack)
    {
        return Block.stairSingle.getBlockTextureFromSideAndMetadata(2, itemstack.getItemDamage());
    }

    public int func_21012_a(int i)
    {
        return i;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        return (new StringBuilder()).append(super.getItemName()).append(".").append(BlockStep.field_22037_a[itemstack.getItemDamage()]).toString();
    }
}
