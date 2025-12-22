package net.minecraft.src;
 

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class ServerGuiFocusAdapter extends FocusAdapter
{

    ServerGuiFocusAdapter(ServerGUI servergui)
    {
        mcServerGui = servergui;
//        super();
    }

    public void focusGained(FocusEvent focusevent)
    {
    }

    final ServerGUI mcServerGui; /* synthetic field */
}
