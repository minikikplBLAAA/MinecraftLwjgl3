package net.minecraft.src;
 

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

class ThreadLoginVerifier extends Thread
{

    ThreadLoginVerifier(NetLoginHandler netloginhandler, Packet1Login packet1login)
    {
        loginHandler = netloginhandler;
        loginPacket = packet1login;
//        super();
    }

    public void run()
    {
        try
        {
            String s = NetLoginHandler.getServerId(loginHandler);
            URL url = new URL((new StringBuilder()).append("http://www.minecraft.net/game/checkserver.jsp?user=").append(loginPacket.username).append("&serverId=").append(s).toString());
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s1 = bufferedreader.readLine();
            bufferedreader.close();
            if(s1.equals("YES"))
            {
                NetLoginHandler.setLoginPacket(loginHandler, loginPacket);
            } else
            {
                loginHandler.kickUser("Failed to verify username!");
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    final Packet1Login loginPacket; /* synthetic field */
    final NetLoginHandler loginHandler; /* synthetic field */
}
