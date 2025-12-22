package net.minecraft.src;
 


public class BlockCloth extends Block
{

    public BlockCloth()
    {
        super(35, 64, Material.cloth);
    }

    public int func_22009_a(int i, int j)
    {
        if(j == 0)
        {
            return blockIndexInTexture;
        } else
        {
            j = ~(j & 0xf);
            return 113 + ((j & 8) >> 3) + (j & 7) * 16;
        }
    }

    protected int damageDropped(int i)
    {
        return i;
    }

    public static int func_21033_c(int i)
    {
        return ~i & 0xf;
    }

    public static int func_21034_d(int i)
    {
        return ~i & 0xf;
    }
}
