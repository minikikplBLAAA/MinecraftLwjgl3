package net.minecraft.src;
 

import java.io.*;

public class Packet38 extends Packet
{

    public Packet38()
    {
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        entityId = datainputstream.readInt();
        entityStatus = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeInt(entityId);
        dataoutputstream.writeByte(entityStatus);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_9447_a(this);
    }

    public int getPacketSize()
    {
        return 5;
    }

    public int entityId;
    public byte entityStatus;
}
