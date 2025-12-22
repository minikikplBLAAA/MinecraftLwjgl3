package net.minecraft.src;
 


public class ItemSaddle extends Item
{

    public ItemSaddle(int i)
    {
        super(i);
        maxStackSize = 1;
        maxDamage = 64;
    }

    public void saddleEntity(ItemStack itemstack, EntityLiving entityliving)
    {
        if(entityliving instanceof EntityPig)
        {
            EntityPig entitypig = (EntityPig)entityliving;
            if(!entitypig.func_21068_q())
            {
                entitypig.func_21069_a(true);
                itemstack.stackSize--;
            }
        }
    }

    public void hitEntity(ItemStack itemstack, EntityLiving entityliving)
    {
        saddleEntity(itemstack, entityliving);
    }
}
