package net.minecraft.src;
 

import java.util.Random;

public abstract class WorldGenerator
{

    public WorldGenerator()
    {
    }

    public abstract boolean generate(World world, Random random, int i, int j, int k);

    public void func_420_a(double d, double d1, double d2)
    {
    }
}
