package net.minecraft.src;
 


public interface IChunkProvider
{

    public abstract boolean chunkExists(int i, int j);

    public abstract Chunk provideChunk(int i, int j);

    public abstract void populate(IChunkProvider ichunkprovider, int i, int j);

    public abstract boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate);

    public abstract boolean func_361_a();

    public abstract boolean func_364_b();
}
