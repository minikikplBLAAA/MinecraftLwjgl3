package net.minecraft.src;
 

import java.util.Comparator;

public class EntitySorter
    implements Comparator
{

    public EntitySorter(Entity entity)
    {
        field_1594_a = entity;
    }

    public int func_1063_a(WorldRenderer worldrenderer, WorldRenderer worldrenderer1)
    {
        return worldrenderer.distanceToEntity(field_1594_a) >= worldrenderer1.distanceToEntity(field_1594_a) ? 1 : -1;
    }

    public int compare(Object obj, Object obj1)
    {
        return func_1063_a((WorldRenderer)obj, (WorldRenderer)obj1);
    }

    private Entity field_1594_a;
}
