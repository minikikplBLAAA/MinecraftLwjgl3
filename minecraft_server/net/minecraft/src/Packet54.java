package net.minecraft.src;
 

import java.io.*;

public class Packet54 extends Packet
{

    public Packet54()
    {
    }

    public Packet54(int i, int j, int k, int l, int i1)
    {
        xLocation = i;
        yLocation = j;
        zLocation = k;
        instrumentType = l;
        pitch = i1;
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        xLocation = datainputstream.readInt();
        yLocation = datainputstream.readShort();
        zLocation = datainputstream.readInt();
        instrumentType = datainputstream.read();
        pitch = datainputstream.read();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeInt(xLocation);
        dataoutputstream.writeShort(yLocation);
        dataoutputstream.writeInt(zLocation);
        dataoutputstream.write(instrumentType);
        dataoutputstream.write(pitch);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_21004_a(this);
    }

    public int getPacketSize()
    {
        return 12;
    }

    public int xLocation;
    public int yLocation;
    public int zLocation;
    public int instrumentType;
    public int pitch;
}
