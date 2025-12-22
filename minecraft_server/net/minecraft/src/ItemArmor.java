package net.minecraft.src;
 


public class ItemArmor extends Item
{

    public ItemArmor(int i, int j, int k, int l)
    {
        super(i);
        armorLevel = j;
        armorType = l;
        renderIndex = k;
        damageReduceAmount = damageReduceAmountArray[l];
        maxDamage = maxDamageArray[l] * 3 << j;
        maxStackSize = 1;
    }

    private static final int damageReduceAmountArray[] = {
        3, 8, 6, 3
    };
    private static final int maxDamageArray[] = {
        11, 16, 15, 13
    };
    public final int armorLevel;
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;

}
