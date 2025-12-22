package net.minecraft.src;
 


public interface IChunkProvider
{

    public abstract boolean chunkExists(int i, int j);

    public abstract Chunk provideChunk(int i, int j);

    public abstract void populate(IChunkProvider ichunkprovider, int i, int j);

    public abstract boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate);

    public abstract boolean func_532_a();

    public abstract boolean func_536_b();

    public abstract String toString();
}
