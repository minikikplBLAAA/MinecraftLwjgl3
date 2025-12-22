package net.minecraft.src;
 

import java.util.Random;

public class MobSpawnerForest extends MobSpawnerBase
{

    public MobSpawnerForest()
    {
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(5) == 0)
        {
            return new WorldGenForest();
        }
        if(random.nextInt(3) == 0)
        {
            return new WorldGenBigTree();
        } else
        {
            return new WorldGenTrees();
        }
    }
}
