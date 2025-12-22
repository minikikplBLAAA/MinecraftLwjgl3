package net.minecraft.src;
 

import java.io.*;

public class Packet100 extends Packet
{

    public Packet100()
    {
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_20087_a(this);
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        windowId = datainputstream.readByte();
        inventoryType = datainputstream.readByte();
        windowTitle = datainputstream.readUTF();
        slotsCount = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeByte(windowId);
        dataoutputstream.writeByte(inventoryType);
        dataoutputstream.writeUTF(windowTitle);
        dataoutputstream.writeByte(slotsCount);
    }

    public int getPacketSize()
    {
        return 3 + windowTitle.length();
    }

    public int windowId;
    public int inventoryType;
    public String windowTitle;
    public int slotsCount;
}
