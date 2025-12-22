package net.minecraft.src;
 

import java.util.Random;

public class MobSpawnerRainforest extends MobSpawnerBase
{

    public MobSpawnerRainforest()
    {
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(3) == 0)
        {
            return new WorldGenBigTree();
        } else
        {
            return new WorldGenTrees();
        }
    }
}
