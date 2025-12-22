package net.minecraft.src;
 


public interface IRecipe
{

    public abstract boolean func_21135_a(InventoryCrafting inventorycrafting);

    public abstract ItemStack func_21136_b(InventoryCrafting inventorycrafting);

    public abstract int getRecipeSize();
}
