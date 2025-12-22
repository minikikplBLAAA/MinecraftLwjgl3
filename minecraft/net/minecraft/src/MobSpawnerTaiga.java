package net.minecraft.src;
 

import java.util.Random;

public class MobSpawnerTaiga extends MobSpawnerBase
{

    public MobSpawnerTaiga()
    {
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(3) == 0)
        {
            return new WorldGenTaiga1();
        } else
        {
            return new WorldGenTaiga2();
        }
    }
}
