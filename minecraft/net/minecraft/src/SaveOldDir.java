package net.minecraft.src;
 

import java.io.File;
import java.util.List;

public class SaveOldDir extends SaveHandler
{

    public SaveOldDir(File file, String s, boolean flag)
    {
        super(file, s, flag);
    }

    public IChunkLoader func_22149_a(WorldProvider worldprovider)
    {
        File file = func_22153_a();
        if(worldprovider instanceof WorldProviderHell)
        {
            File file1 = new File(file, "DIM-1");
            file1.mkdirs();
            return new McRegionChunkLoader(file1);
        } else
        {
            return new McRegionChunkLoader(file);
        }
    }

    public void func_22148_a(WorldInfo worldinfo, List list)
    {
        worldinfo.func_22289_d(19132);
        super.func_22148_a(worldinfo, list);
    }
}
