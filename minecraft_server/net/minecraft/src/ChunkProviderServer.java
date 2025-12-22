package net.minecraft.src;
 

import java.io.IOException;
import java.util.*;

public class ChunkProviderServer
    implements IChunkProvider
{

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider)
    {
        field_725_a = new HashSet();
        field_728_e = new HashMap();
        field_727_f = new ArrayList();
        field_724_b = new EmptyChunk(worldserver, new byte[32768], 0, 0);
        field_726_g = worldserver;
        field_729_d = ichunkloader;
        field_730_c = ichunkprovider;
    }

    public boolean chunkExists(int i, int j)
    {
        return field_728_e.containsKey(Integer.valueOf(ChunkCoordIntPair.func_22006_a(i, j)));
    }

    public void func_374_c(int i, int j)
    {
        ChunkCoordinates chunkcoordinates = field_726_g.func_22078_l();
        int k = (i * 16 + 8) - chunkcoordinates.field_22216_a;
        int l = (j * 16 + 8) - chunkcoordinates.field_528_b;
        char c = '\200';
        if(k < -c || k > c || l < -c || l > c)
        {
            field_725_a.add(Integer.valueOf(ChunkCoordIntPair.func_22006_a(i, j)));
        }
    }

    public Chunk loadChunk(int i, int j)
    {
        int k = ChunkCoordIntPair.func_22006_a(i, j);
        field_725_a.remove(Integer.valueOf(k));
        Chunk chunk = (Chunk)field_728_e.get(Integer.valueOf(k));
        if(chunk == null)
        {
            chunk = func_4063_e(i, j);
            if(chunk == null)
            {
                if(field_730_c == null)
                {
                    chunk = field_724_b;
                } else
                {
                    chunk = field_730_c.provideChunk(i, j);
                }
            }
            field_728_e.put(Integer.valueOf(k), chunk);
            field_727_f.add(chunk);
            if(chunk != null)
            {
                chunk.func_4053_c();
                chunk.onChunkLoad();
            }
            if(!chunk.isTerrainPopulated && chunkExists(i + 1, j + 1) && chunkExists(i, j + 1) && chunkExists(i + 1, j))
            {
                populate(this, i, j);
            }
            if(chunkExists(i - 1, j) && !provideChunk(i - 1, j).isTerrainPopulated && chunkExists(i - 1, j + 1) && chunkExists(i, j + 1) && chunkExists(i - 1, j))
            {
                populate(this, i - 1, j);
            }
            if(chunkExists(i, j - 1) && !provideChunk(i, j - 1).isTerrainPopulated && chunkExists(i + 1, j - 1) && chunkExists(i, j - 1) && chunkExists(i + 1, j))
            {
                populate(this, i, j - 1);
            }
            if(chunkExists(i - 1, j - 1) && !provideChunk(i - 1, j - 1).isTerrainPopulated && chunkExists(i - 1, j - 1) && chunkExists(i, j - 1) && chunkExists(i - 1, j))
            {
                populate(this, i - 1, j - 1);
            }
        }
        return chunk;
    }

    public Chunk provideChunk(int i, int j)
    {
        Chunk chunk = (Chunk)field_728_e.get(Integer.valueOf(ChunkCoordIntPair.func_22006_a(i, j)));
        if(chunk == null)
        {
            if(field_726_g.field_9209_x)
            {
                return loadChunk(i, j);
            } else
            {
                return field_724_b;
            }
        } else
        {
            return chunk;
        }
    }

    private Chunk func_4063_e(int i, int j)
    {
        if(field_729_d == null)
        {
            return null;
        }
        try
        {
            Chunk chunk = field_729_d.loadChunk(field_726_g, i, j);
            if(chunk != null)
            {
                chunk.lastSaveTime = field_726_g.func_22080_k();
            }
            return chunk;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    private void func_375_a(Chunk chunk)
    {
        if(field_729_d == null)
        {
            return;
        }
        try
        {
            field_729_d.saveExtraChunkData(field_726_g, chunk);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void func_373_b(Chunk chunk)
    {
        if(field_729_d == null)
        {
            return;
        }
        chunk.lastSaveTime = field_726_g.func_22080_k();
		field_729_d.saveChunk(field_726_g, chunk);
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        Chunk chunk = provideChunk(i, j);
        if(!chunk.isTerrainPopulated)
        {
            chunk.isTerrainPopulated = true;
            if(field_730_c != null)
            {
                field_730_c.populate(ichunkprovider, i, j);
                chunk.setChunkModified();
            }
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        int i = 0;
        for(int j = 0; j < field_727_f.size(); j++)
        {
            Chunk chunk = (Chunk)field_727_f.get(j);
            if(flag && !chunk.neverSave)
            {
                func_375_a(chunk);
            }
            if(!chunk.needsSaving(flag))
            {
                continue;
            }
            func_373_b(chunk);
            chunk.isModified = false;
            if(++i == 24 && !flag)
            {
                return false;
            }
        }

        if(flag)
        {
            if(field_729_d == null)
            {
                return true;
            }
            field_729_d.saveExtraData();
        }
        return true;
    }

    public boolean func_361_a()
    {
        if(!field_726_g.field_816_A)
        {
            for(int i = 0; i < 100; i++)
            {
                if(!field_725_a.isEmpty())
                {
                    Integer integer = (Integer)field_725_a.iterator().next();
                    Chunk chunk = (Chunk)field_728_e.get(integer);
                    chunk.onChunkUnload();
                    func_373_b(chunk);
                    func_375_a(chunk);
                    field_725_a.remove(integer);
                    field_728_e.remove(integer);
                    field_727_f.remove(chunk);
                }
            }

            if(field_729_d != null)
            {
                field_729_d.func_661_a();
            }
        }
        return field_730_c.func_361_a();
    }

    public boolean func_364_b()
    {
        return !field_726_g.field_816_A;
    }

    private Set field_725_a;
    private Chunk field_724_b;
    private IChunkProvider field_730_c;
    private IChunkLoader field_729_d;
    private Map field_728_e;
    private List field_727_f;
    private WorldServer field_726_g;
}
