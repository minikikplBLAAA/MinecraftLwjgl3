package net.minecraft.src;
 

import java.io.File;
import java.io.FileInputStream;

public class SaveFormatOld
    implements ISaveFormat
{

    public SaveFormatOld(File file)
    {
        field_22106_a = file;
    }

    public WorldInfo func_22103_b(String s)
    {
        File file = new File(field_22106_a, s);
        if(!file.exists())
        {
            return null;
        }
        File file1 = new File(file, "level.dat");
        if(file1.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_770_a(new FileInputStream(file1));
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound1);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        return null;
    }

    protected static void func_22104_a(File afile[])
    {
        for(int i = 0; i < afile.length; i++)
        {
            if(afile[i].isDirectory())
            {
                func_22104_a(afile[i].listFiles());
            }
            afile[i].delete();
        }

    }

    public ISaveHandler func_22105_a(String s, boolean flag)
    {
        return new PlayerNBTManager(field_22106_a, s, flag);
    }

    public boolean func_22102_a(String s)
    {
        return false;
    }

    public boolean func_22101_a(String s, IProgressUpdate iprogressupdate)
    {
        return false;
    }

    protected final File field_22106_a;
}
