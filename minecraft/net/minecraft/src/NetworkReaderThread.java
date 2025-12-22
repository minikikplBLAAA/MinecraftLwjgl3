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
        try
        {
        while(NetworkManager.isRunning(netManager) && !NetworkManager.isServerTerminating(netManager)) 
        {
            NetworkManager.readNetworkPacket(netManager);
            try
            {
                sleep(0L);
            }
            catch(InterruptedException interruptedexception) { }
        }
        }
        finally
        {
        synchronized(NetworkManager.threadSyncObject)
        {
            NetworkManager.numReadThreads--;
        }
        }
    }

    final NetworkManager netManager; /* synthetic field */
}
