package net.minecraft.src;
 


public class ItemCloth extends ItemBlock
{

    public ItemCloth(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getIconIndex(ItemStack itemstack)
    {
        return Block.cloth.getBlockTextureFromSideAndMetadata(2, BlockCloth.func_21034_c(itemstack.getItemDamage()));
    }

    public int func_21012_a(int i)
    {
        return i;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        return (new StringBuilder()).append(super.getItemName()).append(".").append(ItemDye.dyeColors[BlockCloth.func_21034_c(itemstack.getItemDamage())]).toString();
    }
}
