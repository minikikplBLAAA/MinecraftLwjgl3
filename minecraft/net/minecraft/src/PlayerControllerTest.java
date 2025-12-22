package net.minecraft.src;
 

import java.util.List;
import net.minecraft.client.Minecraft;

public class PlayerControllerTest extends PlayerController
{

    public PlayerControllerTest(Minecraft minecraft)
    {
        super(minecraft);
        field_1064_b = true;
    }

    public void func_6473_b(EntityPlayer entityplayer)
    {
        for(int i = 0; i < 9; i++)
        {
            if(entityplayer.inventory.mainInventory[i] == null)
            {
                mc.thePlayer.inventory.mainInventory[i] = new ItemStack((Block)Session.registeredBlocksList.get(i));
            } else
            {
                mc.thePlayer.inventory.mainInventory[i].stackSize = 1;
            }
        }

    }

    public boolean shouldDrawHUD()
    {
        return false;
    }

    public void func_717_a(World world)
    {
        super.func_717_a(world);
    }

    public void updateController()
    {
    }
}
