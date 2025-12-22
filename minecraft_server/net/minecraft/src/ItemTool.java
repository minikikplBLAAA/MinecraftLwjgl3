package net.minecraft.src;
 


public class ItemTool extends Item
{

    protected ItemTool(int i, int j, EnumToolMaterial enumtoolmaterial, Block ablock[])
    {
        super(i);
        efficiencyOnProperMaterial = 4F;
        toolMaterial = enumtoolmaterial;
        blocksEffectiveAgainst = ablock;
        maxStackSize = 1;
        maxDamage = enumtoolmaterial.getMaxUses();
        efficiencyOnProperMaterial = enumtoolmaterial.getEfficiencyOnProperMaterial();
        damageVsEntity = j + enumtoolmaterial.getDamageVsEntity();
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        for(int i = 0; i < blocksEffectiveAgainst.length; i++)
        {
            if(blocksEffectiveAgainst[i] == block)
            {
                return efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    public void hitEntity(ItemStack itemstack, EntityLiving entityliving)
    {
        itemstack.damageItem(2);
    }

    public void hitBlock(ItemStack itemstack, int i, int j, int k, int l)
    {
        itemstack.damageItem(1);
    }

    public int getDamageVsEntity(Entity entity)
    {
        return damageVsEntity;
    }

    private Block blocksEffectiveAgainst[];
    private float efficiencyOnProperMaterial;
    private int damageVsEntity;
    protected EnumToolMaterial toolMaterial;
}
