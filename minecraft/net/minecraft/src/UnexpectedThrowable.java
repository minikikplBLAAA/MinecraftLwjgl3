package net.minecraft.src;
 


public class UnexpectedThrowable
{

    public UnexpectedThrowable(String s, Throwable throwable)
    {
        description = s;
        exception = throwable;
    }

    public final String description;
    public final Throwable exception;
}
