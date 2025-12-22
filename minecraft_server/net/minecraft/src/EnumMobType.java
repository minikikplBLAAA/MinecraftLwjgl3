package net.minecraft.src;
 


public enum EnumMobType
{
    everything("everything", 0),
    mobs("mobs", 1),
    players("players", 2);
/*
    public static EnumMobType[] values()
    {
        return (EnumMobType[])field_990_d.clone();
    }

    public static EnumMobType valueOf(String s)
    {
        return (EnumMobType)Enum.valueOf(EnumMobType.class, s);
    }*/

    private EnumMobType(String s, int i)
    {
        
    }

    /*public static final EnumMobType everything;
    public static final EnumMobType mobs;
    public static final EnumMobType players;
    private static final EnumMobType field_990_d[]; /* synthetic field *

    static 
    {
        everything = new EnumMobType("everything", 0);
        mobs = new EnumMobType("mobs", 1);
        players = new EnumMobType("players", 2);
        field_990_d = (new EnumMobType[] {
            everything, mobs, players
        });
    }*/
}
