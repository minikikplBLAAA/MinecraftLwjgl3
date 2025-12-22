package net.minecraft.src;
 


class NetworkWriterThread extends Thread
{

    NetworkWriterThread(NetworkManager networkmanager, String s)
    {
    	super(s);
        netManager = networkmanager;
    }

    public void run()
    {
        synchronized(NetworkManager.threadSyncObject)
        {
            NetworkManager.numWriteThreads++;
        }
        for(; NetworkManager.isRunning(netManager); NetworkManager.sendNetworkPacket(netManager)) { }
        synchronized(NetworkManager.threadSyncObject)
        {
            NetworkManager.numWriteThreads--;
        }
//        break MISSING_BLOCK_LABEL_105;
//        Exception exception2;
//        exception2;
        synchronized(NetworkManager.threadSyncObject)
        {
            NetworkManager.numWriteThreads--;
        }
//        throw exception2;
    }

    final NetworkManager netManager; /* synthetic field */
}
