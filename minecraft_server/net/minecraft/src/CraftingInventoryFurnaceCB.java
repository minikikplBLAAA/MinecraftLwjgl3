package net.minecraft.src;
 

import java.util.List;

public class CraftingInventoryFurnaceCB extends CraftingInventoryCB
{

    public CraftingInventoryFurnaceCB(IInventory iinventory, TileEntityFurnace tileentityfurnace)
    {
        field_20138_b = 0;
        field_20141_c = 0;
        field_20140_h = 0;
        field_20139_a = tileentityfurnace;
        func_20122_a(new Slot(tileentityfurnace, 0, 56, 17));
        func_20122_a(new Slot(tileentityfurnace, 1, 56, 53));
        func_20122_a(new Slot(tileentityfurnace, 2, 116, 35));
        for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                func_20122_a(new Slot(iinventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }

        }

        for(int j = 0; j < 9; j++)
        {
            func_20122_a(new Slot(iinventory, j, 8 + j * 18, 142));
        }

    }

    public void func_20128_a(ICrafting icrafting)
    {
        super.func_20128_a(icrafting);
        icrafting.func_20056_a(this, 0, field_20139_a.furnaceCookTime);
        icrafting.func_20056_a(this, 1, field_20139_a.furnaceBurnTime);
        icrafting.func_20056_a(this, 2, field_20139_a.currentItemBurnTime);
    }

    public void func_20125_a()
    {
        super.func_20125_a();
        for(int i = 0; i < field_20133_g.size(); i++)
        {
            ICrafting icrafting = (ICrafting)field_20133_g.get(i);
            if(field_20138_b != field_20139_a.furnaceCookTime)
            {
                icrafting.func_20056_a(this, 0, field_20139_a.furnaceCookTime);
            }
            if(field_20141_c != field_20139_a.furnaceBurnTime)
            {
                icrafting.func_20056_a(this, 1, field_20139_a.furnaceBurnTime);
            }
            if(field_20140_h != field_20139_a.currentItemBurnTime)
            {
                icrafting.func_20056_a(this, 2, field_20139_a.currentItemBurnTime);
            }
        }

        field_20138_b = field_20139_a.furnaceCookTime;
        field_20141_c = field_20139_a.furnaceBurnTime;
        field_20140_h = field_20139_a.currentItemBurnTime;
    }

    public boolean func_20126_b(EntityPlayer entityplayer)
    {
        return field_20139_a.canInteractWith(entityplayer);
    }

    private TileEntityFurnace field_20139_a;
    private int field_20138_b;
    private int field_20141_c;
    private int field_20140_h;
}
