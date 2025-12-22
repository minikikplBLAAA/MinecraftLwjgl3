package net.minecraft.src;
 


public class ChunkCoordinates
    implements Comparable
{

    public ChunkCoordinates()
    {
    }

    public ChunkCoordinates(int i, int j, int k)
    {
        field_22216_a = i;
        field_529_a = j;
        field_528_b = k;
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof ChunkCoordinates))
        {
            return false;
        } else
        {
            ChunkCoordinates chunkcoordinates = (ChunkCoordinates)obj;
            return field_22216_a == chunkcoordinates.field_22216_a && field_529_a == chunkcoordinates.field_529_a && field_528_b == chunkcoordinates.field_528_b;
        }
    }

    public int hashCode()
    {
        return field_22216_a + field_528_b << 8 + field_529_a << 16;
    }

    public int func_22215_a(ChunkCoordinates chunkcoordinates)
    {
        if(field_529_a == chunkcoordinates.field_529_a)
        {
            if(field_528_b == chunkcoordinates.field_528_b)
            {
                return field_22216_a - chunkcoordinates.field_22216_a;
            } else
            {
                return field_528_b - chunkcoordinates.field_528_b;
            }
        } else
        {
            return field_529_a - chunkcoordinates.field_529_a;
        }
    }

    public int compareTo(Object obj)
    {
        return func_22215_a((ChunkCoordinates)obj);
    }

    public int field_22216_a;
    public int field_529_a;
    public int field_528_b;
}
