package net.minecraft.src;
 

import java.io.*;

public class Packet19 extends Packet
{

    public Packet19()
    {
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        entityId = datainputstream.readInt();
        state = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeInt(entityId);
        dataoutputstream.writeByte(state);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_21001_a(this);
    }

    public int getPacketSize()
    {
        return 5;
    }

    public int entityId;
    public int state;
}
