package net.minecraft.src;
 

import java.io.*;

public class Packet105 extends Packet
{

    public Packet105()
    {
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_20090_a(this);
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        windowId = datainputstream.readByte();
        progressBar = datainputstream.readShort();
        progressBarValue = datainputstream.readShort();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeByte(windowId);
        dataoutputstream.writeShort(progressBar);
        dataoutputstream.writeShort(progressBarValue);
    }

    public int getPacketSize()
    {
        return 5;
    }

    public int windowId;
    public int progressBar;
    public int progressBarValue;
}
