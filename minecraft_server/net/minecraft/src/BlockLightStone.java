package net.minecraft.src;
 

import java.util.Random;

public class BlockLightStone extends Block
{

    public BlockLightStone(int i, int j, Material material)
    {
        super(i, j, material);
    }

    public int idDropped(int i, Random random)
    {
        return Item.lightStoneDust.shiftedIndex;
    }
}
