package net.minecraft.src;
 

import java.io.*;

public class Packet9 extends Packet
{

    public Packet9()
    {
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_9448_a(this);
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
    }

    public int getPacketSize()
    {
        return 0;
    }
}
