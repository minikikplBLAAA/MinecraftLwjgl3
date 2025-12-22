package net.minecraft.src;
 

import java.net.URL;

public class SoundPoolEntry
{

    public SoundPoolEntry(String s, URL url)
    {
        soundName = s;
        soundUrl = url;
    }

    public String soundName;
    public URL soundUrl;
}
