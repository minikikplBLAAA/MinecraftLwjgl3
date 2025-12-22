package net.minecraft.src;
 

import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;

public abstract class TexturePackBase
{

    public TexturePackBase()
    {
    }

    public void func_6482_a()
    {
    }

    public void closeTexturePackFile()
    {
    }

    public void func_6485_a(Minecraft minecraft) throws IOException
    {
    }

    public void func_6484_b(Minecraft minecraft)
    {
    }

    public void func_6483_c(Minecraft minecraft)
    {
    }

    public InputStream func_6481_a(String s)
    {
        return (TexturePackBase.class).getResourceAsStream(s);
    }

    public String texturePackFileName;
    public String firstDescriptionLine;
    public String secondDescriptionLine;
    public String field_6488_d;
}
