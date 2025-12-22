package net.minecraft.src;
 

import java.util.Random;

public class ItemBow extends Item
{

    public ItemBow(int i)
    {
        super(i);
        maxStackSize = 1;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if(entityplayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex))
        {
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if(!world.multiplayerWorld)
            {
                world.entityJoinedWorld(new EntityArrow(world, entityplayer));
            }
        }
        return itemstack;
    }
}
