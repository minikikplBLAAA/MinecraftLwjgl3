package net.minecraft.src;
 

import java.io.*;

public class Packet1Login extends Packet
{

    public Packet1Login()
    {
    }

    public Packet1Login(String s, String s1, int i)
    {
        username = s;
        password = s1;
        protocolVersion = i;
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException
    {
        protocolVersion = datainputstream.readInt();
        username = datainputstream.readUTF();
        password = datainputstream.readUTF();
        mapSeed = datainputstream.readLong();
        dimension = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException
    {
        dataoutputstream.writeInt(protocolVersion);
        dataoutputstream.writeUTF(username);
        dataoutputstream.writeUTF(password);
        dataoutputstream.writeLong(mapSeed);
        dataoutputstream.writeByte(dimension);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleLogin(this);
    }

    public int getPacketSize()
    {
        return 4 + username.length() + password.length() + 4 + 5;
    }

    public int protocolVersion;
    public String username;
    public String password;
    public long mapSeed;
    public byte dimension;
}
