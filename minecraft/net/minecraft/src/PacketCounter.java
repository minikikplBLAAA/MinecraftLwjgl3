package net.minecraft.src;
 


class PacketCounter
{

    private PacketCounter()
    {
    }

    public void func_22236_a(int i)
    {
        field_22238_a++;
        field_22237_b += i;
    }

    PacketCounter(Empty1 empty1)
    {
        this();
    }

    private int field_22238_a;
    private long field_22237_b;
}
