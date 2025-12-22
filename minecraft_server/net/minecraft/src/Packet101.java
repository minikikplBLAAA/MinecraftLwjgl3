package net.minecraft.src;
 

import java.io.*;

public class Packet101 extends Packet
{

    public Packet101()
    {
    }

    public Packet101(int i)
    {
        windowId = i;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_20006_a(this);
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        windowId = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeByte(windowId);
    }

    public int getPacketSize()
    {
        return 1;
    }

    public int windowId;
}
