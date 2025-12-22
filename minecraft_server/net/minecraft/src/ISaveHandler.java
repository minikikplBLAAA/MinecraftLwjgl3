package net.minecraft.src;
 

import java.util.List;

public interface ISaveHandler
{

    public abstract WorldInfo func_22096_c();

    public abstract void func_22091_b();

    public abstract IChunkLoader func_22092_a(WorldProvider worldprovider);

    public abstract void func_22095_a(WorldInfo worldinfo, List list);

    public abstract void func_22094_a(WorldInfo worldinfo);

    public abstract IPlayerFileData func_22090_d();

    public abstract void func_22093_e();
}
