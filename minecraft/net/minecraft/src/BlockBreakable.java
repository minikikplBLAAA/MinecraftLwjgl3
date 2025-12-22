package net.minecraft.src;
 


public class BlockBreakable extends Block
{

    protected BlockBreakable(int i, int j, Material material, boolean flag)
    {
        super(i, j, material);
        field_6363_a = flag;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        int i1 = iblockaccess.getBlockId(i, j, k);
        if(!field_6363_a && i1 == blockID)
        {
            return false;
        } else
        {
            return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
        }
    }

    private boolean field_6363_a;
}
