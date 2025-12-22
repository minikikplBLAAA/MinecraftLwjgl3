package net.minecraft.src;
 


public class ServerCommand
{

    public ServerCommand(String s, ICommandListener icommandlistener)
    {
        command = s;
        commandListener = icommandlistener;
    }

    public final String command;
    public final ICommandListener commandListener;
}
