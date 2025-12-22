package net.minecraft.src;
 


public enum EnumMobType
{
   everything,
   mobs,
   players;
/*
    public static EnumMobType[] values()
    {
        return (EnumMobType[])field_1340_d.clone();
    }

    public static EnumMobType valueOf(String s)
    {
        return (EnumMobType)Enum.valueOf(EnumMobType.class, s);
    }

    private EnumMobType(String s, int i)
    {
        super(s, i);
    }

    public static final EnumMobType everything;
    public static final EnumMobType mobs;
    public static final EnumMobType players;
    private static final EnumMobType field_1340_d[]; /* synthetic field */
/*
    static 
    {
        everything = new EnumMobType("everything", 0);
        mobs = new EnumMobType("mobs", 1);
        players = new EnumMobType("players", 2);
        field_1340_d = (new EnumMobType[] {
            everything, mobs, players
        });
    }
*/
}
