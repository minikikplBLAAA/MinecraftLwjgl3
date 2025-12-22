package net.minecraft.src;
 

import java.io.*;

public class Packet9 extends Packet
{

    public Packet9()
    {
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_9002_a(this);
    }

    public void readPacketData(DataInputStream datainputstream)
    {
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    {
    }

    public int getPacketSize()
    {
        return 0;
    }
}
