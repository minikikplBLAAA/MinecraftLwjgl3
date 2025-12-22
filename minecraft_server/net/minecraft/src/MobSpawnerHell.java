package net.minecraft.src;
 


public class MobSpawnerHell extends MobSpawnerBase
{

    public MobSpawnerHell()
    {
        biomeMonsters = (new Class[] {
            EntityGhast.class, EntityPigZombie.class
        });
        biomeCreatures = new Class[0];
    }
}
