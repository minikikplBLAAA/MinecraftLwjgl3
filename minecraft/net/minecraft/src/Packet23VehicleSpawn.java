package net.minecraft.src;
 

import java.io.*;

public class Packet23VehicleSpawn extends Packet
{

    public Packet23VehicleSpawn()
    {
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        entityId = datainputstream.readInt();
        type = datainputstream.readByte();
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.readInt();
        zPosition = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeInt(entityId);
        dataoutputstream.writeByte(type);
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.writeInt(yPosition);
        dataoutputstream.writeInt(zPosition);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleVehicleSpawn(this);
    }

    public int getPacketSize()
    {
        return 17;
    }

    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int type;
}
