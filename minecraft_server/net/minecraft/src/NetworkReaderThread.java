package net.minecraft.src;
 


class NetworkReaderThread extends Thread
{

    NetworkReaderThread(NetworkManager networkmanager, String s)
    {
    	super(s);
        netManager = networkmanager;
    }

    public void run()
    {
        synchronized(NetworkManager.threadSyncObject)
        {
            NetworkManager.numReadThreads++;
        }
        while(NetworkManager.isRunning(netManager) && !NetworkManager.isServerTerminating(netManager)) 
        {
            NetworkManager.readNetworkPacket(netManager);
            try
            {
                sleep(0L);
            }
            catch(InterruptedException interruptedexception) { }
        }
        synchronized(NetworkManager.threadSyncObject)
        {
            NetworkManager.numReadThreads--;
        }
//        break MISSING_BLOCK_LABEL_123;
//        Exception exception2;
//        exception2;
        synchronized(NetworkManager.threadSyncObject)
        {
            NetworkManager.numReadThreads--;
        }
//        throw exception2;
    }

    final NetworkManager netManager; /* synthetic field */
}
