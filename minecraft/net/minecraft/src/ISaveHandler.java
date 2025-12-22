package net.minecraft.src;
 

import java.util.List;

public interface ISaveHandler
{

    public abstract WorldInfo func_22151_c();

    public abstract void func_22150_b();

    public abstract IChunkLoader func_22149_a(WorldProvider worldprovider);

    public abstract void func_22148_a(WorldInfo worldinfo, List list);

    public abstract void func_22152_a(WorldInfo worldinfo);
}
