package net.minecraft.src;
 

import java.io.ByteArrayOutputStream;

class RegionFileChunkBuffer extends ByteArrayOutputStream
{

    public RegionFileChunkBuffer(RegionFile regionfile, int i, int j)
    {
    	super(8096);
        field_22157_a = regionfile;
        field_22156_b = i;
        field_22158_c = j;
    }

    public void close()
    {
        field_22157_a.func_22133_a(field_22156_b, field_22158_c, buf, count);
    }

    private int field_22156_b;
    private int field_22158_c;
    final RegionFile field_22157_a; /* synthetic field */
}
