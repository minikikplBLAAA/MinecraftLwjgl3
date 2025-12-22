package net.minecraft.src;
 


public class ItemMinecart extends Item
{

    public ItemMinecart(int i, int j)
    {
        super(i);
        maxStackSize = 1;
        minecartType = j;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockId(i, j, k);
        if(i1 == Block.minecartTrack.blockID)
        {
            if(!world.multiplayerWorld)
            {
                world.entityJoinedWorld(new EntityMinecart(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, minecartType));
            }
            itemstack.stackSize--;
            return true;
        } else
        {
            return false;
        }
    }

    public int minecartType;
}
