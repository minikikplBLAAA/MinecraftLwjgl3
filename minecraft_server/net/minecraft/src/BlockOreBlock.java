package net.minecraft.src;
 


public class BlockOreBlock extends Block
{

    public BlockOreBlock(int i, int j)
    {
        super(i, Material.iron);
        blockIndexInTexture = j;
    }

    public int getBlockTextureFromSide(int i)
    {
        return blockIndexInTexture;
    }
}
