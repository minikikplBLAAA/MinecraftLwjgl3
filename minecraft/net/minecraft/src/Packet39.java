package net.minecraft.src;
 

import java.io.*;

public class Packet39 extends Packet
{

    public Packet39()
    {
    }

    public int getPacketSize()
    {
        return 8;
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        entityId = datainputstream.readInt();
        vehicleEntityId = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeInt(entityId);
        dataoutputstream.writeInt(vehicleEntityId);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_6497_a(this);
    }

    public int entityId;
    public int vehicleEntityId;
}
