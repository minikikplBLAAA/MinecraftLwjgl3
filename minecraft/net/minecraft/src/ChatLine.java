package net.minecraft.src;
 


public class ChatLine
{

    public ChatLine(String s)
    {
        message = s;
        updateCounter = 0;
    }

    public String message;
    public int updateCounter;
}
