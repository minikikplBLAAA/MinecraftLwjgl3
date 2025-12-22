package net.minecraft.src;
 


public interface IBlockAccess
{

    public abstract int getBlockId(int i, int j, int k);

    public abstract int getBlockMetadata(int i, int j, int k);

    public abstract Material getBlockMaterial(int i, int j, int k);

    public abstract boolean isBlockOpaqueCube(int i, int j, int k);
}
