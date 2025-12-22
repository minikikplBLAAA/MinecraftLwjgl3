package net.minecraft.src;
 

import java.io.ByteArrayOutputStream;

class RegionFileChunkBuffer extends ByteArrayOutputStream
{

    public RegionFileChunkBuffer(RegionFile regionfile, int i, int j)
    {
        super(8096);
        field_22284_a = regionfile;
        field_22283_b = i;
        field_22285_c = j;
    }

    public void close()
    {
        field_22284_a.func_22203_a(field_22283_b, field_22285_c, buf, count);
    }

    private int field_22283_b;
    private int field_22285_c;
    final RegionFile field_22284_a; /* synthetic field */
}
