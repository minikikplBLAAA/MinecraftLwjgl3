package net.minecraft.src;
 


public class RecipesFood
{

    public RecipesFood()
    {
    }

    public void addRecipes(CraftingManager craftingmanager)
    {
        craftingmanager.addRecipe(new ItemStack(Item.bowlSoup), new Object[] {
            "Y", "X", "#", Character.valueOf('X'), Block.mushroomBrown, Character.valueOf('Y'), Block.mushroomRed, Character.valueOf('#'), Item.bowlEmpty
        });
        craftingmanager.addRecipe(new ItemStack(Item.bowlSoup), new Object[] {
            "Y", "X", "#", Character.valueOf('X'), Block.mushroomRed, Character.valueOf('Y'), Block.mushroomBrown, Character.valueOf('#'), Item.bowlEmpty
        });
    }
}
